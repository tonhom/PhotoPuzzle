package com.game.photopuzzle;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    Button btnLevelEasy, btnLevelMedium, btnLevelHard;
    String strUserID = "", question_level = "";

    HttpActivity Http = new HttpActivity();
    JSONUrl json = new JSONUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            strUserID = extras.getString("strUserID");
            //question_level = extras.getString("question_level");
        }

        btnLevelEasy = (Button) findViewById(R.id.btnLevelEasy);
        btnLevelMedium = (Button) findViewById(R.id.btnLevelMedium);
        btnLevelHard = (Button) findViewById(R.id.btnLevelHard);

        CheckLevel();
                
        btnLevelEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), GameChoiActivity.class);
                i.putExtra("strUserID", strUserID);
                i.putExtra("question_level","EASY");
                startActivity(i);
            }
        });
        btnLevelMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), GameChoiActivity.class);
                i.putExtra("strUserID", strUserID);
                i.putExtra("question_level","MEDIUM");
                startActivity(i);
            }
        });
        btnLevelHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), GameActivity.class);
                i.putExtra("strUserID", strUserID);
                i.putExtra("question_level","HARD");
                startActivity(i);
            }
        });
    }

    private void CheckLevel() {
        String url = getString(R.string.str_url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", "check_level"));
        params.add(new BasicNameValuePair("strUserID", strUserID));
        params.add(new BasicNameValuePair("strUser", ""));
        params.add(new BasicNameValuePair("strPass", ""));
        params.add(new BasicNameValuePair("question_level", ""));
        params.add(new BasicNameValuePair("question_id", ""));
        params.add(new BasicNameValuePair("help", ""));

        String resultServer = Http.getHttpPost(url, params);

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            question_level = c.getString("question_level");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        switch (question_level){
            case "EASY" :
                btnLevelEasy.setEnabled(true);
                break;
            case "MEDIUM" :
                btnLevelMedium.setEnabled(true);
                break;
            case "HARD" :
                btnLevelHard.setEnabled(true);
                break;
            default:
                Intent i = new Intent(getBaseContext(), ScoreboardActivity.class);
                i.putExtra("strUserID", strUserID);
                startActivity(i);
                break;
        }

    }
}
