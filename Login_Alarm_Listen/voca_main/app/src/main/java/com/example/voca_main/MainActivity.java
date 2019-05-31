package com.example.voca_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 123;

    private TextView mTvStatus;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvStatus = findViewById(R.id.tv_login_status);
        mLoginBtn = findViewById(R.id.btn_login);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) return;
                String msg = "User(uid: " + user.getUid() + ", email: " + user.getEmail() + ", name: " + user.getDisplayName();
                Log.d(TAG, msg);
                mTvStatus.setText(msg);
                mLoginBtn.setEnabled(false);
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




