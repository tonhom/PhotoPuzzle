package com.game.photopuzzle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Administrator on 4/6/2016.
 */
public class VideoActivity extends Activity {
    HttpActivity Http = new HttpActivity();
    JSONUrl json = new JSONUrl();
    Button btnClose;
    String strUserID = "", question_level = "", video = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            strUserID = extras.getString("strUserID");
            question_level = extras.getString("question_level");
            video = extras.getString("video");
        }

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnClose = (Button) findViewById(R.id.btnClose);
        final VideoView myVideoV = (VideoView) findViewById(R.id.videoView1);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), GameActivity.class);
                i.putExtra("strUserID", strUserID);
                i.putExtra("question_level", question_level);
                startActivity(i);
            }
        });

        myVideoV.setVideoURI(Uri.parse(getString(R.string.str_url_video) + video+".mp4"));
        myVideoV.setMediaController(new MediaController(this));
        myVideoV.start();
        myVideoV.requestFocus();

    }
}
