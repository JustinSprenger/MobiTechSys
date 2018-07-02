package com.example.justin.usbdev;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import static java.lang.System.exit;

public class Uart extends AppCompatActivity {

    UsbManager usbManager;
    UsbDevice usbDevice;
    UsbInterface usbInterface;
    UsbEndpoint endIn;
    UsbEndpoint endOut;
    Intent intent;
    PendingIntent mPermissionIntent;
    Thread com;
    Runnable uartRun;
    private String ACTION_USB_PERMISSION;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uart);
        tv = (TextView) findViewById(R.id.textView);
        intent = new Intent();
        getDevice();
        //listDevice();

        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);


        ACTION_USB_PERMISSION ="com.android.justin.usbdev.USB_PERMISSION";
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);


        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        if (usbDevice != null){
            mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            tv.setText("klappt");
            try {
                getEndIn(usbDevice);
                uartRun = new UartRun(usbManager,usbDevice,usbInterface,endIn,endOut,this);
                com = new Thread(uartRun);
                com.start();
                conn.controlTransfer(0x40, 0x03, baudrate115200, 0, null, 0, 0)
                tv.setText(tv.getText() + "\n Thread gestartet");
            }catch (Exception e){
                Toast.makeText(this, "err", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getEndIn(UsbDevice dev) {
        usbInterface = dev.getInterface(0);

        for(int i = 0; i < usbInterface.getEndpointCount(); i++){
            tv.setText(tv.getText() + "\n EP: "+String.format("0x%02X", usbInterface.getEndpoint(i).getAddress()));
            if(usbInterface.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK){
                tv.setText(tv.getText() + "\n Bulk Endpoint");
                if(usbInterface.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN) {
                    endIn = usbInterface.getEndpoint(i);
                    tv.setText(tv.getText() + "\n EndpointIn: " + endIn.toString());
                }
                else {
                    endOut = usbInterface.getEndpoint(i);
                    tv.setText(tv.getText() + "\n EndpointOut: " + endOut.toString());
                }
            }else{
                tv.setText(tv.getText() + "\n Not Bulk");
            }
        }
    }

    public void getDevice(){
        //usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            Toast.makeText(this, device.getDeviceName(), Toast.LENGTH_SHORT).show();

            if(device != null) {
                usbDevice = device;
            }
        }
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                        }
                    } else {
                        Log.d("bla", "permission denied for device " + device);
                    }
                }
            }
        }
    };

    /*
    private static final int DEFAULT_BAUDRATE = 115200;

    private byte[] bytes;
    private static int TIMEOUT = 50;
    private boolean forceClaim = true;
    private boolean mStop = false;
    private boolean mStopped = true;

    UsbManager usbManager;
    UsbDevice usbDevice;
    UsbRequest usbRequest;
    UsbInterface intf = null;
    UsbEndpoint input, output;
    UsbDeviceConnection connection;
    Intent intent;
    PendingIntent mPermissionIntent;
    private Thread reading = null;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uart);
        tv = (TextView) findViewById(R.id.textView);
        intent = new Intent();
        getDevice();
        //listDevice();

        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        final String ACTION_USB_PERMISSION =
                "com.android.example.USB_PERMISSION";
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        if (usbDevice != null){
            mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            tv.setText("klappt");
            try {
                reading = new Thread(readData);
                reading.start();
                tv.setText(tv.getText() + "\n Thread gestartet");
            }catch (Exception e){
                Toast.makeText(this, "err", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void listDevice(View v){
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        tv.setText("1. test");
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        tv.setText("2. test");
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            Toast.makeText(this, device.getDeviceName(), Toast.LENGTH_SHORT).show();

            tv.append("\n" + device.getVendorId());
            tv.append("\n" + device.getProductId());
            tv.append("\n" + device.getDeviceClass());
            tv.append("\n" + device.getDeviceSubclass());
            tv.append("\n" + device.getDeviceProtocol());
            tv.append("\n" + device.getInterface(0));

            if(device != null) {
                usbDevice = device;
            }
            //tv.setText("10000000. test");
        }
    }

    public void getDevice(){
        //usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            Toast.makeText(this, device.getDeviceName(), Toast.LENGTH_SHORT).show();

            if(device != null) {
                usbDevice = device;
            }
        }
    }

    public void closeConn(){
        BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device != null) {
                        // call your method that cleans up and closes communication with the device
                    }
                }
            }
        };
    }

    public void connectToDevice(){

    }

    public byte[] getConfig(){
        byte[] msg = {
                (byte) 0x80, // [0:3] Baud rate (reverse hex encoding 9600:00 00 25 80 -> 80 25 00 00)
                (byte) 0x25,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0x00, // [4] Stop Bits (0=1, 1=1.5, 2=2)
                (byte) 0x00, // [5] Parity (0=NONE 1=ODD 2=EVEN 3=MARK 4=SPACE)
                (byte) 0x08  // [6] Data Bits (5=5, 6=6, 7=7, 8=8)
        };
        //connection.controlTransfer(UsbConstants.USB_TYPE_CLASS | 0x01, 0x20, 0, 0, msg, msg.length, 5000);
        return msg;
    }

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            //call method to set up device communication
                        }
                    }
                    else {
                        Log.d("bla", "permission denied for device " + device);
                    }
                }
            }
        }
    };

    private Runnable readData = new Runnable() {
        @Override
        public void run() {
            UsbInterface usbinterface = null;
            UsbEndpoint usbendpoint = null;
            UsbDeviceConnection readconnection = null;
            int baudrate115200 = 0x001A;
            int baudrate9600 = 0x4138;
            tv.setText(tv.getText() + "\n im Thread");
            try {


                    if (usbDevice == null)
                        return;
                    UsbManager usbm = (UsbManager) getSystemService(USB_SERVICE);
                    UsbDeviceConnection conn = usbm.openDevice(usbDevice);
                    l("Interface Count: "+usbDevice.getInterfaceCount());
                    l("Using "+String.format("%04X:%04X", usbDevice.getVendorId(), usbDevice.getProductId()));

                    if(!conn.claimInterface(usbDevice.getInterface(0), true))
                        return;

                    conn.controlTransfer(0x40, 0, 0, 0, null, 0, 0);//reset
                    conn.controlTransfer(0x40, 0, 1, 0, null, 0, 0);//clear Rx
                    conn.controlTransfer(0x40, 0, 2, 0, null, 0, 0);//clear Tx
                    conn.controlTransfer(0x40, 0x03, baudrate115200, 0, null, 0, 0);//baudrate 9600

                    UsbEndpoint epIN = null;
                    UsbEndpoint epOUT = null;

                    byte counter = 0;

                    UsbInterface usbIf = usbDevice.getInterface(0);
                    for(int i = 0; i < usbIf.getEndpointCount(); i++){
                        l("EP: "+String.format("0x%02X", usbIf.getEndpoint(i).getAddress()));
                        if(usbIf.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK){
                            l("Bulk Endpoint");
                            if(usbIf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN)
                                epIN = usbIf.getEndpoint(i);
                            else
                                epOUT = usbIf.getEndpoint(i);
                        }else{
                            l("Not Bulk");
                        }
                    }

                    for(;;) {//this is the main loop for transferring
                        conn.bulkTransfer(epOUT, new byte[]{counter}, 1, 0);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(mStop){
                            mStopped = true;
                            return;
                        }

                        l("sent " + counter);
                        counter++;
                        counter = (byte) (counter % 16);
                    }
            }catch (Exception e){
                tv.setText(tv.getText() + "\n" + e.toString());
            }
        }
    };
    private void l(Object s) {
        Log.d("FTDI_USB", ">==< " + s.toString() + " >==<");
        Toast.makeText(this, s.toString(), Toast.LENGTH_SHORT).show();
    }
    private void e(Object s) {
        Log.e("FTDI_USB", ">==< " + s.toString() + " >==<");
        Toast.makeText(this, s.toString(), Toast.LENGTH_SHORT).show();
    }
    */

//    protected static final String ACTION_USB_PERMISSION = "com.example.justin.usbdev.ACTION_USB_DEVICE_ATTACHED";
//    private static final String VID_PID = "10C4:EA60";
//    public static UsbDevice sDevice = null;
//    private static Uart sActivityContext;
//    private boolean mStop = false;
//    private boolean mStopped = true;
//
//    TextView tv;
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        sActivityContext = this;
//        setContentView(R.layout.activity_uart);
//        tv = (TextView) findViewById(R.id.textView);
//    }
//
//    @Override
//    protected void onDestroy() {
//        sActivityContext = null;
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onStart() {
//        mStop = false;
//        if(mStopped)
//            enumerate();
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        mStop = true;
//        unregisterReceiver(mPermissionReceiver);
//        super.onStop();
//    }
//
//
//    private final BroadcastReceiver mPermissionReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
//                if (!intent.getBooleanExtra(
//                        UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
//                    e("Permission not granted :(");
//                } else {
//                    l("Permission granted");
//                    UsbDevice dev = (UsbDevice) intent
//                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                    if (dev != null) {
//                        if (String.format("%04X:%04X", dev.getVendorId(),
//                                dev.getProductId()).equals(VID_PID)) {
//                            mainloop(dev);//has new thread
//                        }
//                    } else {
//                        e("device not present!");
//                    }
//                }
//            }
//        }
//    };
//
//    private void enumerate() {
//        l("enumerating");
//        UsbManager usbman = (UsbManager) getSystemService(USB_SERVICE);
//
//        HashMap<String, UsbDevice> devlist = usbman.getDeviceList();
//        Iterator<UsbDevice> deviter = devlist.values().iterator();
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(
//                ACTION_USB_PERMISSION), 0);
//
//        while (deviter.hasNext()) {
//            UsbDevice d = deviter.next();
//            l("Found device: "
//                    + String.format("%04X:%04X", d.getVendorId(),
//                    d.getProductId()));
//            if (String.format("%04X:%04X", d.getVendorId(), d.getProductId())
//                    .equals(VID_PID)) {
//                // we need to upload the hex file, first request permission
//                l("Device under: " + d.getDeviceName());
//                registerReceiver(mPermissionReceiver, new IntentFilter(
//                        ACTION_USB_PERMISSION));
//                if (!usbman.hasPermission(d))
//                    usbman.requestPermission(d, pi);
//                else
//                    mainloop(d);
//                break;
//            }
//        }
//        l("no more devices found");
//    }
//
//    private void mainloop(UsbDevice d) {
//        sDevice = d;// not really nice...
//        new Thread(mLoop).start();
//    }
//
//    private Runnable mLoop = new Runnable() {
//
//
//            @Override
//            public void run () {
//                try {
//
//
//            UsbDevice dev = sDevice;
//            if (dev == null)
//                return;
//            UsbManager usbm = (UsbManager) getSystemService(USB_SERVICE);
//            UsbDeviceConnection conn = usbm.openDevice(dev);
//            l("Interface Count: " + dev.getInterfaceCount());
//            l("Using " + String.format("%04X:%04X", sDevice.getVendorId(), sDevice.getProductId()));
//
//            if (!conn.claimInterface(dev.getInterface(0), true))
//                return;
//
//            conn.controlTransfer(0x40, 0, 0, 0, null, 0, 0);//reset
//            conn.controlTransfer(0x40, 0, 1, 0, null, 0, 0);//clear Rx
//            conn.controlTransfer(0x40, 0, 2, 0, null, 0, 0);//clear Tx
//            conn.controlTransfer(0x40, 0x03, 0x001A, 0, null, 0, 0);//baudrate 9600
//
//            UsbEndpoint epIN = null;
//            UsbEndpoint epOUT = null;
//
//            byte counter = 0;
//
//            UsbInterface usbIf = dev.getInterface(0);
//            for (int i = 0; i < usbIf.getEndpointCount(); i++) {
//                l("EP: " + String.format("0x%02X", usbIf.getEndpoint(i).getAddress()));
//                if (usbIf.getEndpoint(i).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
//                    l("Bulk Endpoint");
//                    l(usbIf.getEndpoint(i).getDirection());
//                    if (usbIf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_IN)
//                        epIN = usbIf.getEndpoint(i);
//                    else
//                        epOUT = usbIf.getEndpoint(i);
//                } else {
//                    l("Not Bulk");
//                }
//            }
//
//            for (int i = 1;i<100 ;i++ ) {//this is the main loop for transferring
//                conn.bulkTransfer(epOUT, new byte[]{counter}, 1, 0);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                if (mStop) {
//                    mStopped = true;
//                    return;
//                }
//                l("sent " + counter);
//                try{
//                    counter++;
//                    counter = (byte) (counter % 16);
//                }catch (Exception e){
//                        tv.setText(tv.getText()+"\n"+e.toString());
//                }
//
//            }
//                    tv.setText(tv.getText()+"\n blablabla");
//                }catch (Exception e){
//                    tv.setText(tv.getText()+"\n"+e.toString());
//                }
//        }
//
//    };
//
//    private void l(Object s) {
//        tv.setText(tv.getText()+"\n"+s.toString());
//        Log.d("FTDI_USB", ">==< " + s.toString() + " >==<");
//    }
//
//    private void e(Object s) {
//        tv.setText(tv.getText()+"\n"+s.toString());
//        Log.e("FTDI_USB", ">==< " + s.toString() + " >==<");
//    }
//
//    //--------------------------------------------------------------------------
//
//    private static int checksum(byte[] data) {
//        int result = 0;
//        for (int i = 0; i < data.length; i++) {
//            int x = data[i];
//            // dang, no unsigned ints in java
//            if (x < 0) x += 256;
//            result += x;
//        }
//        return result;
//    }
//
//    public boolean readData(UsbRequest request, int length) {
//        request.setClientData(this);
//        return request.queue(mDataBuffer, length);
//    }

    public void print(final String msg)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void printToTextview(final String msg)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               tv.setText(tv.getText() + "\n" + msg);
            }
        });
    }

    public void clearTextview()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText("");
            }
        });
    }
}

