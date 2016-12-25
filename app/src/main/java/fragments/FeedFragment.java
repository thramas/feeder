package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

/**
 * Created by samarth on 12/25/16.
 */

public class FeedFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.feed_fragment_layout, container,false);
        initUIElements();
        return view;
    }

    private void initUIElements() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            String response = bundle.getString("response");
            if(!TextUtils.isEmpty(response)){
                setMediaAdapter(response);
            }
        }
    }

    private void setMediaAdapter(String response) {
        List<JSONObject> dataList;
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray mArray = jsonObject.optJSONArray("data");
            dataList = returnListFromArray(mArray);
            initSwipeAdapter(dataList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initSwipeAdapter(final List<JSONObject> dataList) {
        SwipeFlingAdapterView mediaList = (SwipeFlingAdapterView) view.findViewById(R.id.media_list);
        final MediaAdapter mAdapter = new MediaAdapter(getContext(),dataList);
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
