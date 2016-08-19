package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tech.honc.android.apps.soldier.model.Avatar;
import tech.honc.android.apps.soldier.model.Certification;
import tech.honc.android.apps.soldier.model.Image;
import tech.honc.android.apps.soldier.model.Status;

/**
 * Created by MrJiang on 2016/4/29.
 */
public interface PhotoService {
  /**
   * 获取相册
   */
  @GET("/album/list") Call<ArrayList<Image>> getImages();

  /**
   * 上传图片
   */
  @FormUrlEncoded @POST("/user/upload_album") Call<Status> uploadPhoto(
      @Field("urls[]") ArrayList<String> list);

  /**
   * 删除照片
   */
  @FormUrlEncoded @POST("/album/delete") Call<Status> deletePhoto(

      @Field("picids") Integer photoId);

  /**
   * 修改头像
   */
  @FormUrlEncoded @POST("user/avatar") Call<Avatar> updateAvatar(@Field("url") String url);

  /**
   * 用户认证
   */
  @FormUrlEncoded @POST("/identify") Call<Certification> userCertification(
      @Field("pic") String pic);
}
