package com.woystech.fasdes.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.woystech.common.ApplicationConstants;
import com.woystech.fasdes.BundleKey;
import com.woystech.fasdes.R;
import com.woystech.fasdes.config.Config;
import com.woystech.fasdes.config.Utilities;
import com.woystech.fasdes.models.Client;
import com.woystech.fasdes.net.Endpoint;
import com.woystech.net.NetworkRequestListener;
import com.woystech.net.NetworkUtilsImpl;
import com.woystech.net.application.AppController;
import com.woystech.net.connectivity.NetworkReceiver;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by firok on 5/20/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = LoginActivity.class.getSimpleName();


    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.btn_login)
    Button login;
    @BindView(R.id.sign_up)
    Button sign_up;
    @BindView(R.id.forgot_password)
    TextView forgot_password;
    @BindView(R.id.login_message)
    TextView login_message;
    @BindView(R.id.input_layout_password)
    TextInputLayout input_layout_password;
    @BindView(R.id.input_layout_email)
    TextInputLayout input_layout_email;
    @BindView(R.id.mLayout)
    LinearLayout mLayout;
    private String endpoint;
    private Map<String, String> urlParams;

    private Client client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        checkConnection();
        resetTextWatcher();
    }

    // Method to check connection status
    private void checkConnection() {
        boolean isConnected = NetworkReceiver.isConnected();
        if (!isConnected) {
            Intent intent = new Intent(LoginActivity.this, NoNetworkActivity.class);
            startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_NETWORK);

        }
    }

    public void resetTextWatcher() {
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

    @OnClick(R.id.sign_up)
    public void onSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpFirstStepActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    public void onLogin() {
        String PASSWORD = password.getText().toString();
        String EMAIL = email.getText().toString();
        if (PASSWORD.isEmpty()) {
            input_layout_password.setError(getString(R.string.required));
        }
        if (EMAIL.isEmpty()) {
            input_layout_email.setError(getString(R.string.required));
        }

        if (!EMAIL.isEmpty() && !Utilities.validate(EMAIL)) {
            input_layout_email.setError(getString(R.string.enter_valid_email));
        }


        if (!PASSWORD.isEmpty() && !EMAIL.isEmpty() && Utilities.validate(EMAIL)) {

            initEndpointAndUrlParams(EMAIL,PASSWORD);
            try{
                new NetworkUtilsImpl().executeJsonObjectRequest(Request.Method.GET, new StringBuilder(endpoint), urlParams, new NetworkRequestListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        Client client = new Gson().fromJson(jsonResponse.toString(), Client.class);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(BundleKey.EXTRA_CLIENT, client);
                        AppController.getInstance().userLogin(client.getId(),client.getUserName(),client
                        .getEmail());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.v(TAG, error.getMessage());
                        login_message.setVisibility(View.VISIBLE);
                    }
                });
            }
            catch (Exception e){

            }
        }
    }
    private void initEndpointAndUrlParams(String email, String password) {
        endpoint = Endpoint.CLIENT+"login/";
        urlParams = new HashMap<>();
        urlParams.put(Config.EMAIL, email);
        urlParams.put(Config.PASSWORD, password);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mLayout.requestFocus();
    }


}
