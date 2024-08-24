package anywheresoftware.b4a.samples.serial.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_1{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[1/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 3;BA.debugLine="Panel1.HorizontalCenter=50%x"[1/General script]
views.get("panel1").vw.setLeft((int)((50d / 100 * width) - (views.get("panel1").vw.getWidth() / 2)));
//BA.debugLineNum = 4;BA.debugLine="Panel2.HorizontalCenter=50%x"[1/General script]
views.get("panel2").vw.setLeft((int)((50d / 100 * width) - (views.get("panel2").vw.getWidth() / 2)));
//BA.debugLineNum = 5;BA.debugLine="Panel2.VerticalCenter=50%y"[1/General script]
views.get("panel2").vw.setTop((int)((50d / 100 * height) - (views.get("panel2").vw.getHeight() / 2)));
//BA.debugLineNum = 6;BA.debugLine="Panel3.HorizontalCenter=50%x"[1/General script]
views.get("panel3").vw.setLeft((int)((50d / 100 * width) - (views.get("panel3").vw.getWidth() / 2)));
//BA.debugLineNum = 7;BA.debugLine="Panel3.VerticalCenter=50%y"[1/General script]
views.get("panel3").vw.setTop((int)((50d / 100 * height) - (views.get("panel3").vw.getHeight() / 2)));
//BA.debugLineNum = 8;BA.debugLine="Panel4.HorizontalCenter=50%x"[1/General script]
views.get("panel4").vw.setLeft((int)((50d / 100 * width) - (views.get("panel4").vw.getWidth() / 2)));
//BA.debugLineNum = 9;BA.debugLine="Panel4.VerticalCenter=50%y"[1/General script]
views.get("panel4").vw.setTop((int)((50d / 100 * height) - (views.get("panel4").vw.getHeight() / 2)));
//BA.debugLineNum = 10;BA.debugLine="Panel5.HorizontalCenter=50%x"[1/General script]
views.get("panel5").vw.setLeft((int)((50d / 100 * width) - (views.get("panel5").vw.getWidth() / 2)));
//BA.debugLineNum = 11;BA.debugLine="Panel6.HorizontalCenter=50%x"[1/General script]
views.get("panel6").vw.setLeft((int)((50d / 100 * width) - (views.get("panel6").vw.getWidth() / 2)));
//BA.debugLineNum = 12;BA.debugLine="Panel6.VerticalCenter=50%y"[1/General script]
views.get("panel6").vw.setTop((int)((50d / 100 * height) - (views.get("panel6").vw.getHeight() / 2)));
//BA.debugLineNum = 13;BA.debugLine="Panel7.HorizontalCenter=50%x"[1/General script]
views.get("panel7").vw.setLeft((int)((50d / 100 * width) - (views.get("panel7").vw.getWidth() / 2)));
//BA.debugLineNum = 14;BA.debugLine="Panel7.VerticalCenter=50%y"[1/General script]
views.get("panel7").vw.setTop((int)((50d / 100 * height) - (views.get("panel7").vw.getHeight() / 2)));

}
}