import requests
url = "https://www.thebluealliance.com/api/v3/event/2019miken/matches"
headers = {'X-TBA-Auth-Key': 'SUyoasEkfFcavkG7KbfaQjAo70zNlJWT9Uxj1qvKGRw589MVVdvvGbXgajPyz2CL'}
response = requests.request("GET", url, headers=headers)
print(response.json())
