package com.pukingminion.feeder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

        Fresco.initialize(this);
        initUIElements();
    }


    private void initUIElements() {
        mediaList = (SwipeFlingAdapterView) findViewById(R.id.media_list);
        mediaList.setVisibility(View.GONE);
        giphyApiPath = "http://api.giphy.com/v1/gifs/trending?api_key=dc6zaTOxFJmzC";

        MyApplication.initContexualData(getApplicationContext(),this);
//        MediaAdapter mAdapter = new MediaAdapter();
//        mAdapter.setValues(MyApplication.mContext,dataList);
//        mediaList.setAdapter(mAdapter);

        NetworkUtils.asyncGET(0, giphyApiPath, null, null, new NetworkListenerCallback() {
            @Override
            public void onNetworkRequestSuccess(int requestCode, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray mArray = jsonObject.optJSONArray("data");
                    fetchResults(mArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetworkRequestFailure(int requestCode, NetworkUtils.NetworkException e) {

            }
        });
    }

    private void fetchResults(JSONArray mArray) {
        dataList = returnListFromArray(mArray);
        final MediaAdapter mAdapter = new MediaAdapter();
        mediaList.setVisibility(View.VISIBLE);
        mAdapter.setValues(MyApplication.mContext,dataList);
        if(mediaList != null) {
            mediaList.setAdapter(mAdapter);
            mediaList.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                @Override
                public void removeFirstObjectInAdapter() {
                    dataList.remove(0);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onLeftCardExit(Object o) {

                }

                @Override
                public void onRightCardExit(Object o) {

                }

                @Override
                public void onAdapterAboutToEmpty(int i) {

                }

                @Override
                public void onScroll(float v) {

                }
            });
        }
    }

    private List<JSONObject> returnListFromArray(JSONArray mArray) {
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < mArray.length(); i++) {
            try {
                JSONObject jsonObject = mArray.getJSONObject(i);
                list.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
