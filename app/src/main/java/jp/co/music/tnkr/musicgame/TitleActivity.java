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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPool.load(getApplicationContext(), R.raw.gamestart, 0);

        ImageView img = (ImageView) findViewById(R.id.gameStart);
        Animation bound = AnimationUtils.loadAnimation(this, R.anim.updown);
        img.startAnimation(bound);

        //サイズ確認用
        TextView txWidth = (TextView)findViewById(R.id.width_id);
        TextView txHeight = (TextView)findViewById(R.id.height_id);
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        String width = "Width = " + size.x;
        String height = "Height = " + size.y;
        txWidth.setText(width);
        txHeight.setText(height);
    }

    public void onStart(View v) {
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
