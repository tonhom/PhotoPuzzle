package com.game.photopuzzle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardActivity extends AppCompatActivity {
    private String strUserID = "";
    private TextView txtScore;
    private Button btReset, btExit;
    private  HttpActivity Http = new HttpActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            strUserID = extras.getString("strUserID");
        }

        txtScore = (TextView)findViewById(R.id.textViewScore);
        btReset = (Button)findViewById(R.id.btnReset);
        btExit = (Button)findViewById(R.id.btnExit);

        ShowScore();

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.setTitle("ยืนยันการรีเซ็ตคะแนน");
                ad.setMessage("คุณแน่ใจหรือว่าต้องการรีเซ็ตคะแนน?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ResetScore();
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
            }
        });
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("strUserID", strUserID);
                startActivity(i);
            }
        });
    }

    private void ShowScore() {
        String url = getString(R.string.str_url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", "score"));
        params.add(new BasicNameValuePair("strUserID", strUserID));
        params.add(new BasicNameValuePair("strUser", ""));
        params.add(new BasicNameValuePair("strPass", ""));
        params.add(new BasicNameValuePair("question_level", ""));
        params.add(new BasicNameValuePair("question_id", ""));
        params.add(new BasicNameValuePair("help", ""));

        String resultServer = Http.getHttpPost(url, params);
        String score_total = "";

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            score_total = c.getString("score_total");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        txtScore.setText(score_total);
        if(!"0".equals(score_total)){
            btReset.setEnabled(true);
        }
    }

    private void ResetScore() {
        String url = getString(R.string.str_url);
        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", "clear_game"));
        params.add(new BasicNameValuePair("strUserID", strUserID));
        params.add(new BasicNameValuePair("strUser", ""));
        params.add(new BasicNameValuePair("strPass", ""));
        params.add(new BasicNameValuePair("question_level", ""));
        params.add(new BasicNameValuePair("question_id", ""));
        params.add(new BasicNameValuePair("help", ""));

        Http.getHttpPost(url, params);

        btReset.setEnabled(false);
        txtScore.setText("0");
        //ShowScore();
    }
}
