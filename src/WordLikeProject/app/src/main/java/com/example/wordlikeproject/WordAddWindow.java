package com.example.wordlikeproject;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WordAddWindow extends AppCompatActivity {
    private SQLiteDatabase db;
    public static final String TAG = "dbstate2";
    String title;
    EditText addSpellingEdit;
    EditText addMeaningEdit;
    EditText addSentenceEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_add_window);
        createDatabase("wordlist");
        Intent intent  = getIntent();
        processIntent(intent);
        addSpellingEdit = (EditText)findViewById(R.id.addSpellingEdit);
        addMeaningEdit = (EditText)findViewById(R.id.addMeaningEdit);
        addSentenceEdit = (EditText)findViewById(R.id.addSentenceEdit);
    }
    public void processIntent(Intent intent){
        if(intent !=null){
            title = intent.getStringExtra("title");
        }
        Toast.makeText(getApplicationContext(),title,Toast.LENGTH_LONG);
    }
    private void createDatabase(String name) {
        println("creating database [" + name + "].");

        try {
            db = openOrCreateDatabase(
                    name,
                    Activity.MODE_PRIVATE, null);
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

    public void applyButtonOnClicked(View view){
        String INSERT_SQL =  "insert into "+ title + "(spelling, meaning, example, rank) values (\"" +addSpellingEdit.getText().toString()+"\", \""+addMeaningEdit.getText().toString()+"\", \""+addSentenceEdit.getText().toString() +"\", 1)";
        db.execSQL(INSERT_SQL);

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
