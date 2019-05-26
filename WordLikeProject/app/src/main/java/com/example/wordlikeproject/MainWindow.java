package com.example.wordlikeproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainWindow extends AppCompatActivity {
    EditText searchBar;
    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        Intent intent  = getIntent();
        processIntent(intent);
    }
    public void processIntent(Intent intent){
        if(intent !=null){
            ID = intent.getStringExtra("ID");
        }
    }
    public void searchButtonOnClicked(View view){
        searchBar = (EditText) findViewById(R.id.searchBar);
        String searchString = searchBar.getText().toString();
        if(!searchString.equals("")) {
            Intent intent = new Intent(this, SearchingResultWindow.class);
            intent.putExtra("ID", ID);
            intent.putExtra("searchString", searchString);
            startActivityForResult(intent, 111);
        }
    }

    public void notificationButtonOnClicked(View view){
        Intent intent = new Intent(this, NotificationWindow.class);
        startActivityForResult(intent, 111);
    }

    public void memberSecessionButtonOnClicked(View view){
        Toast.makeText(getApplicationContext(), "회원탈퇴 버튼이 눌림", Toast.LENGTH_SHORT).show();
    }

    public void wordbookListButtonOnClicked(View view){
        Intent intent = new Intent(this, WordBookListWindow.class);
        startActivityForResult(intent, 111);
    }
}