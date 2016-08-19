package tech.honc.android.apps.soldier.api.Service;

import com.smartydroid.android.starter.kit.model.ErrorModel;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by Administrator on 2016/4/19.
 */
public interface AuthService
{
  /**
   * 注册接口
   */
  @FormUrlEncoded @POST("/auth/register") Call<User> register(@Field("mobile") String phone,
      @Field("code") String code, @Field("password") String password);

  /**
   * 登陆接口
   */
  @FormUrlEncoded @POST("/auth/login") Call<User> login(@Field("mobile") String phone,
      @Field("password") String password);

  /*
 基本资料提交
   */
  @FormUrlEncoded @POST("/auth/profile") Call<User> postFile(@Field("avatar") String avatar,
      @Field("nickname") String nickname, @Field("gender") String gender,
      @Field("role") String role, @Field("city") String city,@Field("user_id") Integer userid);

  /**
   * 修改密码接口
   */
  @FormUrlEncoded @POST("/auth/updatePwd") Call<User> modification(@Field("mobile") String mobile,
      @Field("old_password") String oldpassword, @Field("password") String password);

  /**
   * 获取验证码
   */
  @FormUrlEncoded @POST("/auth/sms") Call<ErrorModel> postCode(@Field("phone") String phone);

  /**
   * 忘记密码接口
   */
  @FormUrlEncoded @POST("/auth/forget") Call<User> doForgot(@Field("username") String phone,
      @Field("code") String code, @Field("password") String password);

  /**
   * 第三方注册接口
   */
  @FormUrlEncoded @POST("/auth/third") Call<User> registerThird(@Field("openid") String openid,
      @Field("platform") String platform);

  /**
   * 第三方登录接口
   */
  @FormUrlEncoded @POST("/auth/thirdLogin") Call<User> loginThird(
      @Field("platform") String platform, @Field("open_id") String openid);
}
