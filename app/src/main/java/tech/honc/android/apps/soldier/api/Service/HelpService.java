package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tech.honc.android.apps.soldier.model.Helper;

/**
 * Created by MrJiang on 4/28/2016.
 */
public interface HelpService {
  /**
   * 获取用户助人列表
   */
  @GET("/task/usefulList") Call<ArrayList<Helper>> getHelperList();

  /**
   * 别人的助人列表
   */
  @GET("/task/usefulList") Call<ArrayList<Helper>> getOtherHelperList(@Query("id") Integer id);
}
