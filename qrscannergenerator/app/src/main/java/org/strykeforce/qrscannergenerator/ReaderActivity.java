package org.strykeforce.qrscannergenerator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileWriter;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.PrintWriter;


public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn;
    private CheckBox[] checkboxes = new CheckBox[6]; //refers to off/on checkboxes images
    private String scanResult;
    private static final String FIREBASE_URL = "https://testproj1-dc6de.firebaseio.com/"; //set to URL of firebase to send to
//    private Firebase firebaseRef;
    private static final int NUM_INT=28, NUM_STG=4, NUM_ELEMENTS_SENDING = NUM_INT + NUM_STG;
    private ChatMessage[] scoutingData = new ChatMessage[6];
    private int curScoutID, numOfTeams;
    private GoogleApiClient client;
    String [] teamNames = new String[9000] ;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //method that creates everything when app is opened
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader); //sets to layout of app

        //initializes firebase to be able to send data to that URL
//        Firebase.setAndroidContext(this);
//        firebaseRef = new Firebase(FIREBASE_URL);

        //initializes all off/on check boxes
        checkboxes[0] = (CheckBox) findViewById(R.id.checkRed1);
        checkboxes[1] = (CheckBox) findViewById(R.id.checkRed2);
        checkboxes[2] = (CheckBox) findViewById(R.id.checkRed3);
        checkboxes[3] = (CheckBox) findViewById(R.id.checkBlue1);
        checkboxes[4] = (CheckBox) findViewById(R.id.checkBlue2);
        checkboxes[5] = (CheckBox) findViewById(R.id.checkBlue3);

        //sets visibility of check boxes
        for (int j = 0; j < checkboxes.length; j++) {
            checkboxes[j].setChecked(false);
        }
        getTeamNames();


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
                        resetMatch(); //calls method to restart match
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

            }

        });

        //initializes scan button and sets scan button to open camera and scan when pressed
        scan_btn = (Button) findViewById(R.id.scan_btn);
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        Button print_btn = (Button) findViewById(R.id.scan_btn);
    }


    //method that scans QR code from camera and stores in a string
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Lilian" , "Arrived in onActivityResult! uwu");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                Log.d("Lilian" , "Arrived in onActivityResult (again)! uwu");
                scanResult = result.getContents(); //gets data from QR code and stores in private string
                //Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show(); //displays data from QR code on screen
                storeScout();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void getTeamNames() {
        numOfTeams = 0;
         String temp;
        String[] readLine;
        String returned = "no name";

        try {
            Scanner s = new Scanner(new File("/storage/emulated/0/TeamNames.csv"));

            while (s.hasNextLine()) {
                temp = s.nextLine();
                readLine = temp.split(",");
                teamNames[Integer.parseInt(readLine[0])]=readLine[1];
                numOfTeams++;

            }
            s.close();
 /*           s = new Scanner(new File("/storage/emulated/0/TeamNames.csv"));

 //           String[][] teamNameArray = new String[numOfTeams][3];
            for (int i = 0; i < numOfTeams; i++) {
 //               teamNameArray[i] = new String[3];
                String[] args = s.nextLine().split(",");
                for (int ii = 0; ii < 2; ii++) {
                    teamNameArray[i][ii] = args[ii];
                }
            }
            for (int i = 0; i < numOfTeams; i++) {
                if (Integer.valueOf(teamNameArray[i][0]) == teamNumber) {
                    return teamNameArray[i][1] = returned;
                }
            }
            System.out.println("</getTeamNames>\n");
            s.close();
 */       } catch (Exception e) {
            System.out.println("oh nose!");
 //           return null;
        }

 //       return returned;
    }

    //stores scout data from QR string to chatmessage array and sets match number to red 1's match num
    public void storeScout(){
        //gets QR results from private string
        String message = scanResult;

        if(!message.equals(""))
        {
            Log.d("Lilian" , "Arrived in store scout! uwu");
            ChatMessage sendingObj = findElements(message);
            scoutingData[curScoutID-1] = sendingObj;
             if(curScoutID==1)
            {

            }
            scanResult = "";
        }
    }

    public void storeLocal()
    {
        try
        {
        PrintWriter fw =  new PrintWriter(new FileWriter(new File("/storage/emulated/0/MasterDataJSON.txt"), true));
                for(int j=0; j<6; j++)  {
                    try {
                        JSONObject o = new JSONObject();
                        o.put("Scout_ID", scoutingData[j].scoutIDint);
                        o.put("Team", scoutingData[j].teamNumberInt);
                        o.put("Name", teamNames[scoutingData[j].teamNumberInt]);
                        o.put("Match",scoutingData[j].matchNumberint);
                        o.put("HatchCargoShipAuto",scoutingData[j].hatchCargoShipAuto);
                        o.put("HatchTopAuto",scoutingData[j].hatchTopAuto);
                        o.put("HatchMidAuto",scoutingData[j].hatchMidAuto);
                        o.put("hatchLowAuto",scoutingData[j].hatchLowAuto);
                        o.put("cargoCargoShipAuto",scoutingData[j].cargoCargoShipAuto);
                        o.put("cargoTopAuto",scoutingData[j].cargoTopAuto);
                        o.put("cargoMidAuto",scoutingData[j].cargoMidAuto);
                        o.put("cargoLowAuto",scoutingData[j].cargoLowAuto);
                        o.put("levelOneAuto",scoutingData[j].levelOneAuto);
                        o.put("levelTwoAuto",scoutingData[j].levelTwoAuto);
                        o.put("preload",scoutingData[j].preload);
                        o.put("hatchCargoShipTele",scoutingData[j].hatchCargoShipTele);
                        o.put("hatchTopTele",scoutingData[j].hatchTopTele);
                        o.put("hatchMidTele",scoutingData[j].hatchMidTele);
                        o.put("hatchLowTele",scoutingData[j].hatchLowTele);
                        o.put("cargoCargoShipTele",scoutingData[j].cargoCargoShipTele);
                        o.put("cargoTopTele",scoutingData[j].cargoTopTele);
                        o.put("cargoMidTele",scoutingData[j].cargoMidTele);
                        o.put("cargoLowTele",scoutingData[j].cargoLowTele);
                        o.put("climbTimeTele",scoutingData[j].climbTimeTele);
                        o.put("levelOneTele",scoutingData[j].levelOneTele);
                        o.put("levelTwoTele",scoutingData[j].levelTwoTele);
                        o.put("levelThreeTele",scoutingData[j].levelThreeTele);
                        o.put("penalties",scoutingData[j].penalties);
                        o.put("robotFailed",scoutingData[j].robotFailed);
                        o.put("playedDefense",scoutingData[j].playedDefense);
                        o.put("rank", scoutingData[j].rank);
                        o.put("scouterNotes",scoutingData[j].scouterNotes);
                        o.put("scouterInitials",scoutingData[j].scouterInitials);
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
