package com.example.wordlikeproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
    RecyclerView recyclerView;
    private SQLiteDatabase db;
    boolean databaseCreated;
    ArrayList<Word> words;
    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list_window);
        Intent intent  = getIntent();
        processIntent(intent);
        init();
        createDatabase("wordlist");

        selected = -1;

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // event code

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(), words.get(position).getWordSpelling() + "이 선택됨.", Toast.LENGTH_SHORT).show();
                        selected = position;
                    }
                }));

        getAndShowRecords();
    }
    public void deleteButtonOnClicked(View view){
        try {
            String DELETE_from_table_list = "DELETE FROM " + title +" WHERE spelling = " + "\"" + words.get(selected).getWordSpelling() +"\"";
            db.execSQL(DELETE_from_table_list);

        } catch (Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
        getAndShowRecords();
    }
    public void retouchButtonOnClicked(View view){
        Intent intent = new Intent(this, WordRetouchWindow.class);
        intent.putExtra("title", title);
        intent.putExtra("spelling", words.get(selected).getWordSpelling());
        startActivityForResult(intent, 112);
    }
    public void createButtonOnClicked(View view){
        Intent intent = new Intent(this, WordAddWindow.class);
        intent.putExtra("title", title);
        startActivityForResult(intent, 111);
    }
    public void listenButtonOnClicked(View view){
        Toast.makeText(getApplicationContext(),words.get(selected).getWordSpelling()+"을 듣습니다!",Toast.LENGTH_LONG);
    }
    public void examButtonOnClicked(View view){

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);/// result code sms ok ...
        if(requestCode == 111 ||requestCode == 112){
            try {
                getAndShowRecords();
                Toast.makeText(getApplicationContext(), "단어 생성 또는 추가 완료", Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Log.d(TAG, "Exception in on ActivityResult");
            }
        }
    }
}
