package com.pukingminion.feeder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;

import constants.Urls;
import fragments.FeedFragment;
import network.NetworkListenerCallback;
import network.NetworkUtils;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.initContexualData(getApplicationContext(), this);
        Fresco.initialize(this);

        initUIElements();
    }

    private void initUIElements() {
        fetchInitialResultsFromAPI();
    }

    private void fetchInitialResultsFromAPI() {
        NetworkUtils.asyncGET(0, Urls.GIPHY_TRENDING_API, null, null, new NetworkListenerCallback() {
            @Override
            public void onNetworkRequestSuccess(int requestCode, String response) {
                openFeedFragment(response);
            }

            @Override
            public void onNetworkRequestFailure(int requestCode, NetworkUtils.NetworkException e) {

            }
        });
    }

    private void openFeedFragment(String response) {
        FragmentManager fragmentManager = ((FragmentActivity) this).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new FeedFragment();
        Bundle bundle = new Bundle();
        bundle.putString("response", response);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(android.R.id.content, fragment, "fragment-tag-" + "");
        fragmentTransaction.addToBackStack("fragment-tag-" + "").commitAllowingStateLoss();
    }


}