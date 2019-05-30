package com.example.wordlikeproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class WordsListWindow extends AppCompatActivity {
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list_window);
        Intent intent  = getIntent();
        processIntent(intent);


    }

    public void processIntent(Intent intent){
        if(intent !=null){
            title = intent.getStringExtra("title");
        }
    }
}
