from bs4 import BeautifulSoup
import requests
import json

url1 = "http://www.habous.gov.ma/fr/horaire%20de%20priere%20fr/horaire_hijri.php?ville="
url2 = "&mois=4"

htmlFile = requests.get(url1+str(1)+url2).text
parsedhtml = BeautifulSoup(htmlFile, 'html5lib')


resultDict = {}

form = parsedhtml.find('form')
items = form.find_all('option')
print(len(items))


for i in range(len(items)):
    resultDict[str(i+1)] = items[i].text.strip()

print(resultDict.get("3"))


output = open('villes.json', 'w').write(json.dumps(resultDict))