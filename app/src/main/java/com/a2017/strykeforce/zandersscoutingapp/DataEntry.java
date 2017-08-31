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

public class DataEntry extends AppCompatActivity {

    public static int team_matches[][] =
    {
        {11, 12, 13, 14, 15, 16},
        {21, 22, 23, 24, 25, 26},
        {31, 32, 33, 34, 35, 36},
        {41, 42, 43, 44, 45, 46},
        {51, 52, 53, 54, 55, 56}
    };
    public static int match_index, team_index;

    void set_spinner_items_by_id(Spinner spinner, int arrayid)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayid, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
    }
    void set_all_switches_to_false(int ... ids)
    {
        int i;
        for(i = 0;i<ids.length;i++)
        {
            Switch sw = (Switch) findViewById(ids[i]);
            sw.setChecked(false);
        }
    }
    void set_all_edit_texts_to_0(int ... ids)
    {
        int i;
        for(i = 0;i<ids.length;i++)
        {
            EditText et = (EditText) findViewById(ids[i]);
            et.setText("0");
        }
    }
    void set_all_edit_texts_to_empty_string(int ... ids)
    {
        int i;
        for(i = 0;i<ids.length;i++)
        {
            EditText et = (EditText) findViewById(ids[i]);
            et.setText("");
        }
    }
    void reset_data_entry_from_ui()
    {
        TextView detntv = (TextView) findViewById(R.id.data_entry_team_number_text_view);
        detntv.setText("Team Number: " + team_matches[match_index][team_index]);
        TextView demntv = (TextView) findViewById(R.id.data_entry_match_number_text_view);
        demntv.setText("Match Number: " + (match_index + 1));
        set_all_edit_texts_to_0
                (
                        R.id.data_entry_auton_high_goals_scored_edit_text,
                        R.id.data_entry_tele_gears_delievered_edit_text,
                        R.id.data_entry_tele_low_goal_edit_text,
                        R.id.data_entry_tele_high_goals_scored_edit_text
                );
        set_all_switches_to_false
                (
                        R.id.data_entry_auton_cross_base_line_switch,
                        R.id.data_entry_auton_low_goal_dumped_switch,
                        R.id.data_entry_tele_picks_gears_off_ground_switch,
                        R.id.data_entry_tele_defence_interferes_switch,
                        R.id.data_entry_end_activated_touchpad_switch,
                        R.id.data_entry_other_played_defense_switch
                );
        set_all_edit_texts_to_empty_string
                (
                        R.id.data_entry_other_scout_initials_edit_text,
                        R.id.data_entry_other_notes_edit_text
                );
        ((SeekBar)findViewById(R.id.data_entry_end_rope_climb_time_seek_bar)).setProgress(15);
    }
    void initalize_data_entry_form_ui()
    {
        Spinner spinner = (Spinner) findViewById(R.id.data_entry_gear_placement_spinner);
        set_spinner_items_by_id (spinner, R.array.gear_positions);
        Button detdgdb = (Button) findViewById(R.id.data_entry_tele_decrement_gears_delievered_button);
        Button detigdb = (Button) findViewById(R.id.data_entry_tele_increment_gears_delievered_button);
        detdgdb.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View var1)
                    {
                        EditText detgdet = (EditText) findViewById(R.id.data_entry_tele_gears_delievered_edit_text);
                        int current_val = Integer.valueOf(detgdet.getText().toString());
                        if(current_val > 0)
                        {
                            current_val--;
                        }
                        detgdet.setText("" + current_val);
                    }
                }
        );
        detigdb.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View var1)
                    {
                        EditText detgdet = (EditText) findViewById(R.id.data_entry_tele_gears_delievered_edit_text);
                        detgdet.setText("" + (Integer.valueOf(detgdet.getText().toString()) + 1));
                    }
                }
        );
        Button detdlglb = (Button) findViewById(R.id.data_entry_tele_decrement_low_goal_loads_button);
        Button detilglb = (Button) findViewById(R.id.data_entry_tele_increment_low_goal_loads_button);
        detdlglb.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View var1)
                    {
                        EditText detgdet = (EditText) findViewById(R.id.data_entry_tele_low_goal_edit_text);
                        int current_val = Integer.valueOf(detgdet.getText().toString());
                        if(current_val > 0)
                        {
                            current_val--;
                        }
                        detgdet.setText("" + current_val);
                    }
                }
        );
        detilglb.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View var1)
                    {
                        EditText detgdet = (EditText) findViewById(R.id.data_entry_tele_low_goal_edit_text);
                        detgdet.setText("" + (Integer.valueOf(detgdet.getText().toString()) + 1));
                    }
                }
        );
        reset_data_entry_from_ui();
    }
    void data_entry_form_on_click_done_button(View v)
    {
        boolean CrossBaseLine, AutoLow;
        String PlaceGear;
        String autohigh;
        boolean gearoffground;
        String highgoals;
        boolean getsdefended, touchpad, defense;
        String scoutid;
        String scoutName, notes;
        int progressSeek;
        EditText inputText1 = (EditText) findViewById(R.id.data_entry_auton_high_goals_scored_edit_text);
        autohigh = inputText1.getText().toString();
        EditText inputText2 = (EditText) findViewById(R.id.data_entry_other_scout_initials_edit_text);
        scoutName = inputText2.getText().toString();
        if(scoutName.isEmpty())
        {
            scoutName = ".";
        }
        EditText inputText3 = (EditText) findViewById(R.id.data_entry_other_notes_edit_text);
        notes = inputText3.getText().toString();
        if(notes.isEmpty())
        {
            notes = ".";
        }
        EditText inputText4 = (EditText) findViewById(R.id.data_entry_tele_high_goals_scored_edit_text);
        highgoals = inputText4.getText().toString();
        Switch baselineswitch = (Switch) findViewById(R.id.data_entry_auton_cross_base_line_switch);
        CrossBaseLine = baselineswitch.isChecked();
        Spinner placegearSwitch = (Spinner) findViewById(R.id.data_entry_auton_gear_placement_spinner);
        PlaceGear = (String) placegearSwitch.getSelectedItem();
        Switch lowgoalAutoSwitch = (Switch) findViewById(R.id.data_entry_auton_low_goal_dumped_switch);
        AutoLow = lowgoalAutoSwitch.isChecked();
        Switch touchpadSwitch = (Switch) findViewById(R.id.data_entry_end_activated_touchpad_switch);
        touchpad = touchpadSwitch.isChecked();
        Switch gearoffgroundSwitch = (Switch) findViewById(R.id.data_entry_tele_picks_gears_off_ground_switch);
        gearoffground = gearoffgroundSwitch.isChecked();
        Switch OnDefenseSwitch = (Switch) findViewById(R.id.data_entry_other_played_defense_switch);
        defense = OnDefenseSwitch.isChecked();
        Switch GetsDefendedSwitch = (Switch) findViewById(R.id.data_entry_tele_defence_interferes_switch);
        getsdefended = GetsDefendedSwitch.isChecked();
        SeekBar seekbar3 = (SeekBar) findViewById(R.id.data_entry_end_rope_climb_time_seek_bar);
        progressSeek = seekbar3.getProgress();
        String qr_code =  "Scout ID: " + (team_index + 1) + "\t"
                +"Team: " + (team_matches[match_index][team_index]) + "\t"
                +"Match: " + (match_index+1) + "\t"
                +"Auto High: " + autohigh + "\t"
                +"Tele High: " + highgoals + "\t"
                +"Tele Low: " + lowgoalLoadsTele+ "\t"
                +"Tele Gear: " + gearsDeliveredTele +"\t"
                +"Climb rope time: " + ((touchpad) ? (progressSeek) : ("0")) + "\t"
                +"Auto Low: " + AutoLow + "\t"
                +"Auto Gear: " + PlaceGear + "\t"
                +"Crosses base line: "+ CrossBaseLine+ "\t"
                +"Can pick gears off ground: " + gearoffground + "\t"
                +"On defence: " + defense + "\t"
                +"Defended shooting high: " + getsdefended + "\t"
                +"Touchpad: " + touchpad + "\t"
                +"Scout Name: " + scoutName + "\t"
                +"Notes: " + notes + "\t";
        System.out.println("qr_code == " + qr_code);
        QR_Scanner.code = qr_code;
        Intent intent = new Intent(this, QR_Scanner.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_entry_form);
        initalize_data_entry_form_ui();
    }
}
