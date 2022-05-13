package com.itschoolproject.weather;

import android.content.Intent;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String CITY = "Ставрополь";
    String API = "cd326c78cac78563972704eaebb6c272";
    Double lon = 41.9733;
    Double lat = 45.0428;


    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt, feels_likeTxt;

    ImageButton updw, changecity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        feels_likeTxt = findViewById(R.id.feels_like);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        updw = (ImageButton) findViewById(R.id.updateweather);
        changecity = (ImageButton) findViewById(R.id.changeCity);
        updw.setOnClickListener(this);
        changecity.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra("city")) {
            intent.getStringExtra("city");
            CITY = intent.getStringExtra("city");
            new weatherTask().execute();
            new ForecastTask().execute();
        } else {
            new weatherTask().execute();
            new ForecastTask().execute();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.changeCity:
                Intent intent = new Intent(this, ChangeCity.class);
                startActivity(intent);
                break;
            case R.id.updateweather:
                new weatherTask().execute();
                new ForecastTask().execute();
                break;
        }
    }


    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&lang=ru&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Обновлено " + new SimpleDateFormat("HH:mm").format(new Date((updatedAt * 1000)));
                String temp = main.getInt("temp") + "°C";
                String feelslike = main.getString("feels_like") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                switch (weather.getString("id")) {
                    case "800": statusTxt.setText("ЯСНО");break;
                    case "801": statusTxt.setText("ОБЛАЧНО");break;
                    case "802": statusTxt.setText("ОБЛАЧНО");break;
                    case "803": statusTxt.setText("ОБЛАЧНО");break;
                    case "804": statusTxt.setText("ОБЛАЧНО");break;
                }
                switch (Character.toString(weather.getString("id").charAt(0))) {
                    case "2": statusTxt.setText("ГРОЗА");break;
                    case "3": statusTxt.setText("ИЗМОРОСЬ");break;
                    case "5": statusTxt.setText("ДОЖДЬ");break;
                    case "6": statusTxt.setText("СНЕГ");break;
                    case "7": statusTxt.setText(weather.getString("description"));break;
                }



                String address = jsonObj.getString("name") + ", " + sys.getString("country");


                addressTxt.setText(address);
                updated_atTxt.setText(updatedAtText);
                tempTxt.setText(temp);
                sunriseTxt.setText(new SimpleDateFormat("HH:mm").format(new Date(sunrise * 1000)));
                sunsetTxt.setText(new SimpleDateFormat("HH:mm").format(new Date(sunset * 1000)));
                windTxt.setText(windSpeed);
                pressureTxt.setText(pressure);
                humidityTxt.setText(humidity);
                feels_likeTxt.setText(feelslike);


                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }

    class ForecastTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&lang=ru&appid=" + API);
            try {
                JSONObject ll = new JSONObject(response);
                lat = ll.getJSONObject("coord").getDouble("lat");
                lon = ll.getJSONObject("coord").getDouble("lon");
            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }
            String forecast = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/onecall?&lon=" + lon + "&lat=" + lat + "&exclude=minutely&units=metric&lang=ru&appid=" + API);
            return forecast;
        }

        @Override
        protected void onPostExecute(String forecast) {


            try {
                JSONObject jsonObj = new JSONObject(forecast);
                JSONObject current = jsonObj.getJSONObject("current");
                JSONArray hourlyforecast = jsonObj.getJSONArray("hourly");
                JSONObject cw = current.getJSONArray("weather").getJSONObject(0);
                TextView time = findViewById(R.id.time0);
                time.setText("Сейчас");
                TextView tmp = findViewById(R.id.tmp0);
                String temp = current.getInt("temp") + "°C";
                tmp.setText(temp);
                TextView wind = findViewById(R.id.wind0);
                wind.setText(current.getString("wind_speed") + "м/с");
                ImageView img = findViewById(R.id.weathericon0);
                switch (cw.getString("icon")) {
                    case "50d": img.setBackgroundResource(R.drawable.wi50d); break;
                    case "50n": img.setBackgroundResource(R.drawable.wi50n); break;
                    case "01d": img.setBackgroundResource(R.drawable.wi01d); break;
                    case "01n": img.setBackgroundResource(R.drawable.wi01n); break;
                    case "02d": img.setBackgroundResource(R.drawable.wi02d); break;
                    case "02n": img.setBackgroundResource(R.drawable.wi02n); break;
                    case "03d": img.setBackgroundResource(R.drawable.wi03d); break;
                    case "03n": img.setBackgroundResource(R.drawable.wi03n); break;
                    case "04d": img.setBackgroundResource(R.drawable.wi04d); break;
                    case "04n": img.setBackgroundResource(R.drawable.wi04n); break;
                    case "09d": img.setBackgroundResource(R.drawable.wi09d); break;
                    case "09n": img.setBackgroundResource(R.drawable.wi09n); break;
                    case "10d": img.setBackgroundResource(R.drawable.wi10d); break;
                    case "10n": img.setBackgroundResource(R.drawable.wi10n); break;
                    case "11d": img.setBackgroundResource(R.drawable.wi11d); break;
                    case "11n": img.setBackgroundResource(R.drawable.wi11n); break;
                    case "13d": img.setBackgroundResource(R.drawable.wi13d); break;
                    case "13n": img.setBackgroundResource(R.drawable.wi13n); break;
                }
                int counter = 1;
                while(counter<=23) {
                    counter = counter + 1;
                    TextView times = (TextView) findViewById(getResources().getIdentifier("time" + (counter - 1), "id", getPackageName()));
                    TextView tmps = (TextView) findViewById(getResources().getIdentifier("tmp" + (counter - 1), "id", getPackageName()));
                    TextView winds = (TextView) findViewById(getResources().getIdentifier("wind" + (counter - 1), "id", getPackageName()));
                    ImageView imgs = (ImageView) findViewById(getResources().getIdentifier("weathericon" + (counter - 1), "id", getPackageName()));
                    times.setGravity(Gravity.CENTER_HORIZONTAL);
                    tmps.setGravity(Gravity.CENTER_HORIZONTAL);
                    winds.setGravity(Gravity.CENTER_HORIZONTAL);
                    imgs.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
                    Long t = hourlyforecast.getJSONObject(counter - 1).getLong("dt");
                    TimeZone tz = TimeZone.getTimeZone(jsonObj.getString("timezone"));
                    SimpleDateFormat tm = new SimpleDateFormat("HH:mm");
                    tm.setTimeZone(tz);
                    times.setText(tm.format(new Date(t * 1000)));
                    String temps = hourlyforecast.getJSONObject(counter - 1).getInt("temp") + "°C";
                    tmps.setText(temps);
                    winds.setText(hourlyforecast.getJSONObject(counter - 1).getString("wind_speed") + "м/с");
                    switch (hourlyforecast.getJSONObject(counter - 1).getJSONArray("weather").getJSONObject(0).getString("icon")) {
                        case "50d": imgs.setBackgroundResource(R.drawable.wi50d); break;
                        case "50n": imgs.setBackgroundResource(R.drawable.wi50n); break;
                        case "01d": imgs.setBackgroundResource(R.drawable.wi01d); break;
                        case "01n": imgs.setBackgroundResource(R.drawable.wi01n); break;
                        case "02d": imgs.setBackgroundResource(R.drawable.wi02d); break;
                        case "02n": imgs.setBackgroundResource(R.drawable.wi02n); break;
                        case "03d": imgs.setBackgroundResource(R.drawable.wi03d); break;
                        case "03n": imgs.setBackgroundResource(R.drawable.wi03n); break;
                        case "04d": imgs.setBackgroundResource(R.drawable.wi04d); break;
                        case "04n": imgs.setBackgroundResource(R.drawable.wi04n); break;
                        case "09d": imgs.setBackgroundResource(R.drawable.wi09d); break;
                        case "09n": imgs.setBackgroundResource(R.drawable.wi09n); break;
                        case "10d": imgs.setBackgroundResource(R.drawable.wi10d); break;
                        case "10n": imgs.setBackgroundResource(R.drawable.wi10n); break;
                        case "11d": imgs.setBackgroundResource(R.drawable.wi11d); break;
                        case "11n": imgs.setBackgroundResource(R.drawable.wi11n); break;
                        case "13d": imgs.setBackgroundResource(R.drawable.wi13d); break;
                        case "13n": imgs.setBackgroundResource(R.drawable.wi13n); break;
                    }
                }


                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }
}