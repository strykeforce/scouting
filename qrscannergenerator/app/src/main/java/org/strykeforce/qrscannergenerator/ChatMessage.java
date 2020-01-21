package org.strykeforce.qrscannergenerator;

public class ChatMessage {
    public int scoutIDint, teamNumberInt, matchNumberint;
    private static final String[] labels = {"ID","TEAM","MATCH","LIL","APU","API","APO","APB","PI",
            "PO","PB","WOF2","WOF3",
            "CLI","TIME","COG", "FAIL", "NOTE"};
    private int numInt = 28, numStg = 4, numSending = numInt+numStg;
    private int[] nums;
    private String[] strings;

    int leftLine;
    int cellsPickedUp;
    int innerAutonScored;
    int outerAutonScored;
    int bottomAutonScored;

    int innerScored;
    int outerScored;
    int bottomScored;
    int stage2Complete;
    int stage3Complete;

    int climbed;
    int climbTime;
    int adjustCOG;

    int robotFailed;
    String scouterNotes;

    public ChatMessage(int[] data, String[] names)
    {
        this.scoutIDint = data[0];
        this.teamNumberInt = data[1];
        this.matchNumberint = data[2];

        this.leftLine = data[3];
        this.cellsPickedUp = data[4];
        this.innerAutonScored = data[5];
        this.outerAutonScored = data[6];
        this.bottomAutonScored = data[7];

        this.innerScored = data[8];
        this.outerScored = data[9];
        this.bottomScored = data[10];
        this.stage2Complete = data[11];
        this.stage3Complete = data[12];

        this.climbed = data[13];
        this.climbTime = data[14];
        this.adjustCOG = data[15];

        this.robotFailed = data[16];

        this.scouterNotes = names[0];

        this.nums = data;
        this.strings = names;
    }
}