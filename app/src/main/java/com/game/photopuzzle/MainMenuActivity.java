package com.game.photopuzzle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    Button btnLevelEasy, btnLevelMedium, btnLevelHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.btnLevelEasy = (Button) findViewById(R.id.btnLevelEasy);
        this.btnLevelMedium = (Button) findViewById(R.id.btnLevelMedium);
        this.btnLevelHard = (Button) findViewById(R.id.btnLevelHard);

        this.btnLevelEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), GameActivity.class);
                i.putExtra("strUserID","");
                i.putExtra("question_level","EASY");
                startActivity(i);
            }
        });
    }
}
