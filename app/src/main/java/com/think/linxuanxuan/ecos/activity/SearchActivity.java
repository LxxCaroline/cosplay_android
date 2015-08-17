package com.think.linxuanxuan.ecos.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.CourseListViewAdapter;
import com.think.linxuanxuan.ecos.adapter.SearchDisplayListAdapter;
import com.think.linxuanxuan.ecos.adapter.SearchHistoryAdapter;
import com.think.linxuanxuan.ecos.fragment.DisplayFragment;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.model.Share;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.course.CourseListRequest;
import com.think.linxuanxuan.ecos.request.share.ShareListRequest;
import com.think.linxuanxuan.ecos.views.PopupHelper;
import com.think.linxuanxuan.ecos.views.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/7/27.
 */
public class SearchActivity extends BaseActivity implements XListView.IXListViewListener {

    private static final String TAG = "Ecos---Search";
    public static final String SearchWord = "SearchWord";

    public static final String SEARCH_TYPE = "type";

    public static final int TYPE_COURSE = 2;
    public static final int TYPE_SHARE = 3;

    private int TYPE = TYPE_COURSE; //default course

    private static java.util.List<String> HistoryList = new ArrayList<>();  //搜索历史记录

    @InjectView(R.id.et_search)
    EditText et_search;
    @InjectView(R.id.tv_confirm)
    TextView tv_confirm;
    @InjectView(R.id.ll_left)
    LinearLayout ll_left;
    @InjectView(R.id.lv_searchHistory)
    ListView lv_searchHistory;  //历史记录
    @InjectView(R.id.lv_searchList)
    XListView lv_searchList;   //搜索结果

    @InjectView(R.id.ll_searchType)
    LinearLayout ll_searchType;
    @InjectView(R.id.tv_searchType)
    TextView tv_searchType;

    //for no data
    @InjectView(R.id.resultImageView)
    ImageView resultImageView;

    PopupWindow courseTypePopupWindow;
    PopupWindow shareSortTypePopupWindow;

    private SearchHistoryAdapter searchHistoryAdapter;

    //result adapter
    private CourseListViewAdapter courseListViewAdapter;
    private SearchDisplayListAdapter displayListViewAdapter;
    //for request
    private ShareListRequest shareListRequest;
    private GetShareListResponse getShareListResponse;

    //for request
    private CourseListRequest courseListRequest;
    private CourseListResponse courseListResponse;

    //to record which item is selected
    private int selectPosition = 0;
    //to record the search key word
    private String searchWord;

    private int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        initData();
        initView();
        initListener();
        getHistory();
        setHistoryList();
    }

    private void setHistoryList() {
        searchHistoryAdapter = new SearchHistoryAdapter(this, HistoryList);
        lv_searchHistory.setAdapter(searchHistoryAdapter);
    }

    private void setHistory(String s) {
        if (!TextUtils.isEmpty(s)) {
            searchHistoryAdapter.getList().add(0, s);
        }
        SharedPreferences setting = getSharedPreferences("Search", 0);
        setting.edit().putString("History", getString(searchHistoryAdapter.getList())).commit();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        TYPE = bundle.getInt(SEARCH_TYPE);
        if (TYPE == TYPE_COURSE) {
            courseListRequest = new CourseListRequest();
            courseListResponse = new CourseListResponse();
        } else {
            tv_searchType.setText("全部");
            shareListRequest = new ShareListRequest();
            getShareListResponse = new GetShareListResponse();
        }
    }

    private void initView() {
        if (TYPE == TYPE_COURSE)
            courseTypePopupWindow = PopupHelper.newSixTypePopupWindow(SearchActivity.this);
        else
            shareSortTypePopupWindow = PopupHelper.newShareSortTypePopupWindow(SearchActivity.this);
    }

    private void initListener() {
        ll_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setHistory(searchWord);
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE == TYPE_COURSE)
                    pageIndex = 0;
                else
                    pageIndex = 1;
                searchWord = et_search.getText().toString();
                if (searchWord.equals("")) {
                    Toast.makeText(SearchActivity.this, getResources().getString(R.string.noContent), Toast.LENGTH_SHORT).show();
                    return;
                }
                lv_searchHistory.setVisibility(View.GONE);
                if (TYPE == TYPE_COURSE) {
                    showProcessBar(getResources().getString(R.string.loading));
                    courseListRequest.request(courseListResponse, CourseListRequest.Type.筛选, CourseCategoryActivity.courseTypes[selectPosition], searchWord, CourseListRequest.SortRule.时间, 0);
                } else {
                    showProcessBar(getResources().getString(R.string.loading));
                    shareListRequest.request(getShareListResponse, DisplayFragment.shareTypes[selectPosition], searchWord, 1);
                }
                setHistory(searchWord);
                et_search.setText("");
            }
        });

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //TODO 软键盘确定事件 搜索事件
                    //确定后隐藏lv_historyList 显示lv_searchList
                    return true;
                }
                return false;
            }
        });

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_searchHistory.setVisibility(View.VISIBLE);
                lv_searchList.setVisibility(View.GONE);
            }
        });

        lv_searchList.setDividerHeight(2);
        lv_searchList.initRefleshTime(this.getClass().getSimpleName());
        lv_searchList.setPullLoadEnable(true);
        lv_searchList.setXListViewListener(this);
        lv_searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        lv_searchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TYPE == TYPE_COURSE)
                    pageIndex = 0;
                else
                    pageIndex = 1;
                searchWord = ((TextView) view.findViewById(R.id.tv_search)).getText().toString();
                if (searchWord.equals("")) {
                    Toast.makeText(SearchActivity.this, getResources().getString(R.string.noContent), Toast.LENGTH_SHORT).show();
                    return;
                }
                lv_searchHistory.setVisibility(View.GONE);
                if (TYPE == TYPE_COURSE) {
                    showProcessBar(getResources().getString(R.string.loading));
                    courseListRequest.request(courseListResponse, CourseListRequest.Type.筛选, CourseCategoryActivity.courseTypes[selectPosition], searchWord, CourseListRequest.SortRule.时间, 0);
                } else {
                    showProcessBar(getResources().getString(R.string.loading));
                    shareListRequest.request(getShareListResponse, DisplayFragment.shareTypes[selectPosition], searchWord, 1);
                }
                et_search.setText("");
            }
        });


        ll_searchType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TYPE == TYPE_COURSE) {
                    if (courseTypePopupWindow.isShowing()) {
                        courseTypePopupWindow.dismiss();
                    } else {
                        PopupHelper.showSixTypePopupWindow(courseTypePopupWindow, SearchActivity.this, v, new PopupHelper.IPopupListner() {
                            @Override
                            public void clickListner(int type, View v, PopupWindow popupWindow) {
                                if (TYPE == TYPE_COURSE)
                                    pageIndex = 0;
                                else
                                    pageIndex = 1;
                                tv_searchType.setText(((RadioButton) v).getText().toString());
                                selectPosition = type;
                            }
                        });
                    }
                }
                if (TYPE == TYPE_SHARE) {
                    if (shareSortTypePopupWindow.isShowing()) {
                        shareSortTypePopupWindow.dismiss();
                    } else {
                        PopupHelper.showSixTypePopupWindow(shareSortTypePopupWindow, SearchActivity.this, v, new PopupHelper.IPopupListner() {
                            @Override
                            public void clickListner(int type, View v, PopupWindow popupWindow) {
                                tv_searchType.setText(((RadioButton) v).getText().toString());
                                selectPosition = type;
                            }
                        });
                    }
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                inputMethodManager.showSoftInput(et_search, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_searchList.stopRefresh();
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        Toast.makeText(this, getResources().getString(R.string.loadMore2), Toast.LENGTH_SHORT).show();
        //1秒后关闭加载
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_searchList.stopLoadMore();
            }
        }, 1000);

        if (TYPE == TYPE_COURSE) {

            if (courseListViewAdapter == null)
                pageIndex = 0;
            pageIndex++;
            courseListRequest.request(new CourseListRequest.ICourseListResponse() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(SearchActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void doAfterFailedResponse(String message) {
                    Toast.makeText(SearchActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void responseNoGrant() {

                }

                @Override
                public void success(List<Course> courseList) {
                    Log.d(TAG, "CourseListResponse.success()");
                    if (courseList.size() == 0) {
                        Toast.makeText(SearchActivity.this, getResources().getString(R.string.nothingLeft), Toast.LENGTH_SHORT).show();
                        pageIndex--;
                    } else {
                        courseListViewAdapter.getCourseList().addAll(courseList); // 添加ListView的内容
                        courseListViewAdapter.notifyDataSetChanged();
                    }
                }
            }, CourseListRequest.Type.筛选, CourseCategoryActivity.courseTypes[selectPosition], searchWord, CourseListRequest.SortRule.时间, pageIndex);

        } else {

            if (displayListViewAdapter == null)
                pageIndex = 1;
            pageIndex++;
            shareListRequest.request(new ShareListRequest.IShareListResponse() {

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(SearchActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void doAfterFailedResponse(String message) {
                    Toast.makeText(SearchActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void responseNoGrant() {

                }

                @Override
                public void success(List<Share> shareList) {
                    Log.d(TAG, "CourseListResponse.success()");
                    if (shareList.size() == 0) {
                        Toast.makeText(SearchActivity.this, getResources().getString(R.string.nothingLeft), Toast.LENGTH_SHORT).show();
                        pageIndex--;
                    } else {
                        displayListViewAdapter.getShareList().addAll(shareList); // 添加ListView的内容
                        displayListViewAdapter.notifyDataSetChanged();
                    }
                }
            }, DisplayFragment.shareTypes[selectPosition], searchWord, pageIndex);
        }
    }

    public void getHistory() {
        SharedPreferences setting = getSharedPreferences("Search", 0);
        String history = setting.getString("History", "");
        HistoryList = getList(history);
    }

    private String getString(List<String> list) {
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            s += list.get(i);
            s += ",";
        }

        if (!TextUtils.isEmpty(s)) {
            s = s.substring(0, s.length() - 1);
        }


        return s;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private List<String> getList(String s) {
        List<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            return list;
        } else {
            String[] l = s.split(",");
            for (int i = 0; i < l.length; i++) {
                list.add(l[i]);
            }
            return list;
        }
    }

    class CourseListResponse extends BaseResponceImpl implements CourseListRequest.ICourseListResponse {

        @Override
        public void success(List<Course> courseList) {
            dismissProcessBar();
            if (courseList.size() == 0) {
                lv_searchList.setVisibility(View.GONE);
                resultImageView.setVisibility(View.VISIBLE);
                resultImageView.setImageResource(R.mipmap.no_data);
                return;
            }
            courseListViewAdapter = new CourseListViewAdapter(SearchActivity.this, courseList);
            lv_searchList.setVisibility(View.VISIBLE);
            lv_searchList.setAdapter(courseListViewAdapter);
            pageIndex = 0;


        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            lv_searchList.setVisibility(View.GONE);
            resultImageView.setVisibility(View.VISIBLE);
            resultImageView.setImageResource(R.mipmap.server_error);
            Toast.makeText(SearchActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            lv_searchList.setVisibility(View.GONE);
            resultImageView.setVisibility(View.VISIBLE);
            resultImageView.setImageResource(R.mipmap.server_error);
        }
    }

    class GetShareListResponse extends BaseResponceImpl implements ShareListRequest.IShareListResponse {

        @Override
        public void success(List<Share> shareList) {
            dismissProcessBar();
            if (shareList.size() == 0) {
                lv_searchList.setVisibility(View.GONE);
                resultImageView.setVisibility(View.VISIBLE);
                resultImageView.setImageResource(R.mipmap.no_data);
                return;
            }
            displayListViewAdapter = new SearchDisplayListAdapter(SearchActivity.this, shareList);
            lv_searchList.setVisibility(View.VISIBLE);
            lv_searchList.setAdapter(displayListViewAdapter);
            pageIndex = 1;
        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            lv_searchList.setVisibility(View.GONE);
            resultImageView.setVisibility(View.VISIBLE);
            resultImageView.setImageResource(R.mipmap.server_error);
            Toast.makeText(SearchActivity.this, "error happens:" + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            lv_searchList.setVisibility(View.GONE);
            resultImageView.setVisibility(View.VISIBLE);
            resultImageView.setImageResource(R.mipmap.server_error);
            Toast.makeText(SearchActivity.this, "泪奔！服务器出错了:" + VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }

}
