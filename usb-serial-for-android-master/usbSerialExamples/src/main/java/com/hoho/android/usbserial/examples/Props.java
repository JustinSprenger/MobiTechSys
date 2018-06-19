package com.hoho.android.usbserial.examples;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import src.com.hoho.android.usbserial.examples.Settings;

public class Props extends AppCompatActivity {
    Settings sett;

    private Spinner spinner;
    private static final String[]paths = {"item 1", "item 2", "item 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_props);
        sett = new Settings();


        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(Props.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                Toast.makeText(this, "item1", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "item1", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "item1", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
