package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tech.honc.android.apps.soldier.model.AccountCenter;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by Administrator on 2016/4/19.
 */
public interface AccountService
{
  @GET("/user/list") Call<ArrayList<User>> recommend(@Query("role") String role,
      @Query("max-id") int maxId, @Query("city") String city);

  /**
   * 获取推荐
   */
  @GET("/user/hotLine") Call<ArrayList<User>> hotLine();

  /**
   * 获取用户详情
   */
  @GET("/user/detail") Call<User> getAccountDetail(@Query("userid") int userId);

  /**
   * 获取个人中心
   */
  @GET("/user/center") Call<AccountCenter> getAccountCenter();

  /**
   * 获取用户IM信息
   * @param imId
   * @return
   */
  @GET("/user/getUserByIm") Call<User> getUserIm(@Query("im_id") String imId);
}
