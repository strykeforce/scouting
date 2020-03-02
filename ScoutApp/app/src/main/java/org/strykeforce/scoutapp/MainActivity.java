
package org.strykeforce.scoutapp;



import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import android.widget.TextView;

import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import android.graphics.Bitmap;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import static org.strykeforce.scoutapp.R.id.nextbutton;
import static org.strykeforce.scoutapp.R.id.scouterNameBox;

public class MainActivity extends AppCompatActivity {

    //TAG is used for inserting tags later on for troubleshooting purposes
    private final static String TAG = "Lilian";

    int ScoutId;
    int StartMatch;

    private String QRStr;

    private int MatchLimit = 1000;

    private static int MATCH_NUMBER = 0, TEAM_NUMBER = 0, SCOUT_ID = 0; //current match and team num
    String scouterNotes = "", scouterName = ""; //scouter notes
    List<String> tabNames = new ArrayList<>(Arrays.asList("Auton", "Teleop", "Endgame", "Postgame")); //sidebar list

    //initialize variables
    int cellsPickedUp = 0;
    int innerAutonScored = 0;
    int outerAutonScored = 0;
    int bottomAutonScored = 0;

    boolean leftLine = false;

    int innerScored = 0;
    int outerScored = 0;
    int bottomScored = 0;

    boolean stage2Complete = false;
    boolean stage3Complete = false;

    boolean climbed = false;
    boolean centeredCOG = false;
    boolean robotFailed= false;

    double startTime = 0;
    double endTime = 0;
    int climbTime;
    int timeStatus;

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
        final TextView scoutid = findViewById(R.id.editText);
        final TextView startmatch = findViewById(R.id.editText7);

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

    public void changeScreen(int position) {
        //switch to corresponding screen
        switch(position)
        {
            case 0: goAuton();
                break;
            case 1: goPower();
                break;
            case 2: goEndgame();
                break;
            case 3: goPostgame();
                break;
        }
    }

    public void goAuton() {
        //display auton screen
        setContentView(R.layout.auton);

        //provide us with a variable that can be used to read/write to the screen objects
        final TextView teamdata = findViewById(R.id.teamdata);
        final TextView matchdata = findViewById(R.id.matchdata);

        //initialize onscreen text and buttons
        final CheckBox leftLineCheck = findViewById(R.id.leaveLine);

        final TextView pickedUp = findViewById(R.id.pickCellAuton);
        //final TextView innerAuton = findViewById(R.id.innerAuton);
        final TextView outerAuton = findViewById(R.id.outerAuton);
        final TextView bottomAuton = findViewById(R.id.bottomAuton);

        final Button pickedUpMinus = findViewById(R.id.cellAutonMinus);
        //final Button innerAutonMinus = findViewById(R.id.innerMinusAuton);
        final Button outerAutonMinus = findViewById(R.id.outerMinusAuton);
        final Button bottomAutonMinus = findViewById(R.id.bottomMinusAuton);

        final Button pickedUpPlus = findViewById(R.id.cellAutonPlus);
        //final Button innerAutonPlus = findViewById(R.id.innerPlusAuton);
        final Button outerAutonPlus = findViewById(R.id.outerPlusAuton);
        final Button bottomAutonPlus = findViewById(R.id.bottomPlusAuton);

        //create list on side for easy navigation between pages
        final ListView screenTabs = findViewById(R.id.screenTabs);
        ArrayAdapter<String> textFormat = new ArrayAdapter<String>(this,
                R.layout.listviewformat, android.R.id.text1, tabNames);

        screenTabs.setAdapter(textFormat);

        //when a screen is displayed, the objects default back to false, zero, so we have to
        //initialize the screen objects to whatever they were set to before
        //so that they will be correct if we arrived at this screen after switching to a differing one
        leftLineCheck.setChecked(leftLine);

        pickedUp.setText(""+cellsPickedUp);
        //innerAuton.setText(""+innerAutonScored);
        outerAuton.setText(""+outerAutonScored);
        bottomAuton.setText(""+bottomAutonScored);

        //this sets the display of the match number, team number, and scouter id
        teamdata.setText(Integer.toString(TEAM_NUMBER));
        matchdata.setText(Integer.toString(MATCH_NUMBER + 1));

        if (SCOUT_ID < 3) {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#ff0000"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Red " + (SCOUT_ID + 1));
        } else {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#1d34e2"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Blue " + (SCOUT_ID - 2));
        }

        //pick up power cell buttons
        pickedUpPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cellsPickedUp++;
                pickedUp.setText(""+cellsPickedUp);
            }
        });

        pickedUpMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cellsPickedUp != 0){
                    cellsPickedUp--;
                }
                pickedUp.setText(""+cellsPickedUp);
            }
        });

        //pick up power cell buttons
        pickedUpPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cellsPickedUp++;
                pickedUp.setText(""+cellsPickedUp);
            }
        });

        pickedUpMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cellsPickedUp != 0){
                    cellsPickedUp--;
                }
                pickedUp.setText(""+cellsPickedUp);
            }
        });

        //inner power cell buttons
        /*innerAutonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                innerAutonScored++;
                innerAuton.setText(""+innerAutonScored);
            }
        });

        innerAutonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(innerAutonScored != 0){
                    innerAutonScored--;
                }
                innerAuton.setText(""+innerAutonScored);
            }
        });*/

        //outer power cell buttons
        outerAutonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outerAutonScored++;
                outerAuton.setText(""+outerAutonScored);
            }
        });

        outerAutonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(outerAutonScored != 0){
                    outerAutonScored--;
                }
                outerAuton.setText(""+outerAutonScored);
            }
        });

        //bottom power cell buttons
        bottomAutonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomAutonScored++;
                bottomAuton.setText(""+bottomAutonScored);
            }
        });

        bottomAutonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomAutonScored != 0){
                    bottomAutonScored--;
                }
                bottomAuton.setText(""+bottomAutonScored);
            }
        });

        //tell what happens when an item on list is clicked
        screenTabs.setClickable(true);
        screenTabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //save info that needs to be saved
                if(leftLineCheck.isChecked()) {
                    leftLine = true;
                } else {leftLine = false;}
                changeScreen(position);
            }
        });

        //change screen to qr screen when done button is pressed
        findViewById(R.id.donebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save info that needs to be saved
                if(leftLineCheck.isChecked()) {
                    leftLine = true;
                } else {leftLine = false;}

                goQR();
            }
        });
    }

    public void goPower() {
        //display power cell screen screen
        setContentView(R.layout.powercells);

        //initialize onscreen text and buttons
        final CheckBox stage2 = findViewById(R.id.rotation);
        final CheckBox stage3 = findViewById(R.id.position);

        //final TextView inner = findViewById(R.id.inner);
        final TextView outer = findViewById(R.id.outer);
        final TextView bottom = findViewById(R.id.bottom);

        //final Button innerPlus = findViewById(R.id.innerPlus);
        final Button outerPlus = findViewById(R.id.outerPlus);
        final Button bottomPlus = findViewById(R.id.bottomPlus);

        //final Button innerMinus = findViewById(R.id.innerMinus);
        final Button outerMinus = findViewById(R.id.outerMinus);
        final Button bottomMinus = findViewById(R.id.bottomMinus);

        //create list on side for easy navigation between pages
        final ListView screenTabs = findViewById(R.id.screenTabs);
        ArrayAdapter<String> textFormat = new ArrayAdapter<String>(this,
                R.layout.listviewformat, android.R.id.text1, tabNames);

        screenTabs.setAdapter(textFormat);

        //when a screen is displayed, the objects default back to false, zero, so we have to
        //initialize the screen objects to whatever they were set to before
        //so that they will be correct if we arrived at this screen using a "back" button
        stage2.setChecked(stage2Complete);
        stage3.setChecked(stage3Complete);

        //inner.setText(""+innerScored);
        outer.setText(""+outerScored);
        bottom.setText(""+bottomScored);

        //this sets the display of the match, team, and scouter id
        final TextView teamdata = findViewById(R.id.teamdata);
        final TextView matchdata = findViewById(R.id.matchdata);

        teamdata.setText(Integer.toString(TEAM_NUMBER));
        matchdata.setText(Integer.toString(MATCH_NUMBER + 1));

        if (SCOUT_ID < 3) {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#ff0000"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Red " + (SCOUT_ID + 1));
        } else {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#1d34e2"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Blue " + (SCOUT_ID - 2));
        }

        //inner power cell buttons
        /*innerPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                innerScored++;
                inner.setText(""+innerScored);
            }
        });

        innerMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(innerScored != 0){
                    innerScored--;
                }
                inner.setText(""+innerScored);
            }
        });*/

        //outer power cell buttons
        outerPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outerScored++;
                outer.setText(""+outerScored);
            }
        });

        outerMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(outerScored != 0){
                    outerScored--;
                }
                outer.setText(""+outerScored);
            }
        });

        //bottom power cell buttons
        bottomPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomScored++;
                bottom.setText(""+bottomScored);
            }
        });

        bottomMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomScored != 0){
                    bottomScored--;
                }
                bottom.setText(""+bottomScored);
            }
        });

        //tell what happens when an item on list is clicked
        screenTabs.setClickable(true);
        screenTabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //save info that needs to be saved
                if(stage2.isChecked()) {
                    stage2Complete = true;
                } else {stage2Complete = false;}

                if(stage3.isChecked()) {
                    stage3Complete = true;
                } else {stage3Complete = false;}
                changeScreen(position);
            }
        });

        //change screen to qr screen when done button is pressed
        findViewById(R.id.donebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save info that needs to be saved
                if(stage2.isChecked()) {
                    stage2Complete = true;
                } else {stage2Complete = false;}

                if(stage3.isChecked()) {
                    stage3Complete = true;
                } else {stage3Complete = false;}

                goQR();
            }
        });
    }

    public void goEndgame() {
        //display endgame screen
        setContentView(R.layout.endgame);

        //initialize onscreen text and buttons
        final CheckBox climbedBox = findViewById(R.id.climb);
        final CheckBox centeredCOGBox = findViewById(R.id.adjustCOG);

        final Button timeButton = findViewById(R.id.climbTime);
        final TextView timeDisplay = findViewById(R.id.timeDisplay);

        //when a screen is displayed, the objects default back to false, zero, so we have to
        //initialize the screen objects to whatever they were set to before
        //so that they will be correct if we arrived at this screen using a "back" button
        climbedBox.setChecked(climbed);
        centeredCOGBox.setChecked(centeredCOG);

        timeDisplay.setText(""+climbTime);

        if(climbTime != 0) {
            timeButton.setText("RESTART?");
            timeStatus = 2;
        } else {
            timeButton.setText("START");
            timeStatus = 0;
        }




        //this sets the display of the scout team (red 1, blue 2) in the top middle of the screen
        final TextView teamdata = findViewById(R.id.teamdata);
        final TextView matchdata = findViewById(R.id.matchdata);

        teamdata.setText(Integer.toString(TEAM_NUMBER));
        matchdata.setText(Integer.toString(MATCH_NUMBER + 1));
        if (SCOUT_ID < 3) {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#ff0000"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Red " + (SCOUT_ID + 1));
        } else {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#1d34e2"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Blue " + (SCOUT_ID - 2));
        }

        //create list on side for easy navigation between pages
        final ListView screenTabs = findViewById(R.id.screenTabs);
        ArrayAdapter<String> textFormat = new ArrayAdapter<String>(this,
                R.layout.listviewformat, android.R.id.text1, tabNames);

        screenTabs.setAdapter(textFormat);

        //tell what happens when climb time button is pressed
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timeStatus == 0) {
                    //get start time
                    startTime = System.currentTimeMillis();
                    timeButton.setText("STOP");
                    timeButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    timeStatus = 1;
                }
                else if (timeStatus == 1) {
                    endTime = System.currentTimeMillis();
                    timeButton.setText("RESTART?");timeButton.setBackgroundColor(Color.LTGRAY);
                    climbTime = (int) Math.round((endTime/1000-startTime/1000));
                    timeDisplay.setText(""+climbTime);
                    timeStatus = 2;
                }
                else if (timeStatus == 2) {
                    timeButton.setText("START");
                    timeStatus = 3;
                }
                else if (timeStatus == 3) {
                    timeDisplay.setText("");
                    startTime = System.currentTimeMillis();
                    timeButton.setText("STOP");
                    timeButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    timeStatus = 1;
                }
            }
        });

        //tell what happens when an item on list is clicked
        screenTabs.setClickable(true);
        screenTabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //save info that needs to be saved
                if(climbedBox.isChecked()) {
                    climbed = true;
                } else {climbed = false;}

                if(centeredCOGBox.isChecked()) {
                    centeredCOG = true;
                } else {centeredCOG = false;}
                changeScreen(position);
            }
        });

        //change screen to qr screen when done button is pressed
        findViewById(R.id.donebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save info that needs to be saved
                if(climbedBox.isChecked()) {
                    climbed = true;
                } else {climbed = false;}

                if(centeredCOGBox.isChecked()) {
                    centeredCOG = true;
                } else {centeredCOG = false;}

                //go to qr screen
                goQR();
            }
        });
    }

    public void goPostgame() {
        //change to postgame screen
        setContentView(R.layout.postgame);
        //initialize onscreen text and buttons
        final CheckBox robotFailedBox = findViewById(R.id.robotFailed);
        final TextView notesBox = findViewById(R.id.notes);
        final TextView nameBox = findViewById(R.id.scouterNameBox);

        //when a screen is displayed, the objects default back to false, zero, so we have to
        //initialize the screen objects to whatever they were set to before
        //so that they will be correct if we arrived at this screen using a "back" button
        robotFailedBox.setChecked(robotFailed);
        notesBox.setText(scouterNotes);
        nameBox.setText(scouterName);


        //this sets the display of the scout team (red 1, blue 2) in the top middle of the screen
        final TextView teamdata = findViewById(R.id.teamdata);
        final TextView matchdata = findViewById(R.id.matchdata);

        teamdata.setText(Integer.toString(TEAM_NUMBER));
        matchdata.setText(Integer.toString(MATCH_NUMBER + 1));
        if (SCOUT_ID < 3) {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#ff0000"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Red " + (SCOUT_ID + 1));
        } else {
            ((TextView) findViewById(R.id.scoutDisplay)).setTextColor(Color.parseColor("#1d34e2"));
            ((TextView) findViewById(R.id.scoutDisplay)).setText("Blue " + (SCOUT_ID - 2));
        }

        //create list on side for easy navigation between pages
        final ListView screenTabs = findViewById(R.id.screenTabs);
        ArrayAdapter<String> textFormat = new ArrayAdapter<String>(this,
                R.layout.listviewformat, android.R.id.text1, tabNames);

        //tells what happens when an item on list is clicked
        screenTabs.setAdapter(textFormat);
        screenTabs.setClickable(true);
        screenTabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //save info that needs to be saved
                if(robotFailedBox.isChecked()) {
                    robotFailed = true;
                } else {robotFailed = false;}

                scouterNotes = notesBox.getText().toString();
                scouterName = nameBox.getText().toString();

                changeScreen(position);
            }
        });

        //change screen to qr screen when done button is pressed
        findViewById(R.id.donebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save info that needs to be saved
                if(robotFailedBox.isChecked()) {
                    robotFailed = true;
                } else {robotFailed = false;}

                scouterNotes = notesBox.getText().toString();
                scouterName = nameBox.getText().toString();

                goQR();
            }
        });
    }

    public void goQR() {
        //Display the QR code screen
        setContentView(R.layout.qrscreen);

        //Display the QR code
        final ImageView qrImageView = findViewById(R.id.imageView2);
        qrImageView.setImageBitmap(generateQRImage(GenerateQRString()));

        //Tells what to do when backbutton is pressed
        findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAuton();
            }
        });

        final AlertDialog.Builder builderReset = new AlertDialog.Builder(this);
        builderReset.setTitle("RESET MATCH?");
        builderReset.setMessage("Are you sure? Did the Master Scouter say to go to the next match?");
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
                TextView msgTxt = alert.findViewById(android.R.id.message);
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
        //clear all
        cellsPickedUp = 0;
        innerAutonScored = 0;
        outerAutonScored = 0;
        bottomAutonScored = 0;

        leftLine = false;

        innerScored = 0;
        outerScored = 0;
        bottomScored = 0;

        stage2Complete = false;
        stage3Complete = false;

        climbed = false;
        centeredCOG = false;
        robotFailed= false;

        startTime = 0;
        endTime = 0;
        climbTime = 0;
        timeStatus = 0;

        scouterNotes = "";
        scouterName = "";

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
        //generates the string to be passed to the papa panda through qr code.
        //titles must be kept as short as possible so as to keep the qr code as small as possible
        //or it will be harder to scan at competition.
        QRStr = "ID: " + (SCOUT_ID + 1) + "\t" //Scout ID
                + "TEAM: " + TEAM_NUMBER + "\t" //Team
                + "MATCH: " + MATCH_NUMBER + "\t"//Match Number
                + "LIL: " + booltoInt(leftLine) + "\t" //Left Initiation Line
                + "APU: " + cellsPickedUp + "\t" //Picked Up Cells in Auton
                + "API: " + innerAutonScored + "\t"
                + "APO: " + outerAutonScored + "\t"
                + "APB: " + bottomAutonScored + "\t"
                + "PI: " + innerScored + "\t"
                + "PO: " + outerScored + "\t"
                + "PB: " + bottomScored + "\t"
                + "WOF2: " + booltoInt(stage2Complete) + "\t"
                + "WOF3: " + booltoInt(stage3Complete) + "\t"
                + "CLI: " + booltoInt(climbed) + "\t"
                + "TIME: " + climbTime + "\t"
                + "COG: " + booltoInt(centeredCOG) + "\t"
                + "FAIL: " + booltoInt(robotFailed) + "\t"
                + "NOTE: " + scouterNotes + "\t"
                + "INT: " + scouterName + "\t";
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