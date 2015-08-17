package com.think.linxuanxuan.ecos.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.activity.PersonageDetailActivity;
import com.think.linxuanxuan.ecos.model.Comment;
import com.think.linxuanxuan.ecos.utils.RoundImageView;
import com.think.linxuanxuan.ecos.utils.SDImageCache;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Think on 2015/7/22.
 */
public class WorkDetailListViewAdapter extends BaseAdapter implements View.OnClickListener {
    private boolean isDetail = false;
    private Context mcontext;
    private List<Comment> commentList;
    //for NetWorkImageView
    static ImageLoader.ImageCache imageCache;
    RequestQueue queue;
    private int commentCount = 0;

    public WorkDetailListViewAdapter(Context context, boolean isDetail) {
        this.mcontext = context;
        //init the data for NetWorkImageView
        commentList = new ArrayList<>();
        queue = MyApplication.getRequestQueue();
        imageCache = new SDImageCache();
        this.isDetail = isDetail;
    }

    public void updateCommentList(List<Comment> commentList) {
        this.commentList.clear();
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new CommentViewHolder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_comment_detail_more, null);
            viewHolder.setImageView((RoundImageView) convertView.findViewById(R.id.commentPersonImgVw));
            viewHolder.setNameTxVw((TextView) convertView.findViewById(R.id.commentPersonNameTxVw));
            viewHolder.setCommentTxVw((TextView) convertView.findViewById(R.id.commentContentTxVw));
            viewHolder.setAllCommentTxVw((TextView) convertView.findViewById(R.id.all_commentTxVw));
            viewHolder.setTime((TextView)convertView.findViewById(R.id.tv_time));
            convertView.setTag(viewHolder);
        } else
            viewHolder = (CommentViewHolder) convertView.getTag();
        setData(viewHolder, position);
        return convertView;
    }

    void setData(CommentViewHolder viewHolder, int position) {
        ImageLoader imageLoader = new ImageLoader(queue, imageCache);
        if (commentList.get(position).avatarUrl != null && !commentList.get(position).avatarUrl.equals(""))
            viewHolder.imageView.setImageUrl(commentList.get(position).avatarUrl, imageLoader);
        else
            viewHolder.imageView.setImageResource(R.mipmap.bg_female_default);
        viewHolder.imageView.setErrorImageResId(R.mipmap.bg_female_default);
        viewHolder.imageView.setDefaultImageResId(R.mipmap.bg_female_default);
        viewHolder.nameTxVw.setText(commentList.get(position).fromNickName);
        viewHolder.commentTxVw.setText(commentList.get(position).content);
        viewHolder.tv_time.setText(commentList.get(position).getDateDescription());
        if (position == 0 && !isDetail) {
            viewHolder.all_commentTxVw.setVisibility(View.VISIBLE);
            viewHolder.all_commentTxVw.setText(mcontext.getResources().getString(R.string.allComment) + commentCount + mcontext.getResources().getString(R.string.manyComment));
        } else {
            viewHolder.all_commentTxVw.setVisibility(View.GONE);
        }

        viewHolder.imageView.setTag(position);
        viewHolder.imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        Bundle bundle = new Bundle();
        Intent intent = new Intent(mcontext, PersonageDetailActivity.class);
        bundle.putString(PersonageDetailActivity.UserID, commentList.get(position).fromId);
        bundle.putBoolean(PersonageDetailActivity.IsOwn, false);
        intent.putExtras(bundle);
        mcontext.startActivity(intent);
    }

    private static class CommentViewHolder {
        RoundImageView imageView;
        TextView nameTxVw, commentTxVw;
        TextView all_commentTxVw;
        TextView tv_time;

        public void setImageView(RoundImageView imageView) {
            this.imageView = imageView;
        }

        public void setNameTxVw(TextView nameTxVw) {
            this.nameTxVw = nameTxVw;
        }

        public void setCommentTxVw(TextView commentTxVw) {
            this.commentTxVw = commentTxVw;
        }

        public void setAllCommentTxVw(TextView all_commentTxVw) {
            this.all_commentTxVw = all_commentTxVw;
        }

        public void setTime(TextView time) {
            this.tv_time = time;
        }
    }
}
