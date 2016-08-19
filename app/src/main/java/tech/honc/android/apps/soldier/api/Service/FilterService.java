package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import tech.honc.android.apps.soldier.model.Profession;
import tech.honc.android.apps.soldier.model.Region;
import tech.honc.android.apps.soldier.model.Soldiers;

/**
 * Created by MrJiang on 2016/4/26.
 */
public interface FilterService {
  /**
   * 查询所有职业
   */
  @GET("/system/profession") Call<ArrayList<Profession>> searchProfession();

  /**
   * 获取军区
   */
  @GET("/system/region") Call<ArrayList<Region>> searchRegion();

  /**
   * 获取兵种
   */
  @GET("/system/soldiers") Call<ArrayList<Soldiers>> searchSoldier();
}
