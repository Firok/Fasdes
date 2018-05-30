package com.woystech.fasdes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.woystech.fasdes.R;
import com.woystech.net.connectivity.NetworkReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by firok on 5/9/2017.
 */

public class NoNetworkActivity extends AppCompatActivity {

    @BindView(R.id.retry)
    Button retry;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.retry)
    public void onClickRetry(){
        boolean isConnected = NetworkReceiver.isConnected();
        if(isConnected){
            setResult(RESULT_OK);
            finish();
        }
    }


}
