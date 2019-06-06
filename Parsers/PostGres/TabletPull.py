import json
#so it can read json's
with open("Documents\MasterDataJSON.json", encoding="utf8") as data_file:
    data = json.load(data_file)
#data equals the JSON file
CleanData = []
for x in range(len(data)):
    #for each entry do this
    Scouter = data[x]["Scout_ID"]
    Team = data[x]["Team"]
    Match = data[x]["Match"]
    HatchCargoShipAuto = data[x]["HatchCargoShipAuto"]
    HatchTopAuto = data[x]["HatchTopAuto"]
    HatchMidAuto = data[x]["HatchMidAuto"]
    HatchLowAuto = data[x]["hatchLowAuto"]
    CargoCargoShipAuto = data[x]["cargoCargoShipAuto"]
    CargoTopAuto = data[x]["cargoTopAuto"]
    CargoMidAuto = data[x]["cargoMidAuto"]
    CargoLowAuto = data[x]["cargoLowAuto"]
    LevelOneAuto = data[x]["levelOneAuto"]
    LevelTwoAuto = data[x]["levelTwoAuto"]
    Preload = data[x]["preload"]
    HatchCargoShipTele = data[x]["hatchCargoShipTele"]
    HatchTopTele = data[x]["hatchTopTele"]
    HatchMidTele = data[x]["hatchMidTele"]
    HatchLowTele = data[x]["hatchLowTele"]
    CargoCargoShipTele = data[x]["cargoCargoShipTele"]
    CargoTopTele = data[x]["cargoTopTele"]
    CargoMidTele = data[x]["cargoMidTele"]
    CargoLowTele = data[x]["cargoLowTele"]
    LevelOneTele = data[x]["levelOneTele"]
    LevelTwoTele = data[x]["levelTwoTele"]
    LevelThreeTele = data[x]["levelThreeTele"]
    ClimbTimeTele = data[x]["climbTimeTele"]
    Penalties = data[x]["penalties"]
    RobotFailed = data[x]["robotFailed"]
    PlayedDefense = data[x]["playedDefense"]
    ScouterNotes = data[x]["scouterNotes"]
    ScouterInitials = data[x]["scouterInitials"]

    CleanData.append([Scouter, Team, Match, HatchCargoShipAuto, HatchTopAuto, HatchMidAuto, HatchLowAuto,
    CargoCargoShipAuto, CargoTopAuto, CargoMidAuto, CargoLowAuto, LevelOneAuto, LevelTwoAuto, Preload,
    HatchCargoShipTele, HatchTopTele, HatchMidTele, HatchLowTele, CargoCargoShipTele, CargoTopTele, CargoMidTele,
    CargoLowTele, LevelOneTele, LevelTwoTele, LevelThreeTele, ClimbTimeTele, Penalties, RobotFailed, PlayedDefense, ScouterNotes, ScouterInitials])
#add this to CleanData
