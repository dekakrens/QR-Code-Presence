import wiringpi as wpi
from time import sleep

LED_MODULE = 1

wpi.wiringPiSetup() # Must be called before calling any I/O function

try:
    while True:
        wpi.digitalWrite(LED_MODULE, wpi.HIGH)
        sleep(1)
        wpi.digitalWrite(LED_MODULE, wpi.LOW)
        sleep(1)
        wpi.digitalWrite(LED_MODULE, wpi.HIGH)
        sleep(1)
        wpi.digitalWrite(LED_MODULE, wpi.LOW)
        sleep(1)
	
except KeyboardInterrupt :
	wpi.pinMode(LED_MODULE, wpi.OUTPUT)
	wpi.digitalWrite(LED_MODULE, wpi.LOW)
	print("exit")
