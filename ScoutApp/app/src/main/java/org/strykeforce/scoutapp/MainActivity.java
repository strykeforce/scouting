
package org.strykeforce.scoutapp;



import android.content.DialogInterface;

import android.graphics.Color;

import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Button;

import android.view.View;

import android.widget.EditText;

import android.widget.SeekBar;

import android.widget.TextView;

import android.widget.Switch;

import android.widget.ImageView;

import android.graphics.Bitmap;



import com.google.zxing.BarcodeFormat;

import com.google.zxing.MultiFormatWriter;

import com.google.zxing.WriterException;

import com.google.zxing.common.BitMatrix;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import java.io.File;
import java.io.IOException;

import java.io.InputStream;

import java.util.Scanner;


import java.util.*;

public class MainActivity extends AppCompatActivity {

    /*THINGS THAT NEED TO IMPROVE:

    slide bar showing actual numbers

    lock screen orientation -> change manifest file and qr code layout

    no negatives in gear display



    Pack List: paper and ink

    10 tablets

        4 red - 3 red scouts and 1 extra unnamed

        4 blue - 3 blue scouts and 1 extra unnamed

        1 Large Black Master Tablet

        1 Small Black Extra Master Tablet

     Need to Buy Still:

        7-8 clickers

        10-12 battery packs



        ORDER OF ITEMS IS WRONG

    */



    boolean CrossBaseLine = false, PlaceGear = false, AutoLow = false;

    String autohigh = "0";

    boolean gearoffground = false;

    String highgoals = "0";

    boolean getsdefended = false, touchpad = false, defense = false, switchCube = false;

    String scoutid;

    private ImageView QRImageView;

    private String QRStr;

    int lowgoalLoadsTele = 0, gearsDeliveredTele = 0;

    TextView lowgoaldisplay, geardisplay;

    String scoutName = "n/a", notes = "none";

    private static SeekBar seek_bar;

    private TextView scoutDisplay, masterDisplay;

    private ImageView qrdisplay;

    private Button continueQR, backbtn;

    int step = 1, max = 30, min = 5, progressSeek;



    //Nika's Variables

    private int[][] allTeamNums;

    private boolean[] matchDone;

    private int numMatches;

    private String teamText = "";

    private static int MATCH_NUMBER=0, TEAM_NUMBER = 0, SCOUT_ID = 0; //current match and team num



    @Override

    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        final AlertDialog.Builder builderSend = new AlertDialog.Builder(this);

        builderSend.setTitle("NEXT MATCH?");



        allTeamNums = getTeamNums();

        matchDone = new boolean[numMatches];

        for (int j = 0; j < numMatches; j++)

            matchDone[j] = false;



        setContentView(R.layout.start);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {



                EditText inputText = (EditText) findViewById(R.id.editText);

                SCOUT_ID = Integer.parseInt(inputText.getText().toString()) - 1;



                EditText matchInput = (EditText) findViewById(R.id.editText7);

                MATCH_NUMBER = Integer.parseInt(matchInput.getText().toString()) - 1;



                if (notValidMatch(MATCH_NUMBER) || notValidScout(SCOUT_ID)) {

                    resetScoutScreen();

                }

                else {

                    setContentView(R.layout.auton);

                    scoutDisplay = (TextView) findViewById(R.id.scoutDisplay);

                    setScouter(SCOUT_ID);

                    /*masterDisplay = (TextView) findViewById(R.id.masterDisplay);

                    lowgoaldisplay = (TextView) findViewById(R.id.lowgoalloaddata);

                    geardisplay = (TextView) findViewById(R.id.gearNumDisplay);*/

                    MATCH_NUMBER--;


                    ResetMatch();

                    /*qrdisplay = (ImageView) findViewById(R.id.imageView);

                    continueQR = (Button) findViewById(R.id.continue_btn);

                    backbtn = (Button) findViewById(R.id.back_btn);*/



                    /*if (SCOUT_ID < 3)

                        ((TextView) findViewById(R.id.masterDisplay)).setTextColor(Color.parseColor("#ffcc0000"));

                    else

                        ((TextView) findViewById(R.id.masterDisplay)).setTextColor(Color.parseColor("#283593"));*/




                    builderSend.setMessage("Are you sure you want to continue? Did the MASTER scan your data?");

                    continueQR.setOnClickListener(new View.OnClickListener() {

                        @Override

                        public void onClick(View v) {

                            builderSend.setPositiveButton("YES", new DialogInterface.OnClickListener() { //sets what the yes option will do

                                public void onClick(DialogInterface dialog, int which) {


                                    ResetMatch(); //calls method to next match

                                    dialog.dismiss(); //closes dialog box

                                }

                            });

                            builderSend.setNegativeButton("NO", new DialogInterface.OnClickListener() { //sets what the no option will do

                                @Override

                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss(); //closes dialog box

                                }

                            });

                            AlertDialog alert = builderSend.create();

                            alert.show();

                            TextView msgTxt = (TextView) alert.findViewById(android.R.id.message);

                            msgTxt.setTextSize((float) 35.0);

                        }

                    });


                    SeekBar seekbar = (SeekBar) findViewById(R.id.scaleTime);

                    seekbar.setMax((max - min) / step);



                    seekbar.setOnSeekBarChangeListener(

                            new SeekBar.OnSeekBarChangeListener() {

                                @Override

                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }



                                @Override

                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }



                                @Override

                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                                    double value = min + (progress * step);

                                    progressSeek = progress;

                                }

                            }

                    );

                   /* findViewById(R.id.lowgoalsub).setOnClickListener(new View.OnClickListener() {

                        @Override

                        public void onClick(View v) {

                            lowgoalLoadsTele--;

                            lowgoaldisplay.setText(Integer.toString(lowgoalLoadsTele));

                        }

                    });



                    findViewById(R.id.lowgoaladd).setOnClickListener(new View.OnClickListener() {

                        @Override

                        public void onClick(View v) {

                            lowgoalLoadsTele++;

                            lowgoaldisplay.setText(Integer.toString(lowgoalLoadsTele));

                        }

                    });



                    findViewById(R.id.gearsadd).setOnClickListener(new View.OnClickListener() {

                        @Override

                        public void onClick(View v) {

                            gearsDeliveredTele++;

                            geardisplay.setText(Integer.toString(gearsDeliveredTele));

                        }

                    });



                    findViewById(R.id.gearssub).setOnClickListener(new View.OnClickListener() {

                        @Override

                        public void onClick(View v) {

                            gearsDeliveredTele--;

                            geardisplay.setText(Integer.toString(gearsDeliveredTele));

                        }

                    }); */



                    findViewById(R.id.sendbutton).setOnClickListener(new View.OnClickListener() {

                        @Override

                        public void onClick(View v) {

                            GenerateQRString();

                            if (SCOUT_ID < 3) {

                                masterDisplay.setText("ID: Red " + (SCOUT_ID + 1) + "\nTEAM: " + TEAM_NUMBER + "\nMATCH: " + (MATCH_NUMBER + 1));

                            } else {

                                masterDisplay.setText("ID: Blue " + (SCOUT_ID - 2) + "\nTEAM: " + TEAM_NUMBER + "\nMATCH: " + (MATCH_NUMBER+1));

                            }

                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();



                            try {

                                BitMatrix bitMatrix = multiFormatWriter.encode(QRStr, BarcodeFormat.QR_CODE, 400, 400);

                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

                                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                                qrdisplay.setImageBitmap(bitmap);

                            } catch (WriterException e) {

                                e.printStackTrace();

                            }

                        }

                    });

                }

            }

        });

    }



    public void GenerateQRString()

    {


        EditText inputText3 = (EditText) findViewById(R.id.editText2);

        notes = inputText3.getText().toString();


        Switch baseline = (Switch) findViewById(R.id.baseline);

        CrossBaseLine = baseline.isChecked();


        Switch deliverSwitchAuton = (Switch) findViewById(R.id.deliverSwitchAuton);

        switchCube = deliverSwitchAuton.isChecked();


        /*Switch lowgoalAutoSwitch = (Switch) findViewById(R.id.lowgoaldataauto);

        AutoLow = lowgoalAutoSwitch.isChecked();


        Switch touchpadSwitch = (Switch) findViewById(R.id.touchpad);

        touchpad = touchpadSwitch.isChecked();


        Switch gearoffgroundSwitch = (Switch) findViewById(R.id.gearoffground);

        gearoffground = gearoffgroundSwitch.isChecked();


        Switch OnDefenseSwitch = (Switch) findViewById(R.id.ondefence);

        defense = OnDefenseSwitch.isChecked();


        Switch GetsDefendedSwitch = (Switch) findViewById(R.id.highgoaldefence);

        getsdefended = GetsDefendedSwitch.isChecked();*/



    /*

0    SCOUT ID int

1    TEAM NUM int

2    MATCH NUM int



3    Auto High int



4    Tele High int

5    Tele Low int

  6  Tele Gears int

7    Climb Rope Time int



8    Auto Low BOOL

9    Auto Gears BOOL

 A   Crosses base line BOOL

  B  Picks gear off ground BOOL

   12 On defence BOOL

    D Defended shooting high  BOOL

  E  Touchpad BOOL

  F  Scout Name StrING

  10  Notes STRING

     */
        if (Integer.parseInt(highgoals) < 0) {
            highgoals = "0";
        }
        if (gearsDeliveredTele < 0) {

            gearsDeliveredTele = 0;
        }

            QRStr = "Scout ID: " + (SCOUT_ID + 1) + "\t"
                    +"Team: " + TEAM_NUMBER + "\t"
                    +"Match: " + (MATCH_NUMBER+1) + "\t"
                    +"Auto High: " + autohigh + "\t"
                    +"Tele High: " + highgoals + "\t"
                    +"Tele Low: " + lowgoalLoadsTele+ "\t"
                    +"Tele Gear: " + gearsDeliveredTele +"\t"
                    +"Climb rope time: " + ((touchpad) ? (progressSeek+min) : ("0")) + "\t"
                    +"Auto Low: " + AutoLow + "\t"
                    +"Auto Switch: " + switchCube + "\t"
                    +"Crosses base line: "+ CrossBaseLine+ "\t"
                    +"Can pick gears off ground: " + gearoffground + "\t"
                    +"On defence: " + defense + "\t"
                    +"Defended shooting high: " + getsdefended + "\t"
                    +"Touchpad: " + touchpad + "\t"
                    +"Scout Name: " + scoutName + "\t"
                    +"Notes: " + notes + "\t";



    }



    public void popupscouttScreen(){

        {

            SeekBar seekbar = (SeekBar) (findViewById(R.id.scaleTime));

            seekbar.setMax((max - min) / step);



            seekbar.setOnSeekBarChangeListener(

                    new SeekBar.OnSeekBarChangeListener() {

                        @Override

                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }

                        @Override

                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override

                        public void onProgressChanged(SeekBar seekBar, int progress,

                                                      boolean fromUser) {

                            double value = min + (progress * step);

                            progressSeek = progress;

                        }

                    }

            );



/*
            findViewById(R.id.lowgoalsub).setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    lowgoalLoadsTele--;

                    lowgoaldisplay.setText(Integer.toString(lowgoalLoadsTele));

                }

            });



            findViewById(R.id.lowgoaladd).setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    lowgoalLoadsTele++;

                    lowgoaldisplay.setText(Integer.toString(lowgoalLoadsTele));

                }

            });



            findViewById(R.id.gearsadd).setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    gearsDeliveredTele++;

                    geardisplay.setText(Integer.toString(gearsDeliveredTele));

                }

            });



            findViewById(R.id.gearssub).setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    gearsDeliveredTele--;

                    geardisplay.setText(Integer.toString(gearsDeliveredTele));

                }

            }); */


/*
            findViewById(R.id.sendbutton).setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    GenerateQRString();

                    setContentView(R.layout.popup);

                    popupqrscreen();

                }

            });
            */

        }}



    //Segregated popup from scout screen

    public void popupqrscreen()

    {

        QRImageView = (ImageView) findViewById(R.id.imageView2);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {

            BitMatrix bitMatrix = multiFormatWriter.encode(QRStr, BarcodeFormat.QR_CODE, 400, 400);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            QRImageView.setImageBitmap(bitmap);

        } catch (WriterException e) {

            e.printStackTrace();

        }



        findViewById(R.id.button_No).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                setContentView(R.layout.activity_main);

                popupscouttScreen();

            }

        });



        findViewById(R.id.button_yes).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                setContentView(R.layout.auton);

                ResetMatch();

                popupscouttScreen();

            }

        });

    }

    public void ResetMatch() {

        //gets next match number and team number NB

        MATCH_NUMBER++;

        TextView matchDisplay = (TextView) findViewById(R.id.matchdata);

        matchDisplay.setText(Integer.toString(MATCH_NUMBER + 1));



        TEAM_NUMBER = allTeamNums[MATCH_NUMBER][SCOUT_ID]; //gets team from list generated from text file

        TextView teamDisplay = (TextView) findViewById(R.id.teamdata);

        teamDisplay.setText(Integer.toString(TEAM_NUMBER));



        SeekBar seekbar = (SeekBar) (findViewById(R.id.scaleTime));

        seekbar.setProgress(0);



        Switch baseline  = (Switch) findViewById(R.id.baseline);

        baseline.setChecked(false);



        Switch deliverSwitchAuton = (Switch) findViewById(R.id.deliverSwitchAuton);

        deliverSwitchAuton.setChecked(false);





        gearsDeliveredTele = 0;

        lowgoalLoadsTele = 0;

        geardisplay.setText("0");

        lowgoaldisplay.setText("0");





        /*EditText highgoalsauto = (EditText) findViewById(R.id.editText6);

        highgoalsauto.setText("0");*/

        //highgoalsauto.setTextColor(Color.parseColor("@android:color/holo_green_dark"));



        EditText notes = (EditText) findViewById(R.id.editText2);

        notes.setText(".");



    }



    //gets all the team numbers from text file and
public void println(String line)
{
    System.out.println(line);
}
    /*
    This is a terrible function, and if I were to be using an actually GOOD language, like C, maybe
    This could have been implemented better. This sucks, but it's NOT MY FAULT!
     */
    public int[][] getTeamNums()

    {
        int i;
        numMatches=0;
        try {
            Scanner s = new Scanner(new File("/storage/emulated/0/MyTeamMatches.csv"));

            while (s.hasNextLine()) {
            s.nextLine();
                numMatches++;

            }
            s.close();
            s = new Scanner(new File("/storage/emulated/0/MyTeamMatches.csv"));

            int[][] returned = new int[numMatches][6];
            for(i = 0;i<numMatches;i++)
            {
                returned[i] = new int[6];
                String[] args = s.nextLine().split(",");
                for (int ii = 0; ii < 6; ii++)
                {
                    returned[i][ii] = Integer.valueOf(args[ii]);
                }
            }
            println("</getTeamNums>\n");
            s.close();
            return returned;
        }
        catch(Exception e)
        {
            println("oh nose!");
            return null;
        }

    }



    private void setScouter(int idscout)

    {

        if(idscout<4)

        {

            scoutDisplay.setText("Red " + (idscout + 1));

            scoutDisplay.setTextColor(Color.parseColor("#ff0000"));

        }

        else{

            scoutDisplay.setText("Blue " + (idscout-2));

            scoutDisplay.setTextColor(Color.parseColor("#1d34e2"));

        }

    }



    private boolean notValidMatch(int matchnum)

    {

        if(matchnum>numMatches || matchnum < 0)

        {

            return true;

        }

        else

            return false;

    }



    private boolean notValidScout(int id)

    {

        if(id<0 || id>5)

            return true;

        else

            return false;

    }



    private void resetScoutScreen()

    {

        final AlertDialog.Builder validNumbers = new AlertDialog.Builder(this);

        validNumbers.setTitle("PLEASE ENTER VALID NUMBER");



        validNumbers.setMessage("Please make sure your scout ID is between 1-6 and you enter a valid match number.");

        validNumbers.setNeutralButton("OK", new DialogInterface.OnClickListener() { //sets what the yes option will do

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss(); //closes dialog box

            }

        });

        AlertDialog alert = validNumbers.create();

        alert.show();

        TextView msgTxt = (TextView) alert.findViewById(android.R.id.message);

        msgTxt.setTextSize((float)35.0);

        EditText inputText = (EditText) findViewById(R.id.editText);

        inputText.setText("");



        EditText matchInput = (EditText) findViewById(R.id.editText7);

        matchInput.setText("");

    }




}





