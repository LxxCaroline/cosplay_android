package com.think.linxuanxuan.ecos.request.user;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.think.linxuanxuan.ecos.constants.RequestUrlConstants;
import com.think.linxuanxuan.ecos.model.User;
import com.think.linxuanxuan.ecos.model.UserDataService;
import com.think.linxuanxuan.ecos.request.BaseRequest;
import com.think.linxuanxuan.ecos.request.IBaseResponse;
import com.think.linxuanxuan.ecos.request.MyStringRequest;

import java.util.Map;

/***
 *
 * @ClassName: FollowUserRequest
 * @Description: 关注
 * @author enlizhang
 * @date 2015年7月26日 上午10:30:14
 *
 */
public class FollowUserRequest extends BaseRequest{

	//请求参数键
	/*** 操作类型(关注或取消关注),follow:关注 cancel:取消关注 */
	public static final String TYPE = "type";

	/** 关注或取消关注的对象的用户id*/
	public static final String TO_USRE_ID = "toUserId";

	/** 请求结束回掉函数 */
	IFollowResponce mFollowResponce;

	public String mToUserId;
	boolean mFollow;
	/***
	 *
	 * @param baseresponce
	 * @param toUserId
	 * @param follow true：关注  false:取消关注
	 */
	public void request(IFollowResponce followResponce, final String toUserId, final boolean follow)
	{
		super.initBaseRequest(followResponce);
		mFollowResponce  = followResponce;
		mToUserId = toUserId;
		mFollow = follow;

		MyStringRequest stringRequest = new MyStringRequest(Method.POST, RequestUrlConstants.FOLLOW_USER_INFO,  this, this) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> map = getRequestBasicMap();

				map.put(TO_USRE_ID, toUserId);
				map.put(TYPE, follow?"follow":"cancel");

				traceNormal(TAG, map.toString());
				traceNormal(TAG, FollowUserRequest.this.getUrl(RequestUrlConstants.FOLLOW_USER_INFO, map));
				return map;
			}

		};

		stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		getQueue().add(stringRequest);

	}

	@Override
	public void responceSuccess(String jstring) {
		//		traceNormal(TAG, jstring);
		User user = UserDataService.getSingleUserDataService(getContext()).getUser();

		if(mFollow)
			user.followOtherNum = String.valueOf( Integer.valueOf(user.followOtherNum)+1 );
		else
			user.followOtherNum = String.valueOf( Integer.valueOf(user.followOtherNum)-1 );


		mFollowResponce.success(mToUserId, mFollow);
		/*try {
//			JSONObject json = new JSONObject(jstring).getJSONObject(KEY_DATA);
			
			String toUserId = getString(json, TO_USRE_ID);
			if(mFollowResponce!=null)
			{
				mFollowResponce.success(toUserId, mFollow);
			}
			else
			{
				traceError(TAG,"回调接口为null");
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			if(mBaseResponse!=null)
			{
				mBaseResponse.doAfterFailedResponse("json异常");
			}
		}*/

	}


	/**
	 *
	 * @ClassName: IFollowResponce
	 * @Description: 关注或取消关注某人响应回调接口
	 * @author enlizhang
	 * @date 2015年8月1日 上午9:12:55
	 *
	 */
	public interface IFollowResponce extends IBaseResponse{


		/*** 
		 * 操作成功，并返回操作对象用户id和当前关注结果 
		 * @param userId 关注或取消关注对象id
		 * @param follow true:操作成功后处于关注状态  false:操作成功后处于不关注状态
		 */
		public void success(String userId, boolean follow);
	}
}

