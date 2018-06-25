package src.com.hoho.android.usbserial.examples;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hoho.android.usbserial.examples.R;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Props extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Settings sett;
    String username = "";
    int baudrate = 0;
    int databit = 0;
    int startstop = 0;
    int parity = 0;

    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editUsername;

    private Spinner spinner;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private static final String[]bauditems = {"9600", "115200"};
    private static final String[]databititems = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[]startstopitems = {"1", "2", "3"};
    private static final String[]parityitems = {"0", "1", "2", "3", "4"};

    ArrayAdapter adapterbaud;
    ArrayAdapter adapterdatabit;
    ArrayAdapter adapterstartstop;
    ArrayAdapter adapterparity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_props);
        sett = new Settings();

        Toast.makeText(this, "blabla", Toast.LENGTH_SHORT).show();
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner3 = (Spinner)findViewById(R.id.spinner3);

        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        editText3 = (EditText)findViewById(R.id.editText3);
        editText4 = (EditText)findViewById(R.id.editText4);
        editText5 = (EditText)findViewById(R.id.editText5);
        editUsername = (EditText) findViewById(R.id.editText6);

        editText1.setFocusableInTouchMode(false);
        editText2.setFocusableInTouchMode(false);
        editText3.setFocusableInTouchMode(false);
        editText4.setFocusableInTouchMode(false);
        editText5.setFocusableInTouchMode(false);

        adapterbaud = new ArrayAdapter<String>(Props.this, android.R.layout.simple_spinner_item,bauditems);
        adapterdatabit = new ArrayAdapter<String>(Props.this, android.R.layout.simple_spinner_item,databititems);
        adapterstartstop = new ArrayAdapter<String>(Props.this, android.R.layout.simple_spinner_item,startstopitems);
        adapterparity = new ArrayAdapter<String>(Props.this, android.R.layout.simple_spinner_item,parityitems);

        adapterbaud.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterbaud);
        spinner.setOnItemSelectedListener(this);

        adapterdatabit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapterdatabit);
        spinner1.setOnItemSelectedListener(this);

        adapterstartstop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapterstartstop);
        spinner2.setOnItemSelectedListener(this);

        adapterparity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapterparity);
        spinner3.setOnItemSelectedListener(this);
    }

    public void saveChanges(View view){
        username = editUsername.getText().toString();
        sett.setBaudrate(baudrate);
        sett.setDatabits(databit);
        sett.setStopbit(startstop);
        sett.setParity(parity);
        sett.setUsername(username);

        Intent intent = new Intent(this, SerialConsoleActivity.class);
        intent.putExtra("settings", sett);
        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if(parent.getAdapter().equals(adapterbaud)){
            switch (position) {
                case 0:
                    Toast.makeText(this, "gesetzt: " + sett.getBaudrate(), Toast.LENGTH_SHORT).show();
                    baudrate = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 1:
                    Toast.makeText(this, "gesetzt: " + sett.getBaudrate(), Toast.LENGTH_SHORT).show();
                    baudrate = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
            }
        }else if (parent.getAdapter().equals(adapterdatabit)){
            switch (position) {
                case 0:
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    databit = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 1:
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    databit = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 2:
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    databit = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 3:
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    databit = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 4:
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    databit = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 5:
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    databit = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 6:
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    databit = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 7:
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    databit = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
            }
        }else if (parent.getAdapter().equals(adapterstartstop)){
            switch (position) {
                case 0:
                    Toast.makeText(this, "gesetzt: " + sett.getStopbit(), Toast.LENGTH_SHORT).show();
                    startstop = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 1:
                    Toast.makeText(this, "gesetzt: " + sett.getStopbit(), Toast.LENGTH_SHORT).show();
                    startstop = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 2:
                    Toast.makeText(this, "gesetzt: " + sett.getStopbit(), Toast.LENGTH_SHORT).show();
                    startstop = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
            }
        }else if (parent.getAdapter().equals(adapterparity)){
            switch (position) {
                case 0:
                    Toast.makeText(this, "gesetzt: " + sett.getParity(), Toast.LENGTH_SHORT).show();
                    parity = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 1:
                    Toast.makeText(this, "gesetzt: " + sett.getParity(), Toast.LENGTH_SHORT).show();
                    parity = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 2:
                    Toast.makeText(this, "gesetzt: " + sett.getParity(), Toast.LENGTH_SHORT).show();
                    parity = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 3:
                    Toast.makeText(this, "gesetzt: " + sett.getParity(), Toast.LENGTH_SHORT).show();
                    parity = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
                case 4:
                    Toast.makeText(this, "gesetzt: " + sett.getParity(), Toast.LENGTH_SHORT).show();
                    parity = Integer.parseInt(parent.getAdapter().getItem(position).toString());
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
