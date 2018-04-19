package org.strykeforce.qrscannergenerator;

public class ChatMessage {
    public int scoutIDint, teamNumberInt, matchNumberint;
    private static final String[] labels = {"ID","Team","Match","ABL","Aswitch","Ascale","A2cube","Atime","Pcube","Ccube","Pzcube","Scube","Slcube","Xcube","Aclimb","Sclimb","Lift1", "Lift2", "Lift", "Op", "Rf", "Pen", "Drv", "Notes"};
    private int numInt = 22, numStg = 1, numSending = numInt+numStg;
    private int[] nums;
    private String[] strings;

    public int ScaleTimeInt;
    public int baseLineInt, deliverSwitchInt, secondCubeInt, autoScaleInt;
    public int climbInt, climbAttemptInt, lift1Int, lift2Int, liftedInt, platformInt;
    public int failedInt;
    public int Driveability;

    public int PortalCubes = 0, CenterCubes = 0, ZoneCubes = 0, SwitchCubes = 0, ScaleCubes = 0, ExchangeCubes = 0;
    public String Notes;
    int Penalties;

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
        this.Driveability = data[22];

        this.Notes = names[0];
        this.nums = data;
    }
}