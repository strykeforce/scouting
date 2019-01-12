
package org.strykeforce.scoutapp;



import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.view.View;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;

import android.widget.Spinner;
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

import org.w3c.dom.Text;

import static org.strykeforce.scoutapp.R.id.nextbutton;
import static org.strykeforce.scoutapp.R.id.oneAuto;

public class MainActivity extends AppCompatActivity {

    //TAG is used for inserting tags later on for troubleshooting purposes
    private final static String TAG = "Sam";

    public boolean Lift1, Lift2, Lifted;

    boolean Failed = false;
    int Penalties = 0;
    String Notes = "none";

    int ScoutId;
    int StartMatch;

    int hatchCargoShipAuto = 0, hatchTopAuto = 0, hatchMidAuto = 0, hatchLowAuto = 0;
    int cargoCargoShipAuto = 0, cargoTopAuto = 0, cargoMidAuto = 0, cargoLowAuto = 0;

    boolean levelOneAuto = false, levelTwoAuto = false;

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

        final TextView hatchTop = (TextView) findViewById(R.id.hatchTopTextAuto);
        final TextView hatchMid = (TextView) findViewById(R.id.hatchMidTextAuto);
        final TextView hatchLow = (TextView) findViewById(R.id.hatchLowTextAuto);
        final TextView cargoTop = (TextView) findViewById(R.id.cargoTopTextAuto);
        final TextView cargoMid = (TextView) findViewById(R.id.cargoMidTextAuto);
        final TextView cargoLow = (TextView) findViewById(R.id.cargoLowTextAuto);

        final TextView cargoCargoShip = (TextView) findViewById(R.id.cargoCargoShipAuto);
        final TextView hatchCargoShip = (TextView) findViewById(R.id.hatchCargoShipAuto);

        final CheckBox levelOneAutoBox = (CheckBox) findViewById(R.id.oneAuto);
        final  CheckBox levelTwoAutoBox = (CheckBox) findViewById(R.id.twoAuto);

        //when a screen is displayed, the objects default back to false, zero, so we have to...
        //initialize the screen objects to whatever they were set to before...
        //so that they will be correct if we arrived at this screen using a "back" button
        hatchCargoShip.setText("" + hatchCargoShipAuto);
        hatchTop.setText("" + hatchTopAuto);
        hatchMid.setText("" + hatchMidAuto);
        hatchLow.setText("" + hatchLowAuto);

        cargoCargoShip.setText("" + cargoCargoShipAuto);
        cargoTop.setText("" + cargoTopAuto);
        cargoMid.setText("" + cargoMidAuto);
        cargoLow.setText("" + cargoLowAuto);

        levelOneAutoBox.setChecked(levelOneAuto);
        levelTwoAutoBox.setChecked(levelTwoAuto);

        teamdata.setText(Integer.toString(TEAM_NUMBER));
        matchdata.setText(Integer.toString(MATCH_NUMBER + 1));

        //this sets the display of the scout team (red 1, blue 2) in the top middle of the screen
        if (SCOUT_ID < 3) {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#ff0000"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Red " + (SCOUT_ID + 1));
        } else {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#1d34e2"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Blue " + (SCOUT_ID - 2));
        }



        //hatch buttons
        findViewById(R.id.hatchCargoShipButtAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatchCargoShipAuto++;
                hatchCargoShip.setText("" + hatchCargoShipAuto);
            }
        });

        findViewById(R.id.hatchCargoShipMinAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatchCargoShipAuto--;
                hatchCargoShip.setText("" + hatchCargoShipAuto);
            }
        });



        findViewById(R.id.hatchTopButtonAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatchTopAuto++;
                hatchTop.setText("" + hatchTopAuto);
            }
        });

        findViewById(R.id.hatchTopMinAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatchTopAuto--;
                hatchTop.setText("" + hatchTopAuto);
            }
        });



        findViewById(R.id.hatchMidButtonAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatchMidAuto++;
                hatchMid.setText("" + hatchMidAuto);
            }
        });

        findViewById(R.id.hatchMidMinAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatchMidAuto--;
                hatchMid.setText("" + hatchMidAuto);
            }
        });



        findViewById(R.id.hatchLowButtonAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatchLowAuto++;
                hatchLow.setText("" + hatchLowAuto);
            }
        });

        findViewById(R.id.hatchLowMinAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hatchLowAuto--;
                hatchLow.setText("" + hatchLowAuto);
            }
        });



        //cargo buttons
        findViewById(R.id.cargoCargoShipButtAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargoCargoShipAuto++;
                cargoCargoShip.setText("" + cargoCargoShipAuto);
            }
        });

        findViewById(R.id.cargoCargoShipMinAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargoCargoShipAuto--;
                cargoCargoShip.setText("" + cargoCargoShipAuto);
            }
        });



        findViewById(R.id.cargoTopButtonAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargoTopAuto++;
                cargoTop.setText("" + cargoTopAuto);
            }
        });

        findViewById(R.id.cargoTopMinAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargoTopAuto--;
                cargoTop.setText("" + cargoTopAuto);
            }
        });



        findViewById(R.id.cargoMidButtonAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargoMidAuto++;
                cargoMid.setText("" + cargoMidAuto);
            }
        });

        findViewById(R.id.cargoMidMinAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargoMidAuto--;
                cargoMid.setText("" + cargoMidAuto);
            }
        });



        findViewById(R.id.cargoLowButtonAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargoLowAuto++;
                cargoLow.setText("" + cargoLowAuto);
            }
        });

        findViewById(R.id.cargoLowMinAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargoLowAuto--;
                cargoLow.setText("" + cargoLowAuto);
            }
        });

        //preload spinner
        final Spinner preloadspinner = (Spinner) findViewById(R.id.preload);
        String[] items = new String[]{"hatch" , "cargo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        preloadspinner.setAdapter(adapter);

        //this handles the "next" button on the auton screen
        findViewById(R.id.nextAuton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save the state of the checkboxes
                levelOneAuto = levelOneAutoBox.isChecked();
                levelTwoAuto = levelTwoAutoBox.isChecked();

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

        //restore the current state of the objects on the teleop screen
        teamdata.setText(Integer.toString(TEAM_NUMBER));
        matchdata.setText(Integer.toString(MATCH_NUMBER + 1));

        //this sets the display of the scout team (red 1, blue 2) in the top middle of the screen
        if (SCOUT_ID < 3) {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#ff0000"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Red " + (SCOUT_ID + 1));
        } else {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#1d34e2"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Blue " + (SCOUT_ID - 2));
        }

        //this tells the app what to do when the back button on the teleop screen is pushed
        findViewById(R.id.backteleopbutton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //go back to the auton screen
                goAuton();
            }
        });

        //this tells the app what to do if the sendbutton is pushed
        findViewById(R.id.nextteleopbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        final AlertDialog.Builder builderReset = new AlertDialog.Builder(this);
        builderReset.setTitle("RESET MATCH?");
        builderReset.setMessage("Are you sure? Did the scouting Princess Peach say to go to the next match?");
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

    //checks if the match is within the valid match numbers
    private int ValidMatch(int matchnum) {
        if (matchnum < MatchLimit && matchnum > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    //makes sure the scout id is valid
    private int ValidScout(int id) {
        if (id >= 1 && id <= 6) {
            return 1;
        } else {
            return 0;
        }
    }

    //starts a new match
    public void NewMatch() {
        Lift1 = false;
        Lift2 = false;
        Lifted = false;

        Failed = false;
        Penalties = 0;
        Notes = "none";

        MATCH_NUMBER++;
        TEAM_NUMBER = getTeamNums()[MATCH_NUMBER][SCOUT_ID];
        goAuton();
        Log.d(TAG, "New Match");
    }

    //inputs an array of all the team numbers for every match
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

    //changes a boolean to an integer value for better compatibility with tableau
    private static int booltoInt(Boolean bool) {
        return bool ? 1 : 0;
    }

    public String GenerateQRString() {
        QRStr = "ID: " + (SCOUT_ID + 1) + "\t" //Scout ID
                + "Team: " + TEAM_NUMBER + "\t" //Team
                + "Match: " + (MATCH_NUMBER + 1) + "\t" //Match
                + "Lift1: " + booltoInt(Lift1) + "\t" //Lifted 1
                + "Lift2: " + booltoInt(Lift2) + "\t" //Lifted 2
                + "Lift: " + booltoInt(Lifted) + "\t" //Was Lifted
                + "Rf: " + booltoInt(Failed) + "\t" //Robot Failed
                + "Pen: " + Penalties + "\t" //Penalties
                + "Notes: " + Notes + "\t"; //Notes
        return QRStr;
    }

    //generate the qr code image
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