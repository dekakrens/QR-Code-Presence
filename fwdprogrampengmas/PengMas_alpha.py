import wiringpi as wpi
from time import sleep
import os
from imutils.video import VideoStream
from pyzbar import pyzbar
import argparse
import datetime
import imutils
import time
import cv2

import pyrebase
import datetime


#initialize raspberry
DIGITAL_PUSH_BUTTON = 21
BUZZER_MODULE = 26
LED_MODULE = 1

wpi.wiringPiSetup() # Must be called before calling any I/O function
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
#db.child("siswa").child("atta").update({"kehadiranSiswa": "Hadir"})#update
ap = argparse.ArgumentParser()
ap.add_argument("-o", "--output", type=str, default="barcodes.csv",
                help="path to output CSV file containing barcodes")
args = vars(ap.parse_args())
#initialize the video stream and allow the camera sensor to warm up
print("[INFO] starting video stream...")
# vs = VideoStream(src=0).start()
vs = VideoStream(usePiCamera=True).start()
time.sleep(2.0)
    # open the output CSV file for writing and initialize the set of
    # barcodes found thus far
csv = open(args["output"], "w")
found = set()

while True:
    #print("aku")
    waktu=str(datetime.datetime.now().time()).split(".")
    frame = vs.read()
    frame = imutils.resize(frame, width=400)
    barcodes = pyzbar.decode(frame)
    username=""
    for barcode in barcodes:
        (x, y, w, h) = barcode.rect
        cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
        barcodeData = barcode.data.decode("utf-8")
        #barcodeType = barcode.type
        username=barcodeData
        text = "{} ({})".format(barcodeData, waktu[0])
        cv2.putText(frame, text, (x, y - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
        """if barcodeData not in found:
            csv.write("{},{}\n".format(datetime.datetime.now(),
                                       barcodeData))
            csv.flush()
            found.add(barcodeData)"""
    #print(username)
    cv2.imshow("Barcode Scanner", frame)
    if username!="":
        data=username.split("#")
        db.child("siswa").child(data[0]).update({"kehadiranSiswa": "Hadir"})
        db.child("siswa").child(data[0]).update({"waktu": waktu[0]})
        """wpi.digitalWrite(LED_MODULE, wpi.HIGH)
        sleep(1)
        wpi.digitalWrite(LED_MODULE, wpi.LOW)
        sleep(1)
        wpi.digitalWrite(LED_MODULE, wpi.HIGH)
        sleep(1)
        wpi.digitalWrite(LED_MODULE, wpi.LOW)
        sleep(1)"""
        wpi.digitalWrite(BUZZER_MODULE, wpi.HIGH)
        time.sleep(0.1)
        #wpi.digitalWrite(LED_MODULE, wpi.LOW)
        wpi.digitalWrite(BUZZER_MODULE, wpi.LOW)
        time.sleep(0.1)
        #wpi.digitalWrite(LED_MODULE, wpi.HIGH)
        wpi.digitalWrite(BUZZER_MODULE, wpi.HIGH)
        time.sleep(0.1)
        #wpi.digitalWrite(LED_MODULE, wpi.LOW)
        wpi.digitalWrite(BUZZER_MODULE, wpi.LOW)
        time.sleep(1)
    #print(waktu[0])
    key = cv2.waitKey(1)&0xFF
    #if key == ord("q"):
       # break
    try:
        if wpi.digitalRead(DIGITAL_PUSH_BUTTON) == wpi.HIGH:
            count = count + 1
            print("Button pressed " + str(count) + " times")#raise ValueError
            while wpi.digitalRead(DIGITAL_PUSH_BUTTON) == wpi.HIGH:
                pass
            sleep(0.5)
    except:
        os.system("sudo shutdown -r now")
        print("ValueError Exception!")
   

csv.close()
cv2.destroyAllWindows()
vs.stop()
#os.system("sudo shutdown -r now")


        
    

