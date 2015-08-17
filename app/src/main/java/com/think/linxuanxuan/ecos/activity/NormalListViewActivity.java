package com.think.linxuanxuan.ecos.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.think.linxuanxuan.ecos.R;
import com.think.linxuanxuan.ecos.adapter.EventWantGoAdapter;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.request.BaseResponceImpl;
import com.think.linxuanxuan.ecos.request.VolleyErrorParser;
import com.think.linxuanxuan.ecos.request.activity.SingupPeopleListRequest;
import com.think.linxuanxuan.ecos.request.user.FollowedUserListRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hzjixinyu on 2015/8/4.
 */
public class NormalListViewActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Ecos---NormalList";

    @InjectView(R.id.lly_right_action)
    LinearLayout title_right;
    @InjectView(R.id.tv_right_text)
    TextView title_right_text;
    @InjectView(R.id.tv_title)
    TextView title_text;
    @InjectView(R.id.lly_left_action)
    LinearLayout title_left;
    @InjectView(R.id.lv_list)
    ListView lv_list;

    public static String GET_ACTIVITY_ID = "activity_id";
    public static String LISTVIEW_TYPE = "type";

    public final static int TYPE_EVENT_WANTGO = 0;
    public final static int TYPE_EVENT_FANS = 1;
    public final static int TYPE_EVENT_ATTENTION = 2;

    private int TYPE = TYPE_EVENT_WANTGO;  //当前Activity类型

    private EventWantGoAdapter eventWantGoAdapter;

    //for request
    private SingupPeopleListRequest singupPeopleListRequest;
    private SignUpPeopleListResponse signUpPeopleListResponse;
    private String activityId;
    private List<User> userList;
    private int pageIndex = 0;
    private ArrayList<Boolean> hasFollowed;
    private ArrayList<Boolean> beFollowed;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_listview_normal);
        ButterKnife.inject(this);

        TYPE = getIntent().getExtras().getInt(LISTVIEW_TYPE);
        Log.d(TAG, "type:" + TYPE);
        if (check()) {
            switch (TYPE) {
                case TYPE_EVENT_WANTGO:
                    initEventWantGo();
                    break;
                case TYPE_EVENT_ATTENTION:
                    initFollows();
                    break;
                case TYPE_EVENT_FANS:
                    initFans();
                    break;
            }
        }
    }

    private Boolean check() {
        String id = UserDataService.getSingleUserDataService(NormalListViewActivity.this).getUser().userId;
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(NormalListViewActivity.this, getResources().getString(R.string.null_id), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void initEventWantGo() {
        title_left.setOnClickListener(this);
        title_right.setVisibility(View.INVISIBLE);
        title_text.setText("想去的人");
        try {
            activityId = getIntent().getExtras().getString(GET_ACTIVITY_ID);
            singupPeopleListRequest = new SingupPeopleListRequest();
            signUpPeopleListResponse = new SignUpPeopleListResponse();
            singupPeopleListRequest.request(signUpPeopleListResponse, activityId, pageIndex);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(NormalListViewActivity.this, "getIntentInformation error", Toast.LENGTH_SHORT);
        }
    }

    private void initFollows() {
        title_left.setOnClickListener(this);
        title_right.setVisibility(View.INVISIBLE);
        title_text.setText("我的关注");

        showProcessBar("");
        FollowedUserListRequest request = new FollowedUserListRequest();
        request.requestMyFollows(new followedUserListRequest(), 1);
    }

    private void initFans() {
        title_left.setOnClickListener(this);
        title_right.setVisibility(View.INVISIBLE);
        title_text.setText("我的粉丝");

        showProcessBar("");
        FollowedUserListRequest request = new FollowedUserListRequest();
        request.requestSomeOneFans(new followedUserListRequest(), null, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left_action:
                finish();
                break;
        }
    }

    class followedUserListRequest extends BaseResponceImpl implements FollowedUserListRequest.IFollowUserListResponce {

        @Override
        public void success(List<User> userList) {
            if (TYPE == TYPE_EVENT_ATTENTION) {
                eventWantGoAdapter = new EventWantGoAdapter(NormalListViewActivity.this, TYPE_EVENT_ATTENTION, userList);
                lv_list.setAdapter(eventWantGoAdapter);
            }
            if (TYPE == TYPE_EVENT_FANS) {
                eventWantGoAdapter = new EventWantGoAdapter(NormalListViewActivity.this, TYPE_EVENT_FANS, userList);
                lv_list.setAdapter(eventWantGoAdapter);
            }
            dismissProcessBar();
        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(NormalListViewActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            Toast.makeText(NormalListViewActivity.this, VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }

    class SignUpPeopleListResponse extends BaseResponceImpl implements SingupPeopleListRequest.ISignupPeopleListResponce {

        @Override
        public void success(List<User> userList, boolean[] hasFollowEd, boolean[] beFollowed) {
            if (NormalListViewActivity.this.userList == null)
                NormalListViewActivity.this.userList = new ArrayList<>();
            if (NormalListViewActivity.this.hasFollowed == null)
                NormalListViewActivity.this.hasFollowed = new ArrayList<>();
            if (NormalListViewActivity.this.beFollowed == null)
                NormalListViewActivity.this.beFollowed = new ArrayList<>();
            for (int i = 0; i < userList.size(); i++) {
                NormalListViewActivity.this.userList.add(userList.get(i));
                Log.d(TAG, i + "" + hasFollowEd[i] + "," + beFollowed[i]);
                NormalListViewActivity.this.hasFollowed.add(hasFollowEd[i]);
                NormalListViewActivity.this.beFollowed.add(beFollowed[i]);
            }
            if (userList.size() == 5) {
                pageIndex++;
                singupPeopleListRequest.request(signUpPeopleListResponse, activityId, pageIndex);
            } else {
                eventWantGoAdapter = new EventWantGoAdapter(NormalListViewActivity.this, TYPE_EVENT_WANTGO, NormalListViewActivity.this.userList, NormalListViewActivity.this.hasFollowed, NormalListViewActivity.this.beFollowed);
                lv_list.setAdapter(eventWantGoAdapter);
            }

            dismissProcessBar();
        }

        @Override
        public void doAfterFailedResponse(String message) {
            dismissProcessBar();
            Toast.makeText(NormalListViewActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dismissProcessBar();
            Toast.makeText(NormalListViewActivity.this, VolleyErrorParser.parseVolleyError(volleyError), Toast.LENGTH_SHORT).show();
        }
    }
}
