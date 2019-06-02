package com.example.wordlikeproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SignWindow extends AppCompatActivity {
    private static final String TAG = SignWindow.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;

    private TextView mTvStatus;
    private Button mLoginBtn;
    //private Button mLogoutBtn; 로그인이 잘되는지 확인해 볼려고 만들었습니다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_window);
        mTvStatus = findViewById(R.id.tv_login_status);
        mLoginBtn = findViewById(R.id.btn_login);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                }
        });
        /*mLogoutBtn = findViewById(R.id.btn_logout);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        mLogoutBtn.setVisibility(View.GONE);*/
    }

    /*private void toggleLogout() {
        mLogoutBtn.setVisibility(mLogoutBtn.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }*/

    private void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN
        );
    }

    /*private void signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplication(), "Success logout", Toast.LENGTH_SHORT).show();
                toggleLogout();
                mLoginBtn.setEnabled(true);
                mTvStatus.setText("");
            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) return;
                Intent intent = new Intent(getApplicationContext(), MainWindow.class);
                startActivity(intent);
                //String msg = "User(uid: " + user.getUid() + ", email: " + user.getEmail() + ", name: " + user.getDisplayName();
                //Log.d(TAG, msg);
                //mTvStatus.setText(msg);
                mLoginBtn.setEnabled(false);


                /*if (BuildConfig.DEBUG)
                    toggleLogout();*/
            } else {
                if (response == null) {
                    Log.e(TAG, "Login failed");
                    return;
                }
                if (response.getError() != null && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    response.getError().printStackTrace();
                }
            }
        }
    }
}
