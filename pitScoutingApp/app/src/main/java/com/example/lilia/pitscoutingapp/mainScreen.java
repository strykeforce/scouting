package com.example.lilia.pitscoutingapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class mainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //go to info screen
        goInfo();
    }


    //go to info screen
    public void goInfo() {
        setContentView(R.layout.activity_main_screen_2);

        //go to picture screen
        findViewById(R.id.pictureButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPictures();
            }
        });


        //set spinner entries



        String[] driveTrain = new String[]{"Tank" , "Swerve", "Slide", "Other"};
        final Spinner driveTrainSpinner = (Spinner) findViewById(R.id.driveTrain);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, driveTrain);
        driveTrainSpinner.setAdapter(adapter);


        String[] wheelType = new String[]{"Colsons" , "Mechanum", "Omni", "Mixed", "Other", "Nitrile/Tread"};
        final Spinner wheelTypeSpinner = (Spinner) findViewById(R.id.wheelTypeSpinner);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, wheelType);
        wheelTypeSpinner.setAdapter(adapter1);


        String[] shiftingGearBoxStrings = new String[]{"Yes", "No"};
        final Spinner shiftingGearBoxSpinner = (Spinner) findViewById(R.id.shiftingGearBox);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, shiftingGearBoxStrings);
        shiftingGearBoxSpinner.setAdapter(adapter2);


        String[] sevenSevenFiveStrings = new String[]{"No" , "Yes"};
        final Spinner sevenSevenFiveSpinner = (Spinner) findViewById(R.id.sevenSevenFive);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sevenSevenFiveStrings);
        sevenSevenFiveSpinner.setAdapter(adapter3);


        //Make prompts link to their pages




        ((Spinner)findViewById(R.id.driveTrain)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapter.getItem(i)== "Other") {


                   setContentView(an
                   )
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




/*
jncfjcfdhytvfjkgjfgtyfhgfujtyhdyhfujhghFruitPunch?
 */

        //final warning pop up before entering information
        final AlertDialog.Builder builderReset = new AlertDialog.Builder(this);
        builderReset.setTitle("ENTER INFORMATION");
        builderReset.setMessage("Do NOT continue unless you are sure the information you entered is correct.");
        findViewById(R.id.saveInformation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderReset.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        saveInfo();
                        dialog.dismiss();
                    }

                });
                builderReset.setNegativeButton("GO BACK!", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog alert = builderReset.create();
                System.out.println(DialogInterface.BUTTON_NEGATIVE);
                alert.show();
                TextView msgTxt = (TextView) alert.findViewById(android.R.id.message);
                msgTxt.setTextSize((float) 35.0);
            }
        });
    }

    //go to pictures screen
    public void goPictures() {
        setContentView(R.layout.picture_screen);

        findViewById(R.id.infoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goInfo();
            }
        });

        //open camera and save pictures
        findViewById(R.id.picture1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText pictureTeamBox = (EditText) findViewById(R.id.pictureTeamInput);
                String pictureTeam = pictureTeamBox.getText().toString();

                Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                File imagesFolder = new File("/storage/emulated/0/teamPictures");
                imagesFolder.mkdirs();

                File image = new File(imagesFolder, pictureTeam + "_1.jpg");
                Uri uriSavedImage = Uri.fromFile(image);

                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(imageIntent, 100);
            }
        });

        findViewById(R.id.picture2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText pictureTeamBox = (EditText) findViewById(R.id.pictureTeamInput);
                String pictureTeam = pictureTeamBox.getText().toString();

                Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                File imagesFolder = new File("/storage/emulated/0/teamPictures");
                imagesFolder.mkdirs();

                File image = new File(imagesFolder, pictureTeam + "_2.jpg");
                Uri uriSavedImage = Uri.fromFile(image);

                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(imageIntent, 100);
            }
        });

        findViewById(R.id.picture3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText pictureTeamBox = (EditText) findViewById(R.id.pictureTeamInput);
                String pictureTeam = pictureTeamBox.getText().toString();

                Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                File imagesFolder = new File("/storage/emulated/0/teamPictures");
                imagesFolder.mkdirs();

                File image = new File(imagesFolder, pictureTeam + "_3.jpg");
                Uri uriSavedImage = Uri.fromFile(image);

                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(imageIntent, 100);
            }
        });
    }

    //save the information
    public void saveInfo() {
        //locate all the text boxes
        final EditText teamInputBox = (EditText) findViewById(R.id.teamInput);
        final Spinner driveTrainDropdown = (Spinner) findViewById(R.id.driveTrain);
        final Spinner wheelTypeDropdown = (Spinner) findViewById(R.id.wheelTypeSpinner);
        final Spinner shiftingGearBoxDropdown = (Spinner) findViewById(R.id.shiftingGearBox);
        final Spinner sevenSevenFiveDropdown = (Spinner) findViewById(R.id.sevenSevenFive);
        final EditText intakeTypeBox = (EditText) findViewById(R.id.intakeTypeInput);
        final EditText cubeReleaseBox = (EditText) findViewById(R.id.cubeReleaseInput);
        final EditText weightBox = (EditText) findViewById(R.id.weightInput);
        final EditText narrowWideSquareBox = (EditText) findViewById(R.id.narrowWideSquareInput);
        final EditText notesBox = (EditText) findViewById(R.id.notes);
        final EditText nameBox = (EditText) findViewById(R.id.teamName);

        //save info in those text boxes
        String teamNumber = teamInputBox.getText().toString();
        String driveType = driveTrainDropdown.getSelectedItem().toString();
        String wheelType = wheelTypeDropdown.getSelectedItem().toString();
        String shiftingGearBox = shiftingGearBoxDropdown.getSelectedItem().toString();
        String sevenFiveFives = sevenSevenFiveDropdown.getSelectedItem().toString();
        String intakeType = intakeTypeBox.getText().toString();
        String cubeRelease = cubeReleaseBox.getText().toString();
        String weight = weightBox.getText().toString();
        String narrowWideSquare = narrowWideSquareBox.getText().toString();
        String notes = notesBox.getText().toString();
        String name = nameBox.getText().toString();

        //output text to txt file
        try {
            PrintWriter fw = new PrintWriter(new FileWriter(new File("/storage/emulated/0/PitScoutingJSON.txt"), true));
            try {
                JSONObject pitData = new JSONObject();
                pitData.put( "number", teamNumber);
                pitData.put( "name", name);
                pitData.put("drive", driveType);
                pitData.put("wheels", wheelType);
                pitData.put("gearBox", shiftingGearBox);
                pitData.put("sevenSevenFives", sevenFiveFives);
                pitData.put("intake", intakeType);
                pitData.put("cubeRelease", cubeRelease);
                pitData.put("weight", weight);
                pitData.put("narrowWideSquare", narrowWideSquare);
                pitData.put("notes",notes);
                String outputString = pitData.toString();
                outputString = outputString + ",";
                fw.println(outputString);

            } catch (Exception e) {
                System.out.println("owo what's this? an error?");
                e.printStackTrace();
            }
            fw.close();
        } catch (Exception e) {
            System.out.println("owo what's this? an error?");
            e.printStackTrace();
        }
    }
}
