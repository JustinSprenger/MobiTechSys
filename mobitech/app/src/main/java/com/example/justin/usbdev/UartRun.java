package com.example.justin.usbdev;

import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
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
    final int BAUDRATE_57600 = 0x0034;
    private static final int OFFSET = 2;
    private final Boolean mEnableAsyncReads;

    public UartRun(UsbManager usbManager, UsbDevice usbDevice, UsbInterface usbInterface, UsbEndpoint endIn, UsbEndpoint endOut,Uart main) {
        this.usbManager = usbManager;
        this.usbDevice = usbDevice;
        this.usbInterface = usbInterface;
        this.usbIn = endIn;
        this.usbOut = endOut;
        this.main = main;
        mEnableAsyncReads = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1);
    }

    @Override
    public void run() {

        try{

            String test = "";
            int count = 0;
            byte[] testbyte = test.getBytes();
            byte[] dest = new byte[usbIn.getMaxPacketSize()];
            int testbyteread;
            //UsbDeviceConnection readconnection = null;
            //ByteBuffer buffer = ByteBuffer.allocate(1);

            UsbDeviceConnection conn = usbManager.openDevice(usbDevice);
            conn.claimInterface(usbInterface, true);

            conn.getSerial();

            //conn.controlTransfer(0x40, 0, 0, 0, null, 0, 0);//reset
            //conn.controlTransfer(0x40, 0, 1, 0, null, 0, 0);//clear Rx
            //conn.controlTransfer(0x40, 0, 2, 0, null, 0, 0);//clear Tx
            //conn.controlTransfer(0x40, 0x03, BAUDRATE_115200, 0, null, 0, 0);//baudrate 9600


            conn.controlTransfer(0x40, 0, 0, 0, null, 0, 0);// reset
            conn.controlTransfer(0x40, 0, 1, 0, null, 0, 0);//clear Rx
            //conn.controlTransfer(0x40, 0, 2, 0, null, 0, 0);// clear Tx
            conn.controlTransfer(0x40, 0x02, 0x0000, 0, null, 0, 0);// flowcontrol none
            conn.controlTransfer(0x40, 0x03, BAUDRATE_115200, 0, null, 0, 0);// baudrate
            conn.controlTransfer(0x40, 0x04, 0x0008, 0, null, 0, 0);// data bit 8, parity none, stop bit 1, tx off

            if(usbDevice == null){
                main.printToTextview("Device = Null");
            }

            if(conn == null){
                main.printToTextview("Connection = Null");
            }

            if(usbIn == null){
                main.printToTextview("USB In = Null");
            }

            if(usbOut == null){
                main.printToTextview("USB Out = Null");
            }

            main.printToTextview("in run");

            final UsbRequest request = new UsbRequest();
            if (mEnableAsyncReads) {
                if (count > 3) {
                    main.clearTextview();
                    count = 0;
                }
                try {
                    main.printToTextview("ist drin");
                    request.initialize(conn, usbIn);
                    final ByteBuffer buf = ByteBuffer.wrap(dest);
                    if (!request.queue(buf, dest.length)) {
                        throw new IOException("Error queueing request.");
                    }
                    usbRequest = request;
                    final UsbRequest response = conn.requestWait();
                    synchronized (mEnableAsyncReads) {
                        usbRequest = null;
                        main.printToTextview("hier");
                    }
                    if (response == null) {
                        throw new IOException("Null response");
                    }
                    final int nread = buf.position();
                    main.printToTextview(nread + "" + buf.getChar(0));
                    Thread.sleep(200);
                } catch (Exception e) {
                    main.printToTextview(e.getMessage());
                }
                count++;
            }
            main.printToTextview("Ist raus");
            /*
            while (usbIn != null && conn != null) {
                    byte[] recordIn = new byte[usbIn.getMaxPacketSize()];
                    int receivedLength = conn.bulkTransfer(usbIn, recordIn,
                            recordIn.length, 500);
                    if (receivedLength > -1){
                        for(int i = 0;i<recordIn.length;i++){
                            main.printToTextview(recordIn[i]+"");

                        }
                    }
            }
            */
            /*
            while (true) {
                String message = "";
                if (count > 3){
                    main.clearTextview();
                    count = 0;
                }

                conn.bulkTransfer(usbIn, data, data.length, 500);
                Thread.sleep(500);
                for(int i = 0;i<data.length;i++){
                    message = message + data[i];

                }
                main.printToTextview(message + " (Runnable)");
                count++;
            }*/


            //conn.bulkTransfer(usbOut, testbyte, 1, 0);
            /*while (true) {
                if (count > 10){
                    main.clearTextview();
                    count = 0;
                }
                try {
                    testbyteread = conn.bulkTransfer(usbIn, testbyte, testbyte.length, 100);

                    main.printToTextview(testbyteread + " (Runnable)");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
            }*/
            /*usbRequest = new UsbRequest();
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
            }*/
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
