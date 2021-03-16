package com.cst2335.abc040963564;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
public class WeatherForecast extends AppCompatActivity {


    ImageView wicon;
    TextView curtemp;
    TextView mintemp;
    TextView maxtemp;
    TextView uvrating;
    ProgressBar progressBar;
    Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        wicon = findViewById(R.id.imageView);
        curtemp = findViewById(R.id.currenttime);
        mintemp = findViewById(R.id.min);
        maxtemp = findViewById(R.id.max);
        uvrating = findViewById(R.id.uv);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE );
        ForecastQuery fcq = new ForecastQuery();
        fcq.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric",
                "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");  //Type 1
    }
                                                 //Type1     Type2   Type3
    private class ForecastQuery extends AsyncTask< String, Integer, String>
    {
        Bitmap image = null;
        String sUV, smin, smax, scurtemp;

        //Type3                Type1
        protected String doInBackground(String ... args)
        {
            try {
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

//                int eventType = xpp.getEventType();
//                while(eventType!=XmlPullParser.END_DOCUMENT){}

                //From part 3, slide 20

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature"))
                        {
                            //If you get here, then you are pointing to a <Weather> start tag
                            scurtemp = xpp.getAttributeValue(null,    "value");
                            publishProgress(25);

                            smin = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            smax = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
//                            String icon = xpp.getAttributeValue(null, "iconName ");

                        }
                        else if(xpp.getName().equals("weather")) {
                            String iconName = null;
                            HttpsURLConnection connection;
                            iconName = xpp.getAttributeValue(null, "icon"); //this will run for <Weather outlook="parameter"

                            if(!fileExistance(iconName)){
//                            parameter = xpp.getAttributeValue(null, "windy"); //this will run for <Weather windy="paramter"  >
//                           9.1 Bitmap image = null;

                                URL wurl = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                                connection = (HttpsURLConnection) wurl.openConnection();
                                connection.connect();
                                int responseCode = connection.getResponseCode();

                                if (responseCode == 200) {
                                    image = BitmapFactory.decodeStream(connection.getInputStream());
                                }
                                //9.2
                                FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();

//p9.3
                            }else {
                                FileInputStream fis = null;
                                try {    fis = openFileInput(iconName + ".png");   }

                                catch (FileNotFoundException e) {    e.printStackTrace();  }
                                image = BitmapFactory.decodeStream(fis);
                            }
                            publishProgress(100);
                        }

                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }
            }
            catch (Exception e)
            {
            }

            try {
                //create a URL object of what server to contact:
                URL url2 = new URL(args[1]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url2.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                sUV = String.valueOf(uvReport.getDouble("value"));
//                toString()
            }
            catch (Exception e)
            {
            }

            return "Done";
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }
        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            Log.i("HTTP", "in onProgressUpdate");
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            wicon.setImageBitmap(image);
            mintemp.setText("Minimum tempreture:"+smin);
            maxtemp.setText("Maximum tempreture:"+smax);
            curtemp.setText("current tempreture:"+scurtemp);
            uvrating.setText("UV rating:"+sUV);
            Log.i("HTTP", fromDoInBackground);
            progressBar.setVisibility(View.INVISIBLE );
        }
    }
}
