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
import os

#Initialize the GPIO
DIGITAL_PUSH_BUTTON = 21
BUZZER_MODULE = 26

wpi.wiringPiSetup()		#Harus dipanggil sebelum memanggil fungsi I/O lainnya
wpi.pinMode(DIGITAL_PUSH_BUTTON, wpi.INPUT)
wpi.pullUpDnControl(DIGITAL_PUSH_BUTTON, wpi.PUD_DOWN)
wpi.pinMode(BUZZER_MODULE, wpi.OUTPUT)

#Konfigurasi firebase
config = {
	"apiKey": "AIzaSyA0z1iP__eJb2R4bIkC-zjXDdOAmfhaBFA",
	"authDomain": "rqr-code-presensi.firebaseapp.com",
	"databaseURL": "https://qr-code-presensi.firebaseio.com",
	"storageBucket": "qr-code-presensi.appspot.com"
}
firebase = pyrebase.initialize_app(config)
db = firebase.database()

ap = argparse.ArgumentParser()
ap.add_argument("-o", "--output", type=str, default="barcodes.csv",
	help="path to output CSV file containing barcodes")
args = vars(ap.parse_args())

#initialize the video stream and allow the camera sensor to warm up
print("[INFO] starting video stream...")
vs = VideoStream(usePiCamera=True).start()
time.sleep(2.0)
found = set()

while True:
	waktu = str(datetime.datetime.now().time()).split(".")
	frame = vs.read()
	frame = imutils.resize(frame, width=400)
	barcodes = pyzbar.decode(frame)
	data=""
	for barcode in barcodes:
		(x, y, w, h) = barcode.rect
		cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
		barcodeData = barcode.data.decode("utf-8")
		barcodeType = barcode.type
		text = "{} ({})".format(barcodeData, barcodeType)
		cv2.putText(frame, text, (x, y - 10),
			cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
		
		try:
			key = open("privateKey.pem").read()
			key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PRIVATE KEY-----", "").replace("\n", "")
			key = b64decode(key)
			key = RSA.importKey(key)
			cipher = PKCS1_v1_5.new(key)
			plainText = cipher.decrypt(b64decode(barcodeData), "Error decrypting the message")
			data = str(plainText.decode('utf8'))
		except:
			print('Sorry, something went wrong when decrypting!')

	cv2.imshow("Barcode Scanner", frame)
	
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
	if wpi.digitalRead(DIGITAL_PUSH_BUTTON) == wpi.HIGH:
		os.system("sudo shutdown -r now")
		
	# if the `q` key was pressed, break from the loop
	if key == ord("q"):
		break
cv2.destroyAllWindows()
vs.stop()