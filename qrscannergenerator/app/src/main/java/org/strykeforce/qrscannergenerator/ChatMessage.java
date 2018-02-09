package org.strykeforce.qrscannergenerator;

public class ChatMessage {

    private String scoutID,teamNumber,matchNumber,autoHigh,autoLow,autoGear,teleHigh,teleLow,teleGear;
    private String baseLineCross,canPickGearOffGround,playsDefense,highShotDefended,touchPad,climbRopeTime,scoutName,notes;
    private int scoutIDint, teamNumberInt, matchNumberint;
    private int baseLineCrossint,canPickGearOffGroundint,playsDefenseint,highShotDefendedint,touchPadint,climbRopeTimeint,scoutNameint,notesint;
    private static final String[] labels = {"ID","Team","Match","ABL","Aswitch","Ascale","A2cube","Atime","Pcube","Ccube","Pzcube","Scube","Slcube","Xcube","Aclimb","Sclimb","Lift1", "Lift2", "Lift", "Op", "Rf", "Pen", "Notes"};
    private int numInt = 15, numStg = 2, numSending = numInt+numStg;
    private int[] nums;
    private String[] strings;

    public boolean BaseLineBool, DeliverSwitchBool, SecondCubeBool, AutoScaleBool = false, ClimbAttempt, Climb, Lift1, Lift2, Lifted, Platform, Failed;
    int ScaleTimeInt;
    int baseLineInt, deliverSwitchInt, secondCubeInt, autoScaleInt;
    int climbInt, climbAttemptInt, lift1Int, lift2Int, liftedInt, platformInt;
    int failedInt;

    public int PortalCubes = 0, CenterCubes = 0, ZoneCubes = 0, SwitchCubes = 0, ScaleCubes = 0, ExchangeCubes = 0;

    int Penalties;

    private static int MATCH_NUMBER = 0, TEAM_NUMBER = 0, SCOUT_ID = 0;

    public ChatMessage() {

    }

    private static int booltoInt (Boolean bool){
        return bool ? 1 : 0;
    }



    public ChatMessage(int[] data, String[] names)
    {
        this.scoutIDint = data[0];
        this.teamNumberInt = data[1];
        this.matchNumberint = data[2];

        this.baseLineInt = data[3];
        this.deliverSwitchInt = data[4];
        this.autoScaleInt = data[5];

        this.secondCubeInt = data[6];
        this.ScaleTimeInt = data[7];

        this.PortalCubes = data[8];

        this.CenterCubes = data[9];
        this.ZoneCubes = data[10];
        this.SwitchCubes = data[11];
        this.ScaleCubes = data[12];
        this.ExchangeCubes = data[13];

        this.climbAttemptInt = data[14];
        this.climbInt = data[15];
        this.lift1Int = data[16];
        this.lift2Int = data[17];
        this.liftedInt = data[18];
        this.platformInt = data[19];
        this.failedInt = data[20];

        this.Penalties = data[21];

        this.notes = names[0];
        this.nums = data;
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
    private String toJsonStg(String label, int data)
    {
        return "\""+label+"\":" + data;
    }
    private String toJsonStg(String label, String data)
    {
        return "\""+label+"\":\"" + data + "\"";
    }

    public String jsonObjStg()
    {
        String tempTotal = "{";
        for(int j=0; j<numInt; j++) //num int elements = 15
        {
            tempTotal+=toJsonStg(labels[j],nums[j])+",";
        }
        for(int j=0; j<numStg; j++)
        {
            tempTotal+=toJsonStg(labels[j],strings[j]);
            if(j+numInt+1!=numSending)
            {
                tempTotal+=",";
            }
        }
        tempTotal+="}\n";
        System.out.println("String!" + tempTotal);
        return tempTotal;
    }

   /* public int getTeamNumberInt() {
        return teamNumberInt;
    }
    public int getScoutID() {
        return scoutIDint;
    }
    public int getBaseLine() {

        return baseLineInt;

    }
    public int getAutoGear() {

        return autoGearint;

    }
    public int getPlaysDefense() {

        return playsDefenseint;

    }
    public int getCanPickGearOffGround() {

        return canPickGearOffGroundint;

    }
    public int getHighShotDefended() {

        return highShotDefendedint;

    }
    public int getTouchPad() {

        return touchPadint;

    }

    public int getMatchNumberInt() {return matchNumberint;}
    public int getAutoHigh() {return autoHighint;}
    public int getAutoLow() {return autoLowint;}
    public int getTeleHigh() {return teleHighint;}
    public int getTeleLow() {return teleLowint;}
    public int getTeleGear() {return teleGearint;}
    public int getClimbRopeTime() {return climbRopeTimeint;}
    public String getScoutName() {return scoutName;}
    public String getNotes() {return notes;}*/
}