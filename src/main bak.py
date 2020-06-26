import wiringpi as wpi
from imutils.video import VideoStream
from pyzbar import pyzbar
import argparse
import imutils
import time
import datetime
import cv2
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_v1_5
from base64 import b64decode
import pyrebase

#initialize GPIO on the raspberry
DIGITAL_PUSH_BUTTON = 21
BUZZER_MODULE = 26

wpi.wiringPiSetup() #must be called before calling any I/O function
wpi.pinMode(DIGITAL_PUSH_BUTTON, wpi.INPUT)
wpi.pullUpDnControl(DIGITAL_PUSH_BUTTON, wpi.PUD_DOWN)
wpi.pinMode(BUZZER_MODULE, wpi.OUTPUT)

config = {
	"apiKey": "AIzaSyA0z1iP__eJb2R4bIkC-zjXDdOAmfhaBFA",
	"authDomain": "rqr-code-presensi.firebaseapp.com",
	"databaseURL": "https://qr-code-presensi.firebaseio.com",
	"storageBucket": "qr-code-presensi.appspot.com"
}
firebase = pyrebase.initialize_app(config)
db = firebase.database()

# construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-o", "--output", type=str, default="barcodes.csv",
	help="path to output CSV file containing barcodes")
args = vars(ap.parse_args())

# initialize the video stream and allow the camera sensor to warm up
print("[INFO] starting video stream...")
# vs = VideoStream(src=0).start()  if you use an USB webcam
vs = VideoStream(usePiCamera=True).start()
time.sleep(2.0)
# open the output CSV file for writing and initialize the set of
# barcodes found thus far
#csv = open(args["output"], "w")
found = set()

# loop over the frames from the video stream
while True:
	waktu = str(datetime.datetime.now().time()).split(".")
	# grab the frame from the threaded video stream and resize it to
	# have a maximum width of 400 pixels
	frame = vs.read()
	frame = imutils.resize(frame, width=400)
	# find the barcodes in the frame and decode each of the barcodes
	barcodes = pyzbar.decode(frame)
	data=""
	# loop over the detected barcodes
	for barcode in barcodes:
		# extract the bounding box location of the barcode and draw
		# the bounding box surrounding the barcode on the image
		(x, y, w, h) = barcode.rect
		cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
		# the barcode data is a bytes object so if we want to draw it
		# on our output image we need to convert it to a string first
		barcodeData = barcode.data.decode("utf-8")
		barcodeType = barcode.type
		# draw the barcode data and barcode type on the image
		text = "{} ({})".format(barcodeData, barcodeType)
		cv2.putText(frame, text, (x, y - 10),
			cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
		# if the barcode text is currently not in our CSV file, write
		# the timestamp + barcode to disk and update the set
		
		#decrypting RSA ciphertext from barcode
		key = open("privateKey.pem").read()
		key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PRIVATE KEY-----", "").replace("\n", "")
		key = b64decode(key)
		key = RSA.importKey(key)
		cipher = PKCS1_v1_5.new(key)
		plainText = cipher.decrypt(b64decode(barcodeData), "Error decrypting the message")
		
		data = str(plainText.decode('utf8'))
		#writing to .csv file
		'''mantap= str(plainText)
		if barcodeData not in found:
			csv.write (mantap)
			csv.flush()
			found.add(mantap)'''

	# show the output frame
	cv2.imshow("Barcode Scanner", frame)
	
	#splitting the data from plainText
	dataSplit=data.split("#")
	undb = db.child("siswa").child(dataSplit[0]).child("username").get().val()
	pwdb = db.child("siswa").child(dataSplit[0]).child("pass").get().val()
	
	if dataSplit[0] == undb and dataSplit[2] == pwdb:
		db.child("siswa").child(dataSplit[0]).update({"kehadiranSiswa": waktu[0]})
		wpi.digitalWrite(BUZZER_MODULE, wpi.HIGH)
		time.sleep(0.1)
		wpi.digitalWrite(BUZZER_MODULE, wpi.LOW)
		time.sleep(0.1)
		wpi.digitalWrite(BUZZER_MODULE, wpi.HIGH)
		time.sleep(0.1)
		wpi.digitalWrite(BUZZER_MODULE, wpi.LOW)
		time.sleep(1)
	
	key = cv2.waitKey(1) & 0xFF
 
	# if the `q` key was pressed, break from the loop
	if key == ord("q"):
		break
# close the output CSV file do a bit of cleanup
#print("[INFO] cleaning up...")
#csv.close()
cv2.destroyAllWindows()
vs.stop()