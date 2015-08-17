package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.squareup.picasso.Picasso;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.ContactActivity;
import com.think.linxuanxuan.ecos.model.ModelUtils;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.utils.RoundAngleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzjixinyu on 2015/7/29.
 */
public class ContactAdapter extends BaseAdapter {
    private Context mcontext;
    private List<IMMessage> messageList = new ArrayList<>();
    private String targetIMID;
    private String targetAvatarUrl;

    public ContactAdapter(Context context, List<IMMessage> messageList, String targetIMID, String targetAvatarUrl) {
        this.mcontext = context;
        this.messageList = messageList;
        this.targetIMID = targetIMID;
        this.targetAvatarUrl = targetAvatarUrl;
    }


    class ViewHolder {

        private RoundAngleImageView iv_avatar;
        private TextView tv_text;
        private RoundAngleImageView iv_avatar2;
        private TextView tv_text2;
        private LinearLayout ll_me;
        private LinearLayout ll_other;
        private TextView tv_time;
        private TextView tv_time2;

        public ViewHolder(View root) {
            iv_avatar = (RoundAngleImageView) root.findViewById(R.id.iv_avatar);
            tv_text = (TextView) root.findViewById(R.id.tv_text);
            iv_avatar2 = (RoundAngleImageView) root.findViewById(R.id.iv_avatar2);
            tv_text2 = (TextView) root.findViewById(R.id.tv_text2);
            ll_me = (LinearLayout) root.findViewById(R.id.ll_me);
            ll_other = (LinearLayout) root.findViewById(R.id.ll_other);
            tv_time=(TextView)root.findViewById(R.id.tv_time);
            tv_time2=(TextView)root.findViewById(R.id.tv_time2);

//            iv_avatar.setDefaultImageResId(R.mipmap.bg_female_default);
//            iv_avatar.setErrorImageResId(R.mipmap.bg_nogender_default);
//            iv_avatar2.setDefaultImageResId(R.mipmap.bg_female_default);
//            iv_avatar2.setErrorImageResId(R.mipmap.bg_nogender_default);
//            RequestQueue queue = MyApplication.getRequestQueue();
//            ImageLoader.ImageCache imageCache = new SDImageCache();
//            ImageLoader imageLoader = new ImageLoader(queue, imageCache);
            if (UserDataService.getSingleUserDataService(mcontext).getUser().avatarUrl != null && !TextUtils.isEmpty(UserDataService.getSingleUserDataService(mcontext).getUser().avatarUrl)){
                Picasso.with(mcontext).load(UserDataService.getSingleUserDataService(mcontext).getUser().avatarUrl).placeholder(R.mipmap.bg_female_default).error(R.mipmap.bg_female_default).into(iv_avatar);
            }else {
                iv_avatar.setImageResource(R.mipmap.bg_female_default);
            }
            if (targetAvatarUrl != null && !TextUtils.isEmpty(targetAvatarUrl)){
                Picasso.with(mcontext).load(targetAvatarUrl).placeholder(R.mipmap.bg_female_default).error(R.mipmap.bg_female_default).into(iv_avatar2);
            }else {
                iv_avatar2.setImageResource(R.mipmap.bg_female_default);
            }
//                iv_avatar.setImageUrl(UserDataService.getSingleUserDataService(mcontext).getUser().avatarUrl, imageLoader);

        }

        /**
         * ��������δ��
         */
        public void setData(int position, Boolean isMe) {
            IMMessage item = messageList.get(position);
            tv_text.setText(ContactActivity.getMessageContentByJSONString(item.getContent()));
            tv_text2.setText(ContactActivity.getMessageContentByJSONString(item.getContent()));
            tv_time.setText(ModelUtils.getDateDescription(item.getTime()));
            tv_time2.setText(ModelUtils.getDateDescription(item.getTime()));
            if (isMe) {
                ll_other.setVisibility(View.GONE);
                ll_me.setVisibility(View.VISIBLE);
            } else {
                ll_me.setVisibility(View.GONE);
                ll_other.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * ����Ԫ��
     *
     * @param
     */
    public void add(IMMessage message) {
        messageList.add(message);
    }

    //TODO
    @Override
    public int getCount() {
        return messageList.size();
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

        //判断
        Boolean isMe = !TextUtils.equals(messageList.get(position).getFromAccount(), targetIMID);
        Log.v("contact", messageList.get(position).getFromAccount());
        Log.v("contact", targetIMID);
        if (convertView == null) {
            convertView = parent.inflate(mcontext, R.layout.item_contact_me, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(position, isMe);

        return convertView;
    }

}
