package com.example.timetomeghreb;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class API {
    static public String[] cities = {"Agadir","Aghram","Ain ch\u00e2ir","Akka","Aknoul","Alhoceima","Araich","Arbaoua","Arfoud",
            "Assa","Assila","Azemour","Azilal","Azrou","Ben slimane","Bengrire","Beni Mellal","Berkane","Berrchid","Boujdour",
            "Boulemane","Bourd","Boutagit","Bouznika","Bou\u00e2arfa","BBou\u00e2nane","Casablanca","Chefchaouan","Dakhla","Demnat",
            "Eljadida","Errachidia","Essaouira","Feguig","Fes","Gelmim","Guelmima","Guersif","Hajb","Ifrane","Imouzar Kandar",
            "Jrada","Kal\u00e2a Megouna","Kesbat Tadla","Khouribga","Kh\u00e9missat","Kh\u00e9nifra","K\u00e9nitra","Laayoune","Lagouira",
            "Laksar lakbire","Lgara","Meknes","Melilla","Menzal beni yazgha","Merrakech","Meskoura","Midelet","Missour",
            "Mohammedia","Moulay Bou\u00e2aza","Moulay Ya\u00e2goub","Nador","Ouazane","Oued Amlil","Oued Zam","Oued zam","Ouerzazat",
            "Oujda","Rabat Sal\u00e9","Remani","Rich","Rissani","Safi","Sebta","Sefrou","Settat","Sidi Ifni","Sidi Slimane",
            "Sidi kac\u00e9m","Sidi yahya lgharb","Smara","Souq Arbi\u00e2 Gharb","Tafraout","Talouine","Tandit","Tanger","Tantan",
            "Taounat","Taourirt","Taroudent","Tata","Taza","Tehla","Terfaya","Tetouan","Tiflet","Tinjdad","Tissa","Tizi ousli",
            "Tiznit","Youssoufia","Zag","Zagoura","Zerhoune","kel\u00e2at Esraghna"};

    public static String getTimes(Activity acti, String city, String curr){
        Integer currDate = convertToHijri(curr);

        if(currDate == 0)
            return "";

        String meghreb = getTime(acti, currDate, city, R.raw.meghreb);
        String fajr    = getTime(acti, currDate, city, R.raw.fajr);

        return meghreb+"-"+fajr;
    }


    private static String loadJSONFromRawResource(Activity acti, int resId){
        String json = null;
        try {
            InputStream is = acti.getResources().openRawResource(resId);;
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private static Integer convertToHijri(String miladi){
        //ramadan 2021 Morocco
        Integer miladiTemp = Integer.parseInt(miladi);
        if(miladiTemp != null){
            if(miladiTemp <= 12)
                return miladiTemp + 17;
            else if(miladiTemp <= 30){
                return miladiTemp - 13;
            }
        }
        return 0;
    }



    private static String getTime(Activity acti, Integer date, String city, int ResId ){
        String result = "";
        try {
            JSONObject obj = new JSONObject(loadJSONFromRawResource(acti, ResId));
            JSONArray json_array = obj.getJSONArray(city);

            result = json_array.getString(date - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
