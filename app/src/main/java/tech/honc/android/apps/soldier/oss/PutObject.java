package tech.honc.android.apps.soldier.oss;

import android.util.Log;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

/**
 * Created by YuGang Yang on 03 14, 2016.
 * Copyright 20015-2016 honc.tech. All rights reserved.
 */
public class PutObject
{

  private OSS oss;
  private String testBucket;
  private String testObject;
  private String uploadFilePath;


  public PutObject(OSS client, String testBucket, String testObject, String uploadFilePath) {
    this.oss = client;
    this.testBucket = testBucket;
    this.testObject = testObject;
    this.uploadFilePath = uploadFilePath;
  }

  // 从本地文件上传，使用非阻塞的异步接口
  public void asyncPutObjectFromLocalFile() {
    // 构造上传请求
    PutObjectRequest put = new PutObjectRequest(testBucket, testObject, uploadFilePath);


    OSSAsyncTask task = oss.asyncPutObject(put,
      new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
        @Override public void onSuccess(PutObjectRequest request, PutObjectResult result) {
          Log.d("Callback", result.getServerCallbackReturnBody());
        }

        @Override public void onFailure(PutObjectRequest request, ClientException clientExcepion,
            ServiceException serviceException) {
          // 请求异常
          if (clientExcepion != null) {
            // 本地异常如网络异常等
            clientExcepion.printStackTrace();
          }
          if (serviceException != null) {
            // 服务异常
            Log.e("ErrorCode", serviceException.getErrorCode());
            Log.e("RequestId", serviceException.getRequestId());
            Log.e("HostId", serviceException.getHostId());
            Log.e("RawMessage", serviceException.getRawMessage());
          }
        }
      });
  }
}
