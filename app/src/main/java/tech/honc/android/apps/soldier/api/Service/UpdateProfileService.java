package tech.honc.android.apps.soldier.api.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tech.honc.android.apps.soldier.model.Profile;
import tech.honc.android.apps.soldier.model.UpdateData;

/**
 * Created by Administrator on 2016/4/28.
 */
public interface UpdateProfileService
{
  /**
   * 修改战友资料接口
   */
  @FormUrlEncoded @POST("/user/updateProfile") Call<UpdateData> postUpdtaSoldier(
      @Field("area") String area, @Field("area_id") Integer area_id, @Field("region") String region,
      @Field("region_id") Integer region_id, @Field("enter_time") String enter_time,
      @Field("end_time") String end_time, @Field("real_name") String real_name,
      @Field("age") String age, @Field("nickname") String nickname, @Field("city") String city,
      @Field("education") String education, @Field("height") String height,
      @Field("weight") String weight, @Field("profession") Integer profession,
      @Field("army_kind") String army_kind, @Field("army_kind_id") Integer armykindid,
      @Field("married") Integer married);

  /*
  * 提交意见反馈
  */
  @FormUrlEncoded @POST("/feedback/publish") Call<UpdateData> postFeedBack(
      @Field("content") String content);

  /**
   * 获取个人资料
   **/
  @GET("/user/profile") Call<Profile> getProfile();
}
