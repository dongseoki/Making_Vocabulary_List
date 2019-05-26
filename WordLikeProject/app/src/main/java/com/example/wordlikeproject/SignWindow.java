package com.example.wordlikeproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SignWindow extends AppCompatActivity {
    boolean loginState;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_window);
    }
    public void signInButtonOnClicked(View view){
        ID = "fail";
        ID = signWithGoogle();
        if(!ID.equals("fail")) {

            Intent intent = new Intent(this, MainWindow.class);
            intent.putExtra("ID", ID);
            startActivityForResult(intent, 111);
        }
    }
    public String signWithGoogle(){
        // API 를 이용한 로그인 시도 과정.
        // 지금은 테스트로 어쩔수 없이 id로 leedongseok 을 반환
        // if(실패..?)
        // return new String("fail");
        return new String("leedongseok");
    }
}
