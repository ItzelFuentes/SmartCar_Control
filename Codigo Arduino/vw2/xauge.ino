void ringMeter(int value, int rti, int rtf, int vmin, int vmax, int x, int y, int r, char *units, byte scheme) {
  int valor;
  x += r; y += r;   
  int w = r / 4;   
  int angle = 150;  
  int text_colour = 0; // To hold the text colour
  int v = map(value, vmin, vmax, -angle, angle);
  byte seg = 5; // Segments are 5 degrees wide = 60 segments for 300 degrees
  byte inc = 5; // Draw segments every 5 degrees, increase to 10 for segmented ring
  for (int i = -angle; i < angle; i += inc) {
    mcu_loop();
    if (Scr!=8) return;
    int colour = 0;
    switch (scheme) {
      case 0: colour = RED; break;
      case 1: colour = GREEN; break;
      case 2: colour = BLUE; break;
      case 3: colour = rainbow(map(i, -angle, angle, 0, 127)); break; 
      case 4: colour = rainbow(map(i, -angle, angle, 63, 127)); break; 
      case 5: colour = rainbow(map(i, -angle, angle, 127, 63)); break; 
      default: colour = BLUE; break; // Fixed colour
    }
    float sx = cos((i - 90) * 0.0174532925);
    float sy = sin((i - 90) * 0.0174532925);
    uint16_t x0 = sx * (r - w) + x;
    uint16_t y0 = sy * (r - w) + y;
    uint16_t x1 = sx * r + x;
    uint16_t y1 = sy * r + y;
    float sx2 = cos((i + seg - 90) * 0.0174532925);
    float sy2 = sin((i + seg - 90) * 0.0174532925);
    int x2 = sx2 * (r - w) + x;
    int y2 = sy2 * (r - w) + y;
    int x3 = sx2 * r + x;
    int y3 = sy2 * r + y;

    if (i < v) { // Fill in coloured segments with 2 triangles
      tft.fillTriangle(x0, y0, x1, y1, x2, y2, colour);
      tft.fillTriangle(x1, y1, x2, y2, x3, y3, colour);
      text_colour = colour; // Save the last colour drawn
    }
    else 
    {
      tft.fillTriangle(x0, y0, x1, y1, x2, y2, GREY);
      tft.fillTriangle(x1, y1, x2, y2, x3, y3, GREY);
    }
  }

  valor = map(value, vmin, vmax, rti, rtf);
  char buf[10];
  byte len = 4;
  if (valor > 999) len = 5;
  dtostrf(valor, len, 0, buf);
  showmsgXY(x - 50, y - 20, 4,text_colour,BLACK,buf);
  showmsgXY(x - 35, y + 15, 2,WHITE,BLACK, units);
}

unsigned int rainbow(byte value) {
  byte red = 0; // Red is the top 5 bits of a 16 bit colour value
  byte green = 0;// Green is the middle 6 bits
  byte blue = 0; // Blue is the bottom 5 bits
  byte quadrant = value / 32;

  if (quadrant == 0) {
    blue = 31;
    green = 2 * (value % 32);
    red = 0;
  }
  if (quadrant == 1) {
    blue = 31 - (value % 32);
    green = 63;
    red = 0;
  }
  if (quadrant == 2) {
    blue = 0;
    green = 63;
    red = value % 32;
  }
  if (quadrant == 3) {
    blue = 0;
    green = 63 - 2 * (value % 32);
    red = 31;
  }
  return (red << 11) + (green << 5) + blue;
}

void showmsgXY(int x, int y, int sz, int tc,int fc, const char *msg) {
  int16_t x1, y1;
  uint16_t wid, ht;
  tft.setTextColor(tc, fc);
  tft.setCursor(x, y);
  tft.setTextSize(sz);
  tft.println(msg);
}
