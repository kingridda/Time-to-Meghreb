from bs4 import BeautifulSoup
import requests
import json

url1 = "http://www.habous.gov.ma/fr/horaire%20de%20priere%20fr/horaire_hijri.php?ville="
url2 = "&mois=4"

htmlFile = requests.get(url1+str(1)+url2).text
parsedhtml = BeautifulSoup(htmlFile, 'html5lib')


resultSTR = ''

form = parsedhtml.find('form')
items = form.find_all('option')

print(len(items))

prettyItems = []
for i in range(len(items)):
    prettyItems.append(items[i].text.strip().replace("'", " "))

print(len(prettyItems))
prettyItems.sort()
for i in prettyItems:
    resultSTR += '"'+ i+ '",'

print(resultSTR)

f = open("villesSTR.txt",'w')

f.write(resultSTR)
f.close()