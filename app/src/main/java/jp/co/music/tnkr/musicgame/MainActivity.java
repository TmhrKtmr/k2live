package jp.co.music.tnkr.musicgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity {

    private SoundPool mSoundPool;
    private int tapSound;

    private MediaPlayer gameBgm;

    private Timer mainTimer;                  //タイマー用
    private MainTimerTask mainTimerTask;      //タイマタスククラス
    private TextView countText1;              //テキストビュー
    private double count = 0.00;              //カウント
    private Handler mHandler = new Handler(); //UI Threadへのpost用ハンドラ
    private double musicTime = 33.00;         //曲の時間
    //判定カウント
    public TextView countText2;
    public TextView countText3;
    public TextView countText4;
    private int perfectCount = 0;
    private int goodCount = 0;
    private int missCount = 0;

    private Animation outAnimation;
    private ImageView judgeImage;
    private ImageView missImage;
    private ImageView goodImage;

    public double[] PERFECT_1 = {3.00, 6.00, 9.00, 12.00, 15.00, 18.00, 21.00, 24.00};
    public double[] PERFECT_2 = {5.00, 10.00, 15.00, 20.00, 25.00};
    public double[] PERFECT_3 = {9.00, 18.00, 27.00};
    public double[] PERFECT_4 = {7.00, 14.00, 21.00, 28.00};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //開始時ボタンを押せなくする
        findViewById(R.id.button1).setEnabled(false);
        findViewById(R.id.button2).setEnabled(false);
        findViewById(R.id.button3).setEnabled(false);
        findViewById(R.id.button4).setEnabled(false);

        //音まわり
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        tapSound = mSoundPool.load(this, R.raw.sd_1, 1);
        gameBgm = MediaPlayer.create(this, R.raw.piano08);// BGMファイルを読み込み

        //タイマー
        this.mainTimer = new Timer();
        //タスク
        this.mainTimerTask = new MainTimerTask();

        //テキストビュー
        this.countText1 = (TextView) findViewById(R.id.count_text1);
        this.countText2 = (TextView) findViewById(R.id.count_perfect);
        this.countText3 = (TextView) findViewById(R.id.count_good);
        this.countText4 = (TextView) findViewById(R.id.count_miss);

        //判定表示
        this.judgeImage = (ImageView) findViewById(R.id.judge_image);
        this.goodImage = (ImageView) findViewById(R.id.good_image);
        this.missImage = (ImageView) findViewById(R.id.miss_image);

        findViewById(R.id.judge_image).setVisibility(View.INVISIBLE);
        findViewById(R.id.good_image).setVisibility(View.INVISIBLE);
        findViewById(R.id.miss_image).setVisibility(View.INVISIBLE);
        outAnimation = AnimationUtils.loadAnimation(this, R.anim.judgement);
    }

    public void gameStart(View v) {
        findViewById(R.id.button1).setEnabled(true);
        findViewById(R.id.button2).setEnabled(true);
        findViewById(R.id.button3).setEnabled(true);
        findViewById(R.id.button4).setEnabled(true);

        findViewById(R.id.startButton).setVisibility(View.INVISIBLE);

        gameBgm.seekTo(0); // 再生位置を0ミリ秒に指定
        gameBgm.start(); // 再生開始

        // 画面サイズ取得
        WindowManager wm = getWindowManager();
        Display dsp = wm.getDefaultDisplay();
        Point size = new Point();
        dsp.getSize(size);

        //基準画面サイズ
//    size.x == 1280 && size.y == 720

        ImageView redBtn = (ImageView) findViewById(R.id.imageView1);
//      TranslateAnimation moveToR = new TranslateAnimation(0, -560, 0, 190);//x原点,x移動先,y原点,y移動先
        TranslateAnimation moveToR = new TranslateAnimation(0, -(size.x / 2.29f), 0, size.y / 3.79f);//x原点,x移動先,y原点,y移動先
        moveToR.setDuration(3000);
        redBtn.startAnimation(moveToR);
        moveToR.setRepeatCount(7);

        ImageView greenBtn = (ImageView) findViewById(R.id.imageView2);
//          TranslateAnimation moveToG = new TranslateAnimation(0, -460, 0, 450);
        TranslateAnimation moveToG = new TranslateAnimation(0, -(size.x / 2.78f), 0, size.y / 1.6f);
        moveToG.setStartOffset(2000);   //時間差
        moveToG.setDuration(3000);
        greenBtn.startAnimation(moveToG);
        moveToG.setRepeatCount(4);

        ImageView blueBtn = (ImageView) findViewById(R.id.imageView3);
//      TranslateAnimation moveToB = new TranslateAnimation(0, 460, 0, 450);
        TranslateAnimation moveToB = new TranslateAnimation(0, size.x / 2.78f, 0, size.y / 1.6f);
        moveToB.setStartOffset(6000);
        moveToB.setDuration(3000);
        blueBtn.startAnimation(moveToB);
        moveToB.setRepeatCount(2);

        ImageView yellowBtn = (ImageView) findViewById(R.id.imageView4);
//      TranslateAnimation moveToY = new TranslateAnimation(0, 560, 0, 190);
        TranslateAnimation moveToY = new TranslateAnimation(0, size.x / 2.29f, 0, size.y / 3.79f);
        moveToY.setStartOffset(4000);
        moveToY.setDuration(3000);
        yellowBtn.startAnimation(moveToY);
        moveToY.setRepeatCount(3);

        //タイマースケジュール設定＆開始
        this.mainTimer.schedule(mainTimerTask, 0, 1000);//押して開始するまで、カウントの間隔
    }

    public void padTouch(View v) {

        int[] buttons = {R.id.button1, R.id.button2, R.id.button3, R.id.button4};
        double[][] perfects = new double[][]{PERFECT_1, PERFECT_2, PERFECT_3, PERFECT_4};

        for (int i = 0; i < buttons.length; i++) {//buttonsとperfectsの数が同じなので
            if (v.getId() == buttons[i]) {
                TapResult result = checkTapResult(count, perfects[i]);
                if (judgeImage.getVisibility() == View.INVISIBLE) {

                    if (result == TapResult.PERFECT) {
                        judgeImage.setVisibility(View.VISIBLE);
                        goodImage.setVisibility(View.INVISIBLE);
                        missImage.setVisibility(View.INVISIBLE);
                        findViewById(buttons[i]).setEnabled(true);
                        mSoundPool.play(tapSound, 1.0F, 1.0F, 0, 0, 1.0F);
                        judgeImage.startAnimation(outAnimation);
                        perfectCount++;
                        countText2.setText(String.valueOf(perfectCount));
                    }
                    judgeImage.setVisibility(View.INVISIBLE);

                    if (result == TapResult.GOOD) {
                        goodImage.setVisibility(View.VISIBLE);
                        judgeImage.setVisibility(View.INVISIBLE);
                        missImage.setVisibility(View.INVISIBLE);
                        findViewById(buttons[i]).setEnabled(true);
                        mSoundPool.play(tapSound, 1.0F, 1.0F, 0, 0, 1.0F);
                        goodImage.startAnimation(outAnimation);
                        goodCount++;
                        countText3.setText(String.valueOf(goodCount));
                    }
                    goodImage.setVisibility(View.INVISIBLE);

                    if (result == TapResult.MISS) {
                        missImage.setVisibility(View.VISIBLE);
                        judgeImage.setVisibility(View.INVISIBLE);
                        goodImage.setVisibility(View.INVISIBLE);
                        findViewById(buttons[i]).setEnabled(true);
                        mSoundPool.play(tapSound, 1.0F, 1.0F, 0, 0, 1.0F);
                        missImage.startAnimation(outAnimation);
                        missCount++;
                        countText4.setText(String.valueOf(missCount));
                    }
                    missImage.setVisibility(View.INVISIBLE);
                } else {
                    findViewById(buttons[i]).setEnabled(false);
                    judgeImage.setVisibility(View.INVISIBLE);
                    missImage.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private TapResult checkTapResult(double count, double[] array) {
        if (checkTiming(count, array, 0.50)) return TapResult.PERFECT;
        if (checkTiming(count, array, 1.00)) return TapResult.GOOD;
        if (checkTiming(count, array, 2.00)) return TapResult.MISS;
        return TapResult.BLANK;
    }

    private boolean checkTiming(double count, double[] array, double dx) {
        for (double d : array) {
            if (d - dx <= count && count <= d + dx) {
                return true;
            }
        }
        return false;
    }

    /**
     * タイマータスク派生クラス
     * run()に定周期で処理したい内容を記述
     */
    public class MainTimerTask extends TimerTask {
        @Override
        public void run() {
            //ここに定周期で実行したい処理を記述
            mHandler.post(new Runnable() {
                public void run() {
                    //実行間隔分を加算処理
                    count += 1.0;
                    //画面にカウントを表示
                    countText1.setText(String.valueOf(count));

                    if (missCount >= 5) {
                        gameOver();
                    }
                    musicTime--;
                    if (musicTime <= 0.0) {
                        gameClear();
                    }
                }
            });
        }
    }

    public void gameOver() {
        mainTimer.cancel();
        mainTimer = null;
        Intent intent = new Intent(this, GameOverActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    private void gameClear() {
        mainTimer.cancel();
        mainTimer = null;
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("pCount", perfectCount);
        intent.putExtra("gCount", goodCount);
        intent.putExtra("mCount", missCount);
        startActivity(intent);
        MainActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundPool.release();
        gameBgm.stop(); // プレイ中のBGMを停止する
        gameBgm.release();
    }

    //戻るボタンのダイアログ
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("kitamuLive")
                    .setMessage("アプリケーションを終了してよろしいですか？")
                    .setPositiveButton("終了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mainTimer.cancel();
                            mSoundPool.release();
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
