package com.android.rramirez.countdowntimer;

import java.text.ParseException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;


public class MainActivity extends Activity {

    private TextView dayCount, dayText, horaCount, horaText, minCount, minText, segCount, segText, riverJuega;
    private Context mContext;
    private static final String TAG = "BroadcastTest";
    private Intent intent;
    public static String packageName;

    //Facebook Elements
    // share button
    private ShareButton shareButton;
    //image
    private Bitmap image;
    //counter
    private int counter = 0;

    String url = "http://esteeselfamosoriver.com/app/info.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Initialize SDK before inflating the layout
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        packageName = getPackageName();

        //Get the intent from the service
        intent = new Intent(this, TimerService.class);

        //Declare UI elements for MainActivity
        dayCount = (TextView) findViewById(R.id.dayCount);
        dayText = (TextView) findViewById(R.id.diaText);
        horaCount = (TextView) findViewById(R.id.horaCount);
        horaText = (TextView) findViewById(R.id.horaText);
        minCount = (TextView) findViewById(R.id.minCount);
        minText = (TextView) findViewById(R.id.minText);
        segCount = (TextView) findViewById(R.id.segCount);
        segText = (TextView) findViewById(R.id.segText);
        riverJuega = (TextView) findViewById(R.id.riverJuega);
        //Crea la font Custom and assign to the titles
        Typeface myCustomFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Ubuntu-C.ttf");
        Typeface myCustomFontNegrita = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Ubuntu-B.ttf");
        TextView title1 = (TextView) findViewById(R.id.title1);
        title1.setTypeface(myCustomFontNegrita);
        TextView title2 = (TextView) findViewById(R.id.title2);
        title2.setTypeface(myCustomFontNegrita);
        TextView text1 = (TextView) findViewById(R.id.text1);
        text1.setTypeface(myCustomFont);
        TextView text2 = (TextView) findViewById(R.id.text2);
        text2.setTypeface(myCustomFont);

        dayCount.setTypeface(myCustomFontNegrita);
        horaCount.setTypeface(myCustomFontNegrita);
        minCount.setTypeface(myCustomFontNegrita);
        segCount.setTypeface(myCustomFontNegrita);
        dayText.setTypeface(myCustomFontNegrita);
        horaText.setTypeface(myCustomFontNegrita);
        minText.setTypeface(myCustomFontNegrita);
        segText.setTypeface(myCustomFontNegrita);


        //define River with red V
        String fetchTitle1 = Utility.getString(mContext, "title1", "");
        if (fetchTitle1.equals("RIVER")) {
            String text = "<font color=white>RI</font><font color=#cc0029>V</font><font color=white>ER</font>";
            title1.setText(Html.fromHtml(text));
        } else {
            title1.setText(fetchTitle1);
        }

        String fetchTitle2 = Utility.getString(mContext, "title2", "");
        if (fetchTitle2.equals("RIVER")) {
            String text = "<font color=white>RI</font><font color=#cc0029>V</font><font color=white>ER</font>";
            title2.setText(Html.fromHtml(text));
        } else {
            title2.setText(fetchTitle2);
        }

        //Get and Set the titles
        String fetchText1 = Utility.getString(mContext, "text1", "");
        String fetchText2 = Utility.getString(mContext, "text2", "");
        text1.setText(fetchText1);
        text2.setText(fetchText2);

        //share button
        shareButton = (ShareButton) findViewById(R.id.share_btn);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        shareButton.setShareContent(content);


        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Utility.makeToast(mContext,"tocado");
                postPicture();
            }
        });

    }

    //Gets the updates for the CountDown.
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                updateGUI(intent); // or whatever method used to update your GUI fields
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };


    //Post picture in facebook
    public void postPicture() {
        //check counter
        if(counter == 0) {
            //save the screenshot
            View rootView = findViewById(android.R.id.content).getRootView();
            rootView.setDrawingCacheEnabled(true);
            // creates immutable clone of image
            image = Bitmap.createBitmap(rootView.getDrawingCache());
            // destroy
            rootView.destroyDrawingCache();

            //share dialog
            AlertDialog.Builder shareDialog = new AlertDialog.Builder(this);
            shareDialog.setTitle("Share Screen Shot");
            shareDialog.setMessage("Share image to Facebook?");
            shareDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //share the image to Facebook
                    SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
                    SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                    shareButton.setShareContent(content);
                    counter = 1;
                    shareButton.performClick();
                }
            });
            shareDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            shareDialog.show();
        }
        else {
            counter = 0;
            shareButton.setShareContent(null);
        }
    }

    private void updateGUI(Intent intent) throws ParseException {
        if (intent.getExtras() != null) {

            String day = Utility.getString(this, "daysRemaining", "");
            String hour = Utility.getString(this, "hoursRemaining", "");
            String min = Utility.getString(this, "minutesRemaining", "");
            String sec = Utility.getString(this, "secRemaining", "");
            dayCount.setText(day);
            horaCount.setText(hour);
            minCount.setText(min);
            segCount.setText(sec);

            int diasFaltan = Integer.parseInt(day);
            int horasFaltan = Integer.parseInt(hour);
            int minFaltan = Integer.parseInt(min);

            if (diasFaltan <= 1) {
                dayText.setText("Día");
            } else {
                dayText.setText("Días");
            }

            if (horasFaltan <= 1) {
                horaText.setText("Hora");
            } else {
                horaText.setText("Horas");
            }

            if (minFaltan <= 1) {
                minText.setText("Minuto");
            } else {
                minText.setText("Minutos");
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(TimerService.COUNTDOWN_BR));
        boolean internet = Utility.isNetworkAvailable(this);
        if (!internet) {
            Utility.makeToast(this, "No se pudo conectar al servidor");
        } else {
            //Start service to fetchData
            new getFechaTask(this.getApplicationContext()).execute(url);
        }

        Log.i(TAG, "Registered broacast receiver");

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.i(TAG, "Unregistered broacast receiver");
    }

    @Override
    public void onStop() {
        try {
           unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}



