package com.example.wordlikeproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class WordsListWindow extends AppCompatActivity {
    String title;
    private String wordSpelling;
    private String wordMeaning;
    private String wordSentence;
    private int rank;
    public static final String TAG = "dbstate2";
    private RecyclerAdapter2 adapter;
    private DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    private SQLiteDatabase db;
    boolean databaseCreated;
    ArrayList<Word> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list_window);
        Intent intent  = getIntent();
        processIntent(intent);
        init();
        createDatabase("wordlist");
        getAndShowRecords();
    }
    private void createDatabase(String name) {
        println("creating database [" + name + "].");

        try {
            db = openOrCreateDatabase(
                    name,
                    Activity.MODE_PRIVATE, null);

            databaseCreated = true;
            println("database is created.");
        } catch(Exception ex) {
            ex.printStackTrace();
            println("database is not created.");
        }
    }
    private void println(String msg) {
        Log.d(TAG, msg);
        //status.append("\n" + msg);
    }

    public void processIntent(Intent intent){
        if(intent !=null){
            title = intent.getStringExtra("title");
        }
        Toast.makeText(getApplicationContext(),title,Toast.LENGTH_LONG);
    }
    private void init() {
        recyclerView = findViewById(R.id.wordsTable);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter2();
        recyclerView.setAdapter(adapter);
    }

    private void getAndShowRecords() {
        adapter = new RecyclerAdapter2();
        recyclerView.setAdapter(adapter);
        words = new ArrayList<Word>();


        String SELECT_SQL = "select * from " + title ;

        try {
            Cursor c = db.rawQuery(SELECT_SQL, null);
            if (c.moveToFirst()) {
                while ( !c.isAfterLast() ) {
                    wordSpelling = c.getString(c.getColumnIndex("spelling"));
                    wordMeaning = c.getString(c.getColumnIndex("meaning"));
                    wordSentence = c.getString(c.getColumnIndex("example"));
                    rank = c.getInt(c.getColumnIndex("rank"));
                    words.add( new Word(wordSpelling, wordMeaning, wordSentence, rank) );
                    c.moveToNext();
                }
            }
        } catch(Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }

        for (int i = 0; i < words.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
//            WordsList data = new WordsList();
//            data.setTitle(arrTblNames.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(words.get(i));
        }

        adapter.notifyDataSetChanged();
    }
}
