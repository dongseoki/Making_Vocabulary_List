package com.example.wordlikeproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WordsListsDownloadCenter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_lists_download_center);
    }

    protected String getWordLists(String... urls){
        StringBuilder jsonHtml = new StringBuilder();
        try{
            URL url - new URL(urls[0]);

            HttpURLConnection conn = (HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    for(;;){
                        String line = br.readline
                    }
            }
        }
    }

}
