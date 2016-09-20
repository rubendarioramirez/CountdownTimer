package com.android.rramirez.countdowntimer;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;


public class getFechaTask extends AsyncTask<String, Void, String> {

        private Context mContext;

        public getFechaTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String titles = "UNDEFINED";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                //JSONObject topLevel = new JSONObject(builder.toString());
                JSONObject theArray = new JSONObject(builder.toString());
                JSONArray info = theArray.getJSONArray("info");
                JSONObject titulos = info.getJSONObject(0);

                String firstTitle = titulos.optString("title1");
                String secondTitle = titulos.optString("title2");
                String thirdTitle = titulos.optString("title3");
                String fourthTitle = titulos.optString("title4");
                String notHoraTitle = titulos.optString("notHoraTitle");
                String notHoraContent = titulos.optString("notHoraContent");
                String notMedTitle = titulos.optString("notMedTitle");
                String notMedContent = titulos.optString("notMedContent");
                String notFinalTitle = titulos.optString("notFinalTitle");
                String notFinalContent = titulos.optString("notFinalContent");
                String fechayhora = titulos.optString("fechayhora");


                titles = firstTitle + ","
                          + secondTitle + ","
                          + thirdTitle +  ","
                          + fourthTitle + ","
                          + notHoraTitle+ ","
                          + notHoraContent+ ","
                          + notMedTitle+ ","
                          + notMedContent+ ","
                          + notFinalTitle+ ","
                          + notFinalContent + ","
                          + fechayhora;

                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return titles;
        }

        @Override
        protected void onPostExecute(String titles) {

            //Split the info by commas, self explanatory below
            String currentString = titles;
            String[] separated = currentString.split(",");

            //save new data to a file
            Utility.saveFile(mContext, currentString);

            //Save title in shared Preferences for Widget Usage
            Utility.putString(mContext,"title1",separated[0]);
            Utility.putString(mContext,"title2",separated[1]);
            Utility.putString(mContext,"text1",separated[2]);
            Utility.putString(mContext,"text2",separated[3]);

            //Notification titles
            Utility.putString(mContext,"notHoraTitle",separated[4]);
            Utility.putString(mContext,"notHoraContent",separated[5]);
            Utility.putString(mContext,"notMedTitle",separated[6]);
            Utility.putString(mContext,"notMedContent",separated[7]);
            Utility.putString(mContext,"notFinalTitle",separated[8]);
            Utility.putString(mContext,"notFinalContent",separated[9]);

            //Date
            Utility.putString(mContext,"fecha",separated[10]);

        }
    }