package com.a2017.strykeforce.remadescoutingapp2017;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    int[][] allTeamNums =
            {
                    // Red 1, Red 2, Red 3, Blue 1, Blue 2, Blue 3
                    {11, 12, 13, 14, 15, 16}, // round 1
                    {21, 22, 23, 24, 25, 26}, // round 2
                    {31, 32, 33, 34, 35, 36}, // round 3
                    {41, 42, 43, 44, 45, 46}, // round 4
            };
    ImageView QRImageView;
    int scoutid, current_match_number, team_number, lowgoalLoadsTele, gearsDeliveredTele;

    boolean CrossBaseLine, PlaceGear, AutoLow, touchpad, gearoffground, defense, getsdefended;
    String QRStr;

    TextView lowgoaldisplay;
    TextView geardisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                System.out.println("You clicked on the button!");
                try
                {
                    scoutid = Integer.valueOf(((EditText) findViewById(R.id.scoutid_edittext)).getText().toString());
                    System.out.println("scoutid == " + scoutid);
                    current_match_number = Integer.valueOf(((EditText) findViewById(R.id.match_number_edit_text)).getText().toString());
                    System.out.println("current_match_number == " + current_match_number);
                    if(1 <= scoutid && scoutid <= 6)
                    {
                        if(1 <= current_match_number && current_match_number <= allTeamNums.length) {
                            setContentView(R.layout.activity_main);
                            ResetMatch();
                            popupscouttScreen();
                            lowgoaldisplay = (TextView) findViewById(R.id.lowgoalloaddata);
                            geardisplay = (TextView) findViewById(R.id.textView6);
                        }
                }}
                catch(NumberFormatException nfe)
                {
                    System.out.println("not typed correctly!");
                }
            }
        });
    }
    public void popupscouttScreen()
    {
        findViewById(R.id.lowgoalsub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowgoalLoadsTele--;
                lowgoaldisplay.setText(Integer.toString(lowgoalLoadsTele));
            }});
        findViewById(R.id.lowgoaladd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lowgoalLoadsTele++;
                lowgoaldisplay.setText(Integer.toString(lowgoalLoadsTele));
            }});
        findViewById(R.id.gearsadd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gearsDeliveredTele++;
                geardisplay.setText(Integer.toString(gearsDeliveredTele));
            }});
        findViewById(R.id.gearssub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gearsDeliveredTele--;
                geardisplay.setText(Integer.toString(gearsDeliveredTele));
            }});

        findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                GenerateQRString();
                setContentView(R.layout.popup);
                QRImageView = (ImageView) findViewById(R.id.qr_code_image);
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix = multiFormatWriter.encode(QRStr,BarcodeFormat.QR_CODE,400,400);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    QRImageView.setImageBitmap(bitmap);
                }
                catch (WriterException e) {
                    e.printStackTrace();

                }

                findViewById(R.id.go_back_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.activity_main);
                        popupscouttScreen();
                    }});

                findViewById(R.id.next_match_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.activity_main);
                        System.out.println("findViewById(R.id.next_match_button).setOnClickListener");
                        if(current_match_number < allTeamNums.length) {
                            current_match_number++;
                        }
                        else
                        {
                            current_match_number = 1;
                        }
                        ResetMatch();
                        popupscouttScreen();
                    }});
            }
        });
    }
    public void ResetMatch() {
        //gets next match number and team number NB
        TextView matchDisplay = (TextView) findViewById(R.id.matchdata);
        matchDisplay.setText(Integer.toString(current_match_number));
        team_number = allTeamNums[current_match_number - 1][scoutid - 1]; //gets team from list generated from text file
        TextView teamDisplay = (TextView) findViewById(R.id.teamdata);
        System.out.println("team_number == " + team_number);
        teamDisplay.setText(Integer.toString(team_number));

        Switch baselineswitch  = (Switch) findViewById(R.id.baseline);
        baselineswitch.setChecked(false);

        Switch placegearSwitch = (Switch) findViewById(R.id.placegearautodata);
        placegearSwitch.setChecked(false);

        Switch lowgoalAutoSwitch = (Switch) findViewById(R.id.lowgoaldataauto);
        lowgoalAutoSwitch.setChecked(false);

        Switch touchpadSwitch = (Switch) findViewById(R.id.touchpad);
        touchpadSwitch.setChecked(false);;

        Switch gearoffgroundSwitch = (Switch) findViewById(R.id.gearoffground);
        gearoffgroundSwitch.setChecked(false);


        Switch OnDefenseSwitch = (Switch) findViewById(R.id.ondefence);
        OnDefenseSwitch.setChecked(false);


        Switch GetsDefendedSwitch = (Switch) findViewById(R.id.highgoaldefence);
        GetsDefendedSwitch.setChecked(false);
    }
    public void GenerateQRString()
    {
        Switch baselineswitch  = (Switch) findViewById(R.id.baseline);
        CrossBaseLine = baselineswitch.isChecked();

        Switch placegearSwitch = (Switch) findViewById(R.id.placegearautodata);
        PlaceGear = placegearSwitch.isChecked();

        Switch lowgoalAutoSwitch = (Switch) findViewById(R.id.lowgoaldataauto);
        AutoLow = lowgoalAutoSwitch.isChecked();

        Switch touchpadSwitch = (Switch) findViewById(R.id.touchpad);
        touchpad = touchpadSwitch.isChecked();

        Switch gearoffgroundSwitch = (Switch) findViewById(R.id.gearoffground);
        gearoffground = gearoffgroundSwitch.isChecked();


        Switch OnDefenseSwitch = (Switch) findViewById(R.id.ondefence);
        defense = OnDefenseSwitch.isChecked();


        Switch GetsDefendedSwitch = (Switch) findViewById(R.id.highgoaldefence);
        getsdefended = GetsDefendedSwitch.isChecked();

        QRStr = "Scout ID: " + scoutid + "\n"
                + "Low Goal Loads in Tele: " + lowgoalLoadsTele+ "\n"
                + " Crosses base line: "+ CrossBaseLine+"\n"
                + "Gears Dilivered in Tele: " + gearsDeliveredTele +"\n"
                + " Places Gear in Auton: " + PlaceGear +"\n"
                + " Scores the low goal in auton: " + AutoLow +"\n"
                + " On defence: " + defense + "\n"
                + " Can pick a gear off the ground: " + gearoffground + "\n"
                + " Defended while shooting high goal: " + getsdefended + "\n"
                + " Touchpad: " + touchpad;

    }
}
