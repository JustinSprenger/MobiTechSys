/* Copyright 2011-2013 Google Inc.
 * Copyright 2013 mike wakerly <opensource@hoho.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * Project home page: https://github.com/mik3y/usb-serial-for-android
 */

package src.com.hoho.android.usbserial.examples;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.examples.R;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Monitors a single {@link UsbSerialPort} instance, showing all data
 * received.
 *
 * @author mike wakerly (opensource@hoho.com)
 */
public class SerialConsoleActivity extends Activity {

    private final String TAG = SerialConsoleActivity.class.getSimpleName();
    private static JSONParser json;

    /**
     * Driver instance, passed in statically via
     * {@link #show(Context, UsbSerialPort)}.
     * <p>
     * <p/>
     * This is a devious hack; it'd be cleaner to re-create the driver using
     * arguments passed in with the {@link #startActivity(Intent)} intent. We
     * can get away with it because both activities will run in the same
     * process, and this is a simple demo.
     */
    private static UsbSerialPort sPort = null;
    private static UsbSerialPort originPort = null;

    private TextView mTitleTextView;
    private TextView mDumpTextView;
    private ScrollView mScrollView;
    private TextView sendText;
    private String firstKey = "zzzzz";
    private String user = "<>";

    private static Settings sett;

    public void sendenButton(View v) {
        try {
            String msg = sendText.getText().toString();
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, msg.getBytes().toString(), Toast.LENGTH_SHORT).show();
            senden(msg);
            sendText.setText("");
        } catch (Exception e) {
            e.getCause();
        }
    }

    public void senden(String msg) {
        byte[] message;
        byte[] newmessage;
        byte[] pack = new byte[16];
        boolean first = true;
        try {
            //Keys funktionsfähig machen und SendText appenden
            switch (msg) {
                case "ato":
                    mSerialIoManager.getDriver().write("ato".getBytes(), 5000);
                    break;
                case "+++":
                    mSerialIoManager.getDriver().write("+++".getBytes(), 5000);
                    break;
                default:

                    if (first) {
                        message = (firstKey + user + " " + msg).getBytes();
                        first = false;
                    } else {
                        message = (firstKey + msg).getBytes();
                    }

                    mSerialIoManager.getDriver().write((firstKey + user + " " + msg).getBytes(), 5000);
                    // 16 auf 8 reduzieren, zzzzz zu jeder Nachricht am Anfang hinzufügen, ab zweiter Nachricht senden
                    final int MAX_MSG_LENGTH = 8;
                    while (message.length > MAX_MSG_LENGTH) {
                        System.arraycopy(message, 0, pack, 0, MAX_MSG_LENGTH);
                        newmessage = new byte[message.length - MAX_MSG_LENGTH];
                        System.arraycopy(message, MAX_MSG_LENGTH, newmessage, 0, message.length - MAX_MSG_LENGTH);
                        message = newmessage;
                        mSerialIoManager.getDriver().write(pack, 5000);
                    }


                    //mSerialIoManager.writeAsync((firstKey + user + " " + msg).getBytes());
                    //mSerialIoManager.getDriver().write((firstKey + user + " " + msg).getBytes() , 5000);
                    //sPort = originPort;
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
        }
    }

    public void configuration(View v) {
        Props.show(this, sPort);
        //Intent intent = new Intent(this, Props.class);
        //startActivity(intent);
    }

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private SerialInputOutputManager mSerialIoManager;

    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {
                    SerialConsoleActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SerialConsoleActivity.this.updateReceivedData(data);
                        }
                    });
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial_console);
        mTitleTextView = (TextView) findViewById(R.id.demoTitle);
        mDumpTextView = (TextView) findViewById(R.id.consoleText);
        mScrollView = (ScrollView) findViewById(R.id.demoScroller);
        sendText = (TextView) findViewById(R.id.sendText);

        json = new JSONParser(getApplicationContext());
        sett = new Settings(json.getBaudrate(), json.getParity(), json.getStartStop(), json.getDataBit(), json.getUsername());

        Toast.makeText(this, sett.getUsername(), Toast.LENGTH_LONG).show();

        Intent i = getIntent();
        if (i != null) {
            sett = (Settings) i.getSerializableExtra("settings");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopIoManager();
        if (sPort != null) {
            try {
                sPort.close();
            } catch (IOException e) {
                // Ignore.
            }
            sPort = null;
        }
        finish();
    }

    void showStatus(TextView theTextView, String theLabel, boolean theValue) {
        String msg = theLabel + ": " + (theValue ? "enabled" : "disabled") + "\n";
        theTextView.append(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Resumed, port=" + sPort);

        json = new JSONParser(getApplicationContext()); //Parser
        sett = new Settings(json.getBaudrate(), json.getParity(), json.getStartStop(), json.getDataBit(), json.getUsername()); //init Settings

        if (sPort == null) {
            mTitleTextView.setText("No serial device.");
        } else {
            final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

            UsbDeviceConnection connection = usbManager.openDevice(sPort.getDriver().getDevice());
            if (connection == null) {
                mTitleTextView.setText("Opening device failed");
                return;
            }

            try {
                sPort.open(connection);
                //sPort.setParameters(sett.getBaudrate(), sett.getDatabits(), sett.getStopbit(), sett.getParity());
                try {
                    Toast.makeText(this, sett.getUsername() + "Baudrate", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show();
                }
                user = "<" + sett.getUsername() + ">";
                sPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

            } catch (IOException e) {
                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
                mTitleTextView.setText("Error opening device: " + e.getMessage());
                try {
                    sPort.close();
                } catch (IOException e2) {
                    // Ignore.
                }
                sPort = null;
                return;
            }
            mTitleTextView.setText("Serial device: " + sPort.getClass().getSimpleName());
        }
        onDeviceStateChange();
    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            Toast.makeText(this, "Stopping IO Manager", Toast.LENGTH_LONG).show();
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            Toast.makeText(this, "Starting IO Manager", Toast.LENGTH_LONG).show();
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    private void updateReceivedData(byte[] data) {
        final String message = "\n" + HexDump.dumpHexString(data) + "\n\n";
        String filteredMessage = "";
        if (message.contains("zz")) {
            filteredMessage = message.substring(6);
            mDumpTextView.append(message);
        }
        mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
    }

    /**
     * Starts the activity, using the supplied driver instance.
     *
     * @param context
     * @param port
     */
    public static void show(Context context, UsbSerialPort port) {
        sPort = port;
        originPort = port;
        final Intent intent = new Intent(context, SerialConsoleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    public static void showagain(Context context, UsbSerialPort port, Settings setting) {
        sPort = port;
        sett = setting;

        final Intent intent = new Intent(context, SerialConsoleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

}
