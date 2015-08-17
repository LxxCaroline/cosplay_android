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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.CommentDetailActivity;
import com.think.linxuanxuan.ecos.activity.DisplayDetailActivity;
import com.think.linxuanxuan.ecos.activity.PersonageDetailActivity;
import com.think.linxuanxuan.ecos.model.Comment;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.course.PraiseRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hzjixinyu on 2015/7/23.
 */
public class PersonDisplayAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mcontext;
    private List<Share> shareList;

    private PraiseRequest praiseRequest;
    private PraiseResponse praiseResponse;
    private int praisePos;

    public PersonDisplayAdapter(Context context) {
        this.mcontext = context;
    }

    public PersonDisplayAdapter(Context context, List<Share> shareList) {
        this.mcontext = context;
        this.shareList = shareList;
    }

    public void setShareList(List<Share> shareList) {
        this.shareList = shareList;
    }

    public List<Share> getShareList() {
        return shareList;
    }

    class ViewHolder {

        private ImageView iv_cover;
        private TextView tv_coverNum;
        private TextView tv_coverTitle;
        private TextView tv_coverTime;

        private TextView tv_praise;
        private TextView tv_evaluate;
        private ImageView iv_praise;
        private ImageView iv_evaluate;
        private LinearLayout ll_praise;
        private LinearLayout ll_evaluate;


        public ViewHolder(View root) {
            iv_cover = (ImageView) root.findViewById(R.id.iv_cover);
            tv_coverNum = (TextView) root.findViewById(R.id.tv_coverNum);
            tv_coverTitle = (TextView) root.findViewById(R.id.tv_coverTitle);
            tv_coverTime = (TextView) root.findViewById(R.id.tv_coverTime);
            tv_praise = (TextView) root.findViewById(R.id.tv_praise);
            tv_evaluate = (TextView) root.findViewById(R.id.tv_evaluation);
            iv_praise = (ImageView) root.findViewById(R.id.iv_praise);
            iv_evaluate = (ImageView) root.findViewById(R.id.iv_evaluation);
            ll_praise = (LinearLayout) root.findViewById(R.id.ll_praise);
            ll_evaluate = (LinearLayout) root.findViewById(R.id.ll_evaluation);
        }

        /**
         * 传入数据未定
         */
        public void setData(int position) {
            if (shareList.get(position).coverUrl != null &&  !TextUtils.isEmpty(shareList.get(position).coverUrl))
                Picasso.with(mcontext).load(shareList.get(position).coverUrl).placeholder(R.drawable.img_default).into(iv_cover);
            else
                iv_cover.setImageResource(R.drawable.img_default);
            tv_coverNum.setText(shareList.get(position).totalPageNumber + "");
            tv_coverTitle.setText(shareList.get(position).title);
            tv_coverTime.setText(shareList.get(position).getDateDescription() + "");
            tv_praise.setText(shareList.get(position).praiseNum + "");
            tv_evaluate.setText(shareList.get(position).commentNum + "");

            iv_cover.setTag(position);
            tv_coverTitle.setTag(position);
            ll_praise.setTag(position);
            ll_evaluate.setTag(position);
            iv_cover.setOnClickListener(PersonDisplayAdapter.this);
            tv_coverTitle.setOnClickListener(PersonDisplayAdapter.this);
            ll_praise.setOnClickListener(PersonDisplayAdapter.this);
            ll_evaluate.setOnClickListener(PersonDisplayAdapter.this);
        }
    }

    @Override
    public int getCount() {
        if (shareList == null){
            return 0;
        }else {
            return shareList.size();
        }
    }


    @Override
    public Object getItem(int position) {
        return shareList.get(position);

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = parent.inflate(mcontext, R.layout.item_recruitment_detail_work, null);
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
        int position = (int) v.getTag();
        Bundle bundle = new Bundle();
        Intent intent;
        switch (v.getId()){
            case R.id.iv_cover:
            case R.id.tv_coverTitle:
            case R.id.ll_praise:
                intent = new Intent(mcontext, DisplayDetailActivity.class);
                bundle.putString(DisplayDetailActivity.ShareId, shareList.get(position).shareId);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
                break;
//            case R.id.ll_praise:
//                praisePos = position;
//                if (praiseRequest == null)
//                    praiseRequest = new PraiseRequest();
//                if (praiseResponse == null)
//                    praiseResponse = new PraiseResponse();
//                praiseRequest.praiseShare(praiseResponse, shareList.get(praisePos).shareId, !shareList.get(praisePos).hasPraised);
//                break;
            case R.id.ll_evaluation:
                intent = new Intent(mcontext, CommentDetailActivity.class);
                bundle.putString(CommentDetailActivity.FromId, shareList.get(position).shareId);
                bundle.putString(CommentDetailActivity.CommentType, Comment.CommentType.分享.getBelongs());
                bundle.putBoolean(CommentDetailActivity.IsPraised, shareList.get(position).hasPraised);
                intent.putExtras(bundle);
                mcontext.startActivity(intent);
                break;
        }
    }
    class PraiseResponse extends BaseResponceImpl implements PraiseRequest.IPraiseResponce {

        @Override
        public void success(String userId, boolean praise) {
            shareList.get(praisePos).hasPraised = !shareList.get(praisePos).hasPraised;
            notifyDataSetChanged();
        }

        @Override
        public void doAfterFailedResponse(String message) {
            //Toast.makeText(mcontext, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }
}
