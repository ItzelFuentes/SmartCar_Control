package anywheresoftware.b4a.samples.serial;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;
    public static boolean dontPause;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "anywheresoftware.b4a.samples.serial", "anywheresoftware.b4a.samples.serial.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(this, processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "anywheresoftware.b4a.samples.serial", "anywheresoftware.b4a.samples.serial.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "anywheresoftware.b4a.samples.serial.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        if (!dontPause)
            BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        else
            BA.LogInfo("** Activity (main) Pause event (activity is not paused). **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        if (!dontPause) {
            processBA.setActivityPaused(true);
            mostCurrent = null;
        }

        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtlog = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtsend = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button4 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button5 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button6 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button7 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel4 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _pro = null;
public anywheresoftware.b4a.objects.LabelWrapper _ala = null;
public anywheresoftware.b4a.objects.LabelWrapper _enc = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel6 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button10 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext1 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext2 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext3 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext4 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel7 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button11 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext6 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext7 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext8 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext5 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext9 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext10 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button12 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext11 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext12 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext13 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _checkbox1 = null;
public static String _gas = "";
public static String _ldr = "";
public anywheresoftware.b4a.objects.LabelWrapper _label21 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label23 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext15 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext14 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext16 = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edittext17 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _checkbox2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button33 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button34 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button20 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button21 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button22 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button23 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button24 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button25 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button26 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button27 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button28 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button29 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button30 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button31 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button32 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button35 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label30 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label31 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label32 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label33 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label34 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label35 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label36 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label37 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label38 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label39 = null;
public b4a.example.dateutils _dateutils = null;
public anywheresoftware.b4a.samples.serial.starter _starter = null;
public anywheresoftware.b4a.samples.serial.xuiviewsutils _xuiviewsutils = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 89;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 90;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 };
 //BA.debugLineNum = 92;BA.debugLine="Activity.LoadLayout(\"1\")";
mostCurrent._activity.LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 93;BA.debugLine="EditText10.Text=Starter.kvs.Get(\"BT_name\")";
mostCurrent._edittext10.setText(BA.ObjectToCharSequence(mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._get("BT_name")));
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 106;BA.debugLine="Sub Activity_Pause(UserClosed As Boolean)";
 //BA.debugLineNum = 107;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 };
 //BA.debugLineNum = 110;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 96;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 101;BA.debugLine="Conec(Starter.Connected)";
_conec(mostCurrent._starter._connected /*boolean*/ );
 //BA.debugLineNum = 104;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 217;BA.debugLine="Private Sub Button1_Click";
 //BA.debugLineNum = 218;BA.debugLine="If Button1.Text=\"Conectar\" Then";
if ((mostCurrent._button1.getText()).equals("Conectar")) { 
 //BA.debugLineNum = 219;BA.debugLine="Button1.Enabled=False";
mostCurrent._button1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 220;BA.debugLine="CallSub2(Starter,\"Conectar\",True)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Conectar",(Object)(anywheresoftware.b4a.keywords.Common.True));
 }else {
 //BA.debugLineNum = 222;BA.debugLine="Button1.Enabled=True";
mostCurrent._button1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 223;BA.debugLine="CallSub2(Starter,\"Conectar\",False)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Conectar",(Object)(anywheresoftware.b4a.keywords.Common.False));
 };
 //BA.debugLineNum = 225;BA.debugLine="End Sub";
return "";
}
public static String  _button10_click() throws Exception{
 //BA.debugLineNum = 276;BA.debugLine="Private Sub Button10_Click";
 //BA.debugLineNum = 277;BA.debugLine="Panel5.Visible=True";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 278;BA.debugLine="Panel1.Visible=True";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 279;BA.debugLine="Panel7.Visible=False";
mostCurrent._panel7.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 280;BA.debugLine="CallSub2(Starter,\"send\",\"bt_send=0\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"send",(Object)("bt_send=0"));
 //BA.debugLineNum = 281;BA.debugLine="End Sub";
return "";
}
public static String  _button11_click() throws Exception{
 //BA.debugLineNum = 283;BA.debugLine="Private Sub Button11_Click";
 //BA.debugLineNum = 284;BA.debugLine="Panel5.Visible=False";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 285;BA.debugLine="Panel1.Visible=False";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 286;BA.debugLine="Panel7.Visible=True";
mostCurrent._panel7.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 287;BA.debugLine="CallSub2(Starter,\"send\",\"bt_send=1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"send",(Object)("bt_send=1"));
 //BA.debugLineNum = 288;BA.debugLine="End Sub";
return "";
}
public static String  _button12_click() throws Exception{
 //BA.debugLineNum = 290;BA.debugLine="Private Sub Button12_Click";
 //BA.debugLineNum = 291;BA.debugLine="DateTime.DateFormat=\"ddMMyyy\"";
anywheresoftware.b4a.keywords.Common.DateTime.setDateFormat("ddMMyyy");
 //BA.debugLineNum = 292;BA.debugLine="DateTime.TimeFormat =\"HHmmss\"";
anywheresoftware.b4a.keywords.Common.DateTime.setTimeFormat("HHmmss");
 //BA.debugLineNum = 293;BA.debugLine="CallSub2(Starter,\"Send\",\"time\" & DateTime.Date( D";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("time"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+BA.NumberToString((anywheresoftware.b4a.keywords.Common.DateTime.GetDayOfWeek(anywheresoftware.b4a.keywords.Common.DateTime.getNow())))));
 //BA.debugLineNum = 294;BA.debugLine="End Sub";
return "";
}
public static String  _button13_click() throws Exception{
 //BA.debugLineNum = 362;BA.debugLine="Private Sub Button13_Click";
 //BA.debugLineNum = 363;BA.debugLine="CallSub2(Starter,\"Send\",\"m34=\"&gas.Trim)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m34="+mostCurrent._gas.trim()));
 //BA.debugLineNum = 364;BA.debugLine="End Sub";
return "";
}
public static String  _button14_click() throws Exception{
 //BA.debugLineNum = 366;BA.debugLine="Private Sub Button14_Click";
 //BA.debugLineNum = 367;BA.debugLine="CallSub2(Starter,\"Send\",\"m36=\"&gas.Trim)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m36="+mostCurrent._gas.trim()));
 //BA.debugLineNum = 368;BA.debugLine="End Sub";
return "";
}
public static String  _button15_click() throws Exception{
 //BA.debugLineNum = 370;BA.debugLine="Private Sub Button15_Click";
 //BA.debugLineNum = 371;BA.debugLine="CallSub2(Starter,\"Send\",\"mem40=\"&ldr.Trim)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("mem40="+mostCurrent._ldr.trim()));
 //BA.debugLineNum = 372;BA.debugLine="End Sub";
return "";
}
public static String  _button16_click() throws Exception{
 //BA.debugLineNum = 374;BA.debugLine="Private Sub Button16_Click";
 //BA.debugLineNum = 375;BA.debugLine="CallSub2(Starter,\"Send\",\"mem42=\"&ldr.Trim)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("mem42="+mostCurrent._ldr.trim()));
 //BA.debugLineNum = 376;BA.debugLine="End Sub";
return "";
}
public static String  _button2_click() throws Exception{
 //BA.debugLineNum = 227;BA.debugLine="Private Sub Button2_Click";
 //BA.debugLineNum = 228;BA.debugLine="Panel1.Visible=False";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 229;BA.debugLine="Panel5.Visible=False";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 230;BA.debugLine="Panel2.Visible=True";
mostCurrent._panel2.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 231;BA.debugLine="End Sub";
return "";
}
public static String  _button20_click() throws Exception{
 //BA.debugLineNum = 402;BA.debugLine="Private Sub Button20_Click";
 //BA.debugLineNum = 403;BA.debugLine="Button20.Color=Colors.Gray";
mostCurrent._button20.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 404;BA.debugLine="CallSub2(Starter,\"Send\",\"set0313-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0313-1"));
 //BA.debugLineNum = 405;BA.debugLine="End Sub";
return "";
}
public static String  _button21_click() throws Exception{
 //BA.debugLineNum = 407;BA.debugLine="Private Sub Button21_Click";
 //BA.debugLineNum = 408;BA.debugLine="Button21.Color=Colors.Gray";
mostCurrent._button21.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 409;BA.debugLine="CallSub2(Starter,\"Send\",\"set0305-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0305-1"));
 //BA.debugLineNum = 410;BA.debugLine="End Sub";
return "";
}
public static String  _button22_click() throws Exception{
 //BA.debugLineNum = 412;BA.debugLine="Private Sub Button22_Click";
 //BA.debugLineNum = 413;BA.debugLine="Button22.Color=Colors.Gray";
mostCurrent._button22.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 414;BA.debugLine="CallSub2(Starter,\"Send\",\"set0311-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0311-1"));
 //BA.debugLineNum = 415;BA.debugLine="End Sub";
return "";
}
public static String  _button23_click() throws Exception{
 //BA.debugLineNum = 417;BA.debugLine="Private Sub Button23_Click";
 //BA.debugLineNum = 418;BA.debugLine="Button23.Color=Colors.Gray";
mostCurrent._button23.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 419;BA.debugLine="CallSub2(Starter,\"Send\",\"set0310-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0310-1"));
 //BA.debugLineNum = 420;BA.debugLine="End Sub";
return "";
}
public static String  _button24_click() throws Exception{
 //BA.debugLineNum = 422;BA.debugLine="Private Sub Button24_Click  'auxiliar";
 //BA.debugLineNum = 423;BA.debugLine="Button24.Color=Colors.Gray";
mostCurrent._button24.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 424;BA.debugLine="CallSub2(Starter,\"Send\",\"set0302-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0302-1"));
 //BA.debugLineNum = 425;BA.debugLine="End Sub";
return "";
}
public static String  _button25_click() throws Exception{
 //BA.debugLineNum = 427;BA.debugLine="Private Sub Button25_Click";
 //BA.debugLineNum = 428;BA.debugLine="Button25.Color=Colors.Gray";
mostCurrent._button25.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 429;BA.debugLine="CallSub2(Starter,\"Send\",\"set0314-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0314-1"));
 //BA.debugLineNum = 430;BA.debugLine="End Sub";
return "";
}
public static String  _button26_click() throws Exception{
 //BA.debugLineNum = 432;BA.debugLine="Private Sub Button26_Click";
 //BA.debugLineNum = 433;BA.debugLine="Button26.Color=Colors.Gray";
mostCurrent._button26.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 434;BA.debugLine="CallSub2(Starter,\"Send\",\"set0307-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0307-1"));
 //BA.debugLineNum = 435;BA.debugLine="End Sub";
return "";
}
public static String  _button27_click() throws Exception{
 //BA.debugLineNum = 437;BA.debugLine="Private Sub Button27_Click";
 //BA.debugLineNum = 438;BA.debugLine="Button27.Color=Colors.Gray";
mostCurrent._button27.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 439;BA.debugLine="CallSub2(Starter,\"Send\",\"set0306-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0306-1"));
 //BA.debugLineNum = 440;BA.debugLine="End Sub";
return "";
}
public static String  _button28_click() throws Exception{
 //BA.debugLineNum = 442;BA.debugLine="Private Sub Button28_Click";
 //BA.debugLineNum = 443;BA.debugLine="Button28.Color=Colors.Gray";
mostCurrent._button28.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 444;BA.debugLine="CallSub2(Starter,\"Send\",\"set0304-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0304-1"));
 //BA.debugLineNum = 445;BA.debugLine="End Sub";
return "";
}
public static String  _button29_click() throws Exception{
 //BA.debugLineNum = 447;BA.debugLine="Private Sub Button29_Click";
 //BA.debugLineNum = 448;BA.debugLine="Button29.Color=Colors.Gray";
mostCurrent._button29.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 449;BA.debugLine="CallSub2(Starter,\"Send\",\"set0309-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0309-1"));
 //BA.debugLineNum = 450;BA.debugLine="End Sub";
return "";
}
public static String  _button3_click() throws Exception{
 //BA.debugLineNum = 233;BA.debugLine="Private Sub Button3_Click";
 //BA.debugLineNum = 234;BA.debugLine="Panel1.Visible=True";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 235;BA.debugLine="Panel5.Visible=True";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 236;BA.debugLine="Panel2.Visible=False";
mostCurrent._panel2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 237;BA.debugLine="End Sub";
return "";
}
public static String  _button30_click() throws Exception{
 //BA.debugLineNum = 452;BA.debugLine="Private Sub Button30_Click";
 //BA.debugLineNum = 453;BA.debugLine="Button30.Color=Colors.Gray";
mostCurrent._button30.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 454;BA.debugLine="CallSub2(Starter,\"Send\",\"set0312-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0312-1"));
 //BA.debugLineNum = 455;BA.debugLine="End Sub";
return "";
}
public static String  _button31_click() throws Exception{
 //BA.debugLineNum = 457;BA.debugLine="Private Sub Button31_Click";
 //BA.debugLineNum = 458;BA.debugLine="Button31.Color=Colors.Gray";
mostCurrent._button31.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 459;BA.debugLine="CallSub2(Starter,\"Send\",\"set0308-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0308-1"));
 //BA.debugLineNum = 460;BA.debugLine="End Sub";
return "";
}
public static String  _button32_click() throws Exception{
 //BA.debugLineNum = 462;BA.debugLine="Private Sub Button32_Click";
 //BA.debugLineNum = 463;BA.debugLine="Button32.Color=Colors.Gray";
mostCurrent._button32.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 464;BA.debugLine="CallSub2(Starter,\"Send\",\"set0303-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0303-1"));
 //BA.debugLineNum = 465;BA.debugLine="End Sub";
return "";
}
public static String  _button33_click() throws Exception{
 //BA.debugLineNum = 467;BA.debugLine="Private Sub Button33_Click";
 //BA.debugLineNum = 468;BA.debugLine="Button33.Color=Colors.Gray";
mostCurrent._button33.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 469;BA.debugLine="CallSub2(Starter,\"Send\",\"set0300-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0300-1"));
 //BA.debugLineNum = 470;BA.debugLine="End Sub";
return "";
}
public static String  _button34_click() throws Exception{
 //BA.debugLineNum = 472;BA.debugLine="Private Sub Button34_Click";
 //BA.debugLineNum = 473;BA.debugLine="Button34.Color=Colors.Gray";
mostCurrent._button34.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 474;BA.debugLine="CallSub2(Starter,\"Send\",\"set0301-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0301-1"));
 //BA.debugLineNum = 475;BA.debugLine="End Sub";
return "";
}
public static String  _button35_click() throws Exception{
 //BA.debugLineNum = 477;BA.debugLine="Private Sub Button35_Click";
 //BA.debugLineNum = 478;BA.debugLine="Button35.Color=Colors.Gray";
mostCurrent._button35.setColor(anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 479;BA.debugLine="CallSub2(Starter,\"Send\",\"set0315-1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0315-1"));
 //BA.debugLineNum = 480;BA.debugLine="End Sub";
return "";
}
public static String  _button4_click() throws Exception{
 //BA.debugLineNum = 239;BA.debugLine="Private Sub Button4_Click";
 //BA.debugLineNum = 240;BA.debugLine="Panel1.Visible=False";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 241;BA.debugLine="Panel5.Visible=False";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 242;BA.debugLine="Panel3.Visible=True";
mostCurrent._panel3.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 243;BA.debugLine="End Sub";
return "";
}
public static String  _button5_click() throws Exception{
 //BA.debugLineNum = 245;BA.debugLine="Private Sub Button5_Click";
 //BA.debugLineNum = 246;BA.debugLine="Panel1.Visible=True";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 247;BA.debugLine="Panel5.Visible=True";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 248;BA.debugLine="Panel3.Visible=False";
mostCurrent._panel3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static String  _button6_click() throws Exception{
 //BA.debugLineNum = 251;BA.debugLine="Private Sub Button6_Click";
 //BA.debugLineNum = 252;BA.debugLine="Panel1.Visible=True";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 253;BA.debugLine="Panel5.Visible=True";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 254;BA.debugLine="Panel4.Visible=False";
mostCurrent._panel4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 255;BA.debugLine="End Sub";
return "";
}
public static String  _button7_click() throws Exception{
 //BA.debugLineNum = 257;BA.debugLine="Private Sub Button7_Click";
 //BA.debugLineNum = 258;BA.debugLine="Panel1.Visible=False";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 259;BA.debugLine="Panel5.Visible=False";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 260;BA.debugLine="Panel4.Visible=True";
mostCurrent._panel4.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 261;BA.debugLine="End Sub";
return "";
}
public static String  _button8_click() throws Exception{
 //BA.debugLineNum = 263;BA.debugLine="Private Sub Button8_Click";
 //BA.debugLineNum = 264;BA.debugLine="Panel1.Visible=True";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 265;BA.debugLine="Panel5.Visible=True";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 266;BA.debugLine="Panel6.Visible=False";
mostCurrent._panel6.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 267;BA.debugLine="End Sub";
return "";
}
public static String  _button9_click() throws Exception{
 //BA.debugLineNum = 269;BA.debugLine="Private Sub Button9_Click";
 //BA.debugLineNum = 270;BA.debugLine="Panel5.Visible=False";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 271;BA.debugLine="Panel1.Visible=False";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 272;BA.debugLine="Panel6.Visible=True";
mostCurrent._panel6.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 273;BA.debugLine="End Sub";
return "";
}
public static String  _cerrar() throws Exception{
 //BA.debugLineNum = 205;BA.debugLine="Private Sub Cerrar";
 //BA.debugLineNum = 206;BA.debugLine="Button1.Text=\"Conectar\"";
mostCurrent._button1.setText(BA.ObjectToCharSequence("Conectar"));
 //BA.debugLineNum = 207;BA.debugLine="EditText10.Visible=True";
mostCurrent._edittext10.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 208;BA.debugLine="Panel1.Visible=True";
mostCurrent._panel1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 209;BA.debugLine="Panel2.Visible=False";
mostCurrent._panel2.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 210;BA.debugLine="Panel3.Visible=False";
mostCurrent._panel3.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 211;BA.debugLine="Panel4.Visible=False";
mostCurrent._panel4.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 212;BA.debugLine="Panel5.Visible=False";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 213;BA.debugLine="Panel6.Visible=False";
mostCurrent._panel6.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 214;BA.debugLine="Panel7.Visible=False";
mostCurrent._panel7.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 215;BA.debugLine="End Sub";
return "";
}
public static String  _checkbox1_click() throws Exception{
 //BA.debugLineNum = 296;BA.debugLine="Private Sub CheckBox1_click";
 //BA.debugLineNum = 297;BA.debugLine="If CheckBox1.Checked Then";
if (mostCurrent._checkbox1.getChecked()) { 
 //BA.debugLineNum = 298;BA.debugLine="CallSub2(Starter,\"Send\",\"mem38=1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("mem38=1"));
 }else {
 //BA.debugLineNum = 300;BA.debugLine="CallSub2(Starter,\"Send\",\"mem38=0\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("mem38=0"));
 };
 //BA.debugLineNum = 302;BA.debugLine="End Sub";
return "";
}
public static String  _checkbox2_click() throws Exception{
 //BA.debugLineNum = 394;BA.debugLine="Private Sub CheckBox2_click";
 //BA.debugLineNum = 395;BA.debugLine="If CheckBox1.Checked Then";
if (mostCurrent._checkbox1.getChecked()) { 
 //BA.debugLineNum = 396;BA.debugLine="CallSub2(Starter,\"Send\",\"set1\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set1"));
 }else {
 //BA.debugLineNum = 398;BA.debugLine="CallSub2(Starter,\"Send\",\"set0\")";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("set0"));
 };
 //BA.debugLineNum = 400;BA.debugLine="End Sub";
return "";
}
public static String  _conec(boolean _cmd) throws Exception{
 //BA.debugLineNum = 192;BA.debugLine="Sub Conec(cmd As Boolean)";
 //BA.debugLineNum = 193;BA.debugLine="Button1.Enabled=True";
mostCurrent._button1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 194;BA.debugLine="If cmd=True Then";
if (_cmd==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 195;BA.debugLine="Button1.Text=\"Desconectar\"";
mostCurrent._button1.setText(BA.ObjectToCharSequence("Desconectar"));
 //BA.debugLineNum = 196;BA.debugLine="EditText10.Visible=False";
mostCurrent._edittext10.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 197;BA.debugLine="Panel5.Visible=True";
mostCurrent._panel5.setVisible(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 199;BA.debugLine="Button1.Text=\"Conectar\"";
mostCurrent._button1.setText(BA.ObjectToCharSequence("Conectar"));
 //BA.debugLineNum = 200;BA.debugLine="EditText10.Visible=True";
mostCurrent._edittext10.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 201;BA.debugLine="Cerrar";
_cerrar();
 };
 //BA.debugLineNum = 203;BA.debugLine="End Sub";
return "";
}
public static String  _edittext1_enterpressed() throws Exception{
 //BA.debugLineNum = 304;BA.debugLine="Private Sub EditText1_EnterPressed";
 //BA.debugLineNum = 305;BA.debugLine="CallSub2(Starter,\"Send\",\"m0=\"&EditText1.Text.Trim";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m0="+mostCurrent._edittext1.getText().trim()));
 //BA.debugLineNum = 306;BA.debugLine="End Sub";
return "";
}
public static String  _edittext10_enterpressed() throws Exception{
 //BA.debugLineNum = 340;BA.debugLine="Private Sub EditText10_EnterPressed";
 //BA.debugLineNum = 341;BA.debugLine="Starter.kvs.Put(\"BT_name\",EditText10.Text)";
mostCurrent._starter._kvs /*b4a.example3.keyvaluestore*/ ._put("BT_name",(Object)(mostCurrent._edittext10.getText()));
 //BA.debugLineNum = 342;BA.debugLine="ToastMessageShow(\"Guardado\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Guardado"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 343;BA.debugLine="End Sub";
return "";
}
public static String  _edittext11_enterpressed() throws Exception{
 //BA.debugLineNum = 345;BA.debugLine="Private Sub EditText11_EnterPressed";
 //BA.debugLineNum = 346;BA.debugLine="CallSub2(Starter,\"Send\",\"m28=\"&EditText11.Text.Tr";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m28="+mostCurrent._edittext11.getText().trim()));
 //BA.debugLineNum = 347;BA.debugLine="End Sub";
return "";
}
public static String  _edittext12_enterpressed() throws Exception{
 //BA.debugLineNum = 349;BA.debugLine="Private Sub EditText12_EnterPressed";
 //BA.debugLineNum = 350;BA.debugLine="CallSub2(Starter,\"Send\",\"m30=\"&EditText12.Text.Tr";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m30="+mostCurrent._edittext12.getText().trim()));
 //BA.debugLineNum = 351;BA.debugLine="End Sub";
return "";
}
public static String  _edittext13_enterpressed() throws Exception{
 //BA.debugLineNum = 353;BA.debugLine="Private Sub EditText13_EnterPressed";
 //BA.debugLineNum = 354;BA.debugLine="CallSub2(Starter,\"Send\",\"m32=\"&EditText13.Text.Tr";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m32="+mostCurrent._edittext13.getText().trim()));
 //BA.debugLineNum = 355;BA.debugLine="End Sub";
return "";
}
public static String  _edittext14_enterpressed() throws Exception{
 //BA.debugLineNum = 378;BA.debugLine="Private Sub EditText14_EnterPressed";
 //BA.debugLineNum = 379;BA.debugLine="CallSub2(Starter,\"Send\",\"mem34=\"&EditText14.text.";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("mem34="+mostCurrent._edittext14.getText().trim()));
 //BA.debugLineNum = 380;BA.debugLine="End Sub";
return "";
}
public static String  _edittext15_enterpressed() throws Exception{
 //BA.debugLineNum = 382;BA.debugLine="Private Sub EditText15_EnterPressed";
 //BA.debugLineNum = 383;BA.debugLine="CallSub2(Starter,\"Send\",\"mem36=\"&EditText15.text.";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("mem36="+mostCurrent._edittext15.getText().trim()));
 //BA.debugLineNum = 384;BA.debugLine="End Sub";
return "";
}
public static String  _edittext16_enterpressed() throws Exception{
 //BA.debugLineNum = 386;BA.debugLine="Private Sub EditText16_EnterPressed";
 //BA.debugLineNum = 387;BA.debugLine="CallSub2(Starter,\"Send\",\"mem40=\"&EditText16.text.";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("mem40="+mostCurrent._edittext16.getText().trim()));
 //BA.debugLineNum = 388;BA.debugLine="End Sub";
return "";
}
public static String  _edittext17_enterpressed() throws Exception{
 //BA.debugLineNum = 390;BA.debugLine="Private Sub EditText17_EnterPressed";
 //BA.debugLineNum = 391;BA.debugLine="CallSub2(Starter,\"Send\",\"mem42=\"&EditText17.text.";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("mem42="+mostCurrent._edittext17.getText().trim()));
 //BA.debugLineNum = 392;BA.debugLine="End Sub";
return "";
}
public static String  _edittext2_enterpressed() throws Exception{
 //BA.debugLineNum = 308;BA.debugLine="Private Sub EditText2_EnterPressed";
 //BA.debugLineNum = 309;BA.debugLine="CallSub2(Starter,\"Send\",\"m2=\"&EditText2.Text.Trim";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m2="+mostCurrent._edittext2.getText().trim()));
 //BA.debugLineNum = 310;BA.debugLine="End Sub";
return "";
}
public static String  _edittext3_enterpressed() throws Exception{
 //BA.debugLineNum = 312;BA.debugLine="Private Sub EditText3_EnterPressed";
 //BA.debugLineNum = 313;BA.debugLine="CallSub2(Starter,\"Send\",\"m4=\"&EditText3.Text.Trim";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m4="+mostCurrent._edittext3.getText().trim()));
 //BA.debugLineNum = 314;BA.debugLine="End Sub";
return "";
}
public static String  _edittext4_enterpressed() throws Exception{
 //BA.debugLineNum = 316;BA.debugLine="Private Sub EditText4_EnterPressed";
 //BA.debugLineNum = 317;BA.debugLine="CallSub2(Starter,\"Send\",\"m6=\"&EditText4.Text.Trim";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m6="+mostCurrent._edittext4.getText().trim()));
 //BA.debugLineNum = 318;BA.debugLine="End Sub";
return "";
}
public static String  _edittext5_enterpressed() throws Exception{
 //BA.debugLineNum = 320;BA.debugLine="Private Sub EditText5_EnterPressed";
 //BA.debugLineNum = 321;BA.debugLine="CallSub2(Starter,\"Send\",\"m8=\"&EditText5.Text.Trim";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m8="+mostCurrent._edittext5.getText().trim()));
 //BA.debugLineNum = 322;BA.debugLine="End Sub";
return "";
}
public static String  _edittext6_enterpressed() throws Exception{
 //BA.debugLineNum = 324;BA.debugLine="Private Sub EditText6_EnterPressed";
 //BA.debugLineNum = 325;BA.debugLine="CallSub2(Starter,\"Send\",\"m13=\"&EditText6.Text.Tri";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m13="+mostCurrent._edittext6.getText().trim()));
 //BA.debugLineNum = 326;BA.debugLine="End Sub";
return "";
}
public static String  _edittext7_enterpressed() throws Exception{
 //BA.debugLineNum = 328;BA.debugLine="Private Sub EditText7_EnterPressed";
 //BA.debugLineNum = 329;BA.debugLine="CallSub2(Starter,\"Send\",\"m19=\"&EditText7.Text.Tri";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m19="+mostCurrent._edittext7.getText().trim()));
 //BA.debugLineNum = 330;BA.debugLine="End Sub";
return "";
}
public static String  _edittext8_enterpressed() throws Exception{
 //BA.debugLineNum = 332;BA.debugLine="Private Sub EditText8_EnterPressed";
 //BA.debugLineNum = 333;BA.debugLine="CallSub2(Starter,\"Send\",\"m21=\"&EditText8.Text.Tri";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m21="+mostCurrent._edittext8.getText().trim()));
 //BA.debugLineNum = 334;BA.debugLine="End Sub";
return "";
}
public static String  _edittext9_enterpressed() throws Exception{
 //BA.debugLineNum = 336;BA.debugLine="Private Sub EditText9_EnterPressed";
 //BA.debugLineNum = 337;BA.debugLine="CallSub2(Starter,\"Send\",\"m23=\"&EditText9.Text.Tri";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)("m23="+mostCurrent._edittext9.getText().trim()));
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return "";
}
public static boolean  _getbit(int _b,int _index) throws Exception{
int _t = 0;
 //BA.debugLineNum = 112;BA.debugLine="Sub GetBit(b As Int, index As Int) As Boolean";
 //BA.debugLineNum = 113;BA.debugLine="Dim t As Int = Bit.ShiftLeft(1, index)";
_t = anywheresoftware.b4a.keywords.Common.Bit.ShiftLeft((int) (1),_index);
 //BA.debugLineNum = 114;BA.debugLine="Return Bit.And(b, t) = t";
if (true) return anywheresoftware.b4a.keywords.Common.Bit.And(_b,_t)==_t;
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return false;
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 16;BA.debugLine="Private txtLog As EditText";
mostCurrent._txtlog = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Private txtSend As EditText";
mostCurrent._txtsend = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private Button1 As Button";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private Button3 As Button";
mostCurrent._button3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private Button2 As Button";
mostCurrent._button2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private Panel2 As Panel";
mostCurrent._panel2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Private Button4 As Button";
mostCurrent._button4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private Panel3 As Panel";
mostCurrent._panel3 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private Button5 As Button";
mostCurrent._button5 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private Button6 As Button";
mostCurrent._button6 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private Button7 As Button";
mostCurrent._button7 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private Panel4 As Panel";
mostCurrent._panel4 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private Panel5 As Panel";
mostCurrent._panel5 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private Pro As Label";
mostCurrent._pro = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private Ala As Label";
mostCurrent._ala = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private Enc As Label";
mostCurrent._enc = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private Panel6 As Panel";
mostCurrent._panel6 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private Button10 As Button";
mostCurrent._button10 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private EditText1 As EditText";
mostCurrent._edittext1 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private EditText2 As EditText";
mostCurrent._edittext2 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private EditText3 As EditText";
mostCurrent._edittext3 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private EditText4 As EditText";
mostCurrent._edittext4 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private Panel7 As Panel";
mostCurrent._panel7 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private Button11 As Button";
mostCurrent._button11 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private EditText6 As EditText";
mostCurrent._edittext6 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private EditText7 As EditText";
mostCurrent._edittext7 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private EditText8 As EditText";
mostCurrent._edittext8 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private EditText5 As EditText";
mostCurrent._edittext5 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 45;BA.debugLine="Private EditText9 As EditText";
mostCurrent._edittext9 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private EditText10 As EditText";
mostCurrent._edittext10 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 47;BA.debugLine="Private Button12 As Button";
mostCurrent._button12 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private EditText11 As EditText";
mostCurrent._edittext11 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 49;BA.debugLine="Private EditText12 As EditText";
mostCurrent._edittext12 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Private EditText13 As EditText";
mostCurrent._edittext13 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 51;BA.debugLine="Private CheckBox1 As CheckBox";
mostCurrent._checkbox1 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private gas As String";
mostCurrent._gas = "";
 //BA.debugLineNum = 53;BA.debugLine="Private ldr As String";
mostCurrent._ldr = "";
 //BA.debugLineNum = 54;BA.debugLine="Private Label21 As Label";
mostCurrent._label21 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 55;BA.debugLine="Private Label23 As Label";
mostCurrent._label23 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 56;BA.debugLine="Private EditText15 As EditText";
mostCurrent._edittext15 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 57;BA.debugLine="Private EditText14 As EditText";
mostCurrent._edittext14 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 58;BA.debugLine="Private EditText16 As EditText";
mostCurrent._edittext16 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 59;BA.debugLine="Private EditText17 As EditText";
mostCurrent._edittext17 = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 60;BA.debugLine="Private CheckBox2 As CheckBox";
mostCurrent._checkbox2 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 61;BA.debugLine="Private Button33 As Button";
mostCurrent._button33 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Private Button34 As Button";
mostCurrent._button34 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private Button20 As Button";
mostCurrent._button20 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private Button21 As Button";
mostCurrent._button21 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 65;BA.debugLine="Private Button22 As Button";
mostCurrent._button22 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 66;BA.debugLine="Private Button23 As Button";
mostCurrent._button23 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 67;BA.debugLine="Private Button24 As Button";
mostCurrent._button24 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Private Button25 As Button";
mostCurrent._button25 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 69;BA.debugLine="Private Button26 As Button";
mostCurrent._button26 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 70;BA.debugLine="Private Button27 As Button";
mostCurrent._button27 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 71;BA.debugLine="Private Button28 As Button";
mostCurrent._button28 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 72;BA.debugLine="Private Button29 As Button";
mostCurrent._button29 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 73;BA.debugLine="Private Button30 As Button";
mostCurrent._button30 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 74;BA.debugLine="Private Button31 As Button";
mostCurrent._button31 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 75;BA.debugLine="Private Button32 As Button";
mostCurrent._button32 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 76;BA.debugLine="Private Button35 As Button";
mostCurrent._button35 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 77;BA.debugLine="Private Label30 As Label";
mostCurrent._label30 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 78;BA.debugLine="Private Label31 As Label";
mostCurrent._label31 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 79;BA.debugLine="Private Label32 As Label";
mostCurrent._label32 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 80;BA.debugLine="Private Label33 As Label";
mostCurrent._label33 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 81;BA.debugLine="Private Label34 As Label";
mostCurrent._label34 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 82;BA.debugLine="Private Label35 As Label";
mostCurrent._label35 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 83;BA.debugLine="Private Label36 As Label";
mostCurrent._label36 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 84;BA.debugLine="Private Label37 As Label";
mostCurrent._label37 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 85;BA.debugLine="Private Label38 As Label";
mostCurrent._label38 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 86;BA.debugLine="Private Label39 As Label";
mostCurrent._label39 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        b4a.example.dateutils._process_globals();
main._process_globals();
starter._process_globals();
xuiviewsutils._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 13;BA.debugLine="End Sub";
return "";
}
public static String  _rellena(String _data) throws Exception{
String _idx = "";
String _val = "";
int _jorge = 0;
 //BA.debugLineNum = 117;BA.debugLine="Sub Rellena(data As String)";
 //BA.debugLineNum = 118;BA.debugLine="Dim idx,val As String";
_idx = "";
_val = "";
 //BA.debugLineNum = 119;BA.debugLine="If data.IndexOf(\"get03\")>=0 Then";
if (_data.indexOf("get03")>=0) { 
 //BA.debugLineNum = 120;BA.debugLine="Dim  jorge=data.substring(5) As Int";
_jorge = (int)(Double.parseDouble(_data.substring((int) (5))));
 //BA.debugLineNum = 121;BA.debugLine="If GetBit(jorge,0) Then	Button33.Color=Colors.Gr";
if (_getbit(_jorge,(int) (0))) { 
mostCurrent._button33.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button33.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 122;BA.debugLine="If GetBit(jorge,1) Then	Button34.Color=Colors.Gr";
if (_getbit(_jorge,(int) (1))) { 
mostCurrent._button34.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button34.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 123;BA.debugLine="If GetBit(jorge,2) Then	Button24.Color=Colors.Gr";
if (_getbit(_jorge,(int) (2))) { 
mostCurrent._button24.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button24.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 124;BA.debugLine="If GetBit(jorge,3) Then	Button32.Color=Colors.Gr";
if (_getbit(_jorge,(int) (3))) { 
mostCurrent._button32.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button32.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 125;BA.debugLine="If GetBit(jorge,4) Then	Button28.Color=Colors.Gr";
if (_getbit(_jorge,(int) (4))) { 
mostCurrent._button28.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button28.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 126;BA.debugLine="If GetBit(jorge,5) Then	Button21.Color=Colors.Gr";
if (_getbit(_jorge,(int) (5))) { 
mostCurrent._button21.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button21.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 127;BA.debugLine="If GetBit(jorge,6) Then	Button27.Color=Colors.Gr";
if (_getbit(_jorge,(int) (6))) { 
mostCurrent._button27.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button27.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 128;BA.debugLine="If GetBit(jorge,7) Then	Button26.Color=Colors.Gr";
if (_getbit(_jorge,(int) (7))) { 
mostCurrent._button26.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button26.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 129;BA.debugLine="If GetBit(jorge,8) Then	Button31.Color=Colors.Gr";
if (_getbit(_jorge,(int) (8))) { 
mostCurrent._button31.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button31.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 130;BA.debugLine="If GetBit(jorge,9) Then Button29.Color=Colors.Gr";
if (_getbit(_jorge,(int) (9))) { 
mostCurrent._button29.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button29.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 131;BA.debugLine="If GetBit(jorge,10) Then Button23.Color=Colors.G";
if (_getbit(_jorge,(int) (10))) { 
mostCurrent._button23.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button23.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 132;BA.debugLine="If GetBit(jorge,11) Then Button22.Color=Colors.G";
if (_getbit(_jorge,(int) (11))) { 
mostCurrent._button22.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button22.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 133;BA.debugLine="If GetBit(jorge,12) Then Button30.Color=Colors.G";
if (_getbit(_jorge,(int) (12))) { 
mostCurrent._button30.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button30.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 134;BA.debugLine="If GetBit(jorge,13) Then Button20.Color=Colors.G";
if (_getbit(_jorge,(int) (13))) { 
mostCurrent._button20.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button20.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 135;BA.debugLine="If GetBit(jorge,14) Then Button25.Color=Colors.G";
if (_getbit(_jorge,(int) (14))) { 
mostCurrent._button25.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button25.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 136;BA.debugLine="If GetBit(jorge,15) Then Button35.Color=Colors.G";
if (_getbit(_jorge,(int) (15))) { 
mostCurrent._button35.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._button35.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 };
 //BA.debugLineNum = 138;BA.debugLine="If data.IndexOf(\"get02\")>=0 Then";
if (_data.indexOf("get02")>=0) { 
 //BA.debugLineNum = 139;BA.debugLine="Dim  jorge=data.substring(5) As Int";
_jorge = (int)(Double.parseDouble(_data.substring((int) (5))));
 //BA.debugLineNum = 140;BA.debugLine="If GetBit(jorge,0) Then	Label37.Color=Colors.Gre";
if (_getbit(_jorge,(int) (0))) { 
mostCurrent._label37.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._label37.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 141;BA.debugLine="If GetBit(jorge,1) Then	Label35.Color=Colors.Gre";
if (_getbit(_jorge,(int) (1))) { 
mostCurrent._label35.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._label35.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 142;BA.debugLine="If GetBit(jorge,2) Then	Label34.Color=Colors.Gre";
if (_getbit(_jorge,(int) (2))) { 
mostCurrent._label34.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._label34.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 143;BA.debugLine="If GetBit(jorge,3) Then	Label39.Color=Colors.Gre";
if (_getbit(_jorge,(int) (3))) { 
mostCurrent._label39.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._label39.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 144;BA.debugLine="If GetBit(jorge,4) Then	Label32.Color=Colors.Gre";
if (_getbit(_jorge,(int) (4))) { 
mostCurrent._label32.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._label32.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 145;BA.debugLine="If GetBit(jorge,5) Then Label30.Color=Colors.Gre";
if (_getbit(_jorge,(int) (5))) { 
mostCurrent._label30.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._label30.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 146;BA.debugLine="If GetBit(jorge,6) Then	Label33.Color=Colors.Gre";
if (_getbit(_jorge,(int) (6))) { 
mostCurrent._label33.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._label33.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 147;BA.debugLine="If GetBit(jorge,7) Then	Label31.Color=Colors.Gre";
if (_getbit(_jorge,(int) (7))) { 
mostCurrent._label31.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._label31.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 148;BA.debugLine="If GetBit(jorge,8) Then	Label38.Color=Colors.Gre";
if (_getbit(_jorge,(int) (8))) { 
mostCurrent._label38.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._label38.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 //BA.debugLineNum = 149;BA.debugLine="If GetBit(jorge,9) Then Label36.Color=Colors.Gre";
if (_getbit(_jorge,(int) (9))) { 
mostCurrent._label36.setColor(anywheresoftware.b4a.keywords.Common.Colors.Green);}
else {
mostCurrent._label36.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);};
 };
 //BA.debugLineNum = 152;BA.debugLine="If data.IndexOf(\"Set1\")>=0 Then CheckBox2.Checked";
if (_data.indexOf("Set1")>=0) { 
mostCurrent._checkbox2.setChecked(anywheresoftware.b4a.keywords.Common.True);};
 //BA.debugLineNum = 153;BA.debugLine="If data.IndexOf(\"Set0\")>=0 Then CheckBox2.Checked";
if (_data.indexOf("Set0")>=0) { 
mostCurrent._checkbox2.setChecked(anywheresoftware.b4a.keywords.Common.False);};
 //BA.debugLineNum = 154;BA.debugLine="If data.IndexOf(\"Gas\")>=0 Then";
if (_data.indexOf("Gas")>=0) { 
 //BA.debugLineNum = 155;BA.debugLine="gas=data.SubString2(3,data.Length)";
mostCurrent._gas = _data.substring((int) (3),_data.length());
 //BA.debugLineNum = 156;BA.debugLine="Label21.Text=\"Gas:\"&gas.Trim";
mostCurrent._label21.setText(BA.ObjectToCharSequence("Gas:"+mostCurrent._gas.trim()));
 };
 //BA.debugLineNum = 158;BA.debugLine="If data.IndexOf(\"LDR\")>=0 Then";
if (_data.indexOf("LDR")>=0) { 
 //BA.debugLineNum = 159;BA.debugLine="ldr=data.SubString2(3,data.Length)";
mostCurrent._ldr = _data.substring((int) (3),_data.length());
 //BA.debugLineNum = 160;BA.debugLine="Label23.Text=\"LDR:\"&ldr.Trim";
mostCurrent._label23.setText(BA.ObjectToCharSequence("LDR:"+mostCurrent._ldr.trim()));
 };
 //BA.debugLineNum = 162;BA.debugLine="If data.IndexOf(\"Mem\")>=0 Then";
if (_data.indexOf("Mem")>=0) { 
 //BA.debugLineNum = 163;BA.debugLine="idx=data.SubString2(3,data.IndexOf(\"=\"))";
_idx = _data.substring((int) (3),_data.indexOf("="));
 //BA.debugLineNum = 164;BA.debugLine="val=data.SubString2(data.IndexOf(\"=\")+1,data.Len";
_val = _data.substring((int) (_data.indexOf("=")+1),_data.length());
 //BA.debugLineNum = 165;BA.debugLine="If idx=\"0\" Then EditText1.Text=val";
if ((_idx).equals("0")) { 
mostCurrent._edittext1.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 166;BA.debugLine="If idx=\"2\" Then EditText2.Text=val";
if ((_idx).equals("2")) { 
mostCurrent._edittext2.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 167;BA.debugLine="If idx=\"4\" Then EditText3.Text=val";
if ((_idx).equals("4")) { 
mostCurrent._edittext3.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 168;BA.debugLine="If idx=\"6\" Then EditText4.Text=val";
if ((_idx).equals("6")) { 
mostCurrent._edittext4.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 169;BA.debugLine="If idx=\"8\" Then EditText5.Text=val";
if ((_idx).equals("8")) { 
mostCurrent._edittext5.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 170;BA.debugLine="If idx=\"13\" Then EditText6.Text=val";
if ((_idx).equals("13")) { 
mostCurrent._edittext6.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 171;BA.debugLine="If idx=\"19\" Then EditText7.Text=val";
if ((_idx).equals("19")) { 
mostCurrent._edittext7.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 172;BA.debugLine="If idx=\"21\" Then EditText8.Text=val";
if ((_idx).equals("21")) { 
mostCurrent._edittext8.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 173;BA.debugLine="If idx=\"23\" Then EditText9.Text=val";
if ((_idx).equals("23")) { 
mostCurrent._edittext9.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 174;BA.debugLine="If idx=\"28\" Then EditText11.Text=val";
if ((_idx).equals("28")) { 
mostCurrent._edittext11.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 175;BA.debugLine="If idx=\"30\" Then EditText12.Text=val";
if ((_idx).equals("30")) { 
mostCurrent._edittext12.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 176;BA.debugLine="If idx=\"32\" Then EditText13.Text=val";
if ((_idx).equals("32")) { 
mostCurrent._edittext13.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 177;BA.debugLine="If idx=\"34\" Then EditText14.Text=val";
if ((_idx).equals("34")) { 
mostCurrent._edittext14.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 178;BA.debugLine="If idx=\"36\" Then EditText15.Text=val";
if ((_idx).equals("36")) { 
mostCurrent._edittext15.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 179;BA.debugLine="If idx=\"38\" Then";
if ((_idx).equals("38")) { 
 //BA.debugLineNum = 180;BA.debugLine="If val=\"0\" Then";
if ((_val).equals("0")) { 
 //BA.debugLineNum = 181;BA.debugLine="CheckBox1.Checked=False";
mostCurrent._checkbox1.setChecked(anywheresoftware.b4a.keywords.Common.False);
 }else {
 //BA.debugLineNum = 183;BA.debugLine="CheckBox1.Checked=True";
mostCurrent._checkbox1.setChecked(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 //BA.debugLineNum = 186;BA.debugLine="If idx=\"40\" Then EditText16.Text=val";
if ((_idx).equals("40")) { 
mostCurrent._edittext16.setText(BA.ObjectToCharSequence(_val));};
 //BA.debugLineNum = 187;BA.debugLine="If idx=\"42\" Then EditText17.Text=val";
if ((_idx).equals("42")) { 
mostCurrent._edittext17.setText(BA.ObjectToCharSequence(_val));};
 };
 //BA.debugLineNum = 189;BA.debugLine="txtLog.Text= data & Chr(10) & Chr(13) & txtLog.Te";
mostCurrent._txtlog.setText(BA.ObjectToCharSequence(_data+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (10)))+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (13)))+mostCurrent._txtlog.getText()));
 //BA.debugLineNum = 190;BA.debugLine="End Sub";
return "";
}
public static String  _txtsend_enterpressed() throws Exception{
 //BA.debugLineNum = 357;BA.debugLine="Private Sub txtSend_EnterPressed";
 //BA.debugLineNum = 358;BA.debugLine="CallSub2(Starter,\"Send\",txtSend.Text)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._starter.getObject()),"Send",(Object)(mostCurrent._txtsend.getText()));
 //BA.debugLineNum = 359;BA.debugLine="txtSend.Text=\"\"";
mostCurrent._txtsend.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 360;BA.debugLine="End Sub";
return "";
}
}
