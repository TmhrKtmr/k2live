package jp.co.music.tnkr.musicgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleActivity extends Activity {
    private SoundPool mSoundPool;
    private int mSoundId;
    private final int SOUND_POOL_MAX = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        mSoundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPool.load(getApplicationContext(), R.raw.gamestart, 0);

        ImageView img = (ImageView) findViewById(R.id.gameStart);
        Animation bound = AnimationUtils.loadAnimation(this, R.anim.upDownEffect);
        img.startAnimation(bound);

        //サイズ確認用
        TextView dspWidth = (TextView)findViewById(R.id.width_id);
        TextView dspHeight = (TextView)findViewById(R.id.height_id);
        WindowManager wm = getWindowManager();
        Display dsp = wm.getDefaultDisplay();
        Point size = new Point();
        dsp.getSize(size);
        String width = "Width = " + size.x;
        String height = "Height = " + size.y;
        dspWidth.setText(width);
        dspHeight.setText(height);
    }

    public void gameStart(View v) {
        mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        TitleActivity.this.finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
