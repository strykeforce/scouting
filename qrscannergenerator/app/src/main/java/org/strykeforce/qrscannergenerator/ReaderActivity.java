package org.strykeforce.qrscannergenerator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.json.simple.parser.*;

import java.io.File;
import java.io.PrintWriter;

import org.json.*;


public class ReaderActivity extends AppCompatActivity {

    private final static String TAG = "cool kid B)";

    ArrayList<String> divisionTeams = new ArrayList<String>();
    private Button scan_btn;
    private CheckBox[] checkboxes = new CheckBox[6]; //refers to off/on checkboxes images
    private String scanResult;
    private static final String FIREBASE_URL = "https://testproj1-dc6de.firebaseio.com/"; //set to URL of firebase to send to
    //    private Firebase firebaseRef;
    private static final int NUM_INT = 17, NUM_STG = 2, NUM_ELEMENTS_SENDING = NUM_INT + NUM_STG;
    public int curMatch = 1, checkMatch, tempMatch;
    public int MatchLimit;
    private Integer[] matchTeams = new Integer[6];
    private ChatMessage[] scoutingData = new ChatMessage[6];
    private int curScoutID, numOfTeams;
    private GoogleApiClient client;
    String[] teamNames = new String[10000];
    String[] rankTitles = new String[]{"1 ┬──┬ ︵(╯。□。）╯", "2 ┐(‘～`；)┌", "3 (◡‿◡✿)", "4 ┏(＾0＾)┛┗(＾0＾) ┓", "5 (づ｡◕‿‿◕｡)づ"};
    String tempText = "", notesText = "";
    int tempTeam, teamPos = 0, tempRank, rankPos = 0;
    Integer[] returned = new Integer[6];


    @Override
    protected void onCreate(Bundle savedInstanceState) { //method that creates everything when app is opened
        super.onCreate(savedInstanceState);
        dScale();
    }

    public void getTeamNames() {
        try {
            Scanner scan = new Scanner(new File("/storage/emulated/0/TeamNames.csv")).useDelimiter(",");
            int count = 0;
            while(scan.hasNextLine()) {
                scan.next();
                teamNames[count] = scan.next();
                Log.d("Lilian",""+teamNames[count]);
                count++;
            }
        } catch(Exception e) {}
    }

    public Integer[] getTeamNums() {
        MatchLimit = 0;
        try {
            Scanner s = new Scanner(new File("/storage/emulated/0/MyTeamMatches.csv"));

            while (s.hasNextLine()) {
                s.nextLine();
                MatchLimit++;

            }
            s.close();
            s = new Scanner(new File("/storage/emulated/0/MyTeamMatches.csv"));

            checkMatch = 1;
            for (int i = 0; i < MatchLimit; i++) {
                String[] args = s.nextLine().split(",");
                if(checkMatch == curMatch) {
                    for (int ii = 0; ii < 6; ii++) {
                        returned[ii] = Integer.parseInt(args[ii]);
                    }
                }
                checkMatch++;
            }
            Log.d("Lilian", "checkMatch is " + checkMatch);
            Log.d("Lilian","curMatch is " + curMatch);
            System.out.println("</getTeamNums>\n");
            s.close();
            Log.d("Lilian","match numbers are " + returned);
            return returned;
        } catch (Exception e) {
            Log.d("Lilian", "error is " + e);
            return null;
        }

    }

    public void dScale() {
        //switch screen
        setContentView(R.layout.dscale_screen);

        //initialize onscreen text/buttons
        final TextView matchDisplay = findViewById(R.id.matchDisplay);

        final TextView changeMatchNumBox = findViewById(R.id.changeMatchNum);

        final Button changeMatchButton = findViewById(R.id.changeButton);
        final Button penalities = findViewById(R.id.penButton);
        final Button savingInfo = findViewById(R.id.saveInfo);
        final Button toScan = findViewById(R.id.matchDone);

        final Spinner defenseDropdown = findViewById(R.id.defensiveSpinner);
        final Spinner rankDropdown = findViewById(R.id.rankSpinner);

        final TextView notesBox = findViewById(R.id.notesBox);

        //import data if coming from same match
        Log.d("Lilian", "displaying text");
        matchDisplay.setText("" + curMatch);

        notesBox.setText(notesText);

        //populate spinners
        matchTeams = getTeamNums();
        Log.d("Lilian", "set spinners");
        final ArrayAdapter<Integer> tadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, matchTeams);
        defenseDropdown.setAdapter(tadapter);

        final ArrayAdapter<String> radapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rankTitles);
        rankDropdown.setAdapter(radapter);
        Log.d("Lilian", "finish set spinners");

        defenseDropdown.setSelection(teamPos);
        rankDropdown.setSelection(rankPos);

        //insert text into notes when certain buttons are pressed
        penalities.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                tempText = "" + notesBox.getText();
                tempText += "Caused excessive penalties. ";
                notesBox.setText(tempText);
            }
        });

        changeMatchButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                Log.d("Lilian", "after button is pressed");
                tempText = changeMatchNumBox.getText() + "";
                if (tempText != null) {
                    Log.d("Lilian", "tempText is " + tempText);
                    Log.d("Lilian", "tempText is " + tempText);
                    tempMatch = Integer.parseInt("" + tempText);
                    if (tempMatch >= 1) {
                        curMatch = Integer.parseInt("" + changeMatchNumBox.getText());
                        Log.d("Lilian", "curMatch is " + curMatch);
                        teamPos = 0;
                        rankPos = 0;
                        notesText = "";
                        dScale();
                    }
                }
            }

        });

        //save info
        savingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempTeam = matchTeams[defenseDropdown.getSelectedItemPosition()];
                tempRank = rankDropdown.getSelectedItemPosition() + 1;
                final AlertDialog.Builder builderReset = new AlertDialog.Builder(ReaderActivity.this);
                builderReset.setTitle("SAVE INFO?");
                builderReset.setMessage("If you continue, this data will be saved forever!! " +
                        "If you wish to change it after this, you will have to edit the JSON file.");
                builderReset.setPositiveButton("YES", new DialogInterface.OnClickListener() { //sets what the yes option will do

                    public void onClick(DialogInterface dialog, int which) {
                        notesText = "" + notesBox.getText();
                        storeD(tempTeam, tempRank , notesText);
                        teamPos = 0;
                        rankPos = 0;
                        notesText = "";
                        dScale();
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
                msgTxt.setTextSize((float) 35.0);
            }
        });

        //move to scan screen
        toScan.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                teamPos = defenseDropdown.getSelectedItemPosition();
                rankPos = rankDropdown.getSelectedItemPosition();
                notesText = "" + notesBox.getText();
                scanScreen();
            }
        });
    }


    public void scanScreen() {
        setContentView(R.layout.activity_reader); //sets to layout of app

        //initializes all off/on check boxes
        checkboxes[0] = findViewById(R.id.checkRed1);
        checkboxes[1] = findViewById(R.id.checkRed2);
        checkboxes[2] = findViewById(R.id.checkRed3);
        checkboxes[3] = findViewById(R.id.checkBlue1);
        checkboxes[4] = findViewById(R.id.checkBlue2);
        checkboxes[5] = findViewById(R.id.checkBlue3);

        //sets visibility of check boxes
        for (int j = 0; j < checkboxes.length; j++) {
            checkboxes[j].setChecked(false);
        }
        //getTeamNames();


        //first, opens a dialog box to check if they are sure with clearing the match, then clears current stored data and resets checkboxes
        final AlertDialog.Builder builderReset = new AlertDialog.Builder(this);
        builderReset.setTitle("RESET MATCH?");
        builderReset.setMessage("Are you absolutely sure? Like, really, really sure?");
        findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                builderReset.setPositiveButton("YES", new DialogInterface.OnClickListener() { //sets what the yes option will do

                    public void onClick(DialogInterface dialog, int which) {
                        storeLocal();
                        resetMatch();
                        curMatch++;
                        teamPos = 0;
                        rankPos = 0;
                        notesText = null;
                        dScale();
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
                msgTxt.setTextSize((float) 35.0);

            }

        });

        //initializes scan button and sets scan button to open camera and scan when pressed
        scan_btn = findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }

        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dScale();
            }
        });
    }

    //method that scans QR code from camera and stores in a string
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Lilian", "Arrived in onActivityResult! uwu");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                Log.d("Lilian", "Arrived in onActivityResult (again)! uwu");
                scanResult = result.getContents(); //gets data from QR code and stores in private string
                //Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show(); //displays data from QR code on screen
                storeScout();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void storeD(int tpos, int rpos, String stringText) {
        try {
            PrintWriter fw = new PrintWriter(new FileWriter(new File("/storage/emulated/0/DefenseJSON.txt"), true));
                try {
                    JSONObject o = new JSONObject();
                    o.put("Match", curMatch);
                    o.put("Team", tpos + ":");
                    o.put("Rank",rpos + ":");
                    o.put("Notes",stringText);
                    String outputString = o.toString();
                    outputString = outputString + ",";
                    fw.println(outputString);
                    fw.flush();
                    dScale();
                } catch (Exception e) {
                    System.out.println("oh noes!");
                    e.printStackTrace();
                }
        } catch (Exception e) {
            System.out.println("oh noes!");
            e.printStackTrace();
        }
    }

    //stores scout data from QR string to chatmessage array and sets match number to red 1's match num
    public void storeScout() {
        //gets QR results from private string
        String message = scanResult;

        if (!message.equals("")) {
            Log.d("Lilian", "Arrived in store scout! uwu");
            ChatMessage sendingObj = findElements(message);
            scoutingData[curScoutID - 1] = sendingObj;
            scanResult = "";
        }
    }

    public void storeLocal()
    {
        getTeamNames();
        try
        {
        PrintWriter fw =  new PrintWriter(new FileWriter(new File("/storage/emulated/0/MasterDataJSON.txt"), true));
                for(int j=0; j<6; j++)  {
                    try {
                        JSONObject o = new JSONObject();
                        o.put("Scout_ID", scoutingData[j].scoutIDint);
                        o.put("Team", scoutingData[j].teamNumberInt);
                        o.put("Name", teamNames[scoutingData[j].teamNumberInt]);
                        o.put("Match", scoutingData[j].matchNumberint);

                        o.put("initiationLeave", scoutingData[j].leftLine);
                        o.put("pickUpAuton", scoutingData[j].cellsPickedUp);
                        o.put("innerPortAuto", scoutingData[j].innerAutonScored);
                        o.put("outerPortAuto", scoutingData[j].outerAutonScored);
                        o.put("lowerPortAuto", scoutingData[j].bottomAutonScored);

                        o.put("innerPort", scoutingData[j].innerScored);
                        o.put("outerPort", scoutingData[j].outerScored);
                        o.put("lowerPort", scoutingData[j].bottomScored);
                        o.put("rotationalWOF", scoutingData[j].stage2Complete);
                        o.put("positionalWOF", scoutingData[j].stage3Complete);

                        o.put("climb", scoutingData[j].climbed);
                        o.put("climbTime", scoutingData[j].climbTime);
                        o.put("centered", scoutingData[j].adjustCOG);

                        o.put("robotFailed",scoutingData[j].robotFailed);
                        o.put("scouterNotes",scoutingData[j].scouterNotes);
                        o.put("scouterName", scoutingData[j].scouterName);
                        String outputString = o.toString();
                outputString = outputString + ","; //added comma to be compatible with qualification match report
                Log.d("lilian", "outputString == " + outputString);
                fw.println(outputString);
            }
            catch(Exception e) {
                System.out.println("oh noes!");
                e.printStackTrace();
            }
        }
            fw.close();
        }
        catch(Exception e) {
            System.out.println("oh noes!");
            e.printStackTrace();

    }};

    //extracts data from QR string and returns it in a single element
    public ChatMessage findElements(String tempScanResult) {
        Scanner scan = new Scanner(tempScanResult); //makes scanner out of string for ease of extraction
        String tempLine, tempString;
        int indexEl;
        int[] data = new int[NUM_INT];
        String[] names = new String[NUM_STG];
        String elements[] = new String[NUM_ELEMENTS_SENDING];
        int j=0; //COUNT OF NUM ELEMENTS

        while (scan.hasNextLine())
        {
            String line = scan.nextLine();
            Scanner lineReader = new Scanner(line);
            lineReader.useDelimiter("\t");
            while (lineReader.hasNext())
            {
                tempLine = lineReader.next();
                indexEl = tempLine.indexOf(':'); //extracts number after colon and stores in array
                elements[j] = tempLine.substring(indexEl + 2);
                if(j<NUM_INT)
                {
                        System.out.println("BREAK: " + elements[j] + " " + j);
                        data[j] = Integer.parseInt(elements[j]);
                }
                else{
                    names[j-NUM_INT] = elements[j];
                }
                if (j==0) //sets check button on/off of which scouter it received from
                {
                    curScoutID = Integer.parseInt(elements[0]);
                    checkboxes[curScoutID - 1].setChecked(true);
                }
                j++;
            }
        }
        ChatMessage otherChat = new ChatMessage(data, names);
        return otherChat;
    }

    //clears the current match stores and resets the checkboxes
    public void resetMatch()
    {
        scoutingData = new ChatMessage[6];
        for(int j=0; j<checkboxes.length; j++)
        {
            checkboxes[j].setChecked(false);
        }
    }

    /*
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.*/

    /*public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Reader Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)

                .build();
    }*/


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());



    }


    @Override
    public void onStop() {

        super.onStop();



        // ATTENTION: This was auto-generated to implement the App Indexing API.

        // See https://g.co/AppIndexing/AndroidStudio for more information.

        //AppIndex.AppIndexApi.end(client, getIndexApiAction());

        //client.disconnect();

    }
}
