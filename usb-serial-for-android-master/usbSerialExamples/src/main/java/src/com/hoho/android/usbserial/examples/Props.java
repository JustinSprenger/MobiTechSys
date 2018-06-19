package src.com.hoho.android.usbserial.examples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.hoho.android.usbserial.examples.R;

public class Props extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Settings sett;

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

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if(parent.getAdapter().equals(adapterbaud)){
            switch (position) {
                case 0:
                    sett.setBaudrate(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getBaudrate(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    sett.setBaudrate(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getBaudrate(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }else if (parent.getAdapter().equals(adapterdatabit)){
            switch (position) {
                case 0:
                    sett.setDatabits(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    sett.setDatabits(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    sett.setDatabits(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    sett.setDatabits(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    sett.setDatabits(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    sett.setDatabits(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    sett.setDatabits(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    sett.setDatabits(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getDatabits(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }else if (parent.getAdapter().equals(adapterstartstop)){
            switch (position) {
                case 0:
                    sett.setStopbit(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getStopbit(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    sett.setStopbit(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getStopbit(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    sett.setStopbit(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getStopbit(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }else if (parent.getAdapter().equals(adapterparity)){
            switch (position) {
                case 0:
                    sett.setParity(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getParity(), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    sett.setParity(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getParity(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    sett.setParity(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getParity(), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    sett.setParity(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getParity(), Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    sett.setParity(Integer.parseInt(parent.getAdapter().getItem(position).toString()));
                    Toast.makeText(this, "gesetzt: " + sett.getParity(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
