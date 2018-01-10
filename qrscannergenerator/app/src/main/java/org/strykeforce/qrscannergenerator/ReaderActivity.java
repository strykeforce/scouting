package org.strykeforce.qrscannergenerator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
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
    private Firebase firebaseRef;
    private static final int NUM_ELEMENTS_SENDING = 17, NUM_INT=15, NUM_STG=2;
    private ChatMessage[] scoutingData = new ChatMessage[6];
    private int curScoutID;
    private GoogleApiClient client;


    //FILE IO VARIABLES
    String root = Environment.getExternalStorageDirectory().toString();
    String usbDirectory = "/storage/usbotg";
    String internalDirectory = "/storage/emulated/0";
    String totalMatchString = "";
    /*
    SCOUT ID int
    TEAM NUM int
    MATCH NUM int

    Auto High int

    Tele High int
    Tele Low int
    Tele Gears int
    Climb Rope Time int

    Auto Low BOOL
    Auto Gears BOOL
    Crosses base line BOOL
    Picks gear off ground BOOL
    On defence BOOL
    Defended shooting high  BOOL
    Touchpad BOOL

    Scout Name StrING
    Notes STRING
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) { //method that creates everything when app is opened
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader); //sets to layout of app

        //initializes firebase to be able to send data to that URL
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(FIREBASE_URL);

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

/*
        final AlertDialog.Builder byuilderusb = new AlertDialog.Builder(this);
        byuilderusb.setTitle("Save All Data To USB?");
        byuilderusb.setMessage("Are You Sure You Want To Transfer All Data To USB?");
        findViewById(R.id.usb).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                byuilderusb.setPositiveButton("YES", new DialogInterface.OnClickListener() { //sets what the yes option will do

                    public void onClick(DialogInterface dialog, int which) {
                        savedata(); //calls method to restart match
                        dialog.dismiss(); //closes dialog box
                    }

                });
                byuilderusb.setNegativeButton("NO", new DialogInterface.OnClickListener() { //sets what the no option will do

                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //closes dialog box
                    }

                });
                final AlertDialog alert = byuilderusb.create();
                System.out.println(DialogInterface.BUTTON_NEGATIVE);
                alert.show();
                TextView msgTxt = (TextView) alert.findViewById(android.R.id.message);
                msgTxt.setTextSize((float)35.0);
                */


    /*    final AlertDialog.Builder storebuilder = new AlertDialog.Builder(this);
        storebuilder.setTitle("Save All Data?");
        storebuilder.setMessage("Make Sure You Have Scanned ALL Scouts.");
        findViewById(R.id.storeButton).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                storebuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() { //sets what the yes option will do

                    public void onClick(DialogInterface dialog, int which) {
                        storeLocal();
                        dialog.dismiss(); //closes dialog box
                    }

                });
                storebuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() { //sets what the no option will do

                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //closes dialog box
                    }

                });
                final AlertDialog alert = storebuilder.create();
                System.out.println(DialogInterface.BUTTON_NEGATIVE);
                alert.show();
                TextView msgTxt = (TextView) alert.findViewById(android.R.id.message);
                msgTxt.setTextSize((float)35.0);

            }

        });
       // storeLocal();


        //initializes scan button and sets scan button to open camera and scan when pressed
        */
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
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    //method that scans QR code from camera and stores in a string
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                scanResult = result.getContents(); //gets data from QR code and stores in private string
                //Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show(); //displays data from QR code on screen
                storeScout();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //stores scout data from QR string to chatmessage array and sets match number to red 1's match num
    public void storeScout(){
        //gets QR results from private string
        String message = scanResult;

        if(!message.equals(""))
        {
            ChatMessage sendingObj = findElements(message);
            scoutingData[curScoutID-1] = sendingObj;
            if(curScoutID==1)
            {

            }
            scanResult = "";
        }
    }

    /*
SCOUT ID int
TEAM NUM int
MATCH NUM int

Auto High int

Tele High int
Tele Low int
Tele Gears int
Climb Rope Time int

Auto Low BOOL
Auto Gears BOOL
Crosses base line BOOL
Picks gear off ground BOOL
On defence BOOL
Defended shooting high  BOOL
Touchpad BOOL

Scout Name StrING
Notes STRING
*/
    public void storeLocal()
    {
        try
        {
        PrintWriter fw = new PrintWriter(new FileWriter(new File("/storage/emulated/0/SampleJSON.json"), true));
        for(int j=0; j<6; j++)  {
            try {
                JSONObject o = new JSONObject();
                o.put("Scout ID", scoutingData[j].getScoutID());
                o.put("Team",scoutingData[j].getTeamNumberInt());
                o.put("Match", scoutingData[j].getMatchNumberInt());
                o.put("Auto High", scoutingData[j].getAutoHigh());
                o.put("Tele High", scoutingData[j].getTeleHigh());
                o.put("Tele Low", scoutingData[j].getTeleLow());
                o.put("Tele Gears",scoutingData[j].getTeleGear());
                o.put("Climb rope time", scoutingData[j].getClimbRopeTime());
                o.put("Auto Low", scoutingData[j].getAutoLow());
                o.put("Auto Gears",  scoutingData[j].getAutoGear());
                o.put("Crosses base line",  scoutingData[j].getBaseLineCross());
                o.put("Picks gear off ground", scoutingData[j].getCanPickGearOffGround());
                o.put("On defense", scoutingData[j].getPlaysDefense());
                o.put("Defended shooting high", scoutingData[j].getHighShotDefended());
                o.put("Touchpad", scoutingData[j].getTouchPad());
                o.put("Scout name", scoutingData[j].getScoutName());
                o.put("Notes", scoutingData[j].getNotes());
                String outputString = o.toString();
                System.out.println("outputString == \"" + outputString + "\"");
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






    //sends data as a single object to firebase

 /*   public void sendMessage() {
        //makes new object and calls findElements method as to push a single chatElement object to firebase
        firebaseRef.push().setValue(scoutingData);
        scoutingData = new ChatMessage[6];
        for(int j=0; j<checkboxes.length; j++)
        {
            checkboxes[j].setChecked(false);
        }
    }*/

    //extracts data from QR string and returns it in a single element
    public ChatMessage findElements(String tempScanResult) {
        Scanner scan = new Scanner(tempScanResult); //makes scanner out of string for ease of extraction
        String tempLine, tempString;
        int indexEl;
        int[] nums = new int[NUM_INT];
        String[] name = new String[NUM_STG];
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
                    if(j>=8 && j<=14)
                    {
                        if(elements[j].equals("false"))
                        {
                            nums[j] = 0;
                        }
                        else
                            nums[j] = 1;
                    }
                    else{
                        System.out.println("BREAK: " + elements[j] + " " + j);
                        nums[j] = Integer.parseInt(elements[j]);

                    }
                }
                else{
                    name[j-NUM_INT] = elements[j];
                }
                if (j==0) //sets check button on/off of which scouter it received from
                {
                    curScoutID = Integer.parseInt(elements[0]);
                    checkboxes[curScoutID - 1].setChecked(true);
                }
                j++;
            }
        }
        //ChatMessage sendingChat = new ChatMessage(elements);
        ChatMessage otherChat = new ChatMessage(nums, name);
      //  System.out.println("\n\n\nMEEEEEEEEEEE\n\n\n" + otherChat.jsonObjStg());
        return otherChat;
    }
    public void savedata() {
        try {


           Process p = Runtime.getRuntime().exec("cp /storage/emulated/0/HelloWorld /storage/usbotg");

            System.out.println("launched");
           p.waitFor();
          System.out.println("waited: " + p.exitValue());
            System.out.println("End");
            while (p.getErrorStream().available() > 0) {
                System.out.print
                        ((char) (p.getErrorStream().read()));
            }
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
;


    //clears the current match stores and resets the checkboxes
    public void resetMatch()
    {
        scoutingData = new ChatMessage[6];
        for(int j=0; j<checkboxes.length; j++)
        {
            checkboxes[j].setChecked(false);
        }
    }

    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.*/

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Reader Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)

                .build();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());



    }


    @Override
    public void onStop() {

        super.onStop();



        // ATTENTION: This was auto-generated to implement the App Indexing API.

        // See https://g.co/AppIndexing/AndroidStudio for more information.

        AppIndex.AppIndexApi.end(client, getIndexApiAction());

        client.disconnect();

    }
}