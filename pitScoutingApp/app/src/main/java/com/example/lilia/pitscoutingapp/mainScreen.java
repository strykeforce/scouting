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

    EditText teamInputBox ;
     Spinner driveTrainDropdown ;
    Spinner wheelTypeDropdown ;
    Spinner shiftingGearBoxDropdown ;
    Spinner sevenSevenFiveDropdown ;
    EditText intakeTypeBox ;
     EditText cubeReleaseBox ;
     EditText weightBox ;
    EditText narrowWideSquareBox ;
     EditText notesBox ;
     EditText nameBox ;
     EditText currentBox;
     EditText wheelMixedBox;
    EditText wheelOtherBox;
    EditText driveOtherBox;

    String teamNumber = "" ;
    String driveType ="";
    String wheelType ="";
    String shiftingGearBox ="";
    String sevenFiveFives ="";
    String intakeType ="";
    String hatchOffGround ="";
    String weight ="";
    String narrowWideSquare ="" ;
    String notes ="";
    String name ="";
    String sevenSevenFive = "";
    String wheelTypeMixed = "";
    String wheelTypeOther = "";
    String driveTrainOther = "";
    static int driveTrainId =0;
    static int wheelTypeId=0;
    static int shiftingGearBoxId=0;
    static int sevenSevenFiveId=0;
    String currentString = "";
    String wheelTypeString = "";
    String shiftingGearBoxString = "";
    String driveTrainString = "";
    String sevenSevenFiveCurrent = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //go to info screen
        goInfo();
    }




public void goCurrentPrompt() {
        setContentView(R.layout.seven_seven_five_prompt);

    currentBox = (EditText) findViewById(R.id.curentlimitprompt);

    currentBox.setText(sevenSevenFive);
    findViewById(R.id.currentOK).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sevenSevenFiveCurrent = currentBox.getText().toString();
            goInfo();
        }
    });



}

public void goDrivePrompt(){
        setContentView(R.layout.drive_train_prompt);
    driveOtherBox = (EditText) findViewById(R.id.driveTrainOtherPrompt);
    driveOtherBox.setText(driveTrainOther);
    findViewById(R.id.driveTrainOK).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            driveTrainOther = driveOtherBox.getText().toString();
           goInfo();
        }
    });


}


public void goWheelTypeOtherPrompt(){

    setContentView(R.layout.wheel_type_other_prompt);
    wheelOtherBox = (EditText) findViewById(R.id.wheelTypeOtherPrompt);
    wheelOtherBox.setText(wheelTypeOther);
    findViewById(R.id.wheelTypeOtherOK).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            wheelTypeOther = wheelOtherBox.getText().toString();
            goInfo();
        }
    });

}



    public void goWheelTypeMixedPrompt(){

        setContentView(R.layout.wheel_type_mixed_prompt);
        wheelMixedBox = (EditText) findViewById(R.id.wheelTypeMixedPrompt);
        wheelMixedBox.setText(wheelTypeMixed);
        findViewById(R.id.wheelMixedOK).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                wheelTypeMixed = wheelMixedBox.getText().toString();
                goInfo();
            }
        });

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

        teamInputBox = (EditText) findViewById(R.id.teamInput);
        intakeTypeBox = (EditText) findViewById(R.id.intakeTypeInput);
        cubeReleaseBox = (EditText) findViewById(R.id.cubeReleaseInput);
        weightBox = (EditText) findViewById(R.id.weightInput);
        narrowWideSquareBox = (EditText) findViewById(R.id.narrowWideSquareInput);
        notesBox = (EditText) findViewById(R.id.notes);
        nameBox = (EditText) findViewById(R.id.teamName);





        //set spinner entries



        final String[] driveTrain = new String[]{"Tank" , "Swerve", "Slide", "Other"};
        final Spinner driveTrainSpinner = (Spinner) findViewById(R.id.driveTrain);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, driveTrain);
        driveTrainSpinner.setAdapter(adapter);


        String[] wheelType = new String[]{"Colsons" , "Mechanum", "Nitrile/Tread", "Omni","Other", "Mixed"};
        final Spinner wheelTypeSpinner = (Spinner) findViewById(R.id.wheelTypeSpinner);

        final ArrayAdapter<String> wheelTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, wheelType);
        wheelTypeSpinner.setAdapter(wheelTypeAdapter);


        String[] shiftingGearBoxStrings = new String[]{"Yes", "No"};
        final Spinner shiftingGearBoxSpinner = (Spinner) findViewById(R.id.shiftingGearBox);

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, shiftingGearBoxStrings);
        shiftingGearBoxSpinner.setAdapter(adapter2);


        String[] sevenSevenFiveStrings = new String[]{"No" , "Yes"};
        final Spinner sevenSevenFiveSpinner = (Spinner) findViewById(R.id.sevenSevenFive);

        final ArrayAdapter<String> adapterCurrent = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sevenSevenFiveStrings);
        sevenSevenFiveSpinner.setAdapter(adapterCurrent);

            teamInputBox.setText(teamNumber);
            System.out.println("ID's: DriveTrain: " + driveTrainId + " wheel Type: " + wheelTypeId + " Shifting gearbox: " + shiftingGearBoxId + " 775's: " + sevenSevenFiveId);
            driveTrainSpinner.setSelection(driveTrainId);
            wheelTypeSpinner.setSelection(wheelTypeId);
            shiftingGearBoxSpinner.setSelection(shiftingGearBoxId);
            sevenSevenFiveSpinner.setSelection(sevenSevenFiveId);
            System.out.println("Done Setting ID's");
            intakeTypeBox.setText(intakeType);
            cubeReleaseBox.setText(hatchOffGround);
            weightBox.setText(weight);
            narrowWideSquareBox.setText(narrowWideSquare);
            notesBox.setText(notes);
            nameBox.setText(name);
        //Make prompts link to their pages

/*
    String teamNumber = "" ;
    String intakeType ="";
    String hatchOffGround ="";
    String weight ="";
    String narrowWideSquare ="" ;
    String notes ="";
    String name ="";

*/
            ((Spinner)findViewById(R.id.shiftingGearBox)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    shiftingGearBoxString= shiftingGearBoxSpinner.getSelectedItem().toString();
                    shiftingGearBoxId = i;
                    System.out.println("Shifting Gearbox Selection: " + adapter2.getItem(i));

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


        ((Spinner)findViewById(R.id.driveTrain)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                driveTrainString = driveTrainSpinner.getSelectedItem().toString();

                System.out.println("Drive Train selection: " + adapter.getItem(i));
                if(adapter.getItem(i)== "Other" && driveTrainId != i) {
                    // save before going to new page
              teamNumber = teamInputBox.getText().toString();
                name = nameBox.getText().toString() ;
                intakeType = intakeTypeBox.getText().toString();
                hatchOffGround = cubeReleaseBox.getText().toString();
                weight = weightBox.getText().toString();
                narrowWideSquare = narrowWideSquareBox.getText().toString();
                notes = notesBox.getText().toString();

                    // Go to fill in page

                   goDrivePrompt();


                }
                driveTrainId = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


            ((Spinner)findViewById(R.id.sevenSevenFive)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    currentString = sevenSevenFiveSpinner.getSelectedItem().toString();
                    if(adapterCurrent.getItem(i)== "Yes" && sevenSevenFiveId != i) {
                        // save before going to new page
                        teamNumber = teamInputBox.getText().toString();
                        name = nameBox.getText().toString() ;
                        intakeType = intakeTypeBox.getText().toString();
                        hatchOffGround = cubeReleaseBox.getText().toString();
                        weight = weightBox.getText().toString();
                        narrowWideSquare = narrowWideSquareBox.getText().toString();
                        notes = notesBox.getText().toString();
                        sevenSevenFiveId = i;

                        // Go to fill in page
                        goCurrentPrompt();


                    }
                    sevenSevenFiveId = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            ((Spinner)findViewById(R.id.wheelTypeSpinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                wheelTypeString = wheelTypeSpinner.getSelectedItem().toString();

                System.out.println("Wheel Type Selection: " + wheelTypeAdapter.getItem(i));
                if(wheelTypeAdapter.getItem(i)== "Other" && wheelTypeId != i) {
                    teamNumber = teamInputBox.getText().toString();
                    name = nameBox.getText().toString() ;
                    intakeType = intakeTypeBox.getText().toString();
                    hatchOffGround = cubeReleaseBox.getText().toString();
                    weight = weightBox.getText().toString();
                    narrowWideSquare = narrowWideSquareBox.getText().toString();
                    notes = notesBox.getText().toString();
                    goWheelTypeOtherPrompt();


                }
                if(wheelTypeAdapter.getItem(i)== "Mixed" && wheelTypeId != i){
                    teamNumber = teamInputBox.getText().toString();
                    name = nameBox.getText().toString() ;
                    intakeType = intakeTypeBox.getText().toString();
                    hatchOffGround = cubeReleaseBox.getText().toString();
                    weight = weightBox.getText().toString();
                    narrowWideSquare = narrowWideSquareBox.getText().toString();
                    notes = notesBox.getText().toString();
                    goWheelTypeMixedPrompt();

                }


                wheelTypeId = i;
            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




/*
uogyfjklhgehuigaerhuiltg4aweTUILGHUBGARIHGWEDILGUFHNAILEDGFHUBweialghfneaiklughILWEUKAHGFLAKEUGHFBILEGHBALKEDBGUBFILawegfbualeigbfaeigfhulgiaujegfiledgfbuidkgbueagfbeulgbuiweaghbeliagb

 */

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
        teamNumber = teamInputBox.getText().toString();
        name = nameBox.getText().toString() ;
        intakeType = intakeTypeBox.getText().toString();
        hatchOffGround = cubeReleaseBox.getText().toString();
        weight = weightBox.getText().toString();
        narrowWideSquare = narrowWideSquareBox.getText().toString();
        notes = notesBox.getText().toString();
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

        //save info in those text boxes
        teamNumber = teamInputBox.getText().toString();
        name = nameBox.getText().toString() ;
        intakeType = intakeTypeBox.getText().toString();
        hatchOffGround = cubeReleaseBox.getText().toString();
        weight = weightBox.getText().toString();
        narrowWideSquare = narrowWideSquareBox.getText().toString();
        notes = notesBox.getText().toString();


        //output text to txt file
        try {
            PrintWriter fw = new PrintWriter(new FileWriter(new File("/storage/emulated/0/PitScoutingJSON.txt"), true));
            try {
                JSONObject pitData = new JSONObject();
                pitData.put( "number", teamNumber);
                pitData.put( "name", name);
                pitData.put("intake", intakeType);
                pitData.put("hatchOffGround", hatchOffGround);
                pitData.put("weight", weight);
                pitData.put("narrowWideSquare", narrowWideSquare);
                pitData.put("notes",notes);
                pitData.put("sevenFiveFive",currentString);
                pitData.put("sevenFiveFiveCurrent",sevenSevenFiveCurrent);
                pitData.put("wheelType",wheelTypeString);
                pitData.put("driveTrain",driveTrainString);
                pitData.put("shiftingGearBox",shiftingGearBoxString);
                pitData.put("sevenFiveFive",sevenSevenFive);
                pitData.put("wheelTypeOther",wheelTypeOther);
                pitData.put("wheelTypeMixed",wheelTypeMixed);
                pitData.put("driveTrainOther",driveTrainOther);
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
