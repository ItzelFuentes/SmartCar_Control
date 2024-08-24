package anywheresoftware.b4a.samples.serial;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class starter extends  android.app.Service{
	public static class starter_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (starter) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, starter.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, true, BA.class);
		}

	}
    static starter mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return starter.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "anywheresoftware.b4a.samples.serial", "anywheresoftware.b4a.samples.serial.starter");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "anywheresoftware.b4a.samples.serial.starter", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!true && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (starter) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (true) {
			ServiceHelper.StarterHelper.runWaitForLayouts();
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA, new Runnable() {
            public void run() {
                handleStart(intent);
            }}))
			;
		else {
			ServiceHelper.StarterHelper.addWaitForLayout (new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (starter) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
                    ServiceHelper.StarterHelper.removeWaitForLayout();
				}
			});
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (true)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (starter) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = ServiceHelper.StarterHelper.handleStartIntent(intent, _service, processBA);
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        if (true) {
            BA.LogInfo("** Service (starter) Destroy (ignored)**");
        }
        else {
            BA.LogInfo("** Service (starter) Destroy **");
		    processBA.raiseEvent(null, "service_destroy");
            processBA.service = null;
		    mostCurrent = null;
		    processBA.setActivityPaused(true);
            processBA.runHook("ondestroy", this, null);
        }
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.randomaccessfile.AsyncStreams _astreams = null;
public static anywheresoftware.b4a.objects.Serial _serial1 = null;
public static anywheresoftware.b4a.objects.Serial.BluetoothAdmin _adminserial = null;
public static boolean _connected = false;
public static b4a.example3.keyvaluestore _kvs = null;
public static anywheresoftware.b4a.agraham.byteconverter.ByteConverter _sendbytes = null;
public b4a.example.dateutils _dateutils = null;
public anywheresoftware.b4a.samples.serial.main _main = null;
public anywheresoftware.b4a.samples.serial.xuiviewsutils _xuiviewsutils = null;
public static String  _adminserial1_devicefound(String _name,String _macaddress) throws Exception{
 //BA.debugLineNum = 83;BA.debugLine="Sub AdminSerial1_DeviceFound (Name As String, MacA";
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return "";
}
public static String  _adminserial1_discoveryfinished() throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Sub AdminSerial1_DiscoveryFinished";
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _adminserial1_discoverystarted() throws Exception{
 //BA.debugLineNum = 77;BA.debugLine="Sub AdminSerial1_DiscoveryStarted";
 //BA.debugLineNum = 78;BA.debugLine="End Sub";
return "";
}
public static String  _adminserial1_statechanged(int _newstate,int _oldstate) throws Exception{
 //BA.debugLineNum = 66;BA.debugLine="Sub AdminSerial1_StateChanged (NewState As Int, Ol";
 //BA.debugLineNum = 67;BA.debugLine="If NewState=12 Then";
if (_newstate==12) { 
 };
 //BA.debugLineNum = 69;BA.debugLine="ToastMessageShow(NewState,True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence(_newstate),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 71;BA.debugLine="If NewState=13 Then";
if (_newstate==13) { 
 //BA.debugLineNum = 72;BA.debugLine="Serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 73;BA.debugLine="CallSub(Main,\"Cerrar\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(processBA,(Object)(mostCurrent._main.getObject()),"Cerrar");
 };
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return "";
}
public static boolean  _application_error(anywheresoftware.b4a.objects.B4AException _error,String _stacktrace) throws Exception{
 //BA.debugLineNum = 34;BA.debugLine="Sub Application_Error (Error As Exception, StackTr";
 //BA.debugLineNum = 35;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 36;BA.debugLine="End Sub";
return false;
}
public static String  _astreams_error() throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub AStreams_Error";
 //BA.debugLineNum = 58;BA.debugLine="CallSub(Main,\"Cerrar\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(processBA,(Object)(mostCurrent._main.getObject()),"Cerrar");
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _astreams_newdata(byte[] _buffer) throws Exception{
String _s = "";
 //BA.debugLineNum = 50;BA.debugLine="Sub AStreams_NewData (Buffer() As Byte)";
 //BA.debugLineNum = 51;BA.debugLine="Dim S As String";
_s = "";
 //BA.debugLineNum = 52;BA.debugLine="S = BytesToString(Buffer, 0, Buffer.Length, \"UTF8";
_s = anywheresoftware.b4a.keywords.Common.BytesToString(_buffer,(int) (0),_buffer.length,"UTF8");
 //BA.debugLineNum = 53;BA.debugLine="CallSub2(Main,\"rellena\",S)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._main.getObject()),"rellena",(Object)(_s));
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _astreams_terminated() throws Exception{
 //BA.debugLineNum = 61;BA.debugLine="Sub AStreams_Terminated";
 //BA.debugLineNum = 63;BA.debugLine="CallSub(Main,\"Cerrar\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(processBA,(Object)(mostCurrent._main.getObject()),"Cerrar");
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public static String  _conectar(boolean _cmd) throws Exception{
String _btname = "";
 //BA.debugLineNum = 99;BA.debugLine="Sub Conectar(cmd As Boolean)";
 //BA.debugLineNum = 100;BA.debugLine="Dim btname As String=kvs.Get(\"BT_name\")";
_btname = BA.ObjectToString(_kvs._get("BT_name"));
 //BA.debugLineNum = 101;BA.debugLine="If btname.Length>0 Then";
if (_btname.length()>0) { 
 //BA.debugLineNum = 102;BA.debugLine="If cmd=True Then";
if (_cmd==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 103;BA.debugLine="If Serial1.IsEnabled Then";
if (_serial1.IsEnabled()) { 
 //BA.debugLineNum = 104;BA.debugLine="Try";
try { //BA.debugLineNum = 105;BA.debugLine="Serial1.Connect( Serial1.GetPairedDevices.Get";
_serial1.Connect(processBA,BA.ObjectToString(_serial1.GetPairedDevices().Get((Object)(_btname))));
 } 
       catch (Exception e8) {
			processBA.setLastException(e8); //BA.debugLineNum = 107;BA.debugLine="Log(\"Wrong password!\")";
anywheresoftware.b4a.keywords.Common.LogImpl("26094856","Wrong password!",0);
 //BA.debugLineNum = 108;BA.debugLine="CallSub2(Main,\"Conec\",False)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._main.getObject()),"Conec",(Object)(anywheresoftware.b4a.keywords.Common.False));
 };
 }else {
 //BA.debugLineNum = 111;BA.debugLine="CallSub2(Main,\"Conec\",False)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._main.getObject()),"Conec",(Object)(anywheresoftware.b4a.keywords.Common.False));
 };
 }else {
 //BA.debugLineNum = 114;BA.debugLine="Serial1.Disconnect";
_serial1.Disconnect();
 //BA.debugLineNum = 115;BA.debugLine="CallSub2(Main,\"Conec\",False)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._main.getObject()),"Conec",(Object)(anywheresoftware.b4a.keywords.Common.False));
 //BA.debugLineNum = 116;BA.debugLine="CallSub(Main,\"Cerrar\")";
anywheresoftware.b4a.keywords.Common.CallSubNew(processBA,(Object)(mostCurrent._main.getObject()),"Cerrar");
 //BA.debugLineNum = 117;BA.debugLine="Connected = False";
_connected = anywheresoftware.b4a.keywords.Common.False;
 };
 };
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim AStreams As AsyncStreams";
_astreams = new anywheresoftware.b4a.randomaccessfile.AsyncStreams();
 //BA.debugLineNum = 10;BA.debugLine="Dim Serial1 As Serial";
_serial1 = new anywheresoftware.b4a.objects.Serial();
 //BA.debugLineNum = 11;BA.debugLine="Dim AdminSerial As BluetoothAdmin";
_adminserial = new anywheresoftware.b4a.objects.Serial.BluetoothAdmin();
 //BA.debugLineNum = 12;BA.debugLine="Dim Connected As Boolean";
_connected = false;
 //BA.debugLineNum = 13;BA.debugLine="Public kvs As KeyValueStore";
_kvs = new b4a.example3.keyvaluestore();
 //BA.debugLineNum = 14;BA.debugLine="Dim sendbytes As ByteConverter";
_sendbytes = new anywheresoftware.b4a.agraham.byteconverter.ByteConverter();
 //BA.debugLineNum = 16;BA.debugLine="End Sub";
return "";
}
public static String  _send(String _dato) throws Exception{
byte[] _buffer = null;
 //BA.debugLineNum = 86;BA.debugLine="Sub Send(Dato As String)";
 //BA.debugLineNum = 87;BA.debugLine="If AStreams.IsInitialized = False Then Return";
if (_astreams.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
if (true) return "";};
 //BA.debugLineNum = 88;BA.debugLine="If Dato.Length > 0 Then";
if (_dato.length()>0) { 
 //BA.debugLineNum = 93;BA.debugLine="Dim buffer() As Byte";
_buffer = new byte[(int) (0)];
;
 //BA.debugLineNum = 94;BA.debugLine="buffer = Dato.GetBytes(\"UTF8\")";
_buffer = _dato.getBytes("UTF8");
 //BA.debugLineNum = 95;BA.debugLine="AStreams.Write(buffer)";
_astreams.Write(_buffer);
 };
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _serial1_connected(boolean _success) throws Exception{
 //BA.debugLineNum = 42;BA.debugLine="Sub Serial1_Connected (Success As Boolean)";
 //BA.debugLineNum = 43;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 44;BA.debugLine="AStreams.Initialize( Serial1.InputStream, Serial";
_astreams.Initialize(processBA,_serial1.getInputStream(),_serial1.getOutputStream(),"astreams");
 };
 //BA.debugLineNum = 46;BA.debugLine="Connected = Success";
_connected = _success;
 //BA.debugLineNum = 47;BA.debugLine="CallSub2(Main,\"Conec\",Success)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._main.getObject()),"Conec",(Object)(_success));
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 18;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 21;BA.debugLine="Serial1.Initialize(\"Serial1\")";
_serial1.Initialize("Serial1");
 //BA.debugLineNum = 22;BA.debugLine="AdminSerial.Initialize(\"AdminSerial1\")";
_adminserial.Initialize(processBA,"AdminSerial1");
 //BA.debugLineNum = 23;BA.debugLine="kvs.Initialize(File.DirInternal, \"datastore2\")";
_kvs._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),"datastore2");
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 38;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 26;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 27;BA.debugLine="Service.StopAutomaticForeground 'Starter service";
mostCurrent._service.StopAutomaticForeground();
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return "";
}
public static String  _service_taskremoved() throws Exception{
 //BA.debugLineNum = 30;BA.debugLine="Sub Service_TaskRemoved";
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return "";
}
}
