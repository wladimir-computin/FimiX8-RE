package com.fimi.libperson.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fimi.kernel.utils.FontUtil;
import com.fimi.libperson.R;
import com.fimi.libperson.entity.PersonSetting;
import java.util.List;

public class PersonSettingAdapt extends BaseAdapter {
    public static State[] mMainState = new State[]{State.UAV_INSURANCE, State.MESSAGE_NOTICE, State.VERSION_UPDATE, State.CLEAN_CACHE, State.GO_TO_APP_STORE, State.BLACK1, State.SERVICE, State.LANGUAGE, State.BLACK2};
    private boolean isSub = false;
    private Context mContext;
    private List<PersonSetting> mList;

    public enum State {
        UAV_INSURANCE,
        MESSAGE_NOTICE,
        VERSION_UPDATE,
        CLEAN_CACHE,
        GO_TO_APP_STORE,
        BLACK1,
        SERVICE,
        LANGUAGE,
        BLACK2,
        USER_AGREEMENT,
        USER_PRIVACY
    }

    private class ViewHolder {
        ImageView mIvArrow;
        RelativeLayout mRlBg;
        TextView mTvContent;
        TextView mTvItemTitle;
        View mViewDivide;

        private ViewHolder() {
        }

        public View initView(ViewGroup parent) {
            View view = LayoutInflater.from(PersonSettingAdapt.this.mContext).inflate(R.layout.adapt_person_setting, parent, false);
            this.mViewDivide = view.findViewById(R.id.v_divide);
            this.mRlBg = (RelativeLayout) view.findViewById(R.id.rl_bg);
            this.mTvItemTitle = (TextView) view.findViewById(R.id.tv_item_title);
            this.mIvArrow = (ImageView) view.findViewById(R.id.iv_arrow);
            this.mTvContent = (TextView) view.findViewById(R.id.tv_content);
            FontUtil.changeFontLanTing(PersonSettingAdapt.this.mContext.getAssets(), this.mTvContent, this.mTvItemTitle);
            return view;
        }
    }

    public PersonSettingAdapt(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        return this.mList == null ? 0 : this.mList.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void setData(List<PersonSetting> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = holder.initView(parent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (this.mList != null) {
            resetDefaultView(holder, convertView.getLayoutParams());
            State positionIndex = ((PersonSetting) this.mList.get(position)).getSettingAdaptState();
            if (positionIndex == State.UAV_INSURANCE) {
                holder.mTvItemTitle.setText(R.string.person_setting_uav_insurance);
            } else if (positionIndex == State.MESSAGE_NOTICE) {
                holder.mTvItemTitle.setText(R.string.person_setting_message_notice);
                if (((PersonSetting) this.mList.get(getSettingPosition(State.MESSAGE_NOTICE))).getContent() != null) {
                    holder.mTvContent.setText(((PersonSetting) this.mList.get(getSettingPosition(State.MESSAGE_NOTICE))).getContent());
                }
            } else if (positionIndex == State.VERSION_UPDATE) {
                if (((PersonSetting) this.mList.get(getSettingPosition(State.VERSION_UPDATE))).getIsOPen().booleanValue()) {
                    if (((PersonSetting) this.mList.get(getSettingPosition(State.VERSION_UPDATE))).isDisplayTv()) {
                        holder.mTvContent.setVisibility(0);
                    } else {
                        holder.mTvContent.setVisibility(4);
                    }
                }
                holder.mTvItemTitle.setText(R.string.person_setting_version_update);
            } else if (positionIndex == State.CLEAN_CACHE) {
                holder.mTvItemTitle.setText(R.string.person_setting_clean_cache);
                holder.mTvContent.setVisibility(0);
                holder.mTvContent.setText(((PersonSetting) this.mList.get(getSettingPosition(State.CLEAN_CACHE))).getContent());
            } else if (positionIndex == State.GO_TO_APP_STORE) {
                holder.mTvItemTitle.setText(R.string.person_setting_go_to_app_store);
            } else if (positionIndex == State.USER_AGREEMENT) {
                holder.mViewDivide.setVisibility(8);
                holder.mTvItemTitle.setText(R.string.person_setting_user_agreement);
            } else if (positionIndex == State.USER_PRIVACY) {
                holder.mTvItemTitle.setText(R.string.person_setting_user_privacy);
            } else if (positionIndex == State.SERVICE) {
                holder.mViewDivide.setVisibility(8);
                holder.mTvItemTitle.setText(R.string.person_setting_service);
            } else if (positionIndex == State.LANGUAGE) {
                holder.mTvItemTitle.setText(R.string.person_setting_language);
            } else if (positionIndex == State.BLACK1 || positionIndex == State.BLACK2) {
                holder.mViewDivide.setVisibility(8);
                holder.mIvArrow.setVisibility(8);
                LayoutParams params = convertView.getLayoutParams();
                params.height = (int) this.mContext.getResources().getDimension(R.dimen.person_setting_height1);
                holder.mRlBg.setLayoutParams(params);
                holder.mRlBg.setBackgroundResource(R.color.person_setting_bg);
            }
        }
        return convertView;
    }

    private void resetDefaultView(ViewHolder holder, LayoutParams params) {
        holder.mViewDivide.setVisibility(0);
        holder.mIvArrow.setVisibility(0);
        holder.mTvContent.setText("");
        params.height = (int) this.mContext.getResources().getDimension(R.dimen.person_setting_height);
        holder.mRlBg.setLayoutParams(params);
    }

    public int getSettingPosition(State settingAdaptState) {
        for (int i = 0; i < this.mList.size(); i++) {
            if (settingAdaptState == ((PersonSetting) this.mList.get(i)).getSettingAdaptState()) {
                return i;
            }
        }
        return 0;
    }
}
