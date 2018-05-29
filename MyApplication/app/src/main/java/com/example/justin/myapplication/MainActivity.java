package com.example.justin.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private static final String UART_DEVICE_NAME = "";
    Button senden;
    TextView textv;
    EditText msg;
    Uart uart;
    //private UartDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        senden = (Button) findViewById(R.id.send);
        textv = (TextView) findViewById(R.id.textView);
        msg = (EditText) findViewById(R.id.editText);

        uart = new Uart();
        // Attempt to access the UART device
        /*try {
            PeripheralManager manager = PeripheralManager.getInstance();
            mDevice = manager.openUartDevice(UART_DEVICE_NAME);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access UART device", e);
        }*/
    }

    public void sendMsg(View v){
        textv.setText(textv.getText()+ "\n" + msg.getText());
        uart.sendText(msg.getText().toString());
        msg.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*if (mDevice != null) {
            try {
                mDevice.close();
                mDevice = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close UART device", e);
            }
        }*/
    }

}
