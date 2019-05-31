package com.example.wordlikeproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WordRetouchWindow extends AppCompatActivity {
    private static final String TAG = "dbstate";
    private SQLiteDatabase db;
    TextView existingWordEdit;
    TextView existingMeaningEdit;
    EditText changedWordEdit;
    EditText changedMeaningEdit;
    EditText changedRankEdit;
    String title;
    String spelling;
    String meaning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_retouch_window);

        createDatabase("wordlist");
        Intent intent  = getIntent();
        processIntent(intent);

        String SELECT_SQL = "select meaning from "+title+" where spelling = '"+spelling+"'";
        try {
            Cursor c = db.rawQuery(SELECT_SQL, null);
            if (c.moveToFirst()) {
                    meaning = c.getString(c.getColumnIndex("meaning"));
            }
        } catch(Exception ex) {
            Log.e(TAG, "Exception in SELECT_SQL", ex);
        }


        existingWordEdit = (TextView)findViewById(R.id.existingWordEdit);
        existingMeaningEdit = (TextView)findViewById(R.id.existingMeaningEdit);
        existingWordEdit.setText(spelling);
        existingMeaningEdit.setText(meaning);

        changedWordEdit = (EditText)findViewById(R.id.changedWordEdit);
        changedMeaningEdit = (EditText)findViewById(R.id.changedMeaningEdit);
        changedRankEdit = (EditText)findViewById(R.id.changedRankEdit);
    }

    public void applyButtonOnClicked(View view){
        try {
            String UPDATE_SQL = "UPDATE "+ title +" SET spelling = '" + changedWordEdit.getText().toString() + "', meaning = '" + changedMeaningEdit.getText().toString() + "', rank = "+ changedRankEdit.getText().toString() +" WHERE spelling = '" + spelling + "'";
            db.execSQL(UPDATE_SQL);
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
        catch(Exception e){
            Log.d(TAG, "update failed ");
            Log.d(TAG, e.getMessage());
            Toast.makeText(getApplicationContext(), "problem", Toast.LENGTH_LONG);
        }

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

    public void processIntent(Intent intent){
        if(intent !=null){
            title = intent.getStringExtra("title");
            spelling = intent.getStringExtra("spelling");
        }
    }
}
