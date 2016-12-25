package com.pukingminion.feeder;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import org.json.JSONObject;

import java.util.List;

import holders.CardViewHolder;

/**
 * Created by samarth on 12/11/16.
 */
public class MediaAdapter extends BaseAdapter {
    private List<JSONObject> dataList;
    private LayoutInflater inflater;

    private CardViewHolder cardViewHolder;
    private Context context;

    public MediaAdapter(Context context, List<JSONObject> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.generic_card_view_layout, parent, false);
            cardViewHolder = new CardViewHolder();
            cardViewHolder.media = (SimpleDraweeView) convertView.findViewById(R.id.media);
            convertView.setTag(cardViewHolder);
        } else {
            cardViewHolder = (CardViewHolder) convertView.getTag();
        }

        cardViewHolder.fill(dataList.get(position));
//        fillCard(position);
        return convertView;
    }

    private void fillCard(int position) {
        JSONObject jsonObject = dataList.get(position);

    }
}
