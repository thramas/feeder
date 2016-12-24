package holders;

import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

/**
 * Created by samarth on 12/24/16.
 */

public class CardViewHolder {
    public SimpleDraweeView media;
    public SimpleDraweeView profileImage;
    public TextView profileName;
    public TextView time;

    public void fill(JSONObject jsonObject) {
        if(jsonObject != null) {
            JSONObject imgObj = jsonObject.optJSONObject("images");
            JSONObject thumbObj = imgObj.optJSONObject("fixed_height_small");
            if(thumbObj != null) {
                String url = thumbObj.optString("webp");
                profileImage.setImageURI(url);
            }
        }
    }
}
