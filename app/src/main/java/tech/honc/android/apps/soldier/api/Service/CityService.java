package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import tech.honc.android.apps.soldier.model.City;

/**
 * Created by MrJiang on 2016/4/25.
 */
public interface CityService {

  @GET("/system/city") Call<ArrayList<City>> getCitys();
  @GET("/system/areas") Call<ArrayList<City>> getAreas();
}
