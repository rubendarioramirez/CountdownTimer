package com.android.rramirez.countdowntimer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends Activity {

    private TextView dayCount, dayText, horaCount, horaText, minCount, minText, segCount, segText;
    private Handler handler;
    private Runnable runnable;
    private boolean alarmaHora;
    private boolean alarmaMinuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Declare UI elements for timer
        dayCount = (TextView) findViewById(R.id.dayCount);
        dayText = (TextView) findViewById(R.id.diaText);
        horaCount = (TextView) findViewById(R.id.horaCount);
        horaText = (TextView) findViewById(R.id.horaText);
        minCount = (TextView) findViewById(R.id.minCount);
        minText = (TextView) findViewById(R.id.minText);
        segCount = (TextView) findViewById(R.id.segCount);
        segText = (TextView) findViewById(R.id.segText);


        //Titulos
        //Crea la font Custom
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(),"fonts/Ubuntu-C.ttf");

        TextView title1 = (TextView) findViewById(R.id.title1);
        title1.setTypeface(myCustomFont);
        TextView title2 = (TextView) findViewById(R.id.title2);
        title2.setTypeface(myCustomFont);
        TextView text1 = (TextView) findViewById(R.id.text1);
        text1.setTypeface(myCustomFont);
        TextView text2 = (TextView) findViewById(R.id.text2);
        text2.setTypeface(myCustomFont);

        //JSON URL
        String url = "http://esteeselfamosoriver.com/app/info.php";

        //If internet is available
        if(isNetworkAvailable(this)){
            makeToast("Informacion actualizada");
            new getFecha(title1,title2,text1,text2).execute(url);

        } else {

            //Create a toast
            makeToast("No se pudo conectar al servidor");

            //Get data offline from stored file and parse it
            String fileToString = readFromFile(this).toString();
            String[] separated = fileToString.split(",");

            //Set titles and countdown
            title1.setText(separated[0]);
            title2.setText(separated[1]);
            text1.setText(separated[2]);
            text2.setText(separated[3]);
            countDownStart(separated[4]);
        }

    }

    //Async task
    private class getFecha extends AsyncTask<String, Void, String> {

        private TextView title1;
        private TextView title2;
        private TextView text1;
        private TextView text2;

        public getFecha(TextView title1, TextView title2, TextView text1, TextView text2) {
            this.title1 = title1;
            this.title2 = title2;
            this.text1 = text1;
            this.text2 = text2;
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
                String fechayhora = titulos.optString("fechayhora");


                titles = firstTitle + "," + secondTitle + "," + thirdTitle + "," + fourthTitle + "," + fechayhora;

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

            //Set titles
            title1.setText(separated[0]);
            title2.setText(separated[1]);
            text1.setText(separated[2]);
            text2.setText(separated[3]);

            //Start countDown with your custom date
            countDownStart(separated[4]);

            //save new data to a file
            saveFile(currentString);


        }
    }


    // //////////////COUNT DOWN START/////////////////////////
    public void countDownStart(String eventDate) {
        final String fetchFecha = eventDate;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    // Here Set your Event Date
                    Date futureDate = dateFormat.parse(fetchFecha);
                    Date currentDate = new Date();
                    if (!currentDate.after(futureDate)) {
                        long diff = futureDate.getTime()
                                - currentDate.getTime();
                        long days = diff / (24 * 60 * 60 * 1000);
                        diff -= days * (24 * 60 * 60 * 1000);
                        long hours = diff / (60 * 60 * 1000);
                        diff -= hours * (60 * 60 * 1000);
                        long minutes = diff/(60*1000);
        diff -= minutes * (60 * 1000);
                        long seconds = diff / 1000;
                        dayCount.setText("" + String.format("%02d", days));
                        horaCount.setText("" + String.format("%02d", hours));
                        minCount.setText("" + String.format("%02d", minutes));
                        segCount.setText("" + String.format("%02d", seconds));


                        Integer remainingDays = Integer.parseInt(dayCount.getText().toString());
                        Integer remainingHours = Integer.parseInt(horaCount.getText().toString());
                        Integer remainingMinutes = Integer.parseInt(minCount.getText().toString());


                        //Change text from singular to plural
                        if(remainingDays<=1){
                            dayText.setText("Dia");
                        } else { dayText.setText("Dias");}

                        if(remainingHours<=2){
                            horaText.setText("Hora");
                        } else { horaText.setText("Horas");}

                        if(remainingMinutes<=1){
                            minText.setText("Minuto");
                        } else { minText.setText("Minutos");}


                        ///////ALARMS //////////
                        //Trigger notification if remains 1 hour.
                        if(remainingHours<=1 && remainingDays==0 && alarmaHora == false){
                            createAlarm();
                            alarmaHora = true;
                        }
                        //Triggers notification if remains 30 minutes or less.
                        if(remainingMinutes<=30 && remainingDays==0 && remainingHours==0 && alarmaMinuto==false){
                            createAlarm();
                            alarmaMinuto = true;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }


    ///Notification set trigger.
    public void createAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

    }

    //Check internet status, both WIFI or 3G
    public static boolean isNetworkAvailable(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null
                    && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null
                        && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }


    public void makeToast(String texto){
        Toast.makeText(this.getBaseContext(),texto, Toast.LENGTH_SHORT).show();
    }


    //Read from the file. File name is hardcoded inside
    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("offlineData");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void saveFile(String currentString){
        String filename = "offlineData";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(currentString.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



