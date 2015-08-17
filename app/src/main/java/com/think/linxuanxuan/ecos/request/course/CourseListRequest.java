package com.think.linxuanxuan.ecos.request.course;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.Course;
import com.think.linxuanxuan.ecos.model.Course.CourseType;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author enlizhang
 * @ClassName: CourseListRequest
 * @Description: 获取教程列表
 * @date 2015年7月26日 上午11:01:18
 */
public class CourseListRequest extends BaseRequest {

    //请求参数
    /**
     * 获取教程列表方式，包括推荐和筛选
     */
    public static final String TYPE = "type";

    /**
     * 教程类型{@link Course.CourseType} (仅在{@link #TYPE}为推荐时有效)
     */
    public static final String COURSE_TYPE = "filterType";

    /**
     * 关键字(仅在{@link #TYPE}为推荐时有效)
     */
    public static final String KEY_WORD = "keyWord";

    /**
     * 排序规则{@link SortRule}，包括时间、被关注数、被点赞数, (仅在{@link #TYPE}为推荐时有效)
     */
    public static final String SORT_RULE = "sortRule";

    //响应参数键
    ICourseListResponse mCourseListRespnce;

    /**
     * 教程列表JSONArray,内含JSONObject
     */
    public static final String JA_COURSE = "courses";

    /**
     * 教程id
     */
    public static final String KEY_COURSE_ID = "courseId";

    /**
     * 封面图url
     */
    public static final String KEY_COVER_URL = "coverUrl";

    /**
     * 作者头像url
     */
    public static final String KEY_AVATAR_URL = "authorAvatarUrl";

    /**
     * 作者昵称
     */
    public static final String KEY_NICKNAME = "author";

    /**
     * 作者id
     */
    public static final String KEY_USER_ID = "userId";

    /**
     * 教程类型
     */
    public static final String KEY_COURSE_TYPE = "courseType";

    /**
     * 标题
     */
    public static final String KEY_TITLE = "title";

    /**
     * 点赞数
     */
    public static final String KEY_PRAISE_NUM = "praiseNum";

    /**
     * 发布时间时间戳
     */
    public static final String KEY_ISSUR_TIME_STAMP = "courseIssueTimeStamp";

    /**
     * 步骤图片JA,其顺序与步骤序号一致
     */
    public static final String KEY_IMG_URLS = "imgUrls";

    /**
     * 步骤描述JA,其顺序与步骤序号一致
     */
    public static final String KEY_DESCRIPTION = "descriptions";


    /**
     * 教程类别
     */
    CourseType mCourseType;


    public void request(ICourseListResponse courseListRespnce, final Type type, final CourseType courseType
            , final String keyWord, final SortRule sortRule, final int pageIndex) {
        super.initBaseRequest(courseListRespnce);
        mCourseListRespnce = courseListRespnce;
        mCourseType = courseType;
        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_COURSE_LIST_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put(TYPE, type.getValue());

                //如果当前获取方式是筛选
                if (type == Type.筛选) {
                    map.put(COURSE_TYPE, courseType.getBelongs());
                    map.put(KEY_WORD, keyWord);
                    map.put(SORT_RULE, sortRule.getValue());
                } else {
                    map.put(COURSE_TYPE, "");
                    map.put(KEY_WORD, "");
                    map.put(SORT_RULE, "");
                }

                map.put(KEY_PAGE_SIZE, String.valueOf(DEFAULT_PAGE_SIZE));
                map.put(KEY_PAGE_INDEX, String.valueOf(pageIndex));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, CourseListRequest.this.getUrl(RequestUrlConstants.GET_COURSE_LIST_URL, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);

    }


    /**
     * 请求个人教程列表
     *
     * @param courseListRespnce
     * @param pageIndex
     */
    public void requestMySelf(ICourseListResponse courseListRespnce, final int pageIndex) {
        super.initBaseRequest(courseListRespnce);
        mCourseListRespnce = courseListRespnce;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_COURSE_LIST_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = getRequestBasicMap();

                map.put(TYPE, "myself");
                map.put(COURSE_TYPE, "");
                map.put(KEY_WORD, "");
                map.put(SORT_RULE, "");

                map.put(KEY_PAGE_SIZE, String.valueOf(DEFAULT_PAGE_SIZE));
                map.put(KEY_PAGE_INDEX, String.valueOf(pageIndex));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, CourseListRequest.this.getUrl(RequestUrlConstants.GET_COURSE_LIST_URL, map));
                return map;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getQueue().add(stringRequest);

    }


    /**
     * 请求其他人教程列表
     *
     * @param courseListRespnce
     * @param otherUserId       要查看的人的userId
     * @param pageIndex
     */
    public void requestOtherCourse(ICourseListResponse courseListRespnce, final String otherUserId, final int pageIndex) {
        super.initBaseRequest(courseListRespnce);
        mCourseListRespnce = courseListRespnce;

        MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.GET_COURSE_LIST_URL, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();

                map.put(TYPE, "myself");
                if(otherUserId==null)
                    map.put(KEY_USER_ID, getUserId());
                else
                    map.put(KEY_USER_ID, otherUserId);
                map.put(COURSE_TYPE, "");
                map.put(KEY_WORD, "");
                map.put(SORT_RULE, "");

                map.put(KEY_PAGE_SIZE, String.valueOf(DEFAULT_PAGE_SIZE));
                map.put(KEY_PAGE_INDEX, String.valueOf(pageIndex));

                traceNormal(TAG, map.toString());
                traceNormal(TAG, CourseListRequest.this.getUrl(RequestUrlConstants.GET_COURSE_LIST_URL, map));
                return map;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getQueue().add(stringRequest);

    }

    @Override
    public void responceSuccess(String jstring) {
        try {
            JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);

            List<Course> courseList = new ArrayList<Course>();
            if (json.has(JA_COURSE) && !json.isNull(JA_COURSE)) {
                JSONArray courseJA = json.getJSONArray(JA_COURSE);
                for (int i = 0; i < courseJA.length(); i++) {
                    JSONObject courseJO = courseJA.getJSONObject(i);
                    Course course = new Course();
                    course.courseId = getString(courseJO, KEY_COURSE_ID);
                    course.userId = getString(courseJO, KEY_USER_ID);
                    course.coverUrl = getString(courseJO, KEY_COVER_URL);
                    course.authorAvatarUrl = getString(courseJO, KEY_AVATAR_URL);
                    course.author = getString(courseJO, KEY_NICKNAME);
                    course.title = getString(courseJO, KEY_TITLE);
                    course.courseId = getString(courseJO, KEY_COURSE_ID);
                    course.userId = getString(courseJO, KEY_USER_ID);
                    course.courseType = CourseType.getCourseType(getString(courseJO, KEY_COURSE_TYPE));
                    course.issueTimeStamp = Long.valueOf(getString(courseJO, KEY_ISSUR_TIME_STAMP)).longValue();
                    String praiseNum = getString(courseJO, KEY_PRAISE_NUM);
                    course.praiseNum = "".equals(praiseNum) ? 0 : Integer.valueOf(praiseNum);
                    course.hasPraised = courseJO.getBoolean("hasPraised");
                    courseList.add(course);
                }
            }
            if (mCourseListRespnce != null) {
                mCourseListRespnce.success(courseList);
            } else {
                traceError(TAG, "回调接口为null");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            if (mBaseResponse != null) {
                mBaseResponse.doAfterFailedResponse("json异常");
            }
        }
    }

    /**
     * @author enlizhang
     * @ClassName: Type
     * @Description: 获取教程列表的方式，包括推荐和筛选
     * @date 2015年7月26日 下午4:45:32
     */
    public static enum Type {
        推荐("recommended"),
        筛选("filter");

        public String type;

        Type(String _type) {
            type = _type;
        }

        public String getValue() {
            return type;
        }
    }

    /**
     * @author enlizhang
     * @ClassName: SortRule
     * @Description: 排序规则，包括时间、被关注数、被点赞数
     * @date 2015年7月26日 下午4:53:39
     */
    public static enum SortRule {
        时间("time"),
        被关注数("followed"),
        被点赞数("praised");

        public String rule;

        SortRule(String _rule) {
            rule = _rule;
        }

        public String getValue() {
            return rule;
        }
    }

    /**
     * @author enlizhang
     * @ClassName: CourseListRespnce
     * @Description: 获取教程列表响应回调
     * @date 2015年7月26日 下午4:59:51
     */
    public interface ICourseListResponse extends IBaseResponse {

        /**
         * 请求成功，返回教程列表
         */
        public void success(List<Course> courseList);

    }


    public List<Course> getTestCourseList() {

        List<Course> courseList = new ArrayList<Course>();

        Course course;

        List<String> bannerList = new ArrayList<String>();
        bannerList.add("http://u4.tdimg.com/7/203/19/46138657748730920288026757971472766587.jpg");
        bannerList.add("http://www.cnnb.com.cn/pic/0/01/49/86/1498602_864010.jpg");
        bannerList.add("http://u3.tdimg.com/6/88/143/_56696781343356143444965292996172123406.jpg");
        bannerList.add("http://i3.cqnews.net/news/attachement/jpg/site82/2011-07-27/4386628352243053135.jpg");

        for (int i = 0; i < 2; i++) {
            course = new Course();
            course.coverUrl = bannerList.get(i);
            course.authorAvatarUrl = "http://p3.gexing.com/G1/M00/A9/D1/rBACFFIZgEPDWDxwAAAlaw2mvB4124_200x200_3.jpg?recache=20131109";
            course.title = "鸣人系列教程-" + i;
            course.author = "小鸣人 -" + i;
            course.courseId = "" + i;
            course.userId = "" + i;
            course.praiseNum = i * 100;
            course.issueTimeStamp = System.currentTimeMillis();

            courseList.add(course);
        }


        return courseList;
    }

}

