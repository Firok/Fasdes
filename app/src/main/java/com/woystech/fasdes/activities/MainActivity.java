package com.woystech.fasdes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.woystech.common.ApplicationConstants;
import com.woystech.fasdes.R;
import com.woystech.fasdes.fragments.AccountFragment;
import com.woystech.fasdes.fragments.CategoriesFragment;
import com.woystech.fasdes.fragments.HomeFragment;
import com.woystech.net.application.AppController;
import com.woystech.net.connectivity.NetworkReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        checkConnection();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        Fragment fragment = new HomeFragment();
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        } else {
            Log.v(TAG, "No Activity selected");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.action_home:
                        getSupportActionBar().setTitle(R.string.app_name);
                        fragment = new HomeFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                        return true;
                    case R.id.action_categories:
                        getSupportActionBar().setTitle(R.string.categories);
                        fragment = new CategoriesFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                        return true;
                    case R.id.action_account:
                        getSupportActionBar().setTitle(R.string.account);
                        fragment = new AccountFragment();
                        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    // Method to check connection status
    private void checkConnection() {
        boolean isConnected = NetworkReceiver.isConnected();
        if (!isConnected) {
            Intent intent = new Intent(MainActivity.this, NoNetworkActivity.class);
            startActivityForResult(intent, ApplicationConstants.REQUEST_CODE_NETWORK);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AppController.getInstance().logout();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
