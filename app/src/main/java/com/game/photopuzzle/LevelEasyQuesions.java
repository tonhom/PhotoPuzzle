package com.game.photopuzzle;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by rattapron on 3/7/2016.
 */

public class LevelEasyQuesions {
    ArrayList<EasyQuestion> Questions;

    public LevelEasyQuesions() {
        this.Questions = new ArrayList<EasyQuestion>();

        // q1
        EasyQuestion q1 = new EasyQuestion();
        q1.answer = "ไก่งามเพราะขนคนงามเพราะแต่ง";
        q1.id = 1;
        q1.imageSource = "easy_01";
        q1.choice = new ArrayList<String>();
        q1.choice.add("ไ");
        q1.choice.add("ก่");
        q1.choice.add("ง");
        q1.choice.add("า");
        q1.choice.add("ม");
        q1.choice.add("เ");
        q1.choice.add("พ");
        q1.choice.add("ร");
        q1.choice.add("า");
        q1.choice.add("ะ");
        q1.choice.add("ข");
        q1.choice.add("น");
        q1.choice.add("ค");
        q1.choice.add("น");
        q1.choice.add("ง");
        q1.choice.add("า");
        q1.choice.add("ม");
        q1.choice.add("เ");
        q1.choice.add("พ");
        q1.choice.add("ร");
        q1.choice.add("า");
        q1.choice.add("ะ");
        q1.choice.add("แ");
        q1.choice.add("ต่");
        q1.choice.add("ง");
        this.Questions.add(q1);
    }

    public EasyQuestion getQuestionById(int id) {
        EasyQuestion q = null;
        for (int i = 0; i < this.Questions.size(); i++) {
            q = this.Questions.get(i);
            if (q.id == id) {
                break;
            }
        }
        return q;
    }
}
