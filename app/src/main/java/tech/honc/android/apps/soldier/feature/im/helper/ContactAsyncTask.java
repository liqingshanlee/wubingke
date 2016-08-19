package tech.honc.android.apps.soldier.feature.im.helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.AccountService;
import tech.honc.android.apps.soldier.feature.im.interfaces.ContactTranform;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by kevin on 16-6-8.
 * 这样获取是不是很傻
 */
public class ContactAsyncTask {

  private static ContactAsyncTask instance;

  private ContactAsyncTask() {

  }

  public static ContactAsyncTask getInstance() {
    if (instance == null) {
      synchronized (ContactAsyncTask.class) {
        instance = new ContactAsyncTask();
      }
    }
    return instance;
  }

  public void getIMProfile(String openIMId, final ContactTranform tranform) {
    AccountService service = ApiService.createAccountService();
    Call<User> userCall = service.getUserIm(openIMId);
    userCall.enqueue(new Callback<User>() {
      @Override public void onResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful()) {
          tranform.ContactTranformCallback(response.body());
        }
      }

      @Override public void onFailure(Call<User> call, Throwable t) {

      }
    });
  }
}
