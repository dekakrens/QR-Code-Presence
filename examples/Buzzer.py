import wiringpi as wpi
import time, os

LED_MODULE = 1
BUZZER_MODULE = 26

wpi.wiringPiSetup()
wpi.pinMode(LED_MODULE, wpi.OUTPUT)
wpi.pinMode(BUZZER_MODULE, wpi.OUTPUT)

wpi.digitalWrite(LED_MODULE, wpi.HIGH)
wpi.digitalWrite(BUZZER_MODULE, wpi.HIGH)
time.sleep(0.1)
wpi.digitalWrite(LED_MODULE, wpi.LOW)
wpi.digitalWrite(BUZZER_MODULE, wpi.LOW)
time.sleep(0.1)
wpi.digitalWrite(LED_MODULE, wpi.HIGH)
wpi.digitalWrite(BUZZER_MODULE, wpi.HIGH)
time.sleep(0.1)
wpi.digitalWrite(LED_MODULE, wpi.LOW)
wpi.digitalWrite(BUZZER_MODULE, wpi.LOW)
time.sleep(1)
