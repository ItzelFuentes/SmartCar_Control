void comm_loop(void) {
  String answer;
  if (Serial.available() > 0) {
    answer = comm.readString();
    answer = answer.substring(0, answer.length());
  }
  if (answer != "") {
    answer.toLowerCase();

    if (clk_en && answer.indexOf("time") >= 0) {
      if (answer.length() > 6) {
        String dia = answer.substring(4, 6);
        String mes = answer.substring(6, 8);
        String ano = answer.substring(8, 12);
        String hra = answer.substring(12, 14);
        String min = answer.substring(14, 16);
        String seg = answer.substring(16, 18);
        String dow = answer.substring(18);
        rtc.setDate(dia.toInt(), mes.toInt(), ano.toInt());
        rtc.setTime(hra.toInt(), min.toInt(), seg.toInt());
        rtc.setDOW(dow.toInt());
        comm.print(" SET TIME: ");
      }
      comm.print(rtc.getDOWStr());
      comm.print(" ");
      comm.print(rtc.getDateStr());
      comm.print("-");
      comm.print(rtc.getTimeStr());
      comm.print("-");
      comm.print(rtc.getTemp());
      comm.print("°");
      tft.buzzer(3);
    }

    if (answer.indexOf("set") >= 0) {
      String gro = answer.substring(3, 5);
      String pos = answer.substring(5, 7);
      String val = answer.substring(7, 9);
      send_byte(2, gro.toInt(), pos.toInt(), val.toInt());
    }
    
    answer = "";
  }
}
