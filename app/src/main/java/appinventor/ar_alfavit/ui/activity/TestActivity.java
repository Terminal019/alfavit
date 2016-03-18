package appinventor.ar_alfavit.ui.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


import appinventor.ar_alfavit.R;
import appinventor.ar_alfavit.adapter.Strings;
import appinventor.ar_alfavit.adapter.TestAdapter;
import appinventor.ar_alfavit.utils.DeviceUtils;

public class TestActivity extends AppCompatActivity {

    private MediaPlayer mPlayLetterTest;//Проигрывает букву
    private MediaPlayer mPlayBackgroundTest; // Фоновая музыка
    private GridView mGridViewTest; // Сетка из букв
    private Random mRandom; // выбирает случайную букву

    private int mIdAudio; // Приобразовывает путь к аудио файлу getResources().getIdentifier(mNameAudio, "raw", getPackageName())
    private int mPositionID; // выдает случайное число из 28 букв
    private int mExit = 1; // число для выхода из теста, в режим обучения

    static public boolean sVerticHorizPosition = true; // Для указаня положения экрана(верткал, горизонт)
    static public int sWidth; // ширина экрана
    static public int sCountVis = 100; // если число меньше 50 то будут последствия

    private ArrayList<String> mAudioList; //
    private ArrayList<String> mPosition; //
    private ArrayList<String> mPositionOriginal; //
    private ArrayList<String> mRusLetter; // отоброжает транскрипцию

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRandom = new Random();

         /*Отключает горизонтальное положение для телефонов*/
        if (!DeviceUtils.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

         /*Для определения ширины букв на экране*/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        sWidth = metrics.widthPixels;

        setLetter(Strings.alfavitViewVertic, Strings.alfavitViewHoriz,
                Strings.alfavitAudioVertic, Strings.alfavitAudioHoriz); //описание в методе
        Toast toast5 = Toast.makeText(getApplicationContext(),
                "Первый уровень!", Toast.LENGTH_LONG);
        toast5.show();
    }


    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            // TODO Сверяет позицию буквы с нажатой кнопкой и выдает один из двух результатов
            if (!mPlayLetterTest.isPlaying()) {
                releaseMP();
                if (mPositionOriginal.get(position) == mPosition.get(mPositionID)) {
                    sCountVis = position;
                    setMediaPlayerYES();

                } else {
                    setMediaPlayerNO();
                }
            }
        }
    };

    void setMediaPlayerYES() {
        if (mPlayLetterTest != null) {
            mPlayLetterTest.release();
        }

        if (mPosition.size() > 1) {
            mPosition.remove(mPositionID);
            mAudioList.remove(mPositionID);
            mRusLetter.remove(mPositionID);
            mPositionID = mRandom.nextInt(mPosition.size());

            /*сообщение правильно. типа угадал*/
            Toast toast3 = Toast.makeText(getApplicationContext(),
                    "Правильно!", Toast.LENGTH_SHORT);
            toast3.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastContainer = (LinearLayout) toast3.getView();
            ImageView catImageView = new ImageView(getApplicationContext());
            catImageView.setImageResource(R.drawable.yes);
            toastContainer.addView(catImageView, 0);
            toast3.show();

            /*Воспоизводит взрыв шара и след букву*/
            mIdAudio = getResources().getIdentifier(mAudioList.get(mPositionID), "raw", getPackageName());
            releaseMP();
            mPlayLetterTest = MediaPlayer.create(TestActivity.this, R.raw.shar1);
            mPlayLetterTest.start();
            mPlayLetterTest.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMP();
                    mPlayLetterTest = MediaPlayer.create(TestActivity.this, mIdAudio);
                    mPlayLetterTest.start();

                }
            });

            /*перезапускает грид чтобы один скрылся*/
            ((BaseAdapter) mGridViewTest.getAdapter()).notifyDataSetInvalidated();

        } else {
            mExit++;
            if (mExit == 2) {
                Toast toast6 = Toast.makeText(getApplicationContext(),
                        "Второй уровень!", Toast.LENGTH_LONG);
                toast6.show();
                sCountVis = 100;
                TestAdapter.sLevel = 2;
                setLetter(Strings.alfavitViewRandomVertic, Strings.alfavitViewRandomHoriz, Strings.alfavitAudioRandomVertic, Strings.alfavitAudioHorizRandom);
            } else if (mExit == 3) {
                Toast toast7 = Toast.makeText(getApplicationContext(),
                        "Третий уровень!", Toast.LENGTH_LONG);
                toast7.show();
                sCountVis = 100;
                TestAdapter.sLevel = 3;
                setLetter(Strings.alfavitViewRandomVertic3, Strings.alfavitViewRandomHoriz3, Strings.alfavitAudioRandomVertic3, Strings.alfavitAudioHorizRandom3);
            } else if (mExit == 4) {
                /*завершает режим теста, с переходом в режим обучения*/
                Toast toast4 = Toast.makeText(getApplicationContext(),
                        "Поздравляем! Вы прошли тест!", Toast.LENGTH_LONG);
                toast4.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastContainer = (LinearLayout) toast4.getView();
                ImageView catImageView = new ImageView(getApplicationContext());
                catImageView.setImageResource(R.drawable.cup);
                toastContainer.addView(catImageView, 0);
                toast4.show();
                if (mPlayBackgroundTest != null) {
                    try {
                        mPlayBackgroundTest.release();
                        mPlayBackgroundTest = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mPlayLetterTest = MediaPlayer.create(TestActivity.this, R.raw.bravo);
                mPlayLetterTest.start();
                onBackPressed();

            }
        }
    }

    void setMediaPlayerNO() {
        /*в случае не правильного ответа*/
        Toast toast3 = Toast.makeText(getApplicationContext(),
                "      Не правильно!\nПокажите букву: " + mRusLetter.get(mPositionID), Toast.LENGTH_SHORT);
        toast3.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContainer = (LinearLayout) toast3.getView();
        ImageView catImageView = new ImageView(getApplicationContext());
        catImageView.setImageResource(R.drawable.no);
        toastContainer.addView(catImageView, 0);
        toast3.show();
        releaseMP();
        mPlayLetterTest = MediaPlayer.create(TestActivity.this, R.raw.oshibka);
        mPlayLetterTest.start();
        mPlayLetterTest.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMP();
                mPlayLetterTest = MediaPlayer.create(TestActivity.this, mIdAudio);
                mPlayLetterTest.start();

            }
        });
    }

    void setLetter(String[] alfavitVert, String[] alfavitHoriz, String[] audioVert, String[] audioHoriz) {

         /*Далее смотря в каком положении экран всавляются буквы и указывается их ширина*/
        mAudioList = new ArrayList<String>();
        mPosition = new ArrayList<String>();
        mRusLetter = new ArrayList<String>();
        mPositionOriginal = new ArrayList<String>();
        mGridViewTest = (GridView) findViewById(R.id.gridview);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            for (int i = 0; i < audioVert.length; i++) {
                mAudioList.add(audioVert[i]);
                mPosition.add(audioVert[i]);
                mRusLetter.add(alfavitVert[i]);
                mPositionOriginal.add(audioVert[i]);
            }
            sVerticHorizPosition = true;
            mGridViewTest.setColumnWidth(sWidth / 5);

        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            for (int i = 0; i < audioHoriz.length; i++) {
                mAudioList.add(audioHoriz[i]);
                mPosition.add(audioHoriz[i]);
                mRusLetter.add(alfavitHoriz[i]);
                mPositionOriginal.add(audioHoriz[i]);
            }
            sVerticHorizPosition = false;
            mGridViewTest.setColumnWidth(sWidth / 8);
        }
        mGridViewTest.setAdapter(new TestAdapter(this));
        mGridViewTest.setOnItemClickListener(gridviewOnItemClickListener);

           /*выбирает случайную букву и говорит указакть ее*/
        mPositionID = mRandom.nextInt(mPosition.size());
        mIdAudio = getResources().getIdentifier(mAudioList.get(mPositionID), "raw", getPackageName());
        releaseMP();
        mPlayLetterTest = MediaPlayer.create(this, R.raw.vopros);
        mPlayLetterTest.start();
        mPlayLetterTest.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseMP();
                mPlayLetterTest = MediaPlayer.create(TestActivity.this, mIdAudio);
                mPlayLetterTest.start();

            }
        });
        mPlayBackgroundTest = MediaPlayer.create(this, R.raw.audio_fon_shum);
        mPlayBackgroundTest.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            /*повтор вопроса*/
            case R.id.replay:
                mPlayLetterTest.start();
                break;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                releaseMP();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные

        if (mPlayBackgroundTest != null) {
            try {
                mPlayBackgroundTest.release();
                mPlayBackgroundTest = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sCountVis = 100;
        TestAdapter.sLevel = 1;
    }

    private void releaseMP() {
        if (mPlayLetterTest != null) {
            try {
                mPlayLetterTest.release();
                mPlayLetterTest = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

