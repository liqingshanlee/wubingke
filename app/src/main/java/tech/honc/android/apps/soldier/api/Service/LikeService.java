package tech.honc.android.apps.soldier.api.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import tech.honc.android.apps.soldier.model.Likes;

/**
 * Created by MrJiang on 2016/5/3.
 */
public interface LikeService {

  @FormUrlEncoded @POST("/articlelikes") Call<Likes> likesCall(@Field("article-id") Integer id);
}
