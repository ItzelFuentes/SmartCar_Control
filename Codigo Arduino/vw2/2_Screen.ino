#define YP A2
#define XM A1
#define YM 6
#define XP 7
#define SDC 13

#define Device0 0x57
#define Device1 0x17

#define RED2RED 0
#define GREEN2GREEN 1
#define BLUE2BLUE 2
#define BLUE2RED 3
#define GREEN2RED 4
#define RED2GREEN 5
#define GREY  0x2104
#define BLACK 0x0000
#define WHITE 0xFFFF
#define BLUE  0x001F
#define RED   0xF800
#define GREEN 0x07E0
#define CYAN  0x07FF
#define PINK  0xF81F
#define YELL  0xFFE0

#define mcu_touch_inverted_x1
#define mcu_touch_inverted_y1
#define mcu_rotate 1

#include <Adafruit_GFX.h>
#include <MCUFRIEND_kbv.h>
#include <TouchScreen.h>
#include "I2C_eeprom.h"
#include <DS3231.h>

I2C_eeprom ee(Device0, I2C_DEVICESIZE_24LC32);
DS3231 rtc(SDA, SCL);
MCUFRIEND_kbv tft;
TouchScreen ts = TouchScreen(XP, YP, XM, YM, 300);

word Scr = 0, aScr, Opc = 0, aOpc;
word Gas, gas_low, gas_high, Ldr = 140, ldr_low = 0, ldr_high = 1024, Tmp, Vol, Vel, Rpm;
word out, aux, in, flag;
word last_out, last_aux, last_in, last_flag;
bool clk_en;
String pass;
unsigned long pTime, aMil, dMil;

union fb {
  float flt;
  byte u8[sizeof(flt)];
} myfb;

union lb {
  word flt;
  byte u8[sizeof(flt)];
} mylb;

typedef struct {
  byte idx;
  byte scr;
  byte action;
  byte group;
  byte pos;
  word x;
  word y;
  word w;
  word h;
  word tx;
  word ty;
  char txt[10];
  word fc;
  word tc;
  byte ts;
} boton_type;

boton_type boton;

void scrb(byte idx, int fc = -1, bool all = true) {
  ee.readBlock(idx * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
  if (fc != -1) boton.fc = fc;
  if (Scr == boton.scr && boton.txt[0] != 0) {
    if (boton.group != 0) {
      switch (boton.group) {
        case 1:
          mylb.flt = aux;
          if (!all && bitRead(aux, boton.pos) == bitRead(last_aux, boton.pos)) return;
          break;
        case 2:
          mylb.flt = in;
          if (!all && bitRead(in, boton.pos) == bitRead(last_in, boton.pos)) return;
          break;
        case 3:
          mylb.flt = out;
          if (!all && bitRead(out, boton.pos) == bitRead(last_out, boton.pos)) return;
          break;
        case 4:
          mylb.flt = flag;
          if (!all && bitRead(flag, boton.pos) == bitRead(last_flag, boton.pos)) return;
          break;
      }
      if (bitRead(mylb.flt, boton.pos))
        boton.fc = GREEN;
      else
        boton.fc = RED;
    } else if (!all) return;
    tft.fillRect(boton.x, boton.y, boton.w, boton.h, boton.fc);
    tft.drawRect(boton.x, boton.y, boton.w, boton.h, WHITE);
    tft.setCursor(boton.tx + boton.x, boton.ty + boton.y);
    tft.setTextSize(boton.ts);
    tft.setTextColor(boton.tc);
    tft.println(boton.txt);
  }
}

void screen(byte scr, byte b = 0) {
  if (Scr != scr || b == 0)
    tft.fillScreen(BLACK);
  if (scr != 9) tft.backlight(true); else  tft.backlight(false);
  Scr = scr;
  Opc = b;
  if (Scr == 8) {
    //(variable,   fromLow, fromHigh, toLow, toHigh,  x, y,color dial,Back color, Range color, rango inicio, rango fin, radio max,
    // número de marcas en el gauge, espesor del gauge, grados bajo horizontal,marcas, color marcas y texto,largo marcas, x texto, y texto  tamaño texto)
    //  if (Scr == 3 && b == 1) Gauge(Gas , 365,  950,   0, 100, 160, 120, GREEN , WHITE , RED ,  0,  20, 100, 100, 30, 60, 4, BLACK , 10, 130, 140, 2);
    if ( Opc == 1) {
      // ringMeter(Ldr, 0, 100, ldr_low, ldr_high, 40, 5,  120, " %", RED2GREEN);
      ringMeter(35, 0, 100, 0, 100, 40, 5,  120, " %", RED2GREEN);
    }
    if ( Opc == 2) ringMeter(Gas, 0, 100, 0, 1024, 40, 5,  120, " %", RED2GREEN);
    //if (Scr == 3 && b == 2) Gauge(Ldr ,   0, 1023,   0, 100, 160, 120, GREEN , BLACK , RED , 25,  50, 100, 50, 30, 45, 4, WHITE , 10, 130, 140, 2);
    if ( Opc == 6) ringMeter(Tmp, 0, 50, 0, 50, 40, 5,  120, "C", BLUE2RED);
    //if ( Opc == 4) gauge(Vol ,   0, 1023,   0,  15, 160, 120, GREEN , BLACK , RED ,  0, 100, 100, 50, 30, 45, 4, WHITE , 10, 130, 140, 2);
    //if ( Opc == 3) gauge(Vel ,   0, 1023,   0, 100, 160, 120, GREEN , BLACK , RED ,  0, 100, 100, 50, 30, 45, 4, WHITE , 10, 130, 140, 2);
    if ( Opc == 5) {
      char cstr[5];
      //   sprintf(cstr, "%4d", FRpm.AddValue(Rpm));
      showmsgXY(60, 95, 8, WHITE, BLACK, cstr);
    }
    if (Opc == 7 && clk_en ) {
      showmsgXY(35,  15, 2, WHITE, BLACK, rtc.getDOWStr());
      showmsgXY(15,  95, 6, WHITE, BLACK, rtc.getTimeStr());
      showmsgXY(45, 195, 4, WHITE, BLACK, rtc.getDateStr());
    }
  }
  else {
    for (byte i = 0; i <= 54; i++)
      scrb(i);
  }
}

void send_byte(byte men, byte group, byte pos, int data = -1) {
  Wire.beginTransmission(Device1);
  Wire.write(men);
  Wire.write(group);
  if (pos != -1) {
    switch (group) {
      case 1:
        last_aux = mylb.flt = aux;
        break;
      case 2:
        last_in = mylb.flt = in;
        break;
      case 3:
        last_out = mylb.flt = out;
        break;
      case 4:
        last_flag = mylb.flt = flag;
        break;
    }
    if (data == -1) data = !bitRead(mylb.flt, pos);
    bitWrite(mylb.flt, pos, data);
    Wire.write(mylb.u8, sizeof(mylb.u8) / sizeof(mylb.u8[0]));
  }
  Wire.endTransmission();
  Wire.requestFrom(Device1, sizeof(mylb.u8) / sizeof(mylb.u8[0]));
  byte conte = 0, c;
  while (Wire.available()) {
    c = Wire.read();
    mylb.u8[conte] = c;
    conte++;
  }
  switch (group) {
    case 1:
      aux = mylb.flt;
      break;
    case 2:
      in = mylb.flt;
      break;
    case 3:
      out = mylb.flt;
      break;
    case 4:
      flag = mylb.flt;
      break;
    case 5:
      Gas = mylb.flt;    
      break;  
  }
  char buffers[15];
  sprintf(buffers, "get%02d%d",group,mylb.flt);
  comm.println(buffers);
  delay(20);
  if (pos != -1) {
    for (byte i = 0; i <= 54; i++) {
      ee.readBlock(i * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      if (group == boton.group && pos == boton.pos) scrb(boton.idx);
    }
  }
}

void db(word X, word Y) {
  for (byte i = 0; i <= 54; i++) {
    ee.readBlock(i * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
    if  (Scr == boton.scr && X > boton.x && X < boton.x + boton.w && Y > boton.y && Y < boton.y + boton.h) {
      tft.buzzer();
      switch (boton.action) {
        case 0:
          send_byte(2, boton.group, boton.pos);
          break;
        case 1:
          pass = "";
          scrb(15);
          pTime = Mil;
          break;
        case 2:
          pass += boton.txt;
          Pass();
          pTime = Mil;
          break;
        case 3:
          screen(0);
          break;
        case 4:
          screen(6);
          break;
        case 5:
          screen(4);
          break;
        case 6:
          screen(5);
          break;
        case 7:
          screen(aScr, aOpc);
          aMil = 0;
          break;
        case 8:
          screen(aScr);
          break;
        case 9:
          aScr = Scr;
          aOpc = Opc;
          screen(1);
          break;
        case 10:
          aScr = Scr;
          aOpc = Opc;
          screen(boton.group, boton.pos);
          break;
        case 16:
          screen(1);
          pTime = Mil;
          break;
      }
      break;
    }
  }
}

void mcu_setup() {
  ee.begin();
  /*                       idx,scr,act,gro,pos
      boton = (boton_type) { 0, 6,  0, 3, 13,   0,   0,  80,  48,   6, 15, "undef"    , RED  , WHITE, 2 }; ee.writeBlock( 0 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) { 1, 6,  0, 3,  5,  80,   0,  80,  48,  14, 15, "Sirena"   , RED  , WHITE, 2 }; ee.writeBlock( 1 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) { 2, 6,  0, 3, 11, 160,   0,  80,  48,  14, 15, "Cer"      , RED  , WHITE, 2 }; ee.writeBlock( 2 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) {47, 6,  0, 3, 10, 240,   0,  80,  48,  14, 15, "Abi"      , RED  , WHITE, 2 }; ee.writeBlock(47 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) { 3, 6,  0, 3,  2,   0,  48,  80,  48,  22, 16, "Aux"      , RED  , WHITE, 2 }; ee.writeBlock( 3 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) {53, 6,  0, 3, 14,  80,  48,  80,  48,  22, 16, "Undef"    , RED  , WHITE, 2 }; ee.writeBlock(53 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) { 4, 6,  0, 3,  7, 160,  48,  80,  48,  10, 16, "Bajas"    , RED  , WHITE, 2 }; ee.writeBlock( 4 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) { 5, 6,  0, 3,  6, 240,  48,  80,  48,  10, 16, "Altas"    , RED  , WHITE, 2 }; ee.writeBlock( 5 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) { 6, 6,  0, 3,  4,   0,  96,  80,  48,   4, 16, "Claxon"   , RED  , WHITE, 2 }; ee.writeBlock( 6 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) { 7, 6,  0, 3, 12, 160,  96,  80,  48,   8, 16, "Cuarto"   , RED  , WHITE, 2 }; ee.writeBlock( 7 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) { 8, 6,  0, 3,  9,  80,  96,  80,  48,  18, 16, "Der"      , RED  , WHITE, 2 }; ee.writeBlock( 8 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) { 9, 6,  0, 3,  8, 240,  96,  80,  48,  18, 16, "Izq"      , RED  , WHITE, 2 }; ee.writeBlock( 9 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) {10, 6,  0, 3,  3,   0, 144,  80,  48,  12, 16, "Bloqueo"  , RED  , WHITE, 2 }; ee.writeBlock(10 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) {11, 6,  0, 3,  0,  80, 144,  80,  48,  12, 16, "Energia"  , RED  , WHITE, 2 }; ee.writeBlock(11 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) {12, 6,  0, 3,  1, 160, 144,  80,  48,  15, 16, "Marcha"   , RED  , WHITE, 2 }; ee.writeBlock(12 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) {54, 6,  0, 3, 15, 240, 144,  80,  48,  15, 16, "undef "   , RED  , WHITE, 2 }; ee.writeBlock(54 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
      boton = (boton_type) {52, 6,  3, 0,  0,   0, 192, 320,  48, 120, 16, "Regresar" , BLUE , WHITE, 2 }; ee.writeBlock(52 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
  */
  /*
     boton = (boton_type) {15, 1,  1, 0,  0,   0, 180, 212,  60,  72, 22, "Borrar"   , BLUE , WHITE, 2 }; ee.writeBlock(15*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {16, 1,  2, 0,  0, 212, 180, 106,  60,  48, 22, "0"        , BLUE , WHITE, 2 }; ee.writeBlock(16*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {17, 1,  2, 0,  0,   0, 120, 106,  60,  48, 22, "1"        , BLUE , WHITE, 2 }; ee.writeBlock(17*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {18, 1,  2, 0,  0, 106, 120, 106,  60,  48, 22, "2"        , BLUE , WHITE, 2 }; ee.writeBlock(18*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {19, 1,  2, 0,  0, 212, 120, 106,  60,  48, 22, "3"        , BLUE , WHITE, 2 }; ee.writeBlock(19*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {20, 1,  2, 0,  0,   0,  60, 106,  60,  48, 22, "4"        , BLUE , WHITE, 2 }; ee.writeBlock(20*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {21, 1,  2, 0,  0, 106,  60, 106,  60,  48, 22, "5"        , BLUE , WHITE, 2 }; ee.writeBlock(21*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {22, 1,  2, 0,  0, 212,  60, 106,  60,  48, 22, "6"        , BLUE , WHITE, 2 }; ee.writeBlock(22*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {23, 1,  2, 0,  0,   0,   0, 106,  60,  48, 20, "7"        , BLUE , WHITE, 2 }; ee.writeBlock(23*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {24, 1,  2, 0,  0, 106,   0, 106,  60,  48, 22, "8"        , BLUE , WHITE, 2 }; ee.writeBlock(24*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {25, 1,  2, 0,  0, 212,   0, 106,  60,  48, 22, "9"        , BLUE , WHITE, 2 }; ee.writeBlock(25*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {26, 2,  7, 0,  0,   0,   0, 320, 240,  45, 90, "Cancelar" , YELL , BLACK, 5 }; ee.writeBlock(26*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {27, 8,  3, 0,  0,   0,   0, 320, 240,   0,  0, " "        , BLACK, WHITE, 2 }; ee.writeBlock(27*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
  */
  /*
     boton = (boton_type) {28, 4, 10, 8,  1, 160,   0, 160,  60,   2, 22, "Luz"      , RED  , WHITE, 2 }; ee.writeBlock(28 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {29, 4, 10, 8,  2,   0,   0, 160,  60,   2, 22, "Gasolina" , RED  , WHITE, 2 }; ee.writeBlock(29 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {30, 4, 10, 8,  3,   0,  60, 160,  60,   2, 22, "Velo"     , RED  , WHITE, 2 }; ee.writeBlock(30 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {31, 4, 10, 8,  5, 160,  60, 160,  60,   2, 22, "Taco"     , RED  , WHITE, 2 }; ee.writeBlock(31 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {32, 4, 10, 8,  6,   0, 120, 160,  60,   2, 22, "Temp"     , RED  , WHITE, 2 }; ee.writeBlock(32 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {33, 4, 10, 8,  7, 160, 120, 160,  60,   2, 22, "Reloj"    , RED  , WHITE, 2 }; ee.writeBlock(33 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {46, 4,  3, 0,  0,   0, 180, 320,  60, 120, 22, "Regresar" , BLUE , WHITE, 2 }; ee.writeBlock(46 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
  */
  /*
     boton = (boton_type) {34, 5, 99, 2,  5,   0,   0, 160,  40,  22, 12, "Aceite"   , RED  , WHITE, 2 }; ee.writeBlock(34*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {35, 5, 99, 2,  7, 160,   0, 160,  40,  22, 12, "Freno M"  , RED  , WHITE, 2 }; ee.writeBlock(35*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {36, 5, 99, 2,  4,   0,  40, 160,  40,  22, 12, "Freno P"  , RED  , WHITE, 2 }; ee.writeBlock(36*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {37, 5, 99, 2,  6, 160,  40, 160,  40,  50, 12, "Carga"    , RED  , WHITE, 2 }; ee.writeBlock(37*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {38, 5, 99, 2,  2,   0,  80, 160,  40,   2, 12, "Embrague" , RED  , WHITE, 2 }; ee.writeBlock(38*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {39, 5, 99, 2,  1, 160,  80, 160,  40,   2, 12, "Puertas"  , RED  , WHITE, 2 }; ee.writeBlock(39*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {40, 5, 99, 2,  9,   0, 120, 160,  40,   2, 12, "Movimien" , RED  , WHITE, 2 }; ee.writeBlock(40*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {41, 5, 99, 2,  0, 160, 120, 160,  40,   2, 12, "Energia"  , RED  , WHITE, 2 }; ee.writeBlock(41*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {42, 5, 99, 2,  8,   0, 160, 160,  40,   2, 12, "Reversa"  , RED  , WHITE, 2 }; ee.writeBlock(42*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {43, 5, 99, 2,  3, 160, 160, 160,  40,  20, 12, "Boton"    , RED  , WHITE, 2 }; ee.writeBlock(43*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {44, 5,  3, 0,  0,   0, 200, 320,  40, 120, 12, "Regresar" , BLUE , WHITE, 2 }; ee.writeBlock(44*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
 */
  /*
     boton = (boton_type) {49, 0,  0, 4,  0,   0,   0, 107,  48,   6, 15, "Protege"  , RED  , WHITE, 2 }; ee.writeBlock(49 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {50, 0,  0, 4,  1, 107,   0, 106,  48,  14, 15, "Alarma"   , RED  , WHITE, 2 }; ee.writeBlock(50 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {51, 0,  0, 4,  2, 213,   0, 107,  48,   8, 16, "Inter"    , RED  , WHITE, 2 }; ee.writeBlock(51 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {48, 0,  4, 0,  0,   0, 192, 107,  48,  20, 16, "Relays"   , BLUE , WHITE, 2 }; ee.writeBlock(48 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {13, 0,  6, 0,  0, 107, 192, 106,  48,  20, 16, "Testigo"  , BLUE , WHITE, 2 }; ee.writeBlock(13 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {14, 0,  5, 0,  0, 213, 192, 107,  48,  10, 16, "Indicad"  , BLUE , WHITE, 2 }; ee.writeBlock(14 * sizeof(boton), (uint8_t *) &boton, sizeof(boton));
     boton = (boton_type) {45, 9, 16, 0,  0,   0,   0, 320, 240,   0,  0, " "        , BLACK, WHITE, 2 }; ee.writeBlock(45*sizeof(boton), (uint8_t *) &boton, sizeof(boton));
  */
  pinMode(SDC, INPUT);
  clk_en = rtc.begin();
  tft.begin(0x9341);
  tft.setRotation(mcu_rotate);
  tft.backlight(true);
  // tft.buzzer(3);
  tft.led(true);
  screen(9);
}

void mcu_loop(void) {
  if (Mil > dMil + 1000) {
    dMil = Mil;
    if (Scr == 8) screen(8, Opc);
  }
  
  TSPoint p = ts.getPoint();
  pinMode(YP, OUTPUT);
  pinMode(XM, OUTPUT);
  
  if (p.z > 200 && p.z < 1500) {
#ifdef mcu_touch_inverted_x
    p.x = map(p.x, 70, 900, tft.width(), 0);
#else
    p.x = map(p.x, 70, 900, 0, tft.width());
#endif
#ifdef mcu_touch_inverted_y
    p.y =  map(p.y, 120, 900,  tft.height(), 0);
#else
    p.y =  map(p.y, 120, 900, 0, tft.height());
#endif
    db(p.x, p.y);
  }
  
  if (digitalRead(SDC)) {
    for (byte i = 1; i <= 5; i++)
      send_byte(1, i, -1);
    for (byte i = 0; i <= 54; i++)
      scrb(i, -1, false);
  }
}

void Pass() {
  scrb(15, RED);
  if (pass == "0304") {
    pass = "";
    screen(0);
  }
  if (pass.length() >= 4) {
    pass = "";
    scrb(15);
    tft.buzzer(3);
  }
}
