package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.ContactActivity;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.activity.NormalListViewActivity;
import com.think.linxuanxuan.ecos.activity.PersonageDetailActivity;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by hzjixinyu on 2015/8/4.
 */
public class EventWantGoAdapter extends BaseAdapter {
    private Context mcontext;
    private int TYPE = 1;
    private List<User> userList;
    private ArrayList<Boolean> hasFollowEd;
    private ArrayList<Boolean> beFollowed;
    //for NetWorkImageView
    static ImageLoader.ImageCache imageCache;
    RequestQueue queue;
    ImageLoader imageLoader;

    //get login user data
    private UserDataService mUserDataService;
    private User mUserData;

    public EventWantGoAdapter(Context context, int type, List<User> userList) {
        this.mcontext = context;
        this.TYPE = type;
        this.userList = userList;
        initData();
    }

    public EventWantGoAdapter(Context context, int type, List<User> userList, ArrayList<Boolean> hasFollowEd, ArrayList<Boolean> beFollowed) {
        this.mcontext = context;
        this.TYPE = type;
        this.userList = userList;
        this.hasFollowEd = hasFollowEd;
        this.beFollowed = beFollowed;
        initData();

    }

    private void initData() {
        queue = MyApplication.getRequestQueue();
        imageCache = new SDImageCache();
        imageLoader = new ImageLoader(queue, imageCache);
        mUserDataService = UserDataService.getSingleUserDataService(mcontext);
        mUserData = mUserDataService.getUser();
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_activity_wantgo, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setTag(position);
        setData(position, parent, viewHolder);

        return convertView;
    }

    public void setData(final int position, ViewGroup parent, ViewHolder viewHolder) {
        User item = userList.get(position);

        //set avatar
        if (item.avatarUrl != null && !item.avatarUrl.equals(""))
            viewHolder.iv_avatar.setImageUrl(item.avatarUrl, imageLoader);
        viewHolder.iv_avatar.setDefaultImageResId(R.mipmap.bg_female_default);
        viewHolder.iv_avatar.setErrorImageResId(R.mipmap.bg_female_default);
        //set nick name
        viewHolder.tv_name.setText(item.nickname);
        //set signature
        if (item.characterSignature != null && !item.characterSignature.equals("")){
            viewHolder.tv_signature.setText(item.characterSignature);
            Log.v("11111111111","1111111111");
        }

        else
            viewHolder.tv_signature.setText(mcontext.getResources().getString(R.string.noSignature));
        if (!item.userId.equals(mUserData.userId)) {
            //set contact
            if (TYPE == NormalListViewActivity.TYPE_EVENT_ATTENTION) {
                viewHolder.tv_contact.setText("私信");
            }
            if (TYPE == NormalListViewActivity.TYPE_EVENT_WANTGO) {
                viewHolder.tv_contact.setText("戳一下");
            }
            if (TYPE == NormalListViewActivity.TYPE_EVENT_FANS) {
                viewHolder.tv_contact.setText("私信");
            }
            viewHolder.tv_contact.setVisibility(View.VISIBLE);
        } else
            viewHolder.tv_contact.setVisibility(View.GONE);

        //set tags
        viewHolder.ll_tagList.removeAllViews();
        Set<User.RoleType> roleTypeList = item.roleTypeSet;
        Iterator i = roleTypeList.iterator();
        int num = 0;
        while (i.hasNext() && num < 3) {
            User.RoleType type = (User.RoleType) i.next();
            View v = parent.inflate(mcontext, R.layout.item_tag, null);
            ((TextView) v.findViewById(R.id.tv_tag)).setText(type.name());
            viewHolder.ll_tagList.addView(v);
            num++;
        }
        if (num == 0) {
            viewHolder.ll_tagList.setVisibility(View.GONE);
        } else {
            viewHolder.ll_tagList.setVisibility(View.VISIBLE);
        }
        if (num==3&&i.hasNext()){
            viewHolder.ll_tagList.removeViewAt(2);
            View v = parent.inflate(mcontext, R.layout.item_tag, null);
            ((TextView) v.findViewById(R.id.tv_tag)).setText("· · ·");
            viewHolder.ll_tagList.addView(v);
        }

        if (TYPE == NormalListViewActivity.TYPE_EVENT_WANTGO) {
            if (hasFollowEd.get(position).booleanValue() && beFollowed.get(position).booleanValue()) {
                viewHolder.iv_relation.setImageResource(R.mipmap.ic_contact_friend);
                viewHolder.iv_relation.setVisibility(View.VISIBLE);
            } else if (hasFollowEd.get(position)) {
                viewHolder.iv_relation.setImageResource(R.mipmap.ic_contact_attention);
                viewHolder.iv_relation.setVisibility(View.VISIBLE);
            } else {
                viewHolder.iv_relation.setVisibility(View.GONE);
            }
        }

        if (TextUtils.equals(userList.get(position).userId, UserDataService.getSingleUserDataService(mcontext).getUser().userId)) {
            viewHolder.iv_relation.setVisibility(View.GONE);
        } else {
            viewHolder.iv_relation.setVisibility(View.VISIBLE);
        }
    }

    class ViewHolder implements View.OnClickListener {

        private RoundImageView iv_avatar;
        private ImageView iv_relation;
        private LinearLayout ll_tagList;
        private TextView tv_name;
        private TextView tv_signature;
        private TextView tv_contact;

        public ViewHolder(View root) {
            iv_avatar = (RoundImageView) root.findViewById(R.id.iv_avatar);
            iv_relation = (ImageView) root.findViewById(R.id.iv_relation);
            ll_tagList = (LinearLayout) root.findViewById(R.id.ll_tagList);
            tv_name = (TextView) root.findViewById(R.id.tv_name);
            tv_signature = (TextView) root.findViewById(R.id.tv_signature);
            tv_contact = (TextView) root.findViewById(R.id.tv_contact);

            iv_avatar.setOnClickListener(this);
            tv_contact.setOnClickListener(this);
            ll_tagList.setOnClickListener(this);
        }

        public void setTag(int position) {
            iv_avatar.setTag(position);
            tv_contact.setTag(position);
        }


        @Override
        public void onClick(View v) {
            Intent intent;
            Bundle bundle = new Bundle();
            switch (v.getId()) {
                case R.id.ll_tagsList:
                case R.id.iv_avatar:
                    intent = new Intent(mcontext, PersonageDetailActivity.class);
                    bundle.putString(PersonageDetailActivity.UserID, userList.get((int) v.getTag()).userId);
                    bundle.putBoolean(PersonageDetailActivity.IsOwn, false);
                    intent.putExtras(bundle);
                    break;
                case R.id.tv_contact:
                    intent = new Intent(mcontext, ContactActivity.class);
                    bundle.putString(ContactActivity.TargetUserID, userList.get((int) v.getTag()).userId);
                    bundle.putString(ContactActivity.TargetUserAvatar, userList.get((int) v.getTag()).avatarUrl);
                    bundle.putString(ContactActivity.TargetUserName, userList.get((int) v.getTag()).nickname);
                    bundle.putString(ContactActivity.TargetUserIMID, userList.get((int) v.getTag()).imId);
                    Log.v("contact", "targetIMID--------   " + userList.get((int) v.getTag()).imId);
                    Log.v("contact", "targetID--------   " + userList.get((int) v.getTag()).userId);
                    intent.putExtras(bundle);
                    break;
                default:
                    return;
            }
            mcontext.startActivity(intent);
        }
    }
}
