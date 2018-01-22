
package org.strykeforce.scoutapp;



import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.widget.Button;

import android.view.View;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import android.widget.Switch;
import android.widget.TextView;

import android.widget.ImageView;

import java.util.Scanner;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    //TAG is used for inserting tags later on for troubleshooting purposes
    private final static String TAG = "Sam";
    /*THINGS THAT NEED TO IMPROVE:


    Pack List: paper and ink

    10 tablets

        4 red - 3 red scouts and 1 extra unnamed

        4 blue - 3 blue scouts and 1 extra unnamed

        1 Large Black Master Tablet

        1 Small Black Extra Master Tablet


    */


    boolean BaseLineBool, DeliverSwitchBool, SecondCubeBool, AutoScaleBool = false;
    int ScaleTimeInt = 0;

    int PortalCubes = 0, CenterCubes = 0, ZoneCubes = 0, SwitchCubes = 0, ScaleCubes = 0, ExchangeCubes = 0;
    boolean ClimbAttempt, Climb, Lift1, Lift2, Lifted, Platform;

    boolean Failed = false;
    int Penalties = 0;
    String Notes = "none";



    int ScoutId;
    int StartMatch;


    private ImageView QRImageView;

    private String QRStr;


    private static SeekBar seek_bar;


    private TextView scoutDisplay, masterDisplay;

    private ImageView qrdisplay;

    private Button continueQR, backbtn;

    int step = 1, max = 30, min = 5, progressSeek;

    int state = 1;

    //Nika's Variables

    private int[][] allTeamNums;
    //allTeamNums = getTeamNums();
    private boolean[] matchDone;

    private int MatchLimit = 1000;

    private String teamText = "";

    private static int MATCH_NUMBER = 0, TEAM_NUMBER = 0, SCOUT_ID = 0; //current match and team num


    /*final public Switch baseline = (Switch) findViewById(R.id.baseline);
    final public Switch deliverSwitchAuton = (Switch) findViewById(R.id.deliverSwitchAuton);
    final public Switch cubex2 = (Switch) findViewById(R.id.cubex2);
    final public Switch autoScale = (Switch) findViewById(R.id.autoScale);
    final public SeekBar scaleTime = (SeekBar) findViewById(R.id.scaleTime);

    final public TextView portalcubes = (TextView) findViewById(R.id.portalcubes);
    final public TextView centercubes = (TextView) findViewById(R.id.centercubes);
    final public TextView zonecubes = (TextView) findViewById(R.id.zonecubes);
    final public TextView switchcubes = (TextView) findViewById(R.id.switchcubes);
    final public TextView scalecubes = (TextView) findViewById(R.id.scalecubes);
    final public TextView exchangecubes = (TextView) findViewById(R.id.exchangecubes);


    final public CheckBox climbattempt = (CheckBox) findViewById(R.id.climbattempt);
    final public CheckBox climb = (CheckBox) findViewById(R.id.climb);
    final public CheckBox lift1 = (CheckBox) findViewById(R.id.lift1);
    final public CheckBox lift2 = (CheckBox) findViewById(R.id.lift2);
    final public CheckBox lifted = (CheckBox) findViewById(R.id.lifted);
    final public CheckBox platform = (CheckBox) findViewById(R.id.platform);


    final public TextView penalties = (TextView) findViewById(R.id.penalties);
    final public CheckBox failed = (CheckBox) findViewById(R.id.failed);
    final public EditText notes = (EditText) findViewById(R.id.notes);*/

    //onCreate defines what happens when the app is started up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //display the start screen
        setContentView(R.layout.start);
        //this is just for troubleshooting purposes
        Log.d(TAG, "start screen displayed ");


        //this defines variables we can use to access the screen objects
        final TextView scoutid = (TextView) findViewById(R.id.editText);
        final TextView startmatch = (TextView) findViewById(R.id.editText7);

        //this tells the app what to do when the user pushes the "ok" button on the start screen
        //key point:  the software reads the lines of code below, but does not pause here to wait
        //for the the button to be pushed.  instead, the code just "takes note" of what to do if
        //the button is pushed, but then continues to read the rest of the code below the
        //"findviewbyiD..." section of code.
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //this records the values entered on the start screen
                ScoutId = Integer.parseInt(scoutid.getText().toString());
                StartMatch = Integer.parseInt(startmatch.getText().toString());

                //this checks that the values entered are valid
                if(ValidScout(ScoutId) == 1 && ValidMatch(StartMatch) == 1) {
                    Log.d(TAG, "ok button pushed");

                    MATCH_NUMBER = Integer.parseInt(startmatch.getText().toString())-1;;
                    SCOUT_ID = Integer.parseInt(scoutid.getText().toString())-1;

                    TEAM_NUMBER = getTeamNums()[MATCH_NUMBER][SCOUT_ID];
                    Log.d(TAG, Integer.toString(TEAM_NUMBER));

                    //go to the auton screen
                    goAuton();
                }
                else{
                    //display/handle the error if we have invalid entries on the start screen
                    if(ValidScout(ScoutId) == 0){
                        scoutid.setText("invalid");
                    }
                    if(ValidMatch(StartMatch) == 0){
                        startmatch.setText("invalid");
                    }
                }
            }
        });
    }

    public void goAuton(){
        //display auton screen
        setContentView(R.layout.auton);

        //provide us with a variable that can be used to read/write to the screen objects
        final TextView teamdata = (TextView) findViewById(R.id.teamdata);
        final TextView matchdata = (TextView) findViewById(R.id.matchdata);

        final Switch baseline = (Switch) findViewById(R.id.baseline);
        final Switch deliverSwitchAuton = (Switch) findViewById(R.id.deliverSwitchAuton);
        final Switch cubex2 = (Switch) findViewById(R.id.cubex2);
        final Switch autoScale = (Switch) findViewById(R.id.autoScale);
        final SeekBar scaleTime = (SeekBar) findViewById(R.id.scaleTime);

        //when a screen is displayed, the objects default back to false, zero, so we have to...
        //initialize the screen objects to whatever they were set to before...
        //so that they will be correct if we arrived at this screen using a "back" button
        teamdata.setText(Integer.toString(TEAM_NUMBER));
        matchdata.setText(Integer.toString(MATCH_NUMBER+1));

        baseline.setChecked(BaseLineBool);
        deliverSwitchAuton.setChecked(DeliverSwitchBool);
        cubex2.setChecked(SecondCubeBool);
        autoScale.setChecked(AutoScaleBool);
        scaleTime.setProgress(ScaleTimeInt);

        //this handles the "next" button on the auton screen
        findViewById(R.id.nextAuton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //store the current state of the objects on the auton screen
                BaseLineBool = baseline.isChecked();
                DeliverSwitchBool = deliverSwitchAuton.isChecked();
                SecondCubeBool = cubex2.isChecked();
                AutoScaleBool = autoScale.isChecked();
                ScaleTimeInt = scaleTime.getProgress();
                //this tag was just for troubleshooting
                Log.d(TAG, "auton next button pushed" + BaseLineBool);
                //go to the tele-op screen
                goTeleOp();
                }
        });
    }

    public void goTeleOp(){
        //display the teleop screen
        setContentView(R.layout.teleop);

        //provide us with variables that can be used to read/write to the screen objects
        final TextView teamdata = (TextView) findViewById(R.id.teamdata);
        final TextView matchdata = (TextView) findViewById(R.id.matchdata);

        final TextView portalcubes = (TextView) findViewById(R.id.portalcubes);
        final TextView centercubes = (TextView) findViewById(R.id.centercubes);
        final TextView zonecubes = (TextView) findViewById(R.id.zonecubes);
        final TextView switchcubes = (TextView) findViewById(R.id.switchcubes);
        final TextView scalecubes = (TextView) findViewById(R.id.scalecubes);
        final TextView exchangecubes = (TextView) findViewById(R.id.exchangecubes);

        final CheckBox climbattempt = (CheckBox) findViewById(R.id.climbattempt);
        final CheckBox climb = (CheckBox) findViewById(R.id.climb);
        final CheckBox lift1 = (CheckBox) findViewById(R.id.lift1);
        final CheckBox lift2 = (CheckBox) findViewById(R.id.lift2);
        final CheckBox lifted = (CheckBox) findViewById(R.id.lifted);
        final CheckBox platform = (CheckBox) findViewById(R.id.platform);

        final TextView penalties = (TextView) findViewById(R.id.penalties);
        final CheckBox failed = (CheckBox) findViewById(R.id.failed);
        final EditText notes = (EditText) findViewById(R.id.notes);

        //restore the current state of the objects on the teleop screen
        teamdata.setText(Integer.toString(TEAM_NUMBER));
        matchdata.setText(Integer.toString(MATCH_NUMBER+1));

        portalcubes.setText(Integer.toString(PortalCubes));
        centercubes.setText(Integer.toString(CenterCubes));
        zonecubes.setText(Integer.toString(ZoneCubes));
        switchcubes.setText(Integer.toString(SwitchCubes));
        scalecubes.setText(Integer.toString(ScaleCubes));
        exchangecubes.setText(Integer.toString(ExchangeCubes));

        climbattempt.setChecked(ClimbAttempt);
        climb.setChecked(Climb);
        lift1.setChecked(Lift1);
        lift2.setChecked(Lift2);
        lifted.setChecked(Lifted);
        platform.setChecked(Platform);

        penalties.setText(Integer.toString(Penalties));
        failed.setChecked(Failed);
        notes.setText(Notes);

        //handle what happens when the user pushes the Plus/Minus buttons
            findViewById(R.id.portalsub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PortalCubes--;
                    portalcubes.setText(Integer.toString(PortalCubes));
                }
            });
            findViewById(R.id.portaladd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PortalCubes++;
                    portalcubes.setText(Integer.toString(PortalCubes));
                }
            });

            findViewById(R.id.centersub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CenterCubes--;
                    centercubes.setText(Integer.toString(CenterCubes));
                }
            });
            findViewById(R.id.centeradd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CenterCubes++;
                    centercubes.setText(Integer.toString(CenterCubes));
                }
            });

            findViewById(R.id.zonesub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZoneCubes--;
                    zonecubes.setText(Integer.toString(ZoneCubes));
                }
            });
            findViewById(R.id.zoneadd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ZoneCubes++;
                    zonecubes.setText(Integer.toString(ZoneCubes));
                }
            });

            findViewById(R.id.switchsub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchCubes--;
                    switchcubes.setText(Integer.toString(SwitchCubes));
                }
            });
            findViewById(R.id.switchadd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchCubes++;
                    switchcubes.setText(Integer.toString(SwitchCubes));
                }
            });

            findViewById(R.id.scalesub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScaleCubes--;
                    scalecubes.setText(Integer.toString(ScaleCubes));
                }
            });
            findViewById(R.id.scaleadd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScaleCubes++;
                    scalecubes.setText(Integer.toString(ScaleCubes));
                }
            });

            findViewById(R.id.exchangesub).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExchangeCubes--;
                    exchangecubes.setText(Integer.toString(ExchangeCubes));
                }
            });
            findViewById(R.id.exchangeadd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExchangeCubes++;
                    exchangecubes.setText(Integer.toString(ExchangeCubes));
                }
            });

        findViewById(R.id.penaltysub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Penalties--;
                penalties.setText(Integer.toString(Penalties));
            }
        });
        findViewById(R.id.penaltyadd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Penalties++;
                penalties.setText(Integer.toString(Penalties));
            }
        });

        //this tells the app what to do when the back button on the teleop screen is pushed
        findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                //store the current state of the objects
                PortalCubes = Integer.parseInt(portalcubes.getText().toString());
                CenterCubes = Integer.parseInt(centercubes.getText().toString());
                ZoneCubes = Integer.parseInt(zonecubes.getText().toString());
                SwitchCubes = Integer.parseInt(switchcubes.getText().toString());
                ScaleCubes = Integer.parseInt(scalecubes.getText().toString());
                ExchangeCubes = Integer.parseInt(exchangecubes.getText().toString());

                ClimbAttempt = climbattempt.isChecked();
                Climb = climb.isChecked();
                Lift1 = lift1.isChecked();
                Lift2 = lift2.isChecked();
                Lifted = lifted.isChecked();
                Platform = platform.isChecked();

                Failed = failed.isChecked();
                Penalties = Integer.parseInt(penalties.getText().toString());
                Notes = notes.getText().toString();

                //go back to the auton screen
                goAuton();
            }
        });

        //this tells the app what to do if the sendbutton is pushed
        findViewById(R.id.sendbutton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                //provide variables that allow us to access the screen objects
                PortalCubes = Integer.parseInt(portalcubes.getText().toString());
                CenterCubes = Integer.parseInt(centercubes.getText().toString());
                ZoneCubes = Integer.parseInt(zonecubes.getText().toString());
                SwitchCubes = Integer.parseInt(switchcubes.getText().toString());
                ScaleCubes = Integer.parseInt(scalecubes.getText().toString());
                ExchangeCubes = Integer.parseInt(exchangecubes.getText().toString());

                //record the state of the objects
                ClimbAttempt = climbattempt.isChecked();
                Climb = climb.isChecked();
                Lift1 = lift1.isChecked();
                Lift2 = lift2.isChecked();
                Lifted = lifted.isChecked();
                Platform = platform.isChecked();

                Failed = failed.isChecked();
                Penalties = Integer.parseInt(penalties.getText().toString());
                Notes = notes.getText().toString();

                //go to the QRcode screen
                goQR();
            }
        });
    }

    public void goQR (){
        setContentView(R.layout.popup);
    }

    private int ValidMatch(int matchnum) {
        if(matchnum<MatchLimit && matchnum > 0) {
            return 1;
        }
        else {
            return 0;
        }
    }

    private int ValidScout(int id) {
        if(id>=1 && id<=6) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /*public void NewMatch (){
        baseline.setChecked(false);
        deliverSwitchAuton.setChecked(false);
        cubex2.setChecked(false);
        autoScale.setChecked(false);
        scaleTime.setProgress(0);

        portalcubes.setText("0");
        centercubes.setText("0");
        zonecubes.setText("0");
        switchcubes.setText("0");
        scalecubes.setText("0");
        exchangecubes.setText("0");

        climbattempt.setChecked(false);
        climb.setChecked(false);
        lift1.setChecked(false);
        lift2.setChecked(false);
        lifted.setChecked(false);
        platform.setChecked(false);

        penalties.setText("");
        failed.setChecked(false);
        notes.setText("Notes");

    }*/

    public int[][] getTeamNums() {
        MatchLimit=0;
        try {
            Scanner s = new Scanner(new File("/storage/emulated/0/MyTeamMatches.csv"));

            while (s.hasNextLine()) {
                s.nextLine();
                MatchLimit++;

            }
            s.close();
            s = new Scanner(new File("/storage/emulated/0/MyTeamMatches.csv"));

            int[][] returned = new int[MatchLimit][6];
            for(int i = 0;i<MatchLimit;i++)
            {
                returned[i] = new int[6];
                String[] args = s.nextLine().split(",");
                for (int ii = 0; ii < 6; ii++)
                {
                    returned[i][ii] = Integer.valueOf(args[ii]);
                }
            }
            System.out.println("</getTeamNums>\n");
            s.close();
            return returned;
        }
        catch(Exception e)
        {
            System.out.println("oh nose!");
            return null;
        }

    }
}

    /*          switch (state) {
                case 1:
                    Log.d(TAG, "Hello World");

                    state++;
                    break;
                case 2:
                    Log.d(TAG, "See This?");
                    break;
                case 3:
                    break;
            }
    }
}


//Old code

/*

        setContentView(R.layout.teleop);

        final AlertDialog.Builder builderSend = new AlertDialog.Builder(this);

        builderSend.setTitle("NEXT MATCH?");


        //SeditallTeamNums = getTeamNums();

        matchDone = new boolean[numMatches];

        for (int j = 0; j < numMatches; j++)

            matchDone[j] = false;



        //setContentView(R.layout.start);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {



                EditText inputText = (EditText) findViewById(R.id.editText);

                SCOUT_ID = Integer.parseInt(inputText.getText().toString()) - 1;



                EditText matchInput = (EditText) findViewById(R.id.editText7);

                MATCH_NUMBER = Integer.parseInt(matchInput.getText().toString()) - 1;



                //if (notValidMatch(MATCH_NUMBER) || notValidScout(SCOUT_ID)) {

                //Sedit    resetScoutScreen();

                //}

                //else {

                    setContentView(R.layout.auton);

                    scoutDisplay = (TextView) findViewById(R.id.scoutDisplay);

                    //SeditsetScouter(SCOUT_ID);
                Log.d(TAG, "I'm in your activity, logging all the things 2");
                    findViewById(R.id.nextAuton).setOnClickListener(new View.OnClickListener() {

                        @Override

                        public void onClick(View v) {
                        setContentView(R.layout.teleop);

                        }


                     masterDisplay = (TextView) findViewById(R.id.masterDisplay);

                    lowgoaldisplay = (TextView) findViewById(R.id.lowgoalloaddata);

                    geardisplay = (TextView) findViewById(R.id.gearNumDisplay);

                    //MATCH_NUMBER--;  Sedit


                    //ResetMatch(); Sedit

                     qrdisplay = (ImageView) findViewById(R.id.imageView);

                    continueQR = (Button) findViewById(R.id.continue_btn);

                    backbtn = (Button) findViewById(R.id.back_btn);



                     if (SCOUT_ID < 3)

                        ((TextView) findViewById(R.id.masterDisplay)).setTextColor(Color.parseColor("#ffcc0000"));

                    else

                        ((TextView) findViewById(R.id.masterDisplay)).setTextColor(Color.parseColor("#283593"));




                     Sedit builderSend.setMessage("Are you sure you want to continue? Did the MASTER scan your data?");

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

                    );Sedit

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

                    });



                     SeditfindViewById(R.id.sendbutton).setOnClickListener(new View.OnClickListener() {

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

    }Sedit



    public void GenerateQRString()

    {


        EditText inputText3 = (EditText) findViewById(R.id.notes);

        notes = inputText3.getText().toString();


        Switch baseline = (Switch) findViewById(R.id.baseline);

        CrossBaseLine = baseline.isChecked();


        Switch deliverSwitchAuton = (Switch) findViewById(R.id.deliverSwitchAuton);

        //switchCube = deliverSwitchAuton.isChecked();


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


         Sedit

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

            });



            findViewById(R.id.sendbutton).setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    GenerateQRString();

                    setContentView(R.layout.popup);

                    popupqrscreen();

                }

            });


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

                setContentView(R.layout.teleop);

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





        //gearsDeliveredTele = 0;

        //lowgoalLoadsTele = 0;

        //geardisplay.setText("0");

        //lowgoaldisplay.setText("0");





         EditText highgoalsauto = (EditText) findViewById(R.id.editText6);

        highgoalsauto.setText("0");

        //highgoalsauto.setTextColor(Color.parseColor("@android:color/holo_green_dark"));



        EditText notes = (EditText) findViewById(R.id.notes);

        notes.setText(".");



    }



    //gets all the team numbers from text file and
public void println(String line)
{
    System.out.println(line);
}

    This is a terrible function, and if I were to be using an actually GOOD language, like C, maybe
    This could have been implemented better. This sucks, but it's NOT MY FAULT!

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



     Seditprivate void resetScoutScreen()

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




} sEDIT->)





;}});}}}
*/