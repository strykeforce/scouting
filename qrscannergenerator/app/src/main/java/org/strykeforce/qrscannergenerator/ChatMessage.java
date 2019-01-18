package org.strykeforce.qrscannergenerator;

public class ChatMessage {
    public int scoutIDint, teamNumberInt, matchNumberint;
    private static final String[] labels = {"ID","TEAM","MATCH","HCSA","HTA","HMA","HLA","CCSA","CTA",
            "CMA","CLA","PRE","L1A","L2A",
            "HCST","HTT","HMT", "HLT", "CCST", "CTT", "CMT", "CLT", "CT", "L1T",
            "L2T", "L3T", "PEN", "FAIL", "DEF", "NOTE", "INIT"};
    private int numInt = 22, numStg = 1, numSending = numInt+numStg;
    private int[] nums;
    private String[] strings;

    int hatchCargoShipAuto, hatchTopAuto, hatchMidAuto, hatchLowAuto;
    int cargoCargoShipAuto, cargoTopAuto, cargoMidAuto, cargoLowAuto;
    String preload;

    int hatchCargoShipTele, hatchTopTele, hatchMidTele, hatchLowTele;
    int cargoCargoShipTele, cargoTopTele, cargoMidTele, cargoLowTele;
    int climbTimeTele;

    int levelOneAuto , levelTwoAuto ;

    int levelOneTele , levelTwoTele , levelThreeTele;

    int penalties = 0;

    int robotFailed, playedDefense;

    String scouterNotes = "", scouterInitials = "";

    public ChatMessage(int[] data, String[] names)
    {
        this.scoutIDint = data[0];
        this.teamNumberInt = data[1];
        this.matchNumberint = data[2];

        this.hatchCargoShipAuto = data[3];
        this.hatchTopAuto = data[4];
        this.hatchMidAuto = data[5];
        this.hatchLowAuto = data[6];

        this.cargoCargoShipAuto = data[7];
        this.cargoTopAuto = data[8];
        this.cargoMidAuto = data[9];
        this.cargoLowAuto = data[10];

        this.levelOneAuto = data[11];
        this.levelTwoAuto = data[12];

        this.preload = names[0];

        this.hatchCargoShipTele = data[13];
        this.hatchTopTele = data[14];
        this.hatchMidTele = data[15];
        this.hatchLowTele = data[16];

        this.cargoCargoShipTele = data[17];
        this.cargoTopTele = data[18];
        this.cargoMidTele = data[19];
        this.cargoLowTele = data[20];

        this.climbTimeTele = data[21];

        this.levelOneTele = data[22];
        this.levelTwoTele = data[23];
        this.levelThreeTele = data[24];

        this.penalties = data[25];
        this.robotFailed = data[26];
        this.playedDefense = data[27];

        this.scouterNotes = names[1];
        this.scouterInitials = names[2];

        this.nums = data;
        this.strings = names;
    }
}