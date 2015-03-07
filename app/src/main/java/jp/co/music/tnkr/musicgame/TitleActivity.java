package jp.co.music.tnkr.musicgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class TitleActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        ImageView img = (ImageView)findViewById(R.id.gameStart);
        Animation bound = AnimationUtils.loadAnimation(this, R.anim.bound);
        img.startAnimation(bound);
    }

    public void onStart(View v) {//クリック時に呼ばれる
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        TitleActivity.this.finish();
    }
}
