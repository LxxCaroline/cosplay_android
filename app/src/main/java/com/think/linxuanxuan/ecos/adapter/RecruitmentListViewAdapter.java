package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.ContactActivity;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.activity.PersonageDetailActivity;
import com.think.linxuanxuan.ecos.activity.RecruitmentDetailActivity;
import com.think.linxuanxuan.ecos.model.Recruitment;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.utils.RoundAngleImageView;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hzjixinyu on 2015/7/23.
 */
public class RecruitmentListViewAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mcontext;
    private List<Recruitment> recruitmentArrayList;

    public RecruitmentListViewAdapter(Context context, List<Recruitment> recruitmentArrayList) {
        this.mcontext = context;
        this.recruitmentArrayList = recruitmentArrayList;
    }

    class ViewHolder {

        private RoundImageView iv_avatar;
        private TextView tv_name;
        private TextView tv_distance;
        private TextView tv_price;
        private TextView tv_talk;
        private RoundAngleImageView iv_cover;
        private ImageView genderImVw;

        public ViewHolder(View root) {
            iv_avatar = (RoundImageView) root.findViewById(R.id.iv_avatar);
            tv_name = (TextView) root.findViewById(R.id.tv_name);
            tv_distance = (TextView) root.findViewById(R.id.tv_distance);
            tv_price = (TextView) root.findViewById(R.id.tv_price);
            tv_talk = (TextView) root.findViewById(R.id.tv_talk);
            iv_cover = (RoundAngleImageView) root.findViewById(R.id.iv_cover);
            genderImVw = (ImageView) root.findViewById(R.id.genderImVw);
        }

        /**
         * 传入数据未定
         */
        public void setData(int position) {
            //set the data for each widget
            iv_avatar.setDefaultImageResId(R.mipmap.bg_female_default);
            iv_avatar.setErrorImageResId(R.mipmap.bg_female_default);
            if (recruitmentArrayList.get(position).avatarUrl != null && !recruitmentArrayList.get(position).avatarUrl.equals("")) {
                RequestQueue queue = MyApplication.getRequestQueue();
                ImageLoader.ImageCache imageCache = new SDImageCache();
                ImageLoader imageLoader = new ImageLoader(queue, imageCache);
                iv_avatar.setImageUrl(recruitmentArrayList.get(position).avatarUrl, imageLoader);
            } else
                iv_avatar.setImageResource(R.mipmap.bg_female_default);
            if (recruitmentArrayList.get(position).coverUrl != null &&  !TextUtils.isEmpty(recruitmentArrayList.get(position).coverUrl))
                Picasso.with(mcontext).load(recruitmentArrayList.get(position).coverUrl).placeholder(R.drawable.img_default).into(iv_cover);
            else
                iv_cover.setImageResource(R.drawable.img_default);
            tv_name.setText(recruitmentArrayList.get(position).nickname);
            tv_distance.setText(recruitmentArrayList.get(position).distanceKM + " " + mcontext.getResources().getString(R.string.KM));
            tv_price.setText(recruitmentArrayList.get(position).averagePrice.substring(0, recruitmentArrayList.get(position).averagePrice.length() - 2) + " " + recruitmentArrayList.get(position).recruitType.getPriceUnit());
            genderImVw.setImageResource(recruitmentArrayList.get(position).gender == User.Gender.男 ? R.mipmap.ic_male_line : R.mipmap.ic_female_line);
            //set tag
            tv_talk.setTag(position);
            iv_cover.setTag(position);
            iv_avatar.setTag(position);
            tv_name.setTag(position);
            //set listener
            tv_name.setOnClickListener(RecruitmentListViewAdapter.this);
            iv_avatar.setOnClickListener(RecruitmentListViewAdapter.this);
            iv_cover.setOnClickListener(RecruitmentListViewAdapter.this);
            tv_talk.setOnClickListener(RecruitmentListViewAdapter.this);

            if (TextUtils.equals(recruitmentArrayList.get(position).userId, UserDataService.getSingleUserDataService(mcontext).getUser().userId)){
                tv_talk.setVisibility(View.GONE);
            }else{
                tv_talk.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getCount() {
        return recruitmentArrayList.size();
    }


    @Override
    public Object getItem(int position) {
        return recruitmentArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = parent.inflate(mcontext, R.layout.item_recruitment_type, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(position);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle = new Bundle();
        int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.tv_name:
            case R.id.iv_avatar:
            case R.id.ll_author:
                intent = new Intent(mcontext, PersonageDetailActivity.class);
                bundle.putString(PersonageDetailActivity.UserID, recruitmentArrayList.get(position).userId);
                bundle.putBoolean(PersonageDetailActivity.IsOwn, false);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
                break;
            case R.id.iv_cover:
                intent = new Intent(mcontext, RecruitmentDetailActivity.class);
                intent.putExtras(bundle);
                bundle.putString(RecruitmentDetailActivity.RecruitID, recruitmentArrayList.get(position).recruitmentId);
                bundle.putString(RecruitmentDetailActivity.RecruitType, recruitmentArrayList.get(position).recruitType.getValue());
                bundle.putString(RecruitmentDetailActivity.UserId, recruitmentArrayList.get(position).userId);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
                break;
            case R.id.tv_talk:
                intent = new Intent(mcontext, ContactActivity.class);
                bundle.putString(ContactActivity.TargetUserID, recruitmentArrayList.get(position).userId);
                bundle.putString(ContactActivity.TargetUserName, recruitmentArrayList.get(position).nickname);
                bundle.putString(ContactActivity.TargetUserAvatar, recruitmentArrayList.get(position).avatarUrl);
                bundle.putString(ContactActivity.TargetUserIMID, recruitmentArrayList.get(position).imId);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
                break;
        }
    }

    public List<Recruitment> getRecruitmentArrayList() {
        return recruitmentArrayList;
    }

    public void setRecruitmentArrayList(List<Recruitment> recruitmentArrayList) {
        this.recruitmentArrayList = recruitmentArrayList;
    }
}
