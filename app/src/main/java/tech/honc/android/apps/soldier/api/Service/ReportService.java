package tech.honc.android.apps.soldier.api.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import tech.honc.android.apps.soldier.model.Status;

/**
 * Created by kevin on 2016/6/23.
 */
public interface ReportService {

  @FormUrlEncoded @POST("/report") Call<Status> report(@Field("type") String type,
      @Field("content_id") Integer id, @Field("reason") String reason);
}
