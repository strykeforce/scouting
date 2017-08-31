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
        Spinner spinner = (Spinner) findViewById(R.id.data_entry_auton_gear_placement_spinner);
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
        boolean auton_cross_base_line_value, auton_low_goal_dumped_value;
        String auton_gear_placement_value;
        String autohigh;
        boolean tele_picks_gears_off_ground_value;
        String tele_high_goals_scored_value;
        boolean tele_defence_interferes_value, end_activated_touchpad_value, other_played_defense_value;
        String scoutName, notes;
        int end_rope_climb_time_value, tele_low_goal_value, tele_gears_delievered_value;
        EditText deahgset = (EditText) findViewById(R.id.data_entry_auton_high_goals_scored_edit_text);
        autohigh = deahgset.getText().toString();
        EditText deosiet = (EditText) findViewById(R.id.data_entry_other_scout_initials_edit_text);
        scoutName = deosiet.getText().toString();
        if(scoutName.isEmpty())
        {
            scoutName = ".";
        }
        EditText deonet = (EditText) findViewById(R.id.data_entry_other_notes_edit_text);
        notes = deonet.getText().toString();
        if(notes.isEmpty())
        {
            notes = ".";
        }
        EditText dethgset = (EditText) findViewById(R.id.data_entry_tele_high_goals_scored_edit_text);
        tele_high_goals_scored_value = dethgset.getText().toString();
        Switch deacbls = (Switch) findViewById(R.id.data_entry_auton_cross_base_line_switch);
        auton_cross_base_line_value = deacbls.isChecked();
        Spinner deagps = (Spinner) findViewById(R.id.data_entry_auton_gear_placement_spinner);
        auton_gear_placement_value = (String) deagps.getSelectedItem();
        Switch aealgds = (Switch) findViewById(R.id.data_entry_auton_low_goal_dumped_switch);
        auton_low_goal_dumped_value = aealgds.isChecked();
        Switch deeats = (Switch) findViewById(R.id.data_entry_end_activated_touchpad_switch);
        end_activated_touchpad_value = deeats.isChecked();
        Switch detpgogs = (Switch) findViewById(R.id.data_entry_tele_picks_gears_off_ground_switch);
        tele_picks_gears_off_ground_value = detpgogs.isChecked();
        Switch deopds = (Switch) findViewById(R.id.data_entry_other_played_defense_switch);
        other_played_defense_value = deopds.isChecked();
        Switch detdis = (Switch) findViewById(R.id.data_entry_tele_defence_interferes_switch);
        tele_defence_interferes_value = detdis.isChecked();
        SeekBar deerctsb = (SeekBar) findViewById(R.id.data_entry_end_rope_climb_time_seek_bar);
        end_rope_climb_time_value = deerctsb.getProgress();
        EditText detlget = (EditText) findViewById(R.id.data_entry_tele_low_goal_edit_text);
        tele_low_goal_value = Integer.valueOf(detlget.getText().toString());
        EditText detgdet = (EditText) findViewById(R.id.data_entry_tele_gears_delievered_edit_text);
        tele_gears_delievered_value = Integer.valueOf(detgdet.getText().toString());
        String qr_code =  "Scout ID: " + (team_index + 1) + "\t"
                +"Team: " + (team_matches[match_index][team_index]) + "\t"
                +"Match: " + (match_index+1) + "\t"
                +"Auto High: " + autohigh + "\t"
                +"Tele High: " + tele_high_goals_scored_value + "\t"
                +"Tele Low: " + tele_low_goal_value+ "\t"
                +"Tele Gear: " + tele_gears_delievered_value +"\t"
                +"Climb rope time: " + ((end_activated_touchpad_value) ? (end_rope_climb_time_value) : ("0")) + "\t"
                +"Auto Low: " + auton_low_goal_dumped_value + "\t"
                +"Auto Gear: " + auton_gear_placement_value + "\t"
                +"Crosses base line: "+ auton_cross_base_line_value+ "\t"
                +"Can pick gears off ground: " + tele_picks_gears_off_ground_value + "\t"
                +"On defence: " + other_played_defense_value + "\t"
                +"Defended shooting high: " + tele_defence_interferes_value + "\t"
                +"Touchpad: " + end_activated_touchpad_value + "\t"
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
