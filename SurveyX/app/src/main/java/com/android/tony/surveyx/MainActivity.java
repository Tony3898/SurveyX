package com.android.tony.surveyx;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
// code by https://linkedin.com/in/tejas-rana-668595128/ Tony
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    int prog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBarmain);
        progressBarUpdate(0);
    }

    void progressBarUpdate(int progress)
    {
        this.prog = progress;
        progressBar.setProgress(progress);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                progressBarUpdate( prog +25);
                if(prog==100)
                {
                    MainActivity.this.finish();
                    startActivity(new Intent(MainActivity.this,SignInActivity.class));
                    //SurveyListActivity
                }
            }
        });
        thread.start();
    }
}

