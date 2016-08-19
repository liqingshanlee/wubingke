package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import java.util.regex.Pattern;
import tech.honc.android.apps.soldier.R;

/**
 * Created by Kevin on 2016/6/14.
 * 查看地图
 */
public class MapViewActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener {

  public static final String MAP_LATITUDE = "latitude";
  public static final String MAP_LONGTITUDE = "longtitude";
  public static final String CONTENT = "content";
  private MapView mMapView;
  private MarkerOptions mMarkerOption;
  private AMap mAMap;
  private String location;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_map_view);
    //获取地图控件引用
    mMapView = (MapView) findViewById(R.id.ui_view_map);
    //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
    assert mMapView != null;
    mMapView.onCreate(savedInstanceState);
    init();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
    mMapView.onDestroy();
  }

  @Override protected void onResume() {
    super.onResume();
    //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
    mMapView.onResume();
  }

  @Override protected void onPause() {
    super.onPause();
    //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
    mMapView.onPause();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
    mMapView.onSaveInstanceState(outState);
  }

  /**
   * 添加marker
   */
  public void addMarker(LatLng latLng, String locations) {
    if (latLng != null) mMarkerOption = new MarkerOptions();
    mMarkerOption.position(latLng);
    mMarkerOption.title(locations);
    mMarkerOption.draggable(true);
    mMarkerOption.icon(
        BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_deep_purple_400_48dp));
    Marker marker2 = mAMap.addMarker(mMarkerOption);
    marker2.showInfoWindow();
  }

  private void init() {
    mAMap = mMapView.getMap();
    location = getIntent().getStringExtra(CONTENT);
    String cityName = null;
    if (location != null && !TextUtils.isEmpty(location)) {
      String reg_charset = "\\u7701([\\u4e00-\\u9fa5]*)\\u5e02";
      Pattern p = Pattern.compile(reg_charset);
      String[] str = p.split(location);
      cityName = location.substring(str[0].length() + 1, location.length() - str[1].length());
    }
    // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
    if (cityName != null && !TextUtils.isEmpty(cityName)) {
      //如何将一个地理位置转换为地理位置
      GeocodeSearch geocoderSearch = new GeocodeSearch(this);
      geocoderSearch.setOnGeocodeSearchListener(this);
      GeocodeQuery query = new GeocodeQuery(location, cityName);
      geocoderSearch.getFromLocationNameAsyn(query);
    }
  }

  @Override public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

  }

  @Override public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    if (geocodeResult.getGeocodeAddressList().size()!=0) {
      double latitude = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint().getLatitude();
      double longitude = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint().getLongitude();
      LatLng latLng = new LatLng(latitude, longitude);
      mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
      addMarker(latLng, location);
    }
  }
}
