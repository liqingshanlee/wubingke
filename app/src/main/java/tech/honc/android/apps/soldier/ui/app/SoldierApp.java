package tech.honc.android.apps.soldier.ui.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import com.alibaba.wxlib.util.SysUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.smartydroid.android.starter.kit.StarterKit;
import com.smartydroid.android.starter.kit.account.Account;
import com.smartydroid.android.starter.kit.retrofit.RetrofitBuilder;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.socialize.PlatformConfig;
import im.fir.sdk.FIR;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.feature.im.helper.InitHelper;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.utils.toolsutils.JsonUtils;

/**
 * Created by MrJiang on 4/14/2016.
 * app
 */
public class SoldierApp extends BaseApp {

  private static Context sContext;
  public static final String NAMESPACE = "Soilder";

  public static Context getContext() {
    return sContext;
  }

  @Override public void onCreate() {
    super.onCreate();
    LeakCanary.install(this);
    FIR.init(this);
    new StarterKit.Builder().setDebug(BuildConfig.DEBUG).build();
    new RetrofitBuilder.Builder().accept(BuildConfig.API_ACCEPT)
        .baseUrl(BuildConfig.API_ENDPOINT)
        .build();
    initFresco();
    initEmeng();
    initOneSDK();
  }

  private void initFresco() {
    ImagePipelineConfig config = ImagePipelineConfig.newBuilder(appContext())
        .setDownsampleEnabled(true)
        .setWebpSupportEnabled(true)
        .build();
    Fresco.initialize(this, config);
  }

  @Override public Account accountFromJson(String json) {
    return JsonUtils.get().toObject(json, User.class);
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  /**
   * 友盟
   */
  public void initEmeng() {
    //微信 appid appsecret
    PlatformConfig.setWeixin(BuildConfig.WEIXIN_APP_ID, BuildConfig.WEIXIN_APP_SECRET);
    //新浪微博 appkey appsecret
    PlatformConfig.setSinaWeibo(BuildConfig.WEIBO_APP_ID, BuildConfig.WEIBO_APP_SECRET);
    // QQ和Qzone appid appkey
    PlatformConfig.setQQZone(BuildConfig.QQ_APP_ID, BuildConfig.QQ_APP_SECRET);
  }

  private void initOneSDK() {
    if (mustRunFirstInsideApplicationOnCreate()) {
      return;
    }
    InitHelper.initYWSDK(this);
  }

  private boolean mustRunFirstInsideApplicationOnCreate() {
    //必须的初始化
    SysUtil.setApplication(this);
    sContext = getApplicationContext();
    return SysUtil.isTCMSServiceProcess(sContext);
  }
}
