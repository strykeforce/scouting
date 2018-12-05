package com.example.lilia.pitscoutingapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import android.content.Intent;
import android.os.Environment;
import android.net.Uri;
import android.provider.MediaStore;

public class mainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goInfo();
    }

    public void goInfo() {
        setContentView(R.layout.activity_main_screen);

        findViewById(R.id.pictureButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPictures();
            }
        });

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

    public void goPictures() {
        setContentView(R.layout.picture_screen);

        findViewById(R.id.infoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goInfo();
            }
        });

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

    public void saveInfo() {
        final EditText teamInputBox = (EditText) findViewById(R.id.teamInput);
        final EditText driveInputBox = (EditText) findViewById(R.id.driveInput);
        final EditText wheelTypeBox = (EditText) findViewById(R.id.wheelTypeInput);
        final EditText shiftingGearBoxBox = (EditText) findViewById(R.id.gearBoxInput);
        final EditText sevenFiveFivesBox = (EditText) findViewById(R.id.sevenFiveFivesInput);
        final EditText limitCurrentBox = (EditText) findViewById(R.id.limitCurrentInput);
        final EditText intakeTypeBox = (EditText) findViewById(R.id.intakeTypeInput);
        final EditText cubeReleaseBox = (EditText) findViewById(R.id.cubeReleaseInput);
        final EditText weightBox = (EditText) findViewById(R.id.weightInput);
        final EditText narrowWideSquareBox = (EditText) findViewById(R.id.narrowWideSquareInput);
        final EditText notesBox = (EditText) findViewById(R.id.notes);
        final EditText nameBox = (EditText) findViewById(R.id.teamName);

        String teamNumber = teamInputBox.getText().toString();
        String driveType = driveInputBox.getText().toString();
        String wheelType = wheelTypeBox.getText().toString();
        String shiftingGearBox = shiftingGearBoxBox.getText().toString();
        String sevenFiveFives = sevenFiveFivesBox.getText().toString();
        String limitCurrent = limitCurrentBox.getText().toString();
        String intakeType = intakeTypeBox.getText().toString();
        String cubeRelease = cubeReleaseBox.getText().toString();
        String weight = weightBox.getText().toString();
        String narrowWideSquare = narrowWideSquareBox.getText().toString();
        String notes = notesBox.getText().toString();
        String name = nameBox.getText().toString();

        try {
            PrintWriter fw = new PrintWriter(new FileWriter(new File("/storage/emulated/0/PitScoutingJSON.txt"), true));
            try {
                JSONObject pitData = new JSONObject();
                pitData.put( "number", teamNumber);
                pitData.put("drive", driveType);
                pitData.put("wheels", wheelType);
                pitData.put("gearBox", shiftingGearBox);
                pitData.put("sevenFiveFives", sevenFiveFives);
                pitData.put("limitCurrent", limitCurrent);
                pitData.put("intake", intakeType);
                pitData.put("cubeRelease", cubeRelease);
                pitData.put("weight", weight);
                pitData.put("narrowWideSquare", narrowWideSquare);
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
