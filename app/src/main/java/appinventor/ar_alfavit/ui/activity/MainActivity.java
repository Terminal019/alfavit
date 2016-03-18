package appinventor.ar_alfavit.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import appinventor.ar_alfavit.R;
import appinventor.ar_alfavit.adapter.AlfavitAdapter;
import appinventor.ar_alfavit.adapter.Strings;
import appinventor.ar_alfavit.utils.DeviceUtils;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mAudioList = new  ArrayList<>(); // будет приобразововать из массива стринг в аррайлист
    private MediaPlayer mPlayLetter;  //Проигрывает букву
    private MediaPlayer mPlayBackground; // Фоновая музыка
    private GridView gridViewLetter; // Сетка из букв
    private String mNameAudio; // Передаёт имя аудио файла
    private int mIdAudio; // Приобразовывает путь к аудио файлу getResources().getIdentifier(mNameAudio, "raw", getPackageName())

    static public boolean sVerticHorizPosition = true; // Для указаня положения экрана(верткал, горизонт)
    static public int sWidthLetter; // Для определения ширины букв на экране

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        /*Отключает горизонтальное положение для телефонов*/
        if (!DeviceUtils.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        /*Для определения ширины букв на экране*/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        sWidthLetter = metrics.widthPixels;

        /*Запускаем фоновую мелодию*/
        mPlayBackground = MediaPlayer.create(this,R.raw.audio_fon_shum);
        mPlayBackground.start();

        /*Далее смотря в каком положении экран всавляются буквы и указывается их ширина*/
        gridViewLetter = (GridView) findViewById(R.id.gridview);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            for (int i = 0; i < Strings.alfavitTtainingAudioVertic.length; i++) {
                mAudioList.add(Strings.alfavitTtainingAudioVertic[i]);}
        sVerticHorizPosition = true;
        gridViewLetter.setColumnWidth(sWidthLetter /5);
        }
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            for (int i = 0; i < Strings.alfavitTtainingAudioHoriz.length; i++) {
                mAudioList.add(Strings.alfavitTtainingAudioHoriz[i]);}
        gridViewLetter.setColumnWidth(sWidthLetter /8);
        sVerticHorizPosition = false;
        }
        gridViewLetter.setAdapter(new AlfavitAdapter(this));
        gridViewLetter.setOnItemClickListener(gridviewOnItemClickListener);
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            // TODO При нажатии по букве, воспроизводится аудио файл
            releaseMP();
            mNameAudio = mAudioList.get(position);
            mIdAudio = getResources().getIdentifier(mNameAudio, "raw", getPackageName());
            mPlayLetter = MediaPlayer.create(MainActivity.this, mIdAudio);
            mPlayLetter.start();
        }
    };

    /*Для перехода в плей маркет*/
    private boolean isActivityStarted(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
    /*Для перехода в плей маркет*/
    public void onClickRateThisApp(MenuItem v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=appinventor.ai_avtocar9525.alfavit20"));
        if (!isActivityStarted(intent)) {
            intent.setData(Uri
                    .parse("https://play.google.com/store/apps/details?id=appinventor.ai_avtocar9525.alfavit20"));
            if (!isActivityStarted(intent)) {
                Toast.makeText(
                        this,
                        "Could not open Android market, please check if the market app installed or not. Try again later",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onClickRateThisApp(item);
            return true;
        }
        switch (id) {
            case R.id.test:
                Intent intent = new Intent(this, TestActivity.class); /*переход на активити с тестом*/
                startActivity(intent);
                break;}

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                break;}
        return super.onOptionsItemSelected(item);
    }


    private void releaseMP() {
        if (mPlayLetter != null) {
            try {
                mPlayLetter.release();
                mPlayLetter = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        // Вырубаем аудио файл
        releaseMP();
        if (mPlayBackground != null) {
            try {
                mPlayBackground.release();
                mPlayBackground = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }

}
