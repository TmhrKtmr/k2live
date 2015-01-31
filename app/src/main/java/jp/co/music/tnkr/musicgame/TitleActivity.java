package jp.co.music.tnkr.musicgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by yukumo3621 on 2015/01/31.
 */
public class TitleActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
    }

    public void onStart(View v) {//クリック時に呼ばれる
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
//        Intent intent = new Intent();
//        intent.setClassName("jp.co.music.tnkr.musicgame", "jp.co.music.tnkr.musicgame.MainActivity");
//        startActivity(intent);
    }
}
