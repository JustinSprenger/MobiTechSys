package com.example.justin.usbdev;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;

import static android.content.Context.USB_SERVICE;

public class UartRun implements Runnable {
    private UsbDevice usbDevice;
    private UsbManager usbManager;
    private UsbInterface usbInterface;
    private UsbEndpoint usbIn;
    private UsbEndpoint usbOut;
    private UsbRequest usbRequest;
    private TextView tv;
    private byte[] data = new byte[128];
    Uart main;

    final int BAUDRATE_115200 = 0x001A;
    final int BAUDRATE_9600 = 0x4138;

    public UartRun(UsbManager usbManager, UsbDevice usbDevice, UsbInterface usbInterface, UsbEndpoint endIn, UsbEndpoint endOut,Uart main) {
        this.usbManager = usbManager;
        this.usbDevice = usbDevice;
        this.usbInterface = usbInterface;
        this.usbIn = endIn;
        this.usbOut = endOut;
        this.main = main;
    }

    @Override
    public void run() {

        try{
        synchronized(this) {
            String test = "hallo";
            byte[] testbyte = test.getBytes();
            int testbyteread;
            UsbDeviceConnection readconnection = null;
            ByteBuffer buffer = ByteBuffer.allocate(1);

            UsbDeviceConnection conn = usbManager.openDevice(usbDevice);
            conn.claimInterface(usbInterface, true);

            conn.controlTransfer(0x40, 0, 0, 0, null, 0, 0);//reset
            conn.controlTransfer(0x40, 0, 1, 0, null, 0, 0);//clear Rx
            conn.controlTransfer(0x40, 0, 2, 0, null, 0, 0);//clear Tx
            conn.controlTransfer(0x40, 0x03, 0x001A, 0, null, 0, 0);//baudrate 9600


            conn.bulkTransfer(usbOut, testbyte, 1, 0);
            testbyteread = conn.bulkTransfer(usbIn,testbyte, testbyte.length, 0);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            usbRequest = new UsbRequest();
            usbRequest.initialize(conn, usbIn);
            while (true) {
                usbRequest.queue(buffer, 1);
                if (conn.requestWait() == usbRequest) {
                    byte read = buffer.get(0);
                    if (read != 0) {
                        main.printToTextview(read + " (Runnable)");
                    }else{
                        main.printToTextview("Read = leer");
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                } else {
                    main.printToTextview("connection closed");
                    break;
                }
            }
        }
            /*

            conn.getSerial();

            conn.controlTransfer(0x40, 0, 0, 0, null, 0, 0);//reset
            conn.controlTransfer(0x40, 0, 1, 0, null, 0, 0);//clear Rx
            conn.controlTransfer(0x40, 0, 2, 0, null, 0, 0);//clear Tx
            conn.controlTransfer(0x40, 0x03, BAUDRATE_115200, 0, null, 0, 0);//baudrate 9600

            if(usbRequest != null && usbRequest.getEndpoint().getType() == UsbConstants.USB_ENDPOINT_XFER_BULK
                    && usbRequest.getEndpoint().getDirection() == UsbConstants.USB_DIR_IN)
            {

            }
            */
        }catch(Exception e){main.print(e.getMessage());}
    }
}
