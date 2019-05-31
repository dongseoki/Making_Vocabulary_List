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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class WordBookListWindow extends AppCompatActivity {
    public static final String TAG = "dbstate";
    private RecyclerAdapter adapter;
    private DatabaseHelper dbHelper;
    ArrayList<String> arrTblNames;
    RecyclerView recyclerView;
    private SQLiteDatabase db;
    boolean databaseCreated;
    EditText titleWordsList;
    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_book_list_window);
        init();
        createDatabase("wordlist");
        //boolean isOpen = openDatabase();
        selected = -1;

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // event code

                        Intent intent = new Intent(getApplicationContext(), WordsListWindow.class);
                        intent.putExtra("title", arrTblNames.get(position).toString());
                        startActivityForResult(intent, 111);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getApplicationContext(), arrTblNames.get(position).toString() + "이 선택됨.", Toast.LENGTH_SHORT).show();
                        selected = position;
                    }
                }));

        //출처: https://liveonthekeyboard.tistory.com/entry/안드로이드-RecyclerView-2-OnItemClick-리스너-구현 [키위남]

        Log.d(TAG, "what is it");

        titleWordsList = (EditText) findViewById(R.id.titleWordsList);
        getAndShowTables();
    }

    private void createDatabase(String name) {
        println("creating database [" + name + "].");

        try {
            db = openOrCreateDatabase(
                    name,
                    Activity.MODE_PRIVATE, null);

            databaseCreated = true;
            println("database is created.");
        } catch (Exception ex) {
            ex.printStackTrace();
            println("database is not created.");
        }
    }

    public void deleteButtonOnClicked(View view) {
        try {
            String DELETE_SQL = "DROP TABLE IF EXISTS " + arrTblNames.get(selected).toString() ;
            String DELETE_from_table_list = "DELETE FROM table_list WHERE title = " + "\"" + arrTblNames.get(selected).toString() +"\"";
            db.execSQL(DELETE_SQL);
            db.execSQL(DELETE_from_table_list);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
        getAndShowTables();
    }


    public void createButtonOnClicked(View view) {
        try {
            String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + titleWordsList.getText().toString() + " ("
                    + "spelling text PRIMARY KEY NOT NULL, "
                    + "meaning text NOT NULL, "
                    + "example text NOT NULL, "
                    + "rank integer NOT NULL);";
            db.execSQL(CREATE_SQL);
            String INSERT_SQL = "insert into " + "table_list" + " (title) " + "values " + " (\"" + titleWordsList.getText().toString() + "\");";
            db.execSQL(INSERT_SQL);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
        getAndShowTables();
        //출처: https://toris.tistory.com/entry/안드로이드에서-SQLite사용 [토리의 만물상]
    }


    private void getAndShowTables() {
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        arrTblNames = new ArrayList<String>();


        String CREATE_SQL = "CREATE TABLE IF NOT EXISTS " + "table_list" + "(title text PRIMARY KEY NOT NULL)";

        try {
            db.execSQL(CREATE_SQL);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in CREATE_SQL", ex);
        }
        Cursor c = db.rawQuery("SELECT title FROM table_list", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(c.getString(c.getColumnIndex("title")));
                c.moveToNext();
            }
        }

        for (int i = 0; i < arrTblNames.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            WordsList data = new WordsList();
            data.setTitle(arrTblNames.get(i));
            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }


    public void retouchButtonOnClicked(View view) {
        try {
            String ALTER_SQL = "ALTER TABLE "+ arrTblNames.get(selected).toString()+" rename TO "+ titleWordsList.getText().toString();
            db.execSQL(ALTER_SQL);
            String UPDATE_SQL = "UPDATE table_list SET title = '"+titleWordsList.getText().toString()+"' WHERE title = '"+arrTblNames.get(selected).toString()+"'";
            db.execSQL(UPDATE_SQL);
        } catch (Exception ex) {
            Log.e(TAG, "Exception in ALTER_AND_UPDATE_SQL", ex);
        }
        getAndShowTables();
        //출처: https://toris.tistory.com/entry/안드로이드에서-SQLite사용 [토리의 만물상]
    }
    public void downloadButtonOnClicked(View view) {
        Intent intent = new Intent(this, WordsListsDownloadCenter.class);
        startActivityForResult(intent, 111);
    }
    public void shareButtonOnClicked(View view) {
        Toast.makeText(getApplicationContext(), arrTblNames.get(selected).toString() + "을 공유합니다!", Toast.LENGTH_SHORT).show();
    }

    private boolean openDatabase() {
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        return true;
    }

    private void init() {
        recyclerView = findViewById(R.id.wordBookListsTable);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void println(String msg) {
        Log.d(TAG, msg);
        //status.append("\n" + msg);
    }


}
