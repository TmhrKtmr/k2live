package jp.co.music.tnkr.musicgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yukumo3621 on 2015/02/03.
 */
public class ResultActivity extends Activity{
    public TextView countText2;
    public TextView countText3;
    public TextView countText4;

    public ImageView ssImage;
    public ImageView sImage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        int pValue = 0;
        int gValue = 0;
        int mValue = 0;

        Bundle extras;
        extras = getIntent().getExtras();
        if(extras != null) {
            pValue = extras.getInt("pCount");
            gValue = extras.getInt("gCount");
            mValue = extras.getInt("mCount");
        }
        String pMessage;
        pMessage =  Integer.toString(pValue);
        String gMessage;
        gMessage =  Integer.toString(gValue);
        String mMessage;
        mMessage =  Integer.toString(mValue);

        this.countText2 = (TextView) findViewById(R.id.perfectText);
        countText2.setText(pMessage);
        this.countText3 = (TextView) findViewById(R.id.goodText);
        countText3.setText(gMessage);
        this.countText4 = (TextView) findViewById(R.id.missText);
        countText4.setText(mMessage);

        this.ssImage = (ImageView) findViewById(R.id.textView1);
        this.sImage = (ImageView) findViewById(R.id.textView2);
        findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
        findViewById(R.id.textView2).setVisibility(View.INVISIBLE);

        if(pValue >= 19&&gValue == 0&&mValue == 0){
            findViewById(R.id.textView1).setVisibility(View.VISIBLE);
            findViewById(R.id.textView2).setVisibility(View.INVISIBLE);
        }
        if(pValue <= 18||gValue >= 1&&mValue == 0){
            findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView2).setVisibility(View.VISIBLE);
        }

    }
}
