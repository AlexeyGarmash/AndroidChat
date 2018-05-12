package com.example.tabloproject.blablachat;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

public class MainActivity extends AppCompatActivity {

    private static final String APP_ID = "A13752B9-B80C-4DEF-BF24-2D3810CB9272";

    private ProgressBar progressBar;



    private EditText inputId, inputPassword;
    private TextInputLayout inputLayoutId, inputLayoutPassword;
    private Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = null;
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        inputLayoutId = (TextInputLayout) findViewById(R.id.input_layout_Id);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputId = (EditText) findViewById(R.id.input_id);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        inputId.addTextChangedListener(new MyTextWatcher(inputId));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = inputId.getText().toString();
                // Remove all spaces from userID
                userId = userId.replaceAll("\\s", "");

                String userNickname = inputPassword.getText().toString();

                submitForm(userId, userNickname);
            }
        });
    }

    private void setNotTouchInterface(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        btnSignUp.setText("");
    }
    private void clearNotTouchInterface(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        btnSignUp.setText(getString(R.string.btn_sign_up));
    }
    private boolean validateView(EditText input, TextInputLayout layout, String error){
        if (input.getText().toString().trim().isEmpty()) {
            layout.setError(error);
            input.requestFocus();
            return false;
        } else {
            layout.setErrorEnabled(false);
        }

        return true;
    }

    private void submitForm(final String userId, final String userNickname) {
        if (!validateView(inputId, inputLayoutId, getString(R.string.err_msg_Id))) {
            return;
        }


        if (!validateView(inputPassword, inputLayoutPassword, getString(R.string.err_msg_password))) {
            return;
        }

        btnSignUp.setEnabled(false);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        setNotTouchInterface();
        SendBird.connect(userId, new SendBird.ConnectHandler() {

            @Override
            public void onConnected(User user, SendBirdException e) {

                if (e != null) {
                    // Error!
                    Toast.makeText(
                            MainActivity.this, "" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show login failure snackbar
                    btnSignUp.setEnabled(true);
                    clearNotTouchInterface();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                clearNotTouchInterface();
                // Update the user's nickname
                updateCurrentUserInfo(userNickname);

                Intent intent = new Intent(MainActivity.this, GroupsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();

        //Intent intent = new Intent(this, GroupsActivity.class);
        //startActivity(intent);
    }

    private void updateCurrentUserInfo(String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            MainActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    return;
                }

            }
        });
    }



    private class MyTextWatcher implements TextWatcher {

        private View viewInput;


        private MyTextWatcher(View viewInp) {
            this.viewInput = viewInp;

        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            switch (viewInput.getId()) {
                case R.id.input_id:
                    validateView(inputId,inputLayoutId,getString(R.string.err_msg_Id));
                    break;
                case R.id.input_password:
                    validateView(inputPassword, inputLayoutPassword,getString(R.string.err_msg_password));
                    break;
            }
        }
    }
}
