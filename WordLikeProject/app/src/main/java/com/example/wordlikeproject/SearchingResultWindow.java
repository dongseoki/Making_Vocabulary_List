package com.example.wordlikeproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SearchingResultWindow extends AppCompatActivity {
    String searchString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_result_window);
        Intent intent  = getIntent();
        processIntent(intent);
    }
    public void processIntent(Intent intent){
        if(intent !=null){
            searchString = intent.getStringExtra("searchString");
        }
    }

}
