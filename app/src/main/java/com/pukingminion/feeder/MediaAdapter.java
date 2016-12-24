package com.pukingminion.feeder;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;

import org.json.JSONArray;
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
            convertView = inflater.inflate(R.layout.generic_card_view_layout, parent, false);
            cardViewHolder = new CardViewHolder();
            cardViewHolder.media = (SimpleDraweeView) convertView.findViewById(R.id.media);
            cardViewHolder.profileImage = (SimpleDraweeView) convertView.findViewById(R.id.profile_image);
            cardViewHolder.profileName = (TextView) convertView.findViewById(R.id.profile_name);
            cardViewHolder.time = (TextView) convertView.findViewById(R.id.time_notif);
            convertView.setTag(cardViewHolder);
        } else {
            cardViewHolder = (CardViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = dataList.get(position);
        if(jsonObject != null) {
            JSONObject imgObj = jsonObject.optJSONObject("images");
            JSONObject thumbObj = imgObj.optJSONObject("fixed_height_small");
            if(thumbObj != null) {
                String url = thumbObj.optString("webp");
                ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(
                            String id,
                            @Nullable ImageInfo imageInfo,
                            @Nullable Animatable anim) {
                        if (imageInfo == null) {
                            return;
                        }
                        QualityInfo qualityInfo = imageInfo.getQualityInfo();
                        FLog.d("Final image received! " +
                                        "Size %d x %d",
                                "Quality level %d, good enough: %s, full quality: %s",
                                imageInfo.getWidth(),
                                imageInfo.getHeight(),
                                qualityInfo.getQuality(),
                                qualityInfo.isOfGoodEnoughQuality(),
                                qualityInfo.isOfFullQuality());
                    }

                    @Override
                    public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
//                        FLog.d("Intermediate image received");
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        FLog.e(getClass(), throwable, "Error loading %s", id);
                    }
                };
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setControllerListener(controllerListener)
                        .setUri(Uri.parse(url))
                        .setAutoPlayAnimations(true)
                        // other setters
                        .build();
                cardViewHolder.media.setController(controller);
            }
        }
        return convertView;
    }

    public void setValues(Context mContext, List<JSONObject> dataList) {
        this.dataList = dataList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
