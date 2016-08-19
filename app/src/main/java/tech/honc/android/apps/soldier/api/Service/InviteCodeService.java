package tech.honc.android.apps.soldier.api.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import tech.honc.android.apps.soldier.model.InviteCode;

/**
 * Created by Administrator on 2016/6/7.
 */
public interface InviteCodeService {

  @GET("/share/getCode") Call<InviteCode> getInviteCode();
}
