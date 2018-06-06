package com.example.justin.usbdev;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

public class Uart extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uart);
        tv = (TextView) findViewById(R.id.textView);
    }

    public void listDevice(View v){
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        tv.setText("1. test");
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        tv.setText("2. test");
        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            tv.setText("Hier");
            Toast.makeText(this, device.getDeviceName(), Toast.LENGTH_SHORT).show();
            tv.setText(tv.getText() + device.getDeviceName());
            tv.setText("10000000. test");
        }
    }
}

