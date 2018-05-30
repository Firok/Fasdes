package com.woystech.fasdes.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woystech.fasdes.R;
import com.woystech.net.BundleKey;
import com.woystech.net.application.AppController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by firok on 11/2/2016.
 */

public class AccountFragment extends Fragment {

    private static final String TAG = AccountFragment.class.getSimpleName();

    @BindView(R.id.username)
    TextView username;

    Unbinder unbinder;

    public AccountFragment() {
    }

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        unbinder = ButterKnife.bind(this, view);

        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences();
        username.setText(sharedPreferences.getString(BundleKey.EDITOR_USER_NAME, getString(R.string.login_or_signup)));

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
