from API import response
import json


j = response.json()
data = json.loads(j)
for a in len(data):
	time = data[a]["actual_time"]
	blueTeam = [data[a]["alliances"]["blue"]["score"]]
	redTeam = [data[a]["alliances"]["red"]["score"]]
	team1 = [data[a]["alliances"]["red"]["teamKeys"][0]]
	team2 = [data[a]["alliances"]["red"]["teamKeys"][1]]
	team3 = [data[a]["alliances"]["red"]["teamKeys"][2]]
	team4 = [data[a]["alliances"]["blue"]["teamKeys"][0]]
	team5 = [data[a]["alliances"]["blue"]["teamKeys"][1]]
	team6 = [data[a]["alliances"]["blue"]["teamKeys"][2]]
	redBay = 0
	while b<8:
		switcher = {
			"Panel": redTeam.append(1,0),
			"Cargo": redTeam.append(0,1),
			"PanelAndCargo": redTeam.append(1,1),
			"": redTeam.append(0,0)
		}
		switcher.get(data[a]["alliances"]["red"]["bay"+b]
		redBay = b+1
	blueBay = 0
	while c<8:
		switcher = {
			"Panel": blueTeam.append(1,0),
			"Cargo": blueTeam.append(0,1),
			"PanelAndCargo": blueTeam.append(1,1),
			"": blueTeam.append(0,0)
		}
		switcher.get(data[a]["alliances"]["blue"]["bay"+b]
		c = c+1
