package com.woystech.fasdes.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.woystech.common.ApplicationConstants;
import com.woystech.fasdes.BundleKey;
import com.woystech.fasdes.R;
import com.woystech.fasdes.config.Utilities;
import com.woystech.fasdes.models.Client;
import com.woystech.fasdes.net.Endpoint;
import com.woystech.net.connectivity.NetworkReceiver;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by firok on 5/20/2017.
 */

public class SignUpFirstStepActivity extends AppCompatActivity {

    private final static String TAG = SignUpFirstStepActivity.class.getSimpleName();

    @BindView(R.id.username)
    EditText userName;
    @BindView(R.id.mobile)
    EditText phoneNumber;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.radioGroup)
    RadioGroup rdGender;
    @BindView(R.id.input_layout_username)
    TextInputLayout input_layout_username;
    @BindView(R.id.input_layout_email)
    TextInputLayout input_layout_email;
    @BindView(R.id.mLayout)
    LinearLayout mLayout;
    private String endpoint;
    private Map<String, String> params;

    private Client client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_first_step);
        ButterKnife.bind(this);
        checkConnection();

        resetTextWatcher();
    }

    // Method to check connection status
    private void checkConnection() {
        boolean isConnected = NetworkReceiver.isConnected();
        if (!isConnected) {
            Intent intent = new Intent(SignUpFirstStepActivity.this, NoNetworkActivity.class);
            startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_NETWORK);

        }
    }

    public void resetTextWatcher() {
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (text.length() > 0 && text.length() <= 3) {
                    userName.setTextColor(Color.RED);
                } else {
                    userName.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String EMAIL = email.getText().toString();
                if (!EMAIL.isEmpty() && Utilities.validate(EMAIL)) {
                    email.setTextColor(Color.BLACK);
                } else {
                    email.setTextColor(Color.RED);
                }
            }
        });
    }

    @OnClick(R.id.login)
    public void onLogin() {
        Intent intent = new Intent(SignUpFirstStepActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.next)
    public void onSignUp() {
        initEndpointAndUrlParam();
        String USERNAME = userName.getText().toString();
        String MOBILE = phoneNumber.getText().toString();
        String EMAIL = email.getText().toString();
        String GENDER =null;
        if (USERNAME.isEmpty()) {
            input_layout_username.setError(getString(R.string.required));
        }
        if (EMAIL.isEmpty()) {
            input_layout_email.setError(getString(R.string.required));
        }

        if (!EMAIL.isEmpty() && !Utilities.validate(EMAIL)) {
            input_layout_email.setError(getString(R.string.enter_valid_email));
        }

        if(rdGender.isSelected()){
           int checkedButton = rdGender.getCheckedRadioButtonId();
            switch (checkedButton){
                case R.id.male:
                    GENDER = getString(R.string.male);
                    break;
                case R.id.female:
                    GENDER = getString(R.string.female);
                    break;
            }
        }


        if (!USERNAME.isEmpty() && !EMAIL.isEmpty() && Utilities.validate(EMAIL)) {
            client = new Client(USERNAME, MOBILE, EMAIL, GENDER);
            Intent intent = new Intent(SignUpFirstStepActivity.this, SignUpSecondStepActivity.class);
            intent.putExtra(BundleKey.EXTRA_CLIENT, client);
            startActivity(intent);
        }
    }

    private void initEndpointAndUrlParam() {
        endpoint = Endpoint.CLIENT;

    }

    @Override
    protected void onResume() {
        super.onResume();

        mLayout.requestFocus();
    }

}
