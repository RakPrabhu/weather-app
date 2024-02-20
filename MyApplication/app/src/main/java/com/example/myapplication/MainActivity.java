package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String CITY;
    String API = "80fa52c0929e364573df5e9909c0924a";

    ImageView search;
    EditText etCity;
    TextView city, country, time, temp, forecast, humidity, min_temp, max_temp, sunrises, sunsets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.Your_city);
        search = findViewById(R.id.search);

        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        time = findViewById(R.id.time);
        temp = findViewById(R.id.temp);
        forecast = findViewById(R.id.forecast);
        humidity = findViewById(R.id.humidity);
        min_temp = findViewById(R.id.min_temp);
        max_temp = findViewById(R.id.max_temp);
        sunrises = findViewById(R.id.sunrises);
        sunsets = findViewById(R.id.sunsets);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CITY = etCity.getText().toString();
                new WeatherTask().execute();
            }
        });
    }

    class WeatherTask extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                JSONObject sys = jsonObj.getJSONObject("sys");

                String city_name = jsonObj.getString("name");
                String countryname = sys.getString("country");

                long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Last Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));

                String temperature = main.getString("temp");
                String cast = weather.getString("description");
                String humi_dity = main.getString("humidity");
                String temp_min = main.getString("temp_min");
                String temp_max = main.getString("temp_max");

                long rise = sys.getLong("sunrise");
                String sunrise = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(rise * 1000));

                long set = sys.getLong("sunset");
                String sunset = new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(set * 1000));

                city.setText(city_name);
                country.setText(countryname);
                time.setText(updatedAtText);
                temp.setText(temperature + "°C");
                forecast.setText(cast);
                humidity.setText(humi_dity);
                min_temp.setText(temp_min);
                max_temp.setText(temp_max);
                sunrises.setText(sunrise);
                sunsets.setText(sunset);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
