B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=11
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	#ExcludeFromLibrary: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim AStreams As AsyncStreams
	Dim Serial1 As Serial
	Dim AdminSerial As BluetoothAdmin
	Dim Connected As Boolean
	Public kvs As KeyValueStore
	Dim sendbytes As ByteConverter
	
End Sub

Sub Service_Create
	'This is the program entry point.
	'This is a good place to load resources that are not specific to a single activity.
	Serial1.Initialize("Serial1")
	AdminSerial.Initialize("AdminSerial1")
	kvs.Initialize(File.DirInternal, "datastore2")
End Sub

Sub Service_Start (StartingIntent As Intent)
	Service.StopAutomaticForeground 'Starter service can start in the foreground state in some edge cases.
End Sub

Sub Service_TaskRemoved
	'This event will be raised when the user removes the app from the recent apps list.
End Sub

Sub Application_Error (Error As Exception, StackTrace As String) As Boolean
	Return True
End Sub

Sub Service_Destroy

End Sub

Sub Serial1_Connected (Success As Boolean)
	If Success Then
		AStreams.Initialize( Serial1.InputStream, Serial1.OutputStream, "astreams")
	End If
	Connected = Success
	CallSub2(Main,"Conec",Success)
End Sub

Sub AStreams_NewData (Buffer() As Byte)
	Dim S As String
	S = BytesToString(Buffer, 0, Buffer.Length, "UTF8")
	CallSub2(Main,"rellena",S)
End Sub

Sub AStreams_Error
'	ToastMessageShow("Error",False)
	CallSub(Main,"Cerrar")
End Sub

Sub AStreams_Terminated
'ToastMessageShow("termino",False)
	CallSub(Main,"Cerrar")
End Sub

Sub AdminSerial1_StateChanged (NewState As Int, OldState As Int)
	If NewState=12 Then
	End If
	ToastMessageShow(NewState,True)

	If NewState=13 Then
		Serial1.Disconnect
		CallSub(Main,"Cerrar")
	End If
End Sub

Sub AdminSerial1_DiscoveryStarted
End Sub

Sub AdminSerial1_DiscoveryFinished
End Sub

Sub AdminSerial1_DeviceFound (Name As String, MacAddress As String)
End Sub

Sub Send(Dato As String)
	If AStreams.IsInitialized = False Then Return
	If Dato.Length > 0 Then
	'	Dim ti As Short=Dato
	'	Dim b() As Byte = sendbytes.ShortsToBytes( Array As Short(ti))
		'AStreams.Write(Array As Byte(5,0,b(0),b(1),0,0))
'		AStreams.Write(Array As Byte(b))
		Dim buffer() As Byte
		buffer = Dato.GetBytes("UTF8")
		AStreams.Write(buffer)		
	End If
End Sub

Sub Conectar(cmd As Boolean)
	Dim btname As String=kvs.Get("BT_name")
	If btname.Length>0 Then
		If cmd=True Then
			If Serial1.IsEnabled Then
				Try
					Serial1.Connect( Serial1.GetPairedDevices.Get(btname))
				Catch
					Log("Wrong password!")
					CallSub2(Main,"Conec",False)
				End Try
			Else
				CallSub2(Main,"Conec",False)
			End If
		Else
			Serial1.Disconnect
			CallSub2(Main,"Conec",False)
			CallSub(Main,"Cerrar")
			Connected = False
		End If
	End If
End Sub