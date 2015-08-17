package com.think.linxuanxuan.ecos.adapter;

/**
 * 类描述：
 * Created by enlizhang on 2015/7/29.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.activity.MyApplication;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.model.InputLength;
import com.think.linxuanxuan.ecos.utils.SDImageCache;

import java.io.File;
import java.util.List;

/**
 * 教程步骤列表适配器
 */
public class CourseStepAdapter extends BaseAdapter {

    private Context mContext;

    private List<Course.Step> mStepsList;

    private LayoutInflater mInflater;

    private AdapterAction mAdapterAction;

    public CourseStepAdapter(Context context, List<Course.Step> stepsList, AdapterAction adapterAction) {
        mContext = context;
        mStepsList = stepsList;
        mAdapterAction = adapterAction;


        mInflater = LayoutInflater.from(mContext);

        mImageLoader = new ImageLoader(MyApplication.getRequestQueue(), new SDImageCache(300,200));
    }

    @Override
    public int getCount() {
        return mStepsList == null ? 0 : mStepsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStepsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        /**
         * 步骤序号
         */
        TextView tv_index;
        /**
         * 置为上一步
         */
        ImageView iv_last_step;
        /**
         * 置为下一步
         */
        ImageView iv_next_step;

        /**
         * 删除当前步骤
         */
        ImageView iv_delete;

        /**
         * 步骤图片
         */
        ImageView niv_course_photo;
        /**
         * 步骤描述
         */
        EditText etv_description;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_build_course, null);
            holder = new ViewHolder();

            holder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
            holder.iv_last_step = (ImageView) convertView.findViewById(R.id.iv_last_step);
            holder.iv_next_step = (ImageView) convertView.findViewById(R.id.iv_next_step);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            holder.niv_course_photo = (ImageView) convertView.findViewById(R.id.niv_course_photo);
            holder.etv_description = (EditText) convertView.findViewById(R.id.etv_description);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.niv_course_photo.setTag(position);
        holder.etv_description.setTag(position);
        holder.iv_last_step.setTag(position);
        holder.iv_next_step.setTag(position);
        holder.iv_delete.setTag(position);

        setData(holder, position, mStepsList.get(position));

        return convertView;
    }

    ImageLoader mImageLoader;

    /**
     * 绑定视图数据
     *
     * @param holder   视图holder
     * @param position 位置
     * @param stepData 步骤数据
     */
    private void setData(ViewHolder holder, int position, Course.Step stepData) {
        holder.tv_index.setText(String.valueOf(stepData.stepIndex));
        holder.niv_course_photo.setImageResource(R.mipmap.ic_add_course_default);
        //从SD卡中读取，可以优化为从内存读取，后续做
        if (!(stepData.imagePath == null) && !("".equals(stepData.imagePath.trim()))) {
            File file = new File(stepData.imagePath);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(stepData.imagePath);
                holder.niv_course_photo.setImageBitmap(bitmap);
            } else {
                Log.e("设置教程步骤图片", "无效路径: " + stepData.imagePath);
            }
        }

        //对步骤描述进行数字限制
        //.......
        holder.etv_description.setText(stepData.description);
        holder.etv_description.setOnFocusChangeListener(onFocusChangeListener);

        holder.niv_course_photo.setOnClickListener(viewClickListener);
        holder.iv_last_step.setOnClickListener(viewClickListener);
        holder.iv_next_step.setOnClickListener(viewClickListener);
        holder.iv_delete.setOnClickListener(viewClickListener);
    }


    /**
     * item点击事件，包括点击{@link ViewHolder#iv_next_step}和{@link ViewHolder#iv_last_step}
     */
    View.OnClickListener viewClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            int position = 0;
            Course.Step tempStep;
            Log.e("点击事件", " ");
            switch (view.getId()) {
                //点击设置图片
                case R.id.niv_course_photo:
                    position = (int) view.getTag();
                    if (mAdapterAction != null)
                        mAdapterAction.setPhotoAtPosition(position);
                    break;
                //点击向上按钮
                case R.id.iv_last_step:
                    position = (int) view.getTag();
                    if (position == 0) {
                        Toast.makeText(mContext, "已经置顶，不能上移了", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mStepsList.get(position).stepIndex--;
                    mStepsList.get(position - 1).stepIndex++;

                    tempStep = mStepsList.get(position);
                    mStepsList.set(position, mStepsList.get(position - 1));
                    mStepsList.set(position - 1, tempStep);

                    notifyDataSetChanged();
                    break;
                //点击向下按钮
                case R.id.iv_next_step:
                    position = (int) view.getTag();
                    if (position == (getCount() - 1)) {
                        Toast.makeText(mContext, "已经在底层，不能下移了", Toast.LENGTH_LONG).show();
                        return;
                    }
                    mStepsList.get(position).stepIndex++;
                    mStepsList.get(position + 1).stepIndex--;

                    tempStep = mStepsList.get(position);
                    mStepsList.set(position, mStepsList.get(position + 1));
                    mStepsList.set(position + 1, tempStep);
                    notifyDataSetChanged();
                    break;
                //删除当前步骤
                case R.id.iv_delete:
                    position = (int) view.getTag();
                    //不能删除第0项
                    if (position == 0)
                        return;
                    position = (int) view.getTag();
                    mStepsList.remove(position);
                    for (int i = position; i < getCount(); i++) {
                        mStepsList.get(i).stepIndex = mStepsList.get(i).stepIndex - 1;
                    }
                    notifyDataSetChanged();
                    break;
            }
        }
    };

    /**
     * 适配器中的动作接口，包括选择图片
     */
    public interface AdapterAction {
        /**
         * 设置第(position+1)个步骤的照片
         *
         * @param position
         */
        public void setPhotoAtPosition(int position);
    }

    /**
     * 将步骤描述编辑框的输入同步到mStepList
     */
    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {

        TexeChangeListener mTexeChangeListener;

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            EditText etv_description = (EditText) v;

            if (mTexeChangeListener != null) {
                mTexeChangeListener = null;
                etv_description.removeTextChangedListener(mTexeChangeListener);
            }

            mTexeChangeListener = new TexeChangeListener(v,(int) v.getTag());
            if (hasFocus)
                etv_description.addTextChangedListener(mTexeChangeListener);
            else {
                if (mTexeChangeListener != null)
                    mTexeChangeListener = null;
                etv_description.removeTextChangedListener(mTexeChangeListener);
            }
        }

        /**
         * 用于监听步骤描述的变化
         */
        class TexeChangeListener implements TextWatcher {

            final int positionOfEtv;
            View view;

            public TexeChangeListener(View v,int position) {
                view=v;
                positionOfEtv = position;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()> InputLength.CourseStepDetail_max){
                    ((EditText)view).setText(s.subSequence(0,InputLength.CourseStepDetail_max-1));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (positionOfEtv >= getCount()) {
                    return;
                }
                mStepsList.get(positionOfEtv).description = s.toString();
            }
        }
    };


    /**
     * 刷新教程第(positon+1)步的图片
     *
     * @param position  图片位置
     * @param imagePath 图片路径
     */
    public void refleshImageAtPosition(int position, String imagePath) {

        Log.i("刷新图片", "position:" + position + ",------------    imagePath: " + imagePath);
        if (position >= 0 && position < getCount()) {
            mStepsList.get(position).imagePath = imagePath;
            notifyDataSetChanged();
        }
    }

    public List<Course.Step> getStepDataList() {
        return mStepsList;
    }

    /**
     * 是否有步骤数据不全
     *
     * @return
     */
    public boolean isSomeEmpty() {

        for (Course.Step step : mStepsList) {
            if (step.isSomeEmpty())
                return true;
        }
        return false;
    }
}

