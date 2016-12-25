package holders;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import org.json.JSONObject;

/**
 * Created by samarth on 12/24/16.
 */

public class CardViewHolder {
    public SimpleDraweeView media;

    public void fill(JSONObject jsonObject) {
        if(jsonObject != null) {
            JSONObject imgObj = jsonObject.optJSONObject("images");
            JSONObject thumbObj = imgObj.optJSONObject("fixed_height_small");
            if(thumbObj != null) {
                String url = thumbObj.optString("webp");
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setControllerListener(new ControllerListener<ImageInfo>() {
                            @Override
                            public void onSubmit(String id, Object callerContext) {

                            }

                            @Override
                            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {

                            }

                            @Override
                            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {

                            }

                            @Override
                            public void onIntermediateImageFailed(String id, Throwable throwable) {

                            }

                            @Override
                            public void onFailure(String id, Throwable throwable) {

                            }

                            @Override
                            public void onRelease(String id) {

                            }
                        })
                        .setUri(Uri.parse(url))
                        .setAutoPlayAnimations(true)
                        .build();
                media.setController(controller);
            }
        }
    }
}
