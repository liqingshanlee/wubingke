package tech.honc.android.apps.soldier;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import com.smartydroid.android.starter.kit.account.AccountManager;
import java.util.ArrayList;
import tech.honc.android.apps.soldier.feature.im.model.IMProfileInfo;
import tech.honc.android.apps.soldier.feature.im.ui.activity.AddFriendActivity;
import tech.honc.android.apps.soldier.feature.im.ui.activity.InviteContactMembersActivity;
import tech.honc.android.apps.soldier.feature.im.ui.activity.SendAddContactRequestActivity;
import tech.honc.android.apps.soldier.model.Image;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.activity.AboutoursActivity;
import tech.honc.android.apps.soldier.ui.activity.AgreementActivity;
import tech.honc.android.apps.soldier.ui.activity.DynamicDetailsActivity;
import tech.honc.android.apps.soldier.ui.activity.FansActivity;
import tech.honc.android.apps.soldier.ui.activity.FeedBackActivity;
import tech.honc.android.apps.soldier.ui.activity.FilterActivity;
import tech.honc.android.apps.soldier.ui.activity.FilterAgainActivity;
import tech.honc.android.apps.soldier.ui.activity.FirstLoginActivity;
import tech.honc.android.apps.soldier.ui.activity.FocusActivity;
import tech.honc.android.apps.soldier.ui.activity.ForgotActivity;
import tech.honc.android.apps.soldier.ui.activity.HelpActivity;
import tech.honc.android.apps.soldier.ui.activity.InformationDetailActivity;
import tech.honc.android.apps.soldier.ui.activity.LoginActivity;
import tech.honc.android.apps.soldier.ui.activity.MainActivity;
import tech.honc.android.apps.soldier.ui.activity.MapActivity;
import tech.honc.android.apps.soldier.ui.activity.MapViewActivity;
import tech.honc.android.apps.soldier.ui.activity.ModificationActivity;
import tech.honc.android.apps.soldier.ui.activity.OtherAttentionsActivity;
import tech.honc.android.apps.soldier.ui.activity.OtherHelpActivity;
import tech.honc.android.apps.soldier.ui.activity.OtherHelperActivity;
import tech.honc.android.apps.soldier.ui.activity.OthersFansActivity;
import tech.honc.android.apps.soldier.ui.activity.PersonageDataActivity;
import tech.honc.android.apps.soldier.ui.activity.PersonalCollectionActivity;
import tech.honc.android.apps.soldier.ui.activity.PersonalFeedActivity;
import tech.honc.android.apps.soldier.ui.activity.PersonalPhotoGalleryActivity;
import tech.honc.android.apps.soldier.ui.activity.PersonalTaskActivity;
import tech.honc.android.apps.soldier.ui.activity.PersonalTaskDetailActivity;
import tech.honc.android.apps.soldier.ui.activity.ProfileRegisterActivity;
import tech.honc.android.apps.soldier.ui.activity.RegisterActivity;
import tech.honc.android.apps.soldier.ui.activity.ReleaseHelpActivity;
import tech.honc.android.apps.soldier.ui.activity.ReleaseOrdinaryActivity;
import tech.honc.android.apps.soldier.ui.activity.SettingActivity;
import tech.honc.android.apps.soldier.ui.activity.SignatureActivity;
import tech.honc.android.apps.soldier.ui.activity.SoldierAppIntro;
import tech.honc.android.apps.soldier.ui.activity.SoldierAreaActivity;
import tech.honc.android.apps.soldier.ui.activity.SoldierCertificationActivity;
import tech.honc.android.apps.soldier.ui.activity.SoldierDetailActivity;
import tech.honc.android.apps.soldier.ui.activity.TaskDynamicDetailsActivity;
import tech.honc.android.apps.soldier.ui.activity.UseTermsActivity;
import tech.honc.android.apps.soldier.ui.activity.UserFeedActivity;
import tech.honc.android.apps.soldier.utils.data.SearchData;
import tech.honc.android.apps.soldier.utils.imagebrowser.PhotoBDActivity;
import tech.honc.android.apps.soldier.utils.imagebrowser.PhotoBrowserActivity;

/**
 * Created by MrJiang on 4/14/2016.
 * 页面跳转
 */
public final class Navigator {
  /**
   * 首页->筛选
   */
  public static void startFilterActivity(Activity activity) {
    Intent intent = new Intent(activity, FilterActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到动态筛选
   */
  public static void startFilterAgainActivity(Activity activity, String identifier,
      ArrayList<User> users, SearchData mSearchData, String username) {
    Intent intent = new Intent(activity, FilterAgainActivity.class);
    intent.putExtra("identity", identifier);
    intent.putExtra("users", users);
    intent.putExtra("data", mSearchData);
    intent.putExtra("name", username);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到手机号登陆
   */
  public static void startLoginActivity(Activity activity) {
    Intent intent = new Intent(activity, LoginActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到详情页
   */
  public static void startFilterSoldierDetailActivity(Activity activity) {
    Intent intent = new Intent(activity, SoldierDetailActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * /* 跳转到注册页
   */
  public static void startRegisterActivity(Activity activity) {
    Intent intent = new Intent(activity, RegisterActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到发布帮助
   */
  public static void startReleaseHelpActivity(Activity activity) {
    Intent intent = new Intent(activity, ReleaseHelpActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * /* 跳转到忘记密码页
   */
  public static void startForgotActivity(Activity activity) {
    Intent intent = new Intent(activity, ForgotActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到基本资料也
   */
  public static void startProfileRegisterActivity(Activity activity) {
    Intent intent = new Intent(activity, ProfileRegisterActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到发布普通动态
   */
  public static void startReleaseOrdinaryActivity(Activity activity) {
    Intent intent = new Intent(activity, ReleaseOrdinaryActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到主页
   */
  public static void startMainActivity(Activity activity) {
    Intent intent = new Intent(activity, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到用户协议
   */
  public static void startagreementActivity(Activity activity) {
    Intent intent = new Intent(activity, AgreementActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  public static void startUserDetailActivty(Activity activity, int id) {
    Intent intent = new Intent(activity, SoldierDetailActivity.class);
    Intent intent1 = new Intent(activity, PersonalFeedActivity.class);
    User user = AccountManager.getCurrentAccount();
    if (user != null && user.id != null && user.id == id) {
      intent1.putExtra("user", user);
      ActivityCompat.startActivity(activity, intent1, null);
    } else {
      intent.putExtra("userid", id);
      ActivityCompat.startActivity(activity, intent, null);
    }
  }

  /**
   * 跳转到动态详情（同城/好友）
   */
  public static void startDynamicDetailsActivity(Activity activity, int id) {
    Intent intent = new Intent(activity, DynamicDetailsActivity.class);
    intent.putExtra("feedid", id);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到地图界面
   */
  public static void startMapActivity(Activity activity, int requestCode) {
    Intent intent = new Intent(activity, MapActivity.class);
    activity.startActivityForResult(intent, requestCode);
  }

  /**
   * 跳到设置界面
   */
  public static void startSettingActivity(Activity activity) {
    Intent intent = new Intent(activity, SettingActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到任务详情
   */
  public static void startTaskDynamicDetailsActivity(Activity activity, int id) {
    Intent intent = new Intent(activity, TaskDynamicDetailsActivity.class);
    intent.putExtra("taskid", id);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳到修改密码
   */
  public static void startModificationActivity(Activity activity) {
    Intent intent = new Intent(activity, ModificationActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳到意见反馈
   */
  public static void startFeedBackActivity(Activity activity) {
    Intent intent = new Intent(activity, FeedBackActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳到关于我们
   */
  public static void startAboutoursActivity(Activity activity) {
    Intent intent = new Intent(activity, AboutoursActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 资讯详情
   */
  public static void startInformationDetailActivity(Activity activity, int id) {
    Intent intent = new Intent(activity, InformationDetailActivity.class);
    intent.putExtra("id", id);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳到首页登录
   */
  public static void startFirstLoginActivity(Activity activity) {
    Intent intent = new Intent(activity, FirstLoginActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 图片浏览
   */
  public static void startPhotoBrowser(Activity activity, ArrayList<Image> images, int position) {
    Intent intent = new Intent(activity, PhotoBrowserActivity.class);
    intent.putParcelableArrayListExtra(PhotoBrowserActivity.IMAGE_LIST, images);
    intent.putExtra(PhotoBrowserActivity.IMAGE_POSITION, position);
    activity.startActivity(intent);
  }

  /**
   * 跳到个人资料
   */
  public static void startPersonageDataActivity(Activity activity) {
    Intent intent = new Intent(activity, PersonageDataActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳到帮助
   */
  public static void startHelpActivity(Activity activity) {
    Intent intent = new Intent(activity, HelpActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳到粉丝
   */
  public static void startFansActivity(Activity activity) {
    Intent intent = new Intent(activity, FansActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳到关注
   */
  public static void startFocusActivity(Activity activity) {
    Intent intent = new Intent(activity, FocusActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 别人的发布
   */
  public static void startUserFeedActivity(Activity activity, User user) {
    Intent intent = new Intent(activity, UserFeedActivity.class);
    intent.putExtra("user", user);
    activity.startActivity(intent);
  }

  /**
   * 我的发布
   */
  public static void startMyFeedActivity(Activity activity, User user) {
    Intent intent = new Intent(activity, PersonalFeedActivity.class);
    intent.putExtra("user", user);
    activity.startActivity(intent);
  }

  /**
   * 我的相册
   */
  public static void startPersonalPhotoGalleryActivity(Activity activity) {
    Intent intent = new Intent(activity, PersonalPhotoGalleryActivity.class);
    activity.startActivity(intent);
  }

  /**
   * 我的求助
   */
  public static void startPersonalTaskActivity(Activity activity) {
    Intent intent = new Intent(activity, PersonalTaskActivity.class);
    activity.startActivity(intent);
  }

  /**
   * 我的收藏
   */
  public static void startPersonalCollectionActivity(Activity activity) {
    Intent intent = new Intent(activity, PersonalCollectionActivity.class);
    activity.startActivity(intent);
  }

  /***
   * 跳转到个性签名
   */
  public static void startSignatureActivity(Activity activity) {
    Intent intent = new Intent(activity, SignatureActivity.class);
    activity.startActivity(intent);
  }

  /**
   * 图片浏览并且删除
   */
  public static void startPhotoBDActivity(Activity activity, ArrayList<Image> images,
      int position) {
    Intent intent = new Intent(activity, PhotoBDActivity.class);
    intent.putParcelableArrayListExtra(PhotoBDActivity.IMAGE_LIST, images);
    intent.putExtra(PhotoBDActivity.IMAGE_POSITION, position);
    activity.startActivity(intent);
  }

  /**
   * 引导页
   */
  public static void startSoldierAppIntro(Activity activity) {
    Intent intent = new Intent(activity, SoldierAppIntro.class);
    activity.startActivity(intent);
  }

  /**
   * 跳转到个人任务详情
   */
  public static void startPersonalTaskDetailActivity(Activity activity, int id) {
    Intent intent = new Intent(activity, PersonalTaskDetailActivity.class);
    intent.putExtra("taskid", id);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 别人的粉丝
   */
  public static void startOthersFansActivity(Activity activity, int id) {
    Intent intent = new Intent(activity, OthersFansActivity.class);
    intent.putExtra("id", id);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 别人的关注
   */
  public static void startOtherAttentionsActivity(Activity activity, int id) {
    Intent intent = new Intent(activity, OtherAttentionsActivity.class);
    intent.putExtra("id", id);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 别人的求助
   */
  public static void startOtherHelpActivity(Activity activity, int id) {
    Intent intent = new Intent(activity, OtherHelpActivity.class);
    intent.putExtra("id", id);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 别人的助人
   */
  public static void startOtherHelperActivity(Activity activity, int id) {
    Intent intent = new Intent(activity, OtherHelperActivity.class);
    intent.putExtra("id", id);
    ActivityCompat.startActivity(activity, intent, null);
  }

  public static void startAddFriendActivity(Activity activity, IMProfileInfo ywProfileInfo) {
    Intent intent = new Intent(activity, AddFriendActivity.class);
    intent.putExtra("profile", ywProfileInfo);
    ActivityCompat.startActivity(activity, intent, null);
  }

  public static void startSendAddContactRequestActivity(Activity activity, String id) {
    Intent intent = new Intent(activity, SendAddContactRequestActivity.class);
    intent.putExtra("id", id);
    ActivityCompat.startActivityForResult(activity, intent, AddFriendActivity.REQUEST_CODE, null);
  }

  public static void startInviteContactMembersActivity(Activity activity) {
    Intent intent = new Intent(activity, InviteContactMembersActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  public static void startUseTermsActivity(Activity activity) {
    Intent intent = new Intent(activity, UseTermsActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 打开地图
   */
  public static void startMapViewActivity(Activity activity, String message) {
    Intent intent = new Intent(activity, MapViewActivity.class);
    intent.putExtra(MapViewActivity.CONTENT, message);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 军人认证
   */
  public static void startSoldierCertificationActivity(Activity activity) {
    Intent intent = new Intent(activity, SoldierCertificationActivity.class);
    ActivityCompat.startActivity(activity, intent, null);
  }

  /**
   * 跳转到服役地区界面
   */
  public static void startSlodierAreaActivity(Activity activity) {
    Intent intent = new Intent(activity, SoldierAreaActivity.class);
    ActivityCompat.startActivityForResult(activity, intent, PersonageDataActivity.REQUEST_AREA,
        null);
  }
}
