package com.fimi.app.x8s.viewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.fimi.app.x8s.R;
import com.fimi.app.x8s.entity.PhotoParamItemEntity;

public class PhotoParamsViewHolder extends ViewHolder {
    public final String VALUE_VIDEO_RESOLUTION_1080P = "1920x1080";
    public final String VALUE_VIDEO_RESOLUTION_2K = "2560x1440";
    public final String VALUE_VIDEO_RESOLUTION_4K = "3840x2160";
    public final String VALUE_VIDEO_RESOLUTION_720P = "1280x720";
    private Context context;
    private ImageView gotoIcon;
    private TextView keytv;
    private TextView valuetv;

    public PhotoParamsViewHolder(View itemView) {
        super(itemView);
        this.context = itemView.getContext();
        this.keytv = (TextView) itemView.findViewById(R.id.item_name);
        this.valuetv = (TextView) itemView.findViewById(R.id.item_value);
        this.gotoIcon = (ImageView) itemView.findViewById(R.id.goto_icon);
    }

    public void initItemData(PhotoParamItemEntity itemEntity, boolean isEnable) {
        if (itemEntity != null) {
            this.keytv.setText(itemEntity.getNickName());
            if (itemEntity.getNickParam() != null) {
                this.valuetv.setText(itemEntity.getNickParam());
            } else if (itemEntity.getParamKey().equals("video_resolution")) {
                updateResolutionValue(itemEntity.getParamValue());
            } else {
                this.valuetv.setText(itemEntity.getParamValue());
            }
        }
    }

    public void updateResolutionValue(String modelValue) {
        if (modelValue != null && !"".equals(modelValue)) {
            String[] values = modelValue.split("\\s+");
            values[1] = values[1].replace("P", "FPS");
            if (values[0].equals("3840x2160")) {
                modelValue = "4K " + values[1];
            } else if (values[0].equals("2560x1440")) {
                modelValue = "2.5K " + values[1];
            } else if (values[0].equals("1920x1080")) {
                modelValue = "1080P " + values[1];
            } else if (values[0].equals("1280x720")) {
                modelValue = "720P " + values[1];
            }
            this.valuetv.setText(modelValue);
        }
    }
}
