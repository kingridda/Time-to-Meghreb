from bs4 import BeautifulSoup
import requests
import json

url1 = "http://www.habous.gov.ma/fr/horaire%20de%20priere%20fr/horaire_hijri.php?ville="
url2 = "&mois=4"

f = open('villes.json')
villes = json.load(f)

resultDict = {}

for villeIndex in villes:
    resultDict[villes[villeIndex]] = []
    url = url1 + villeIndex + url2
    htmlFile = requests.get(url).text
    parsedhtml = BeautifulSoup(htmlFile, 'html5lib')
    tbodys = parsedhtml.find_all('tbody')
    tbody = tbodys[1]
    items = tbody.find_all('td')

    for i in range(30):
        for j in range(9):
            if(i != 0 and j == 3):
                resultDict[villes[villeIndex]].append( items[i*9+j].text.strip() )
    print( villeIndex + villes[villeIndex]+ " ends ")



output = open('fajr.json', 'w').write(json.dumps(resultDict))
f.close()
