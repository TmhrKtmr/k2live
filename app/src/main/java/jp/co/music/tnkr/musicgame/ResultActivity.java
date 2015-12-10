package jp.co.music.tnkr.musicgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {
    public TextView pText;
    public TextView gText;
    public TextView mText;

    public ImageView ssImage;
    public ImageView sImage;
    public ImageView aImage;
    public ImageView bImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        int pValue = 0;
        int gValue = 0;
        int mValue = 0;

        Bundle extras;
        extras = getIntent().getExtras();
        if (extras != null) {
            pValue = extras.getInt("pCount");
            gValue = extras.getInt("gCount");
            mValue = extras.getInt("mCount");
        }
        String pMessage;
        pMessage = Integer.toString(pValue);
        String gMessage;
        gMessage = Integer.toString(gValue);
        String mMessage;
        mMessage = Integer.toString(mValue);

        this.pText = (TextView) findViewById(R.id.perfectText);
        pText.setText(pMessage);
        this.gText = (TextView) findViewById(R.id.goodText);
        gText.setText(gMessage);
        this.mText = (TextView) findViewById(R.id.missText);
        mText.setText(mMessage);

        this.ssImage = (ImageView) findViewById(R.id.judgeView1);
        this.sImage = (ImageView) findViewById(R.id.judgeView2);
        this.aImage = (ImageView) findViewById(R.id.judgeView3);
        this.bImage = (ImageView) findViewById(R.id.judgeView4);

        findViewById(R.id.judgeView1).setVisibility(View.INVISIBLE);
        findViewById(R.id.judgeView2).setVisibility(View.INVISIBLE);
        findViewById(R.id.judgeView3).setVisibility(View.INVISIBLE);
        findViewById(R.id.judgeView4).setVisibility(View.INVISIBLE);

        //カウント別の判定表示
        if (pValue >= 20 && gValue == 0 && mValue == 0) {
            findViewById(R.id.judgeView1).setVisibility(View.VISIBLE);
            findViewById(R.id.judgeView2).setVisibility(View.INVISIBLE);
            findViewById(R.id.judgeView3).setVisibility(View.INVISIBLE);
            findViewById(R.id.judgeView4).setVisibility(View.INVISIBLE);
        }
        else if (pValue >= 18 && gValue <= 1 && mValue == 0) {
            findViewById(R.id.judgeView1).setVisibility(View.INVISIBLE);
            findViewById(R.id.judgeView2).setVisibility(View.VISIBLE);
            findViewById(R.id.judgeView3).setVisibility(View.INVISIBLE);
            findViewById(R.id.judgeView4).setVisibility(View.INVISIBLE);
        }
        else if (pValue >= 16 && gValue <= 2 && mValue == 0) {
            findViewById(R.id.judgeView1).setVisibility(View.INVISIBLE);
            findViewById(R.id.judgeView2).setVisibility(View.INVISIBLE);
            findViewById(R.id.judgeView3).setVisibility(View.VISIBLE);
            findViewById(R.id.judgeView4).setVisibility(View.INVISIBLE);
        }
        else if (pValue <= 15 || gValue >= 3 || mValue >= 1){
            findViewById(R.id.judgeView1).setVisibility(View.INVISIBLE);
            findViewById(R.id.judgeView2).setVisibility(View.INVISIBLE);
            findViewById(R.id.judgeView3).setVisibility(View.INVISIBLE);
            findViewById(R.id.judgeView4).setVisibility(View.VISIBLE);
        }
    }
}
