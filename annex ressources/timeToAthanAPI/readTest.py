import json
f = open('villes.json')
data = json.load(f)
for i in data:
    print(data[i])

f.close()