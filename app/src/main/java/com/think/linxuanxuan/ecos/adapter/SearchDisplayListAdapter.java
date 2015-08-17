package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.CommentDetailActivity;
import com.think.linxuanxuan.ecos.activity.DisplayDetailActivity;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.activity.PersonageDetailActivity;
import com.think.linxuanxuan.ecos.model.Comment;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.request.user.FollowUserRequest;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;
import com.think.linxuanxuan.ecos.views.ExtensibleListView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Think on 2015/8/11.
 */
public class SearchDisplayListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mcontext;
    private List<Share> shareList;
    private FollowUserRequest request;

    public static int mInstances = 0;

    public SearchDisplayListAdapter(Context context, List<Share> shareList) {
        this.mcontext = context;
        this.shareList = shareList;
        request = new FollowUserRequest();
        mInstances++;
    }

    class ViewHolder {

        private RoundImageView iv_avatar;
        private TextView tv_name;
        private TextView tv_focus;

        private ImageView iv_cover;
        private TextView tv_coverNum;
        private TextView tv_coverTitle;
        private TextView tv_coverTime;

        private TextView tv_praise;
        private TextView tv_evaluation;
        private LinearLayout ll_praise;

        private ExtensibleListView lv_evaluation;
        private DisplayItemEvalutionViewAdapter adapter;

        public ViewHolder(View root) {
            iv_avatar = (RoundImageView) root.findViewById(R.id.iv_avatar);
            tv_name = (TextView) root.findViewById(R.id.tv_name);
            tv_focus = (TextView) root.findViewById(R.id.tv_focus);
            iv_cover = (ImageView) root.findViewById(R.id.iv_cover);
            tv_coverNum = (TextView) root.findViewById(R.id.tv_coverNum);
            tv_coverTitle = (TextView) root.findViewById(R.id.tv_coverTitle);
            tv_coverTime = (TextView) root.findViewById(R.id.tv_coverTime);
            tv_praise = (TextView) root.findViewById(R.id.tv_praise);
            tv_evaluation = (TextView) root.findViewById(R.id.tv_evaluation);
            ll_praise = (LinearLayout) root.findViewById(R.id.ll_praise);
            tv_praise = (TextView) root.findViewById(R.id.tv_praise);
            lv_evaluation = (ExtensibleListView) root.findViewById(R.id.lv_evaluation);
        }

        public void setData(final int position) {
            Share item = shareList.get(position);

            //bind the data
            if (item.avatarUrl != null && !item.avatarUrl.equals("")) {
                iv_avatar.setDefaultImageResId(R.mipmap.bg_female_default);
                iv_avatar.setErrorImageResId(R.mipmap.bg_female_default);
                RequestQueue queue = MyApplication.getRequestQueue();
                ImageLoader.ImageCache imageCache = new SDImageCache(300, 200);
                ImageLoader imageLoader = new ImageLoader(queue, imageCache);
                iv_avatar.setImageUrl(item.avatarUrl, imageLoader);
            } else
                iv_avatar.setImageResource(R.mipmap.bg_female_default);

            tv_name.setText(item.nickname);
            if (item.hasAttention)
                tv_focus.setText("已关注");
            else
                tv_focus.setVisibility(View.GONE);
            tv_focus.setTextColor(mcontext.getResources().getColor(item.hasAttention ? R.color.text_gray : R.color.text_white));
            tv_focus.setBackgroundResource(item.hasAttention ? R.drawable.btn_focus_gray : R.drawable.btn_focus_pink);

            if (item.coverUrl != null && !TextUtils.isEmpty(item.coverUrl))
                Picasso.with(mcontext).load(item.coverUrl).placeholder(R.drawable.img_default).into(iv_cover);
            else
                iv_cover.setImageResource(R.drawable.img_default);
            tv_coverNum.setText(item.totalPageNumber + mcontext.getResources().getString(R.string.page));
            tv_coverTitle.setText(item.title);
            tv_coverTime.setText(item.getDateDescription());

            tv_praise.setText(item.praiseNum + mcontext.getResources().getString(R.string.manyFavor));
            tv_evaluation.setText(item.commentNum + mcontext.getResources().getString(R.string.manyComment));

            adapter = new DisplayItemEvalutionViewAdapter(mcontext, item.commentList);
            lv_evaluation.setAdapter(adapter);

            //set tag
//            tv_focus.setTag(position);
            iv_cover.setTag(position);
            tv_coverTitle.setTag(position);
            ll_praise.setTag(position);
            tv_evaluation.setTag(position);
            iv_avatar.setTag(position);
            tv_name.setTag(position);

            //set listener
//            tv_focus.setOnClickListener(DisplayListViewAdapter.this);
            iv_avatar.setOnClickListener(SearchDisplayListAdapter.this);
            tv_name.setOnClickListener(SearchDisplayListAdapter.this);
            iv_cover.setOnClickListener(SearchDisplayListAdapter.this);
            tv_coverTitle.setOnClickListener(SearchDisplayListAdapter.this);
            ll_praise.setOnClickListener(SearchDisplayListAdapter.this);
            tv_evaluation.setOnClickListener(SearchDisplayListAdapter.this);
            lv_evaluation.setOnItemClickListener(new itemListener(position));

            if (!item.hasPraised) {
                //TODO 未赞图片
            }
        }
    }

    @Override
    public int getCount() {
        return shareList.size();
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
        if (convertView == null || convertView.getTag() instanceof Boolean) {
            convertView = parent.inflate(mcontext, R.layout.item_display_replace, null);
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
            case R.id.iv_avatar:
            case R.id.tv_name:
            case R.id.ll_author:
                intent = new Intent(mcontext, PersonageDetailActivity.class);
                bundle.putString(PersonageDetailActivity.UserID, shareList.get(position).userId);
                bundle.putBoolean(PersonageDetailActivity.IsOwn, false);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
                break;
            case R.id.iv_cover:
            case R.id.tv_coverTitle:
                intent = new Intent(mcontext, DisplayDetailActivity.class);
                bundle.putString(DisplayDetailActivity.ShareId, shareList.get(position).shareId);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
                break;
            case R.id.tv_evaluation:
            case R.id.lv_evaluation:
                intent = new Intent(mcontext, CommentDetailActivity.class);
                bundle.putString(CommentDetailActivity.FromId, shareList.get(position).shareId);
                bundle.putString(CommentDetailActivity.CommentType, Comment.CommentType.分享.getBelongs());
                bundle.putBoolean(CommentDetailActivity.IsPraised, shareList.get(position).hasPraised);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
                break;
        }
    }

    class itemListener implements AdapterView.OnItemClickListener {
        int p;

        public itemListener(int p) {
            this.p = p;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(mcontext, CommentDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(CommentDetailActivity.FromId, shareList.get(p).shareId);
            bundle.putString(CommentDetailActivity.CommentType, Comment.CommentType.分享.getBelongs());
            bundle.putBoolean(CommentDetailActivity.IsPraised, shareList.get(p).hasPraised);
            intent.putExtras(bundle);
            mcontext.startActivity(intent);
        }
    }

    public List<Share> getShareList() {
        return shareList;
    }

    public void setShareList(List<Share> shareList) {
        this.shareList = shareList;
    }

    @Override
    public void finalize() {
        mInstances--;
        Log.i("SearchDisplayList", "DisplayListViewAdapter，销毁后有" + mInstances + "个对象");
    }
}

