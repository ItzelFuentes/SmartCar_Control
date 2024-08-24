#include <Wire.h>
#include "iButton.h"
#include <EEPROM.h>
#include <MeanFilterLib.h>

#define SDA   20
#define SCL   21
#define SDC    7

#define PK1   40 //park sensor 1   
#define PK2   41 //park sensor 2   
#define vol   66 //Res 10K Res 4K7  
#define tem   67 //Res 2K Res 1K
#define gas   68
#define ldr   69

#define Device1 0x17

typedef struct {
  word luz_low;
  word seg_time;
  word mar_time;
  word pro_time;
  float TDE;
  word rpm_long;
  word ret_time;
  word sir_time;
  word luz_high;
  word Vel_ticks;
  word Rpm_ticks;
  word gas_low;
  word gas_high;
  word auto_Start;
  word ldr_low;
  word ldr_high;
} Config;

Config set_config;

MeanFilter<long> FGas(20);

word State_in, Last_in ;
word State_out, Last_out;
word State_aux, Last_aux;
word State_flag, Last_flag;
word State_gas, Last_gas;

unsigned long dMil=0;

volatile word  Vel = 0;
volatile unsigned long Vel_time;
volatile word  Vel_ticks;
volatile word  Vel_tick = 1;
volatile word  Rpm = 0;
volatile unsigned long Rpm_time;
volatile word  Rpm_ticks;
volatile word  Rpm_tick = 1;
float  pi = 3.141592654;


word message;
bool S = false;
byte buf[6];

iButton drive(32);

union fb {
  float flt;
  byte u8[sizeof(flt)];
} myfb;

union wb {
  word flt;
  byte u8[sizeof(flt)];
} mywb ;

void setup() {
  Serial.begin(9600);
  Wire.begin(Device1);
  Wire.onRequest(Req);
  Wire.onReceive(Rec);
  pinMode(18, INPUT_PULLUP);
  pinMode(19, INPUT_PULLUP);
  pinMode( SDC, OUTPUT);
  digitalWrite(SDC, LOW);

  attachInterrupt(digitalPinToInterrupt(18), Rpm_Event, FALLING);
  attachInterrupt(digitalPinToInterrupt(19), Vel_Event, FALLING);

  EEPROM.get( 0, set_config);

  drive.subscribe( 0, OUTPUT, &State_out,  0, 22,true); //Energia
  drive.subscribe( 1, OUTPUT, &State_out,  1, 35,true, set_config.mar_time); //Marcha
  drive.subscribe( 2, OUTPUT, &State_out,  2, 24,true); //Auxiliar
  drive.subscribe( 3, OUTPUT, &State_out,  3, 36,true); //Bloqueo
  drive.subscribe( 4, OUTPUT, &State_out,  4, 26,true); //Claxon
  drive.subscribe( 5, OUTPUT, &State_out,  5, 37,true); //Sirena
  drive.subscribe( 6, OUTPUT, &State_out, 15, 28,true); //Luces Altas
  drive.subscribe( 7, OUTPUT, &State_out,  7, 29,true); //Luces Bajas
  drive.subscribe( 8, OUTPUT, &State_out,  8, 30,true); //Intermitente Izquierda
  drive.subscribe( 9, OUTPUT, &State_out,  9, 31,true); //Intermitente derecha
  drive.subscribe(10, OUTPUT, &State_out, 10, 32,true, set_config.seg_time); //Abie
  drive.subscribe(11, OUTPUT, &State_out, 11, 33,true, set_config.seg_time); //Cer
  drive.subscribe(12, OUTPUT, &State_out, 12, 34,true); //Cuartos
  drive.subscribe(13, OUTPUT, &State_out, 13, 23,true); //undef
  drive.subscribe(14, OUTPUT, &State_out, 14, 25,true); //undef
  drive.subscribe(15, OUTPUT, &State_out,  6, 27,true); //undef
  drive.subscribe(16, OUTPUT, &State_aux,  0, 13,true); //Led
  drive.subscribe(17, OUTPUT, &State_aux,  1, 31,true); //Buzzer
  drive.subscribe(18, FLAG  , &State_flag, 0); //proteccion
  drive.subscribe(19, FLAG  , &State_flag, 1); //Alarma
  drive.subscribe(20, FLAG  , &State_flag, 2); //Intermitentes
  drive.subscribe(21, INPUT_PULLUP, &State_in,  0, 38, true); //Energia
  drive.subscribe(22, INPUT_PULLUP, &State_in,  1, 39, true); //Puerta
  drive.subscribe(23, INPUT_PULLUP, &State_in,  8, 46, true); //Reversa
  drive.subscribe(24, INPUT_PULLUP, &State_in,  3, 41, true); //Boton Pulsador
  drive.subscribe(25, INPUT_PULLUP, &State_in,  9, 47, true); //Sensor de movimiento
  drive.subscribe(26, INPUT_PULLUP, &State_in, 10, 48, true); //Testigo de fallo
  drive.subscribe(27, INPUT_PULLUP, &State_in,  6, 44, true); //Carga de Bateria
  drive.subscribe(28, INPUT_PULLUP, &State_in,  7, 45, true); //Freno de mano
  drive.subscribe(29, INPUT_PULLUP, &State_in,  5, 40, true); //Aceite
  drive.subscribe(30, INPUT_PULLUP, &State_in,  4, 42, true); //Freno de Pie
  drive.subscribe(31, INPUT_PULLUP, &State_in,  2, 43, true); //Embrague

  drive.event( 1, All, Btn1A);
  drive.event(13, All, Btn1A);
  drive.event(13, Changed, Btn1C);
  drive.event(13, Higher, Btn1H);
  drive.event(13, Lower, Btn1L);
  drive.event(13, Rising, Btn1R);
  drive.event(13, Falling, Btn1F);
}

void loop() {
  drive.loop();
  digitalWrite(SDC, Last_aux != State_aux || Last_in != State_in || Last_out != State_out || Last_flag != State_flag||State_gas!=Last_gas);
  unsigned long Mil=millis();
  if (Mil > dMil + 1000) {
   Last_gas=State_gas; 
   State_gas = FGas.AddValue(analogRead(gas));
   Serial.println(State_gas);
   dMil=Mil;
  }
  // Serial.println(State_aux, BIN);
  //0 delay(1000);
  // drive.setState(3,!da);
  // drive.setState(1,da);
  // bitWrite(State_in,1,da);
  // da=!da;
  // byte a = 16;
  // byte b =0;
  // int ret = (b << 8) + a ;
}

void Btn1A() {
}

void Btn1C() {
  //  com.println("change");
}

void Btn1H() {
}

void Btn1L() {
}

void Btn1R() {
  //  com.println("rising");
}

void Btn1F() {
  // com.println("Falling");
}

void Rec() {
  word cont = 0;
  while (Wire.available()) {
    byte c = Wire.read();
    buf[cont] = c;
    cont++;
  }
  switch (buf[0]) {
    case 0:
      memo(buf);
      break;
    case 1:  //Entrada
      switch (buf[1]) {
        case 1:
          mywb.flt = Last_aux =  State_aux;
          break;
        case 2:
          mywb.flt = Last_in =  State_in;
          break;
        case 3:
          mywb.flt = Last_out =  State_out;
          break;
        case 4:
          mywb.flt = Last_flag =  State_flag;
          break;
        case 5:
          mywb.flt = Last_gas =  State_gas;
          break;      
      }
      break;
    case 2:  //Salida
      switch (buf[1]) {
        case 1:
          mywb.flt = Last_aux = State_aux = word(buf[3], buf[2]);
          break;
        case 3:
          mywb.flt = Last_out = State_out = word(buf[3], buf[2]);
          break;
        case 4:
          mywb.flt = Last_flag = State_flag = word(buf[3], buf[2]);
          break;
      }
      break;
  }
}

void Req() {
  Wire.write(mywb.u8, sizeof(mywb.u8) / sizeof(mywb.u8[0]));
}

void memo(byte Buf[]) {
  switch (Buf[1]) {
    case 0:
      set_config.luz_low = word(Buf[3], Buf[2]);
      Serial.println(set_config.luz_low);
      break;
    case 1:
      set_config.seg_time = word(Buf[3], Buf[2]);
      drive.debounce(10, set_config.seg_time);
      drive.debounce(11, set_config.seg_time);
      Serial.println(set_config.seg_time);
      break;
    case 2:
      set_config.mar_time = word(Buf[3], Buf[2]);
      drive.debounce(1, set_config.mar_time);
      Serial.println(set_config.mar_time);
      break;
    case 3:
      set_config.pro_time = word(Buf[3], Buf[2]);
      Serial.println(set_config.pro_time);
      break;
    case 4:
      for (word i = 0; i < sizeof(myfb.u8) / sizeof(myfb.u8[0]); i++)
        myfb.u8[i] = Buf[i + 2];
      set_config.TDE = float(Buf[5], Buf[4], Buf[3], Buf[2]);
      set_config.TDE = myfb.flt;
      Serial.println(set_config.TDE, 4);
      break;
    case 5:
      set_config.rpm_long = word(Buf[3], Buf[2]);
      Serial.println(set_config.rpm_long, DEC);
      break;
    case 6:
      set_config.ret_time = word(Buf[3], Buf[2]);
      Serial.println(set_config.ret_time);
      break;
    case 7:
      set_config.sir_time = word(Buf[3], Buf[2]);
      Serial.println(set_config.sir_time);
      break;
    case 8:
      set_config.luz_high = word(Buf[3], Buf[2]);
      Serial.println( set_config.luz_high);
      break;
    case 9:
      set_config.Vel_ticks = word(Buf[3], Buf[2]);
      Serial.println( set_config.Vel_ticks);
      break;
    case 10:
      set_config.Rpm_ticks = word(Buf[3], Buf[2]);
      Serial.println( set_config.Rpm_ticks);
      break;
    case 11:
      set_config.gas_low = word(Buf[3], Buf[2]);
      Serial.println(set_config.gas_low);
      break;
    case 12:
      set_config.gas_high = word(Buf[3], Buf[2]);
      Serial.println( set_config.gas_high);
      break;
    case 13:
      set_config.auto_Start = word(Buf[3], Buf[2]);
      Serial.println(set_config.auto_Start);
      break;
    case 14:
      set_config.ldr_low = word(Buf[3], Buf[2]);
      Serial.println(set_config.ldr_low);
      break;
    case 15:
      set_config.ldr_high = word(Buf[3], Buf[2]);
      Serial.println(set_config.ldr_high);
      break;
  }
  EEPROM.put(0, set_config);
}

void Rpm_Event(void) {
  Rpm_tick++;
  if (Rpm_tick >= Rpm_ticks) {
    unsigned long mic = micros();
    Rpm = 60000000 / (mic - Rpm_time);
    Rpm_time = mic;
    Rpm_tick = 1;
  }
}

void Vel_Event(void) {
  Vel_tick++;
  if (Vel_tick >= Vel_ticks) {
    unsigned long mic = micros();
    Vel = ((set_config.TDE * PI) / ((float)((mic - Vel_time) / 1000000) * 3.6));
    Vel_time = mic;
    Vel_tick = 1;
  }
}

void VelRpm_loop(void) {
  if ((micros() - Rpm_time) > 1000000) Rpm = 0;
  if ((micros() - Vel_time) > 2000000) Vel = 0;
}
