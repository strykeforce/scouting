import psycopg2
#to use postgres
import datetime
#to use date and time
from TabletPull import CleanData
#cleandata is matrix
con = psycopg2.connect("dbname = Scouter user = postgres password = StrykeForce")
#con2 = psycopg2.connect("dbname = Scouter user = postgres password = StrykeForce")
cur = con.cursor()
#cur is now the shorter way of saying this
date = datetime.datetime.now()
#to be used in name
name = date.strftime("%b%d")
#to name tables
print("pandadata."+date.strftime("%b%d")+"raw")
#Used to check that it works
cur.execute("DROP TABLE IF EXISTS pandadata.{}raw;".format(name))
#remove all existing tables
cur.execute("CREATE TABLE PandaData.{}raw (id SERIAL PRIMARY KEY, scouter int, team int, match int, hatchCargoShipAuto int, hatchTopAuto int, hatchMidAuto int, hatchLowAuto int, cargoCargoShipAuto int, cargoTopAuto int, cargoMidAuto int, cargoLowAuto int, levelOneAuto int, levelTwoAuto int, preload VarChar, hatchCargoShipTele int, hatchTopTele int, hatchMidTele int, hatchLowTele int, cargoCargoShipTele int, cargoTopTele int, cargoMidTele int, cargoLowTele int, levelOneTele int, levelTwoTele int, levelThreeTele int, climbTimeTele int, penalties int, robotFailed int, playedDefense int, scouterNotes VarChar, scouterInitials VarChar, keep bool, kick bool, compare bool);".format(name))
#create the new table. {} is filled in by name due to the .format statement
#not case sensitive
for x in range(len(CleanData)):
    cur.execute("INSERT INTO PandaData.{}raw (scouter, team, match, hatchCargoShipAuto, hatchTopAuto, hatchMidAuto, hatchLowAuto, cargoCargoShipAuto, cargoTopAuto, cargoMidAuto, cargoLowAuto, levelOneAuto, levelTwoAuto, preload, hatchCargoShipTele, hatchTopTele, hatchMidTele, hatchLowTele, cargoCargoShipTele, cargoTopTele, cargoMidTele, cargoLowTele, levelOneTele, levelTwoTele, levelThreeTele, climbTimeTele, penalties, robotFailed, playedDefense, scouterNotes, scouterInitials) VALUES ({},{},{},{},{},{},{},{},{},{},{},{},{},'{}',{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},'{}','{}')".format(name, CleanData[x][0], CleanData[x][1], CleanData[x][2], CleanData[x][3], CleanData[x][4], CleanData[x][5], CleanData[x][6], CleanData[x][7], CleanData[x][8], CleanData[x][9], CleanData[x][10], CleanData[x][11], CleanData[x][12], CleanData[x][13], CleanData[x][14], CleanData[x][15], CleanData[x][16], CleanData[x][17], CleanData[x][18], CleanData[x][19], CleanData[x][20], CleanData[x][21], CleanData[x][22], CleanData[x][23], CleanData[x][24], CleanData[x][25], CleanData[x][26], CleanData[x][27], CleanData[x][28], CleanData[x][29], CleanData[x][30]))
#put in all of the data for an 'entry' in this table. Format is puting them in from CleanData, and you manualy have to count out
con.commit()
#actually due stuff yay UwU
cur.execute("TRUNCATE TABLE pandadata.MatchTeamData, pandadata.Matches")
#swipe off the table
cur.execute("INSERT INTO pandadata.matches (MatchNumber) SELECT DISTINCT match  FROM pandadata.{}raw" .format(name))
#put each non repeated match number into matches table
cur.execute("INSERT INTO pandadata.matchteamdata (scouter, teamid, hatchcargoshipauto, hatchtopauto, hatchmidauto,hatchlowauto, cargocargoshipauto, cargotopauto, cargomidauto, cargolowauto, leveloneauto, leveltwoauto, preload, hatchcargoshiptele, hatchtoptele, hatchmidtele, hatchlowtele, cargocargoshiptele, cargotoptele, cargomidtele, cargolowtele, levelonetele, leveltwotele, levelthreetele, climbtimetele, penalties, robotfailed, playeddefense, scouternotes, scouterinitials, matchid) SELECT scouter, teamid, hatchcargoshipauto, hatchtopauto, hatchmidauto,hatchlowauto, cargocargoshipauto, cargotopauto, cargomidauto, cargolowauto, leveloneauto, leveltwoauto, preload, hatchcargoshiptele, hatchtoptele, hatchmidtele, hatchlowtele, cargocargoshiptele, cargotoptele, cargomidtele, cargolowtele, levelonetele, leveltwotele, levelthreetele, climbtimetele, penalties, robotfailed, playeddefense, scouternotes, scouterinitials, matchid  FROM pandadata.{}raw LEFT JOIN pandadata.matches m ON m.MatchNumber = pandadata.{}raw.Match LEFT JOIN pandadata.teams t ON t.TeamNumber = pandadata.{}raw.team".format(name, name, name))
#insert all of the data into  Team Data table
con.commit()
#actually do it
con.close()
#close connection