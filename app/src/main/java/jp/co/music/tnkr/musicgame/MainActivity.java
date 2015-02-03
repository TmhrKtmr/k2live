package jp.co.music.tnkr.musicgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity {

    private double[] PERFECT_1 = {3.0, 6.0, 9.0, 12.0, 15.0, 18.0, 21.0, 24.0};
    private double[] PERFECT_2 = { 5.0, 10.0, 15.0, 20.0, 25.0 };
    private double[] PERFECT_3 = { 9.0, 18.0, 27.0 };
    private double[] PERFECT_4 = { 7.0, 14.0, 21.0, 28.0 };

    private SoundPool mSoundPool;
    private int mSoundID1;
    private int mSoundID2;
    private int mSoundID3;
    private int mSoundID4;

    private MediaPlayer gameBgm;

    private Timer mainTimer;                 //タイマー用
    private MainTimerTask mainTimerTask;     //タイマタスククラス
    private TextView countText1;             //テキストビュー
    private int count = 0;                    //カウント
    private Handler mHandler = new Handler(); //UI Threadへのpost用ハンドラ
    private int musicTime = 33;//曲の時間

    private Animation outAnimation;
    private ImageView judgeImage;
    private ImageView missImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //開始時ボタンを押せなくする
        findViewById(R.id.button1).setEnabled(false);
        findViewById(R.id.button2).setEnabled(false);
        findViewById(R.id.button3).setEnabled(false);
        findViewById(R.id.button4).setEnabled(false);

        //効果音
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundID1 = mSoundPool.load(this, R.raw.sd_1, 1);
        mSoundID2 = mSoundPool.load(this, R.raw.sd_1, 1);
        mSoundID3 = mSoundPool.load(this, R.raw.sd_1, 1);
        mSoundID4 = mSoundPool.load(this, R.raw.sd_1, 1);

        //BGM
        gameBgm = MediaPlayer.create(this, R.raw.piano08);// BGMファイルを読み込み

        //タイマーインスタンス生成
        this.mainTimer = new Timer();
        //タスククラスインスタンス生成
        this.mainTimerTask = new MainTimerTask();

        //テキストビュー
        this.countText1 = (TextView) findViewById(R.id.count_text1);

        //判定表示
        this.judgeImage = (ImageView) findViewById(R.id.judge_image);
        this.missImage = (ImageView) findViewById(R.id.miss_image);
        findViewById(R.id.judge_image).setVisibility(View.INVISIBLE);
        findViewById(R.id.miss_image).setVisibility(View.INVISIBLE);
        outAnimation = (Animation) AnimationUtils.loadAnimation(this, R.anim.out_animation);

    }

    public void onStart(View v) {

        findViewById(R.id.button1).setEnabled(true);
        findViewById(R.id.button2).setEnabled(true);
        findViewById(R.id.button3).setEnabled(true);
        findViewById(R.id.button4).setEnabled(true);

        findViewById(R.id.startButton).setVisibility(View.INVISIBLE);

        gameBgm.seekTo(0); // 再生位置を0ミリ秒に指定
        gameBgm.start(); // 再生開始

        //赤
        ImageView iv1 = (ImageView) findViewById(R.id.imageView1);
//        TranslateAnimation animation_translate1 = new TranslateAnimation(0, -655, 0, 395);//x原点,x移動先,y原点,y移動先
        TranslateAnimation animation_translate1 = new TranslateAnimation(0, -560, 0, 330);
        animation_translate1.setDuration(3000);
        iv1.startAnimation(animation_translate1);
        animation_translate1.setRepeatCount(7);

        //緑
        ImageView iv2 = (ImageView) findViewById(R.id.imageView2);
        TranslateAnimation animation_translate2 = new TranslateAnimation(0, -460, 0, 450);
        animation_translate2.setStartOffset(2000);
        animation_translate2.setDuration(3000);
        iv2.startAnimation(animation_translate2);
        animation_translate2.setRepeatCount(4);

        //青
        ImageView iv3 = (ImageView) findViewById(R.id.imageView3);
        TranslateAnimation animation_translate3 = new TranslateAnimation(0, 460, 0, 450);
        animation_translate3.setStartOffset(6000);
        animation_translate3.setDuration(3000);
        iv3.startAnimation(animation_translate3);
        animation_translate3.setRepeatCount(2);

        //黄
        ImageView iv4 = (ImageView) findViewById(R.id.imageView4);
        TranslateAnimation animation_translate4 = new TranslateAnimation(0, 560, 0, 330);
        animation_translate4.setStartOffset(4000);
        animation_translate4.setDuration(3000);
        iv4.startAnimation(animation_translate4);
        animation_translate4.setRepeatCount(3);

        //タイマースケジュール設定＆開始
        this.mainTimer.schedule(mainTimerTask, 0, 1000);//押して開始するまで、カウントの間隔
    }

    public void padTouch(View v) {

        //int[] buttons  = {R.id.button1, R.id.button2, R.id.button3, R.id.button4 };
        //int[] perfects = { PERFECT_1,  PERFECT_2,  PERFECT_3,  PERFECT_4 };

        if (v.getId() == R.id.button1) {
            TapResult result = checkTapResult(count, PERFECT_1);
            if (judgeImage.getVisibility() == View.INVISIBLE) {

                if (result == TapResult.PERFECT) {
                    judgeImage.setVisibility(View.VISIBLE);
                    missImage.setVisibility(View.INVISIBLE);
                    findViewById(R.id.button1).setEnabled(true);
                    mSoundPool.play(mSoundID1, 1.0F, 1.0F, 0, 0, 1.0F);
                    judgeImage.startAnimation(outAnimation);
                }
                judgeImage.setVisibility(View.INVISIBLE);

                if (result == TapResult.MISS) {
                    missImage.setVisibility(View.VISIBLE);
                    judgeImage.setVisibility(View.INVISIBLE);
                    findViewById(R.id.button1).setEnabled(true);
                    mSoundPool.play(mSoundID1, 1.0F, 1.0F, 0, 0, 1.0F);
                    missImage.startAnimation(outAnimation);
                }
                missImage.setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.button1).setEnabled(false);
                judgeImage.setVisibility(View.INVISIBLE);
                missImage.setVisibility(View.INVISIBLE);
            }
        }
        //Log.i("count",count+"");

        if (v.getId() == R.id.button2) {
            TapResult result = checkTapResult(count, PERFECT_2);
            if (judgeImage.getVisibility() == View.INVISIBLE) {

                if (result == TapResult.PERFECT) {
                    judgeImage.setVisibility(View.VISIBLE);
                    missImage.setVisibility(View.INVISIBLE);
                    findViewById(R.id.button2).setEnabled(true);
                    mSoundPool.play(mSoundID2, 1.0F, 1.0F, 0, 0, 1.0F);
                    judgeImage.startAnimation(outAnimation);
                }
                judgeImage.setVisibility(View.INVISIBLE);

                if (result == TapResult.MISS) {
                    missImage.setVisibility(View.VISIBLE);
                    judgeImage.setVisibility(View.INVISIBLE);
                    findViewById(R.id.button2).setEnabled(true);
                    mSoundPool.play(mSoundID2, 1.0F, 1.0F, 0, 0, 1.0F);
                    missImage.startAnimation(outAnimation);
                }
                missImage.setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.button2).setEnabled(false);
                judgeImage.setVisibility(View.INVISIBLE);
                missImage.setVisibility(View.INVISIBLE);
            }
        }

        if (v.getId() == R.id.button3) {
            TapResult result = checkTapResult(count, PERFECT_3);
            if (judgeImage.getVisibility() == View.INVISIBLE) {

                if (result == TapResult.PERFECT) {
                    judgeImage.setVisibility(View.VISIBLE);
                    missImage.setVisibility(View.INVISIBLE);
                    findViewById(R.id.button3).setEnabled(true);
                    mSoundPool.play(mSoundID3, 1.0F, 1.0F, 0, 0, 1.0F);
                    judgeImage.startAnimation(outAnimation);
                }
                judgeImage.setVisibility(View.INVISIBLE);

                if (result == TapResult.MISS) {
                    missImage.setVisibility(View.VISIBLE);
                    judgeImage.setVisibility(View.INVISIBLE);
                    findViewById(R.id.button3).setEnabled(true);
                    mSoundPool.play(mSoundID3, 1.0F, 1.0F, 0, 0, 1.0F);
                    missImage.startAnimation(outAnimation);
                }
                missImage.setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.button3).setEnabled(false);
                judgeImage.setVisibility(View.INVISIBLE);
                missImage.setVisibility(View.INVISIBLE);
            }
        }

        if (v.getId() == R.id.button4) {
            TapResult result = checkTapResult(count, PERFECT_4);
            if (judgeImage.getVisibility() == View.INVISIBLE) {

                if (result == TapResult.PERFECT) {
                    judgeImage.setVisibility(View.VISIBLE);
                    missImage.setVisibility(View.INVISIBLE);
                    findViewById(R.id.button4).setEnabled(true);
                    mSoundPool.play(mSoundID4, 1.0F, 1.0F, 0, 0, 1.0F);
                    judgeImage.startAnimation(outAnimation);
                }
                judgeImage.setVisibility(View.INVISIBLE);

                if (result == TapResult.MISS) {
                    missImage.setVisibility(View.VISIBLE);
                    judgeImage.setVisibility(View.INVISIBLE);
                    findViewById(R.id.button4).setEnabled(true);
                    mSoundPool.play(mSoundID4, 1.0F, 1.0F, 0, 0, 1.0F);
                    missImage.startAnimation(outAnimation);
                }
                missImage.setVisibility(View.INVISIBLE);
            } else {
                findViewById(R.id.button4).setEnabled(false);
                judgeImage.setVisibility(View.INVISIBLE);
                missImage.setVisibility(View.INVISIBLE);
            }
        }
    }

    private TapResult checkTapResult(int count, double[] array) {
        if (checkTiming(count, array, 0.01)) return TapResult.PERFECT;
        if (checkTiming(count, array, 1.0)) return TapResult.MISS;
        return TapResult.BLANK;
    }

    private boolean checkTiming(int count, double[] array, double dx) {
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
            //ここに定周期で実行したい処理を記述します
            mHandler.post(new Runnable() {
                public void run() {

                    //実行間隔分を加算処理
                    count += 1.0;
                    //画面にカウントを表示
                    countText1.setText(String.valueOf(count));

                    musicTime--;
                    if (musicTime <= 0) {
                        musicEnd();
                    }
                }
            });
        }
    }
    private void musicEnd() {
        mainTimer.cancel();
        mainTimer = null;
        Intent intent = new Intent(this,ResultActivity.class);
        startActivity(intent);
        MainActivity.this.finish();

    }
//    @Override
//    protected void onPause(){
//        super.onPause();
//        if (gameBgm.isPlaying()){
//            mSoundPool.release();
//            gameBgm.pause();
//            gameBgm.release();
//            Toast.makeText(MainActivity.this, "ゲームを中断しました", Toast.LENGTH_SHORT).show();
//        }
//    }

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
                    .setTitle("アプリケーションの終了")
                    .setMessage("アプリケーションを終了してよろしいですか？")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


//        AnimationSet set = new AnimationSet(true);
//
//        AlphaAnimation alpha = new AlphaAnimation(0.1f, 1f);//透明度(開始透明度、終了透明度)
//        RotateAnimation rotate = new RotateAnimation(0, 720, 0, 0);//回転(開始角度、回転角度、回転中心X軸、回転中心Y軸)
//        ScaleAnimation scale = new ScaleAnimation(0.1f, 1, 0.1f, 1);//サイズ()
//        TranslateAnimation translate = new TranslateAnimation(0, 0, -200, 0);//移動()
//
//        set.addAnimation(alpha);
//        set.addAnimation(rotate);
//        set.addAnimation(scale);
//        set.addAnimation(translate);
//        set.setDuration(2500);
//        v.startAnimation(set);

//赤
//        ObjectAnimator animX = ObjectAnimator.ofFloat(iv1, "x", 30f);
//        ObjectAnimator animY = ObjectAnimator.ofFloat(iv1, "y", 320f);
//        AnimatorSet animSetXY = new AnimatorSet();
//        animSetXY.playTogether(animX, animY);
//        animSetXY.setDuration(4000);
//        animSetXY.start();

//緑
//        ObjectAnimator animX2 = ObjectAnimator.ofFloat(iv2, "x", 130f);
//        ObjectAnimator animY2 = ObjectAnimator.ofFloat(iv2, "y", 440f);
//        AnimatorSet animSetXY2 = new AnimatorSet();
//        animSetXY2.playTogether(animX2, animY2);
//        animSetXY2.setDuration(3000);
//        animX2.setRepeatCount(2);
//        animY2.setRepeatCount(2);
//        animSetXY2.start();

//青
//        ObjectAnimator animX3 = ObjectAnimator.ofFloat(iv3, "x", 1050f);
//        ObjectAnimator animY3 = ObjectAnimator.ofFloat(iv3, "y", 440f);
//        AnimatorSet animSetXY3 = new AnimatorSet();
//        animSetXY3.playTogether(animX3, animY3);
//        animSetXY3.setDuration(3000);
//        animSetXY3.start();

//黄
//        ObjectAnimator animX4 = ObjectAnimator.ofFloat(iv4, "x", 1150f);
//        ObjectAnimator animY4 = ObjectAnimator.ofFloat(iv4, "y", 320f);
//        AnimatorSet animSetXY4 = new AnimatorSet();
//        animSetXY4.playTogether(animX4, animY4);
//        animSetXY4.setDuration(3000);
//        animSetXY4.start();

