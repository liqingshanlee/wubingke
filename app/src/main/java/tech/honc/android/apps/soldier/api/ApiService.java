package tech.honc.android.apps.soldier.api;

import com.smartydroid.android.starter.kit.retrofit.RetrofitBuilder;
import retrofit2.Retrofit;
import tech.honc.android.apps.soldier.api.Service.AccountService;
import tech.honc.android.apps.soldier.api.Service.ArticleService;
import tech.honc.android.apps.soldier.api.Service.AttentionService;
import tech.honc.android.apps.soldier.api.Service.AuthService;
import tech.honc.android.apps.soldier.api.Service.CityService;
import tech.honc.android.apps.soldier.api.Service.CollectionService;
import tech.honc.android.apps.soldier.api.Service.FeedService;
import tech.honc.android.apps.soldier.api.Service.FilterService;
import tech.honc.android.apps.soldier.api.Service.FriendService;
import tech.honc.android.apps.soldier.api.Service.HelpService;
import tech.honc.android.apps.soldier.api.Service.InviteCodeService;
import tech.honc.android.apps.soldier.api.Service.LikeService;
import tech.honc.android.apps.soldier.api.Service.PhotoService;
import tech.honc.android.apps.soldier.api.Service.ReportService;
import tech.honc.android.apps.soldier.api.Service.SearchService;
import tech.honc.android.apps.soldier.api.Service.SignatureService;
import tech.honc.android.apps.soldier.api.Service.UpdateProfileService;

/**
 * Created by MrJiang on 4/14/2016.
 */
public class ApiService {

  public static UpdateProfileService createUpdateProfileService() {
    return retrofit().create(UpdateProfileService.class);
  }

  public static AuthService createAuthService() {

    return retrofit().create(AuthService.class);
  }

  public static AccountService createAccountService() {

    return retrofit().create(AccountService.class);
  }

  public static SearchService createSearchService() {
    return retrofit().create(SearchService.class);
  }

  //动态API service
  public static FeedService createFeedService() {

    return retrofit().create(FeedService.class);
  }

  public static ArticleService createArticleService() {
    return retrofit().create(ArticleService.class);
  }

  public static CityService createCityService() {
    return retrofit().create(CityService.class);
  }

  public static FilterService createFilterService() {
    return retrofit().create(FilterService.class);
  }

  public static AttentionService createAttentionService() {
    return retrofit().create(AttentionService.class);
  }

  public static HelpService createHelperService() {
    return retrofit().create(HelpService.class);
  }

  public static PhotoService createPhotoService() {
    return retrofit().create(PhotoService.class);
  }

  public static LikeService createLikeService() {
    return retrofit().create(LikeService.class);
  }

  public static CollectionService createCollectionService() {
    return retrofit().create(CollectionService.class);
  }

  public static SignatureService createSignatureService() {
    return retrofit().create(SignatureService.class);
  }

  /**
   * 添加朋友
   */
  public static FriendService createFriendService() {
    return retrofit().create(FriendService.class);
  }

  public static InviteCodeService createInviteCodeService() {
    return retrofit().create(InviteCodeService.class);
  }

  public static ReportService createReportService() {
    return retrofit().create(ReportService.class);
  }

  private static Retrofit retrofit() {
    return RetrofitBuilder.get().retrofit();
  }
}
