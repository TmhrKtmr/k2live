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
    private int mSoundID1;
    private int mSoundID2;
    private int mSoundID3;
    private int mSoundID4;

    private MediaPlayer gameBgm;

    private Timer mainTimer;                 //タイマー用
    private MainTimerTask mainTimerTask;     //タイマタスククラス
    private TextView countText1;             //テキストビュー
    private double count = 0.0;                    //カウント
    private Handler mHandler = new Handler(); //UI Threadへのpost用ハンドラ
    private int musicTime = 33;//曲の時間
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

    public double[] PERFECT_1 = {3.0, 6.0, 9.0, 12.0, 15.0, 18.0, 21.0, 24.0};
    public double[] PERFECT_2 = {5.0, 10.0, 15.0, 20.0, 25.0};
    public double[] PERFECT_3 = {9.0, 18.0, 27.0};
    public double[] PERFECT_4 = {7.0, 14.0, 21.0, 28.0};

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

    public void onStart(View v) {

        findViewById(R.id.button1).setEnabled(true);
        findViewById(R.id.button2).setEnabled(true);
        findViewById(R.id.button3).setEnabled(true);
        findViewById(R.id.button4).setEnabled(true);

        findViewById(R.id.startButton).setVisibility(View.INVISIBLE);

        gameBgm.seekTo(0); // 再生位置を0ミリ秒に指定
        gameBgm.start(); // 再生開始

        // 画面サイズ取得
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        //基準画面サイズ
        if (size.x == 1280 && size.y == 720) {
            //赤
            ImageView iv1 = (ImageView) findViewById(R.id.imageView1);
            TranslateAnimation translate1 = new TranslateAnimation(0, -560, 0, 190);//x原点,x移動先,y原点,y移動先(基準値)
            translate1.setDuration(3000);
            iv1.startAnimation(translate1);
            translate1.setRepeatCount(7);

            //緑
            ImageView iv2 = (ImageView) findViewById(R.id.imageView2);
            TranslateAnimation translate2 = new TranslateAnimation(0, -460, 0, 450);
            translate2.setStartOffset(2000);
            translate2.setDuration(3000);
            iv2.startAnimation(translate2);
            translate2.setRepeatCount(4);

            //青
            ImageView iv3 = (ImageView) findViewById(R.id.imageView3);
            TranslateAnimation translate3 = new TranslateAnimation(0, 460, 0, 450);
            translate3.setStartOffset(6000);
            translate3.setDuration(3000);
            iv3.startAnimation(translate3);
            translate3.setRepeatCount(2);

            //黄
            ImageView iv4 = (ImageView) findViewById(R.id.imageView4);
            TranslateAnimation translate4 = new TranslateAnimation(0, 560, 0, 190);
            translate4.setStartOffset(4000);
            translate4.setDuration(3000);
            iv4.startAnimation(translate4);
            translate4.setRepeatCount(3);
        }
        /**
         * この上下の画面サイズ以外は正常に動作しません
         * 別サイズでプレイする場合は、
         * タイトル画面で表示されたサイズに応じTranslateAnimationの数値を変更
         * 別サイズ÷基準サイズ×TranslateAnimation値
         * (今後数値に変数を使用してコードを削減予定)
         */
        //別サイズの機種(長辺・短辺ともに基準×1.5の画面サイズ)
        if (size.x == 1920 && size.y == 1080) {
            //赤
            ImageView iv1 = (ImageView) findViewById(R.id.imageView1);
            TranslateAnimation translate1 = new TranslateAnimation(0, -840, 0, 285);//基準値×1.5で算出(以下も同様)
            translate1.setDuration(3000);
            iv1.startAnimation(translate1);
            translate1.setRepeatCount(7);

            //緑
            ImageView iv2 = (ImageView) findViewById(R.id.imageView2);
            TranslateAnimation translate2 = new TranslateAnimation(0, -690, 0, 675);
            translate2.setStartOffset(2000);
            translate2.setDuration(3000);
            iv2.startAnimation(translate2);
            translate2.setRepeatCount(4);

            //青
            ImageView iv3 = (ImageView) findViewById(R.id.imageView3);
            TranslateAnimation translate3 = new TranslateAnimation(0, 690, 0, 675);
            translate3.setStartOffset(6000);
            translate3.setDuration(3000);
            iv3.startAnimation(translate3);
            translate3.setRepeatCount(2);

            //黄
            ImageView iv4 = (ImageView) findViewById(R.id.imageView4);
            TranslateAnimation translate4 = new TranslateAnimation(0, 840, 0, 285);
            translate4.setStartOffset(4000);
            translate4.setDuration(3000);
            iv4.startAnimation(translate4);
            translate4.setRepeatCount(3);
        }
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
                        mSoundPool.play(mSoundID1, 1.0F, 1.0F, 0, 0, 1.0F);
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
                        mSoundPool.play(mSoundID1, 1.0F, 1.0F, 0, 0, 1.0F);
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
                        mSoundPool.play(mSoundID1, 1.0F, 1.0F, 0, 0, 1.0F);
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
        if (checkTiming(count, array, 0.1d)) return TapResult.PERFECT;
        if (checkTiming(count, array, 1.0d)) return TapResult.GOOD;
        if (checkTiming(count, array, 2.0d)) return TapResult.MISS;
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
            //ここに定周期で実行したい処理を記述します
            mHandler.post(new Runnable() {
                public void run() {
                    //実行間隔分を加算処理
                    count += 1.0d;
                    //画面にカウントを表示
                    countText1.setText(String.valueOf(count));

                    if (missCount >= 5) {
                        gameOver();
                    }
                    musicTime--;
                    if (musicTime <= 0) {
                        musicEnd();
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

    private void musicEnd() {
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
