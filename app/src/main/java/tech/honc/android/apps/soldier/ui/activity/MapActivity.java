package tech.honc.android.apps.soldier.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import tech.honc.android.apps.soldier.R;

/**
 * Created by MrJiang
 * on 2016/4/21.
 * 地图选择和展示
 */
public class MapActivity extends BaseActivity
    implements LocationSource, AMapLocationListener, AMap.OnMapClickListener,
    AMap.OnMapLongClickListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener {

  private MapView mapView;
  private AMap aMap;
  private OnLocationChangedListener mListener;
  private AMapLocationClient mlocationClient;
  private AMapLocationClientOption mLocationOption;
  private String mAddressName;
  private GeocodeSearch geocoderSearch;
  private LatLng mLatLng;
  private Marker marker2;
  private MarkerOptions markerOption;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_map);
    mapView = (MapView) findViewById(R.id.map);
    geocoderSearch = new GeocodeSearch(this);
    geocoderSearch.setOnGeocodeSearchListener(this);
    // 此方法必须重写
    assert mapView != null;
    mapView.onCreate(savedInstanceState);
    init();
  }

  /**
   * 设置一些amap的属性
   */
  private void setUpMap() {
    // 自定义系统定位小蓝点
    MyLocationStyle myLocationStyle = new MyLocationStyle();
    // 设置小蓝点的图标
    myLocationStyle.myLocationIcon(
        BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_deep_purple_400_48dp));
    // 设置圆形的边框颜色
    myLocationStyle.strokeColor(Color.BLACK);
    // 设置圆形的填充颜色
    myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
    //设置小蓝点的锚点
    // myLocationStyle.anchor(int,int)
    // 设置圆形的边框粗细
    myLocationStyle.strokeWidth(1.0f);
    aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
    aMap.setMyLocationStyle(myLocationStyle);
    // 设置定位监听
    aMap.setLocationSource(this);
    // 设置默认定位按钮是否显示
    aMap.getUiSettings().setMyLocationButtonEnabled(true);
    // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    aMap.setMyLocationEnabled(true);
    aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
  }

  /**
   * 初始化AMap对象
   */
  private void init() {
    if (aMap == null) {
      aMap = mapView.getMap();
      setUpMap();
    }
    // 对amap添加单击地图事件监听器
    aMap.setOnMapClickListener(this);
    // 对amap添加长按地图事件监听器
    aMap.setOnMapLongClickListener(this);
  }

  /**
   * 方法必须重写
   */
  @Override protected void onResume() {
    super.onResume();
    mapView.onResume();
  }

  /**
   * 方法必须重写
   */
  @Override protected void onPause() {
    super.onPause();
    mapView.onPause();
  }

  /**
   * 方法必须重写
   */
  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mapView.onSaveInstanceState(outState);
  }

  /**
   * 方法必须重写
   */
  @Override protected void onDestroy() {
    super.onDestroy();
    mapView.onDestroy();
  }

  @Override public void onLocationChanged(AMapLocation amapLocation) {
    if (mListener != null && amapLocation != null) {
      if (amapLocation.getErrorCode() == 0) {
        mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        markerOption.title(amapLocation.getCity());
        markerOption.draggable(true);
        //markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_rotate_left));
        marker2 = aMap.addMarker(markerOption);
        marker2.showInfoWindow();
      } else {
        String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
        Log.e("AmapErr", errText);
      }
    }
  }

  public void addMarker(LatLng latLng, String locations) {
    markerOption = new MarkerOptions();
    markerOption.position(new LatLng(latLng.latitude, latLng.longitude));
    markerOption.title(locations);
    markerOption.draggable(true);
    markerOption.icon(
        BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_deep_purple_400_48dp));
    marker2 = aMap.addMarker(markerOption);
    marker2.showInfoWindow();
  }

  @Override public void activate(OnLocationChangedListener listener) {
    mListener = listener;
    if (mlocationClient == null) {
      mlocationClient = new AMapLocationClient(this);
      mLocationOption = new AMapLocationClientOption();
      //设置定位监听
      mlocationClient.setLocationListener(this);
      //设置为高精度定位模式
      mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
      //设置定位参数
      mlocationClient.setLocationOption(mLocationOption);
      // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
      // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
      // 在定位结束后，在合适的生命周期调用onDestroy()方法
      // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
      mlocationClient.startLocation();
    }
  }

  @Override public void deactivate() {
    mListener = null;
    if (mlocationClient != null) {
      mlocationClient.stopLocation();
      mlocationClient.onDestroy();
    }
    mlocationClient = null;
  }

  @Override public void onMapClick(LatLng latLng) {
    double latitude = latLng.latitude;
    double longitude = latLng.longitude;
    mLatLng = latLng;
    LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
    getAddress(latLonPoint);
    addMarker(latLng, mAddressName);
  }

  @Override public void onMapLongClick(LatLng latLng) {
    double latitude = latLng.latitude;
    double longitude = latLng.longitude;
    mLatLng = latLng;
    LatLonPoint latLonPoint = new LatLonPoint(latitude, longitude);
    getAddress(latLonPoint);
    addMarker(latLng, mAddressName);
  }

  public void getAddress(final LatLonPoint latLonPoint) {
    RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
        GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
    geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
  }

  @Override public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
    if (rCode == 1000) {
      if (result != null
          && result.getRegeocodeAddress() != null
          && result.getRegeocodeAddress().getFormatAddress() != null) {
        mAddressName = result.getRegeocodeAddress().getFormatAddress();
      }
    }
  }

  @Override public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

  }

  @Override public boolean onMarkerClick(Marker marker) {
    Intent intent = new Intent(this, ReleaseHelpActivity.class);
    intent.putExtra("latitude", mLatLng.latitude);
    intent.putExtra("longitude", mLatLng.longitude);
    intent.putExtra("address", mAddressName);
    this.setResult(RESULT_OK, intent);
    finish();
    return false;
  }
}
