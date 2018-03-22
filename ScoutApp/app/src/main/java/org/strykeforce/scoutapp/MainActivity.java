
package org.strykeforce.scoutapp;



import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.*;

import android.graphics.Bitmap;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import static org.strykeforce.scoutapp.R.id.nextbutton;

public class MainActivity extends AppCompatActivity {

    //TAG is used for inserting tags later on for troubleshooting purposes
    private final static String TAG = "Sam";

    public boolean BaseLineBool, DeliverSwitchBool, SecondCubeBool, AutoScaleBool = false;
    int ScaleTimeInt = 0;

    public int PortalCubes = 0, CenterCubes = 0, ZoneCubes = 0, SwitchCubes = 0, ScaleCubes = 0, ExchangeCubes = 0, Drivablility = 0;
    public boolean ClimbAttempt, Climb, Lift1, Lift2, Lifted, Platform;

    boolean Failed = false;
    int Penalties = 0;
    String Notes = "none";

    int ScoutId;
    int StartMatch;

    private String QRStr;

    private int MatchLimit = 1000;

    private static int MATCH_NUMBER = 0, TEAM_NUMBER = 0, SCOUT_ID = 0; //current match and team num


    //onCreate defines what happens when the app is started up
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startScreen();
    }

    public void startScreen() {
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
                if (ValidScout(ScoutId) == 1 && ValidMatch(StartMatch) == 1) {
                    Log.d(TAG, "ok button pushed");

                    MATCH_NUMBER = Integer.parseInt(startmatch.getText().toString()) - 1;
                    ;
                    SCOUT_ID = Integer.parseInt(scoutid.getText().toString()) - 1;

                    TEAM_NUMBER = getTeamNums()[MATCH_NUMBER][SCOUT_ID];

                    //go to the auton screen
                    goAuton();
                } else {
                    //display/handle the error if we have invalid entries on the start screen
                    if (ValidScout(ScoutId) == 0) {
                        scoutid.setText("invalid");
                    }
                    if (ValidMatch(StartMatch) == 0) {
                        startmatch.setText("invalid");
                    }
                }
            }
        });
    }

    public void goAuton() {
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
        matchdata.setText(Integer.toString(MATCH_NUMBER + 1));

        baseline.setChecked(BaseLineBool);
        deliverSwitchAuton.setChecked(DeliverSwitchBool);
        cubex2.setChecked(SecondCubeBool);
        autoScale.setChecked(AutoScaleBool);
        scaleTime.setProgress(ScaleTimeInt);


        //this sets the display of the scout team (red 1, blue 2) in the top middle of the screen
        if (SCOUT_ID < 3) {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#ff0000"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Red " + (SCOUT_ID + 1));
        } else {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#1d34e2"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Blue " + (SCOUT_ID - 2));
        }


        //this handles the "next" button on the auton screen
        findViewById(R.id.nextAuton).setOnClickListener(new View.OnClickListener() {
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

    public void goTeleOp() {
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
        matchdata.setText(Integer.toString(MATCH_NUMBER + 1));

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

        //this sets the display of the scout team (red 1, blue 2) in the top middle of the screen
        if (SCOUT_ID < 3) {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#ff0000"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Red " + (SCOUT_ID + 1));
        } else {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#1d34e2"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Blue " + (SCOUT_ID - 2));
        }

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
        findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //go back to the auton screen
                goAuton();
            }
        });

        //this tells the app what to do if the sendbutton is pushed
        findViewById(R.id.sendbutton).setOnClickListener(new View.OnClickListener() {
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

    public void goQR() {
        //Display the QR code screen
        setContentView(R.layout.qrscreen);

        //Display the QR code
        final ImageView qrImageView = (ImageView) findViewById(R.id.imageView2);
        qrImageView.setImageBitmap(generateQRImage(GenerateQRString()));

        //Tells what to do when backbutton is pressed
        findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTeleOp();
            }
        });

        //Tells what to do when nextbutton is pressed
        final AlertDialog.Builder builderReset = new AlertDialog.Builder(this);
        builderReset.setTitle("RESET MATCH?");
        builderReset.setMessage("Are you sure? Did the scouting princess say to go to the next match?");
        findViewById(nextbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PopupWindow
                builderReset.setPositiveButton("YES", new DialogInterface.OnClickListener() { //sets what the yes option will do

                    public void onClick(DialogInterface dialog, int which) {
                        NewMatch(); //calls method to restart match
                        dialog.dismiss(); //closes dialog box
                    }

                });
                builderReset.setNegativeButton("NO", new DialogInterface.OnClickListener() { //sets what the no option will do

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //closes dialog box
                    }
                });
                final AlertDialog alert = builderReset.create();
                System.out.println(DialogInterface.BUTTON_NEGATIVE);
                alert.show();
                TextView msgTxt = (TextView) alert.findViewById(android.R.id.message);
                msgTxt.setTextSize((float)35.0);
                //storeLocal();
            }
        });
    }

    private int ValidMatch(int matchnum) {
        if (matchnum < MatchLimit && matchnum > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private int ValidScout(int id) {
        if (id >= 1 && id <= 6) {
            return 1;
        } else {
            return 0;
        }
    }

    public void NewMatch() {
        BaseLineBool = false;
        DeliverSwitchBool = false;
        SecondCubeBool = false;
        AutoScaleBool = false;
        ScaleTimeInt = 0;

        PortalCubes = 0;
        CenterCubes = 0;
        ZoneCubes = 0;
        SwitchCubes = 0;
        ScaleCubes = 0;
        ExchangeCubes = 0;
        ClimbAttempt = false;
        Climb = false;
        Lift1 = false;
        Lift2 = false;
        Lifted = false;
        Platform = false;

        Failed = false;
        Penalties = 0;
        Notes = "none";

        MATCH_NUMBER++;
        TEAM_NUMBER = getTeamNums()[MATCH_NUMBER][SCOUT_ID];
        goAuton();
        Log.d(TAG, "New Match");
    }

    public int[][] getTeamNums() {
        MatchLimit = 0;
        try {
            Scanner s = new Scanner(new File("/storage/emulated/0/MyTeamMatches.csv"));

            while (s.hasNextLine()) {
                s.nextLine();
                MatchLimit++;

            }
            s.close();
            s = new Scanner(new File("/storage/emulated/0/MyTeamMatches.csv"));

            int[][] returned = new int[MatchLimit][6];
            for (int i = 0; i < MatchLimit; i++) {
                returned[i] = new int[6];
                String[] args = s.nextLine().split(",");
                for (int ii = 0; ii < 6; ii++) {
                    returned[i][ii] = Integer.valueOf(args[ii]);
                }
            }
            System.out.println("</getTeamNums>\n");
            s.close();
            return returned;
        } catch (Exception e) {
            System.out.println("oh nose!");
            return null;
        }

    }

    private static int booltoInt(Boolean bool) {
        return bool ? 1 : 0;
    }

    public String GenerateQRString() {
        QRStr = "ID: " + (SCOUT_ID + 1) + "\t" //Scout ID
                + "Team: " + TEAM_NUMBER + "\t" //Team
                + "Match: " + (MATCH_NUMBER + 1) + "\t" //Match
                + "ABL: " + booltoInt(BaseLineBool) + "\t" //Auto Base Line
                + "Aswitch: " + booltoInt(DeliverSwitchBool) + "\t" //Auto Switch
                + "Ascale: " + booltoInt(AutoScaleBool) + "\t" //Auto Scale
                + "A2cube: " + booltoInt(SecondCubeBool) + "\t" //Auto Second Cubes
                + "Atime: " + (int) (ScaleTimeInt * .15) + "\t" //Auto Scale Time
                + "Pcube: " + PortalCubes + "\t" //Portal Cubes
                + "Ccube: " + CenterCubes + "\t" //Center Cubes
                + "Pzcube: " + ZoneCubes + "\t" //Power Zone Cubes
                + "Scube: " + SwitchCubes + "\t" //Switch Cubes
                + "Slcube: " + ScaleCubes + "\t" //Scale Cubes
                + "Xcube: " + ExchangeCubes + "\t" //Exchange Cubes
                + "Aclimb: " + booltoInt(ClimbAttempt) + "\t" //Attempted Climbs
                + "Sclimb: " + booltoInt(Climb) + "\t" //Successful Climbs
                + "Lift1: " + booltoInt(Lift1) + "\t" //Lifted 1
                + "Lift2: " + booltoInt(Lift2) + "\t" //Lifted 2
                + "Lift: " + booltoInt(Lifted) + "\t" //Was Lifted
                + "Op: " + booltoInt(Platform) + "\t" //On Platform
                + "Rf: " + booltoInt(Failed) + "\t" //Robot Failed
                + "Pen: " + Penalties + "\t" //Penalties
                + "Notes: " + Notes + "\t"; //Notes
        return QRStr;
    }

    private Bitmap generateQRImage(final String content) {

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();

        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints); //OG 512

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();

            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {

                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }


            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }
}