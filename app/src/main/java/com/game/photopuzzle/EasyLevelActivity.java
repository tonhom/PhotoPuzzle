package com.game.photopuzzle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EasyLevelActivity extends AppCompatActivity {

    LevelEasyQuesions easyQ;
    ImageView imgHolder;
    EditText textInput;
    EasyQuestion curretQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_level);
        this.imgHolder = (ImageView) findViewById(R.id.imageView);
        this.textInput = (EditText) findViewById(R.id.answerText);

        this.easyQ = new LevelEasyQuesions();
        int questionId = this.getNextQuestionId();
        this.setQuestion(questionId);
    }

    // ฟังก์ชั่นนี้สำหรับไปเรียกที่ web php เพื่อดูขอเลข id ของคำถามข้อต่อไป
    private int getNextQuestionId() {
        // simple return first question
        return 1;
    }

    private void setQuestion(int id) {
        EasyQuestion question = this.easyQ.getQuestionById(id);
        if (question != null) {
            this.curretQuestion = question;
            Context context = getBaseContext();
            int res_id = context.getResources().getIdentifier(question.imageSource, "drawable", context.getPackageName());
            this.imgHolder.setImageResource(res_id);
        }
    }

    private void setChoice() {

    }

    public void checkAnswer(View iew) {
        String answer = this.textInput.getText().toString();
        if (this.curretQuestion.answer.equals(answer)) {
            // sample code
            Toast.makeText(getBaseContext(), "คำตอบถูกต้อง, ระบบกำลังบันทึก", Toast.LENGTH_SHORT).show();
            // save to database by call php
            // --do--

            // get next question and set it again
            // --do--
        } else {
            // don't correct answer notify user
            Toast.makeText(getBaseContext(), "คำตอบไม่ถูกต้อง ลองพยายามคิดดูใหม่อีกครั้ง สู้ๆนะครับ", Toast.LENGTH_SHORT).show();
        }
    }
}
