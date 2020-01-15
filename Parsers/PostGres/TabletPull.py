import json
#so it can read json's
with open("Documents\MasterDataJSON.json", encoding="utf8") as data_file:
    data = json.load(data_file)
#data equals the JSON file
CleanData = []
for x in range(len(data)):
    #for each entry do this
    scouter = data[x]["Scout_ID"]
    team = data[x]["Team"]
    match = data[x]["Match"]
    innerPortAuto = data[x]["innerPortAuto"]
    outerPortAuto = data[x]["outerPortAuto"]
    lowerPortAuto = data[x]["lowerPortAuto"]
    intiationLeave = data[x]["initiationLeave"]
    pickUpAuton = data[x]["pickUpAuton"]
    innerPort = data[x]["innerPort"]
    outerPort = data[x]["outerPort"]
    lowerPort = data[x]["lowerPort"]
    rotationalWOF = data[x]["rotationalWOF"]
    positionalWOF = data[x]["positionalWOF"]
    climb = data[x]["climb"]
    climbTime = data[x]["climbTime"]
    adjustCOG = data[x]["adjustCOG"]
    robotFailed = data[x]["robotFailed"]
    notes = data[x]["notes"]

    CleanData.append([scouter, team, match, innerPortAuto, outerPortAuto, lowerPortAuto, intiationLeave, pickUpAuton, innerPort, outerPort, lowerPort, rotationalWOF, positionalWOF, climb, climbTime, adjustCOG, robotFailed, notes])
#add this to CleanData
