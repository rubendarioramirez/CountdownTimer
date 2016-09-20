package com.android.rramirez.countdowntimer;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import static com.android.rramirez.countdowntimer.R.drawable.facebook;

public class MainActivity extends Activity {

    private TextView dayCount, dayText, horaCount, horaText, minCount, minText, segCount, segText, riverJugando, designText;

    //Facebook, Twitter and URL buttons
    private ShareButton shareButton;
    private ImageButton twitterBtn, urlBtn;

    private Context mContext;
    private static final String TAG = "BroadcastTest";
    private Intent intent;
    public static String packageName;

    String url = "http://esteeselfamosoriver.com/app/info.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        packageName = getPackageName();

        //Sharing buttons
        shareButton = (ShareButton) findViewById(R.id.share_btn);
        twitterBtn = (ImageButton) findViewById(R.id.twitterBtn);
        urlBtn = (ImageButton) findViewById(R.id.urlBtn);

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
        riverJugando = (TextView) findViewById(R.id.riverJugando);
        designText = (TextView) findViewById(R.id.designText);

        updateTitles();

        //Set hyperLinks
        designText.setText(Html.fromHtml("<b>Design By </b>" + "<a href=\"http://cerebrosdigitales.com\"> " + "CerebrosDigitales" + "</a> "));
        designText.setMovementMethod(LinkMovementMethod.getInstance());

        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postTwitter();
            }
        });

        urlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openURL();
            }
        });



    }

    public void updateTitles(){
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


        //Set the customTypeFace for the texts
        dayCount.setTypeface(myCustomFontNegrita);
        horaCount.setTypeface(myCustomFontNegrita);
        minCount.setTypeface(myCustomFontNegrita);
        segCount.setTypeface(myCustomFontNegrita);
        dayText.setTypeface(myCustomFontNegrita);
        horaText.setTypeface(myCustomFontNegrita);
        minText.setTypeface(myCustomFontNegrita);
        segText.setTypeface(myCustomFontNegrita);
        riverJugando.setTypeface(myCustomFontNegrita);



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //Facebook post
        Bitmap bm = getScreen();
        SharePhoto photo = new SharePhoto.Builder().setBitmap(bm).build();
        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
        shareButton.setBackgroundResource(facebook);
        shareButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        shareButton.setText("");
        shareButton.setShareContent(content);

    }

    public void postTwitter(){
        Bitmap bm = getScreen();
        Uri myUri = getImageUri(mContext, bm);
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("")
                .image(myUri);
        builder.show();
    }

    public void openURL(){
        Uri uri = Uri.parse("http://esteeselfamosoriver.com"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    //Take the screenshot
    private Bitmap getScreen(){
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache();
        // creates immutable clone of image
        Bitmap image = Bitmap.createBitmap(rootView.getDrawingCache());
        // destroy
        rootView.destroyDrawingCache();

        return image;
    }

    //Get the url from the image.
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "screenshot", null);
        return Uri.parse(path);
    }

    //Gets the updates for the CountDown.
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                updateCounter(intent); // or whatever method used to update your GUI fields
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };


    private void updateCounter(Intent intent) throws ParseException {
        if (intent.getExtras() != null) {

            String day = Utility.getString(this, "daysRemaining", "");
            String hour = Utility.getString(this, "hoursRemaining", "");
            String min = Utility.getString(this, "minutesRemaining", "");
            String sec = Utility.getString(this, "secRemaining", "");
            String jugando = Utility.getString(this, "jugando", "");
            dayCount.setText(day);
            horaCount.setText(hour);
            minCount.setText(min);
            segCount.setText(sec);

            int diasFaltan = Integer.parseInt(day);
            int horasFaltan = Integer.parseInt(hour);
            int minFaltan = Integer.parseInt(min);

            if (jugando.equals("true")) {
                setFinalText();
            } else {
                setInitialTexts(diasFaltan, horasFaltan, minFaltan);
            }
        }
    }

    public void setFinalText() {
        horaCount.setText("");
        dayCount.setText("");
        dayText.setText("");
        horaText.setText("");
        minCount.setText("");
        minText.setText("");
        segCount.setText("");
        segText.setText("");
        riverJugando.setText("River esta jugando!");

    }

    public void setInitialTexts(int diasFaltan, int horasFaltan, int minFaltan) {
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
        segText.setText("Segundos");
        riverJugando.setText("");
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
            updateTitles();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
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



