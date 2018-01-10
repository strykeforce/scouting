package org.strykeforce.qrscannergenerator;

public class ChatMessage {

    private String scoutID,teamNumber,matchNumber,autoHigh,autoLow,autoGear,teleHigh,teleLow,teleGear;
    private String baseLineCross,canPickGearOffGround,playsDefense,highShotDefended,touchPad,climbRopeTime,scoutName,notes;
    private int scoutIDint,teamNumberint,matchNumberint,autoHighint,autoLowint,autoGearint,teleHighint,teleLowint,teleGearint;
    private int baseLineCrossint,canPickGearOffGroundint,playsDefenseint,highShotDefendedint,touchPadint,climbRopeTimeint,scoutNameint,notesint;
    private static final String[] labels = {"Scout ID","Team","Match","Auto High","Tele High","Tele Low","Tele Gears","Climb rope time","Auto Low","Auto Gears","Crosses base line","Picks gear off ground","On defense","Defended shooting high","Touchpad","Scout name","Notes"};
    private int numInt = 15, numStg = 2, numSending = numInt+numStg;
    private int[] nums;
    private String[] strings;

    public ChatMessage() {

    }

    public ChatMessage(int[] data, String[] names)
    {
        this.scoutIDint = data[0];
        this.teamNumberint = data[1];
        this.matchNumberint = data[2];

        this.autoHighint = data[3];
        this.autoLowint = data[8];
        this.autoGearint = data[9];

        this.teleHighint = data[4];
        this.teleLowint = data[5];

        this.teleGearint = data[6];

        this.baseLineCrossint = data[10];
        this.canPickGearOffGroundint = data[11];
        this.playsDefenseint = data[12];
        this.highShotDefendedint = data[13];
        this.touchPadint = data[14];
        this.climbRopeTimeint = data[7];

        this.scoutName = names[0];
        this.notes = names[1];

        this.nums = data;
        this.strings = names;
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

    public int getTeamNumberInt() {
        return teamNumberint;
    }
    public int getScoutID() {
        return scoutIDint;
    }
    public int getBaseLineCross() {

        return baseLineCrossint;

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
    public String getNotes() {return notes;}
}