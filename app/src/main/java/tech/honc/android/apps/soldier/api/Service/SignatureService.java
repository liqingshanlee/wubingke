package tech.honc.android.apps.soldier.api.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import tech.honc.android.apps.soldier.model.Signature;

/**
 * Created by Administrator on 2016/5/3.
 */
public interface SignatureService
{

  /**
   * 提交签名接口
   */
  @FormUrlEncoded @POST("user/setSignature") Call<Signature> postSign(@Field("sign") String sign);
}
