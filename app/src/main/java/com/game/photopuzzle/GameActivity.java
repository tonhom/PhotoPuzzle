package com.game.photopuzzle;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    String help_answer = "0", help_skip = "0", help_guide = "0", question_id = "0";

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

        txtAnswer = (EditText) findViewById(R.id.editTextAnswer);

        btAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAnswer(txtAnswer.getText().toString().trim());
            }
        });
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelp();
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("strUserID", strUserID);
                startActivity(i);
            }
        });

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
        CheckHelp();
    }

    private void CheckHelp() {
        String url = getString(R.string.str_url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", "check_help"));
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
            help_answer = c.getString("help_answer");
            help_skip = c.getString("help_skip");
            help_guide = c.getString("help_guide");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if("0".equals(help_answer) || "0".equals(help_skip) || "0".equals(help_guide)){
            btnHelp.setEnabled(true);
        }else{
            btnHelp.setEnabled(false);
        }
    }

    private void DialogHelp() {
        View checkBoxView = View.inflate(this, R.layout.dialog_help, null);
        final Button btnHelpAnswer = (Button) checkBoxView.findViewById(R.id.btnHelpAnswer);
        final Button btnHelpSkip = (Button) checkBoxView.findViewById(R.id.btnHelpSkip);
        final Button btnHelpGuide = (Button) checkBoxView.findViewById(R.id.btnHelpGuide);

        if("0".equals(help_answer)){
            btnHelpAnswer.setEnabled(true);
        }
        if("0".equals(help_skip)){
            btnHelpSkip.setEnabled(true);
        }
        if("0".equals(help_guide)){
            btnHelpGuide.setEnabled(true);
        }

        btnHelpAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strHelpAnswer = gameList.get(i_random).get("question_answer");
                txtAnswer.setText(strHelpAnswer);

                help_answer = "1";
                SaveHelp("help_answer");
                CheckHelp();
                btnHelpAnswer.setEnabled(false);
            }
        });
        btnHelpSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strHelpAnswer = gameList.get(i_random).get("question_answer");
                GamesAll();

                help_skip = "1";
                SaveHelp("help_skip");
                CheckHelp();
                btnHelpSkip.setEnabled(false);
            }
        });
        btnHelpGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strHelpGuide = gameList.get(i_random).get("question_guide");
                txtAnswer.setText(strHelpGuide);

                help_guide = "1";
                SaveHelp("help_guide");
                CheckHelp();
                btnHelpGuide.setEnabled(false);
            }
        });


        AlertDialog.Builder builderInOut = new AlertDialog.Builder(this);
        builderInOut.setTitle("ตัวช่วย");
        builderInOut.setMessage("กรุณาเลือกตัวช่วย")
                .setView(checkBoxView)
                .setCancelable(false)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void SaveHelp(String help) {
        String url = getString(R.string.str_url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", "save_help"));
        params.add(new BasicNameValuePair("strUserID", strUserID));
        params.add(new BasicNameValuePair("strUser", ""));
        params.add(new BasicNameValuePair("strPass", ""));
        params.add(new BasicNameValuePair("question_level", ""));
        params.add(new BasicNameValuePair("question_id", ""));
        params.add(new BasicNameValuePair("help", help));

        Http.getHttpPost(url, params);
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
        params.add(new BasicNameValuePair("question_id", ""));
        params.add(new BasicNameValuePair("help", ""));

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
        if (answer.trim().equals(gameList.get(i_random).get("question_answer").trim())) {
            msgShow("เก่งมาก เป็นคำตอบที่ถูกต้อง ^_^");
            PlayVideo();
        } else {
            msgShow("ไม่ถูกต้องลองพยามหน่อยนะ T_T");
            // PlayVideo();
        }

    }

    private void PlayVideo() {
        Intent i = new Intent(getBaseContext(), VideoActivity.class);
        i.putExtra("strUserID", strUserID);
        i.putExtra("question_level", question_level);
        i.putExtra("video", gameList.get(i_random).get("question_video").trim());
        startActivity(i);
    }

    private void msgShow(String strMsg) {
        Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_SHORT).show();
    }

    private void SaveGame() {
        String url = getString(R.string.str_url);
        question_id = gameList.get(i_random).get("question_id");
        // Paste Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("status", "save_game"));
        params.add(new BasicNameValuePair("strUserID", strUserID));
        params.add(new BasicNameValuePair("strUser", ""));
        params.add(new BasicNameValuePair("strPass", ""));
        params.add(new BasicNameValuePair("question_level", ""));
        params.add(new BasicNameValuePair("question_id", question_id));
        params.add(new BasicNameValuePair("help", ""));

        Http.getHttpPost(url, params);
    }

}
