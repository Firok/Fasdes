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
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.woystech.common.ApplicationConstants;
import com.woystech.fasdes.BundleKey;
import com.woystech.fasdes.R;
import com.woystech.fasdes.config.Utilities;
import com.woystech.fasdes.models.Address;
import com.woystech.fasdes.models.Client;
import com.woystech.fasdes.net.Endpoint;
import com.woystech.net.NetworkRequestListener;
import com.woystech.net.NetworkUtilsImpl;
import com.woystech.net.application.AppController;
import com.woystech.net.connectivity.NetworkReceiver;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by firok on 5/20/2017.
 */

public class SignUpSecondStepActivity extends AppCompatActivity {

    private final static String TAG = SignUpSecondStepActivity.class.getSimpleName();

    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.password_repeat)
    EditText confirmPassword;
    @BindView(R.id.country)
    EditText country;
    @BindView(R.id.city)
    EditText city;
    @BindView(R.id.sign_up)
    Button signUp;
    @BindView(R.id.input_layout_password)
    TextInputLayout input_layout_password;
    @BindView(R.id.input_layout_confirm_password)
    TextInputLayout input_layout_confirm_password;
    private String endpoint;
    private Map<String, String> params;

    private Client client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_second_step);
        ButterKnife.bind(this);
        checkConnection();
        resetTextWatcher();
    }

    // Method to check connection status
    private void checkConnection() {
        boolean isConnected = NetworkReceiver.isConnected();
        if (!isConnected) {
            Intent intent = new Intent(SignUpSecondStepActivity.this, NoNetworkActivity.class);
            startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_NETWORK);

        }
    }

    public void resetTextWatcher() {
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (password.getText().equals(editable)) {
                    confirmPassword.setTextColor(Color.RED);
                } else {
                    confirmPassword.setTextColor(Color.BLACK);
                }
            }
        });
    }

    @OnClick(R.id.sign_up)
    public void onSignUp() {
        initEndpointAndUrlParam();
        String PASSWORD = password.getText().toString();
        String CONFIRM_PASSWORD = confirmPassword.getText().toString();
        String CITY = city.getText().toString();
        String COUNTRY = country.getText().toString();

        if (PASSWORD.equals(CONFIRM_PASSWORD))
        {
        client = getIntent().getParcelableExtra(BundleKey.EXTRA_CLIENT);
        client = new Client(client.getUserName(), PASSWORD, client.getPhoneNumber(), client.getEmail(), new Address("", CITY, COUNTRY,"",""));
        try {
            JSONObject params = new JSONObject(new Gson().toJson(client));
            Log.v(TAG, params.toString());
            new NetworkUtilsImpl().sendRequest(Request.Method.POST, new StringBuilder(endpoint), params, new NetworkRequestListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    Log.v(TAG, "Merci");
                    client = new Gson().fromJson(jsonResponse.toString(), Client.class);
                    Log.v(TAG, client.toString());
                    Intent intent = new Intent(SignUpSecondStepActivity.this, MainActivity.class);
                    intent.putExtra(BundleKey.EXTRA_CLIENT, client);
                    // Closing all previous activities
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //store user info in sharePrefrences
                    AppController.getInstance().userLogin(client.getId(),client.getUserName(),client
                            .getEmail());
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(VolleyError error) {
                    Log.v(TAG, error + "Merci");
                }
            });
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
        }}
        else{
            input_layout_password.setError(getString(R.string.password_not_match));
            input_layout_confirm_password.setError(getString(R.string.password_not_match));
        }
    }

    private void initEndpointAndUrlParam() {
        endpoint = Endpoint.CLIENT;

    }

}
