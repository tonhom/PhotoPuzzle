package com.game.photopuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopScoreboardActivity extends AppCompatActivity {
    final ArrayList<HashMap<String, String>> ScoreArrList = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> map;
    private String strUserID = "";
    private Button btExit;
    private ListView listViewScore;
    private  HttpActivity Http = new HttpActivity();
    private JSONUrl json = new JSONUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_score);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            strUserID = extras.getString("strUserID");
        }

        btExit = (Button)findViewById(R.id.btnExit);
        listViewScore = (ListView)findViewById(R.id.listViewScore);

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("strUserID", strUserID);
                startActivity(i);
            }
        });

        ShowTopScore();
    }

    private void ShowTopScore() {
        String url = getString(R.string.str_url);
        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", "top_score"));
        params.add(new BasicNameValuePair("strUserID", ""));
        params.add(new BasicNameValuePair("strUser", ""));
        params.add(new BasicNameValuePair("strPass", ""));
        params.add(new BasicNameValuePair("question_level", ""));
        params.add(new BasicNameValuePair("question_id", ""));
        params.add(new BasicNameValuePair("help", ""));

        try {
            JSONArray data = new JSONArray(json.getJSONUrl(url, params));
            for (int i = 0; i < data.length(); i++) {
                Integer no = i+1;

                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();

                map.put("no", no.toString());
                map.put("score_id", c.getString("score_id"));
                map.put("score_total", c.getString("score_total"));
                map.put("user_id", c.getString("user_id"));
                map.put("username", c.getString("username"));

                ScoreArrList.add(map);
            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(getBaseContext(), ScoreArrList,
                    R.layout.column_top_score, new String[] {
                    "no", "username", "score_total" }, new int[] {
                    R.id.ColNo, R.id.ColName, R.id.ColScore });

            listViewScore.setAdapter(sAdap);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
