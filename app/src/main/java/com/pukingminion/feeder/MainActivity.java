package com.pukingminion.feeder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import constants.ApiKeys;
import constants.Urls;
import fragments.FeedFragment;
import network.NetworkListenerCallback;
import network.NetworkUtils;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
    private String giphyApiPath;
    private SwipeFlingAdapterView mediaList;
    private List<JSONObject> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication.initContexualData(getApplicationContext(), this);
        Fresco.initialize(this);

        initUIElements();
    }

    private void initUIElements() {
        giphyApiPath = Urls.GIPHY_HOST + "trending?api_key=" + ApiKeys.GIPHY_API;
        fetchInitialResultsFromAPI();
    }

    private void fetchInitialResultsFromAPI() {
        NetworkUtils.asyncGET(0, giphyApiPath, null, null, new NetworkListenerCallback() {
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