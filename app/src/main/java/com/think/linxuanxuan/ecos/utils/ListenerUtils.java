package com.think.linxuanxuan.ecos.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

/**
 * 类描述：
 * Created by enlizhang on 2015/7/29.
 */
public class ListenerUtils {

    /**
     * 剩余可输入字数监听
     */
    class RemainingTextNumberWatcher implements TextWatcher
    {
        /*** 文字最多输入数 */
        public final int MAX_INPUT_WORDS_NUM;

        public TextView tv_remaining_text_num;
        public RemainingTextNumberWatcher(int number,TextView tv_remaining_text_num){
            this.MAX_INPUT_WORDS_NUM = number;
            this.tv_remaining_text_num = tv_remaining_text_num;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            Log.i("beforeTextChanged", "CharSequence" + s + "  start" + start + "  count" + count + "  after" + after);

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            Log.i("onTextChanged", "CharSequence"+s+"  start"+start+"  before"+before+"  count"+count);
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i("afterTextChanged", "    ");
            int rest = MAX_INPUT_WORDS_NUM-s.toString().length();

            if(tv_remaining_text_num==null)
                Log.e("剩余字数提示", "tv_remaining_text_num==null");
            else
                tv_remaining_text_num.setText("当前还可输入"+rest+"个字");
        }
    };
}
