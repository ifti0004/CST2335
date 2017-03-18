package com.example.nadeem.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "WeatherForecast";
    private ProgressBar progressBar;
    private TextView currentTemp;
    private TextView minTemp;
    private TextView maxTemp;
    private ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);


         progressBar = (ProgressBar) findViewById(R.id.weatherProgress);
         progressBar.setVisibility(View.VISIBLE);

         currentTemp = (TextView) findViewById(R.id.weatherCurrentTemp);
         minTemp = (TextView) findViewById(R.id.weatherMinTemp);
         maxTemp = (TextView) findViewById(R.id.weatherMaxTemp);
         weatherImage = (ImageView) findViewById(R.id.weatherImage);


        ForecastQuery forecast = new ForecastQuery();
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
        forecast.execute(urlString);
    }

    //for showing clousy or sunny or raining, building a Bitmap object
    protected static Bitmap getImage(URL url) {
        Log.i(ACTIVITY_NAME, "In getImage for downloading image");
        HttpURLConnection iconConn = null;
        try {
            iconConn = (HttpURLConnection) url.openConnection();
            iconConn.connect();
            int response = iconConn.getResponseCode();
            if (response == 200) {
                return BitmapFactory.decodeStream(iconConn.getInputStream());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (iconConn != null) {
                iconConn.disconnect();
            }
        }
    }

    //check if cloudy, sunny etc images are already present in the local storage directory
    public boolean fileExistance(String fName) {
        Log.i(ACTIVITY_NAME, "In fileExistance for finding weather image locally");
         Log.i(ACTIVITY_NAME, getBaseContext().getFileStreamPath(fName).toString());
        File file = getBaseContext().getFileStreamPath(fName);
        return file.exists();
    }
    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        String min;
        String max;
        String current;
        String iconName;
        Bitmap icon;

        @Override
        protected String doInBackground(String... args) {
            Log.i(ACTIVITY_NAME, "In doInBackground");
            // Given a string representation of a URL, sets up a connection and gets
// an input stream.
            try {
                URL url = new URL(args[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();

                InputStream stream = conn.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (parser.getName().equals("temperature")) {
                        current = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        min = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        max = parser.getAttributeValue(null, "max");
                        publishProgress(75);
                    }
                    if (parser.getName().equals("weather")) {
                        iconName = parser.getAttributeValue(null, "icon");
                        String iconFile = iconName+".png";
                        if (fileExistance(iconFile)) {
                            FileInputStream inputStream = null;
                            try {
                                inputStream = new FileInputStream(getBaseContext().getFileStreamPath(iconFile));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            icon = BitmapFactory.decodeStream(inputStream);
                            Log.i(ACTIVITY_NAME, "Image already exists");
                        } else {
                            URL iconUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                            icon = getImage(iconUrl);
                            FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Log.i(ACTIVITY_NAME, "Downloading new image");
                        }
                       Log.i(ACTIVITY_NAME, "file name="+iconFile);
                        publishProgress(100);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //setting the visibility of progress bar to visible
        @Override
        protected void onProgressUpdate(Integer... value) {
            Log.i(ACTIVITY_NAME, "In onProgressUpdate");
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            String degree = Character.toString((char) 0x00B0);
            currentTemp.setText(currentTemp.getText()+current+degree+"C");
            minTemp.setText(minTemp.getText()+min+degree+"C");
            maxTemp.setText(maxTemp.getText()+max+degree+"C");
            weatherImage.setImageBitmap(icon);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    }


