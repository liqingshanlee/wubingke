package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tech.honc.android.apps.soldier.feature.im.model.IMProfile;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by MrJiang on 2016/4/20.
 */
public interface SearchService {
  /**
   * 查询军嫂
   */
  @FormUrlEncoded @POST("/user/souSaoSao") Call<ArrayList<User>> filterJunSao(
      @Field("age") String age, @Field("height") String height, @Field("weight") String weight,
      @Field("profession") Integer profession, @Field("education") String education,
      @Field("marry") Integer marry, @Field("max_id") Integer maxId);

  /**
   * 查询战友
   * @param age
   * @param height
   * @param weight
   * @param marry
   * @param maxId
   * @param gender
   * @param serviceRegion
   * @param serviceArea
   * @param armyId
   * @param enterTime
   * @param retiredTime
   * @param armyYear
   * @return
   */
  @FormUrlEncoded @POST("/user/souSoldier") Call<ArrayList<User>> filterSoldier(
      @Field("age") String age, @Field("height") String height, @Field("weight") String weight,
      @Field("marry") Integer marry, @Field("max_id") Integer maxId, @Field("gender") String gender,
      @Field("service_region") Integer serviceRegion, @Field("service_area") Integer serviceArea,
      @Field("army_id") Integer armyId, @Field("enter_time") String enterTime,
      @Field("retired_time") String retiredTime, @Field("army_year") String armyYear);

  /**
   * 通过名字搜索
   */
  @GET("/user/findUser") Call<ArrayList<User>> filterByname(@Query("name") String name,
      @Query("role") String role);

  /**
   * 通过名字搜索IM
   * @param name
   * @return
   */
  @GET("/user/search") Call<ArrayList<IMProfile>> queryOpenIm(@Query("name") String name);
}
