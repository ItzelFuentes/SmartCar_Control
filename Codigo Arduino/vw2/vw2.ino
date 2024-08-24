#include <Wire.h>
#include <EEPROM.h>
#define comm Serial

byte buf[6];

unsigned long Mil;

void setup() {
  Wire.begin();
  comm.begin(9600);
  mcu_setup();
}

void loop() {
  Mil=millis();
  mcu_loop();
  comm_loop();
  
}
