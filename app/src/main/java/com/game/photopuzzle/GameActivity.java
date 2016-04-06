package com.game.photopuzzle;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameActivity extends Activity {
    Button imgQuestion, btAnswer, btnHelp, btnEnd, txtLevel;
    EditText txtAnswer;
    final ArrayList<HashMap<String, String>> gameList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;
    int i_random = 0;
    String strUserID = "", question_level = "";
    HttpActivity Http = new HttpActivity();
    JSONUrl json = new JSONUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            strUserID = extras.getString("strUserID");
            question_level = extras.getString("question_level");
        }

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        imgQuestion = (Button) findViewById(R.id.btnImgQuestion);
        btAnswer = (Button) findViewById(R.id.btnSendAnswer);
        btnHelp = (Button) findViewById(R.id.btnHelp);
        btnEnd = (Button) findViewById(R.id.btnEnd);
        txtLevel = (Button) findViewById(R.id.btnLevel);

        txtAnswer = (EditText)findViewById(R.id.editTextAnswer);

        btAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAnswer(txtAnswer.getText().toString().trim());
            }
        });
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameActivity.this, MainActivity.class);
                i.putExtra("strUserID", strUserID);
                startActivity(i);
            }
        });

        switch (question_level){
            case "EASY" : txtLevel.setText("ระดับ : ง่าย");
                break;
            case "MEDIUM" : txtLevel.setText("ระดับ : ปานกลาง");
                break;
            case "HARD" : txtLevel.setText("ระดับ : ยาก");
                break;
            default: txtLevel.setText("ระดับ : ");
                break;
        }
        GamesAll();
    }

    private void GamesAll() {
        String url = getString(R.string.str_url);
        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", "question"));
        params.add(new BasicNameValuePair("strUserID", strUserID));
        params.add(new BasicNameValuePair("strUser", ""));
        params.add(new BasicNameValuePair("strPass", ""));
        params.add(new BasicNameValuePair("question_level", question_level));

        try {
            JSONArray data = new JSONArray(json.getJSONUrl(url, params));
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                //question_id 	question_img 	question_answer 	question_guide 	question_video 	question_level
                map.put("question_id", c.getString("question_id"));
                map.put("question_img", c.getString("question_img"));
                map.put("question_answer", c.getString("question_answer"));
                map.put("question_guide", c.getString("question_guide"));
                map.put("question_video", c.getString("question_video"));
                map.put("question_level", c.getString("question_level"));
                gameList.add(map);
            }
            if (gameList.size() > 0) {
                i_random = randInt(0, gameList.size() - 1);
                int resQuestionId = getResources().getIdentifier(gameList.get(i_random).get("question_img"), "drawable", getPackageName());
                imgQuestion.setBackgroundResource(resQuestionId);
            } else {
                Intent i = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int randInt(int min, int max) {
        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;
        return i1;
    }

    private void CheckAnswer(String answer) {
        if(answer.trim().equals(gameList.get(i_random).get("question_answer").trim())){
            msgShow("เก่งมาก เป็นคำตอบที่ถูกต้อง ^_^");
        } else{
            msgShow("ไม่ถูกต้องลองพยามหน่อยนะ T_T");
        }

    }

    private void msgShow(String strMsg){
        Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_SHORT).show();
    }
}
