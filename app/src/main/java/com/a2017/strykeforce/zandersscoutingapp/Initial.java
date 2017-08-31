package com.a2017.strykeforce.zandersscoutingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Switch;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import com.a2017.strykeforce.zandersscoutingapp.DataEntry;

public class Initial extends AppCompatActivity {

    void set_spinner_items_by_id(Spinner spinner, int arrayid)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayid, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }
    void initalize_inital_form_ui()
    {
        Spinner spinner = (Spinner) findViewById(R.id.inital_form_scouting_id_spinner);
        set_spinner_items_by_id
                (spinner, R.array.scouting_positions);
        spinner.setOnItemSelectedListener
        (
            new AdapterView.OnItemSelectedListener()
            {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
                {
                    DataEntry.team_index = pos;
                }
                public void onNothingSelected(AdapterView parent)
                {
                    // Do nothing.
                }
            }
        );
        NumberPicker match_number = (NumberPicker) findViewById(R.id.inital_form_match_number_picker);
        match_number.setMinValue(1);
        match_number.setValue(1);
        match_number.setMaxValue(DataEntry.team_matches.length - 1);
    }
    public void inital_form_on_next_button_click(View o)
    {
        NumberPicker match_number = (NumberPicker) findViewById(R.id.inital_form_match_number_picker);
        DataEntry.match_index = match_number.getValue() - 1;
        Intent intent = new Intent(this, DataEntry.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inital_form);
        initalize_inital_form_ui();
    }
}
