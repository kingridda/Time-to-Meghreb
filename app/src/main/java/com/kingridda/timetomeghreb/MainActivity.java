package com.kingridda.timetomeghreb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.ACTION_TIME_TICK;
import static com.kingridda.timetomeghreb.API.cities;

public class MainActivity extends AppCompatActivity {
    private TextView mainTextView;
    private TextView expressionTextView;
    private AutoCompleteTextView chooseCityAutoCompleteTextView;

    private SharedPreferences prefs;




    private String currentDate;
    private String currentTime;
    private String expression;
    private String city = "";


    public String meghrebTime;
    public String fajrTime;

    private final BroadcastReceiver timeBroadcastReceiver = new TimeReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainTextView = findViewById(R.id.main_taxt);
        expressionTextView = findViewById(R.id.expression_text);
        chooseCityAutoCompleteTextView = findViewById(R.id.choose_city_auto_complete);

        //getting SharedPrefs: user's preferences
        prefs = getPreferences(Context.MODE_PRIVATE);

        //setting cities in dropdown
        ArrayAdapter cityArrayAdapter = new ArrayAdapter(this, R.layout.item_list_cities, cities);
        chooseCityAutoCompleteTextView.setText( cityArrayAdapter.getItem( Integer.parseInt( prefs.getString("city", "0") ) ).toString() );
        chooseCityAutoCompleteTextView.setAdapter(cityArrayAdapter);
        mainFuntionality();


        //dropdown click listener
        chooseCityAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("city", ""+position);
                editor.apply();
                Toast.makeText(getApplicationContext(), cityArrayAdapter.getItem(position).toString(), Toast.LENGTH_LONG).show();
                mainFuntionality();
            }
        });







        //registering the broadCast Receiver
        registerBroadcastReceiver();

    }




    private String compareTimes(String curr, String meghreb, String fajr){
        String result = "";
        Integer minutesToMeghreb = compareStringTimes(meghreb, curr);
        Integer minutesToFajr    = compareStringTimes(fajr, curr);




        if(minutesToFajr*minutesToMeghreb < 0){
            //Maghreb is the next thing
            result += prettify_time("" + (minutesToMeghreb / 60) );
            result += ":"+prettify_time("" + ( minutesToMeghreb % 60 ) );
            this.expression = getString(R.string.time_to_meghreb);

        }else if(minutesToFajr*minutesToMeghreb > 0){
            //fajr is next
            if(minutesToFajr < 0){
                minutesToFajr += 1440;
            }
            result += prettify_time("" + (minutesToFajr / 60) );
            result += ":"+prettify_time("" + (minutesToFajr  % 60 ) );
            this.expression = getString(R.string.time_to_fajr);

        }else if(minutesToFajr == 0){
            //fajr time
            result = "00:00";
            this.expression = getString(R.string.fajr_time);

        }else{
            //meghreb time
            result = "00:00";
            this.expression = getString(R.string.meghreb_time);

        }

        return result;
    }



    private boolean setFajrAndMeghrebTimesForToday(String currentDate) {
        String temp = API.getTimes(this, city, currentDate);

            if(!temp.equals("")){
                this.meghrebTime = temp.split("-")[0];
                this.fajrTime = temp.split("-")[1];
                return true;
            }

        return false;
    }


    private Integer compareStringTimes(String megh,String curr){
        Integer minutes;

        minutes = Integer.parseInt(megh.split(":")[0]) - Integer.parseInt(curr.split(":")[0]);
        minutes *= 60;

        minutes += Integer.parseInt(megh.split(":")[1]) - Integer.parseInt(curr.split(":")[1]);

        return minutes;
    }

    private String prettify_time(String s){
        if(s.length() == 1) return "0"+s;
        return s;
    }


    private void mainFuntionality(){
        //main functionality
        currentDate = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        city = chooseCityAutoCompleteTextView.getText().toString();
        if(isRamadan()){
            if(!city.equals("")){
                if(setFajrAndMeghrebTimesForToday(currentDate)){
                    String result = compareTimes(currentTime, meghrebTime ,  fajrTime);
                    mainTextView.setText(result);
                    expressionTextView.setText(expression);
                }
            }
        }else{
            expressionTextView.setText( getString(R.string.not_in_ramadan) );
        }

    }


    private void registerBroadcastReceiver() {
        // Create a new broadcast intent filter that will filter the wanted intent
        IntentFilter intentFilter = new IntentFilter(ACTION_TIME_TICK);

        //registering BR to the register
        registerReceiver(timeBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy(){
        // Always call super method.
        super.onDestroy();

        // Unregister the broadcast receiver.
        unregisterReceiver(timeBroadcastReceiver);
    }


    //BraadCast Receiver Class
    private class TimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mainFuntionality();
        }
    }
    private boolean isRamadan(){
        String  year   = new SimpleDateFormat("yy", Locale.getDefault()).format(new Date()),
                month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        Integer day   = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format( new Date() ) );


        if(year.equals("21")){
            if(month.equals("04"))
                if(day >= 14 && day <= 30 )
                    return true;

            if(month.equals("05"))
                if(day <= 12 && day >= 1)
                    return true;
        }

        return false;
    }

}