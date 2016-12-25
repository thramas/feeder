package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.pukingminion.feeder.MediaAdapter;
import com.pukingminion.feeder.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import constants.Urls;
import network.NetworkListenerCallback;
import network.NetworkUtils;

/**
 * Created by samarth on 12/25/16.
 */

public class FeedFragment extends Fragment {
    private View view;
    int offset = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.feed_fragment_layout, container, false);
        initUIElements();
        return view;
    }

    private void initUIElements() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String response = bundle.getString("response");
            if (!TextUtils.isEmpty(response)) {
                setMediaAdapter(response);
            }
        }
    }

    private void setMediaAdapter(String response) {
        List<JSONObject> dataList = getJSONfromResponse(response);
        initSwipeAdapter(dataList);
    }

    private List<JSONObject> getJSONfromResponse(String response) {
        JSONArray mArray = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            mArray = jsonObject.optJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnListFromArray(mArray);
    }

    private void initSwipeAdapter(final List<JSONObject> dataList) {
        SwipeFlingAdapterView mediaList = (SwipeFlingAdapterView) view.findViewById(R.id.media_list);
        final MediaAdapter mAdapter = new MediaAdapter(getContext(), dataList);
        mediaList.setAdapter(mAdapter);
        mediaList.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                dataList.remove(0);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Log.d("SwipeTest",String.valueOf(dataList.size()));

            }

            @Override
            public void onRightCardExit(Object o) {
                Log.d("SwipeTest",String.valueOf(dataList.size()));
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                NetworkUtils.asyncGET(0, Urls.GIPHY_TRENDING_API + "&offset=" + String.valueOf(++offset), null, null, new NetworkListenerCallback() {
                    @Override
                    public void onNetworkRequestSuccess(int requestCode, String response) {
                        List<JSONObject> mList = getJSONfromResponse(response);
                        if(mList != null) {
                            for (int j = 0; j < mList.size(); j++) {
                                dataList.add(mList.get(j));
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onNetworkRequestFailure(int requestCode, NetworkUtils.NetworkException e) {

                    }
                });
            }

            @Override
            public void onScroll(float v) {

            }
        });
    }

    private List<JSONObject> returnListFromArray(JSONArray mArray) {
        if(mArray == null) return null;
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
