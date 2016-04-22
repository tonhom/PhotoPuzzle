package com.game.photopuzzle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by User on 19/4/2559.
 */
public class GameChoiActivity extends Activity{
    private Button iQuestion, btAnswer1, btAnswer2, btAnswer3, btExit, txtLevel;
    final ArrayList<HashMap<String, String>> gameList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;
    int i_random = 0;
    String strUserID = "", question_level = "";
    HttpActivity Http = new HttpActivity();
    JSONUrl json = new JSONUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_choi);

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

        txtLevel = (Button) findViewById(R.id.btnLevel);
        iQuestion = (Button)findViewById(R.id.imgQuestion);
        btAnswer1 = (Button)findViewById(R.id.btnAnswer1);
        btAnswer2 = (Button)findViewById(R.id.btnAnswer2);
        btAnswer3 = (Button)findViewById(R.id.btnAnswer3);
        btExit = (Button)findViewById(R.id.btnExit);

        switch (question_level) {
            case "EASY":
                txtLevel.setText("ระดับ : ง่าย");
                break;
            case "MEDIUM":
                txtLevel.setText("ระดับ : ปานกลาง");
                break;
            case "HARD":
                txtLevel.setText("ระดับ : ยาก");
                break;
            default:
                txtLevel.setText("ระดับ : ");
                break;
        }

        GamesAll();

        btAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAnswer("1");
            }
        });
        btAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAnswer("2");
            }
        });
        btAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAnswer("3");
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

    private void CheckAnswer(String answer) {
        switch (answer) {
            case "1":
                if(gameList.get(i_random).get("answer1").equals(gameList.get(i_random).get("question_answer"))){
                    msgShow("เก่งมาก เป็นคำตอบที่ถูกต้อง ^_^");
                    PlayVideo();
                }else{
                    msgShow("ไม่ถูกต้องลองพยามหน่อยนะ T_T");
                }
                break;
            case "2":
                if(gameList.get(i_random).get("answer2").equals(gameList.get(i_random).get("question_answer"))){
                    msgShow("เก่งมาก เป็นคำตอบที่ถูกต้อง ^_^");
                    PlayVideo();
                }else{
                    msgShow("ไม่ถูกต้องลองพยามหน่อยนะ T_T");
                }
                break;
            case "3":
                if(gameList.get(i_random).get("answer3").equals(gameList.get(i_random).get("question_answer"))){
                    msgShow("เก่งมาก เป็นคำตอบที่ถูกต้อง ^_^");
                    GamesAll();
                }else{
                    msgShow("ไม่ถูกต้องลองพยามหน่อยนะ T_T");
                }
                break;
            default:
                break;
        }

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
                map.put("answer1", c.getString("answer1"));
                map.put("answer2", c.getString("answer2"));
                map.put("answer3", c.getString("answer3"));
                map.put("question_guide", c.getString("question_guide"));
                map.put("question_video", c.getString("question_video"));
                map.put("question_level", c.getString("question_level"));
                gameList.add(map);
            }
            if (gameList.size() > 0) {
                i_random = randInt(0, gameList.size() - 1);
                int resQuestionId = getResources().getIdentifier(gameList.get(i_random).get("question_img"), "drawable", getPackageName());
                iQuestion.setBackgroundResource(resQuestionId);

                String resanswer1 = gameList.get(i_random).get("answer1");
                btAnswer1.setText(resanswer1);
                String resanswer2 = gameList.get(i_random).get("answer2");
                btAnswer2.setText(resanswer2);
                String resanswer3 = gameList.get(i_random).get("answer3");
                btAnswer3.setText(resanswer3);
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

    private void msgShow(String strMsg){
        Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_SHORT).show();
    }

    private void PlayVideo() {
        Intent i = new Intent(getBaseContext(), VideoChoiActivity.class);
        i.putExtra("strUserID", strUserID);
        i.putExtra("question_level",question_level);
        i.putExtra("video", gameList.get(i_random).get("question_video").trim());
        startActivity(i);
    }
}