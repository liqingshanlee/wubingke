package tech.honc.android.apps.soldier.oss;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.smartydroid.android.starter.kit.app.StarterKitApp;
import tech.honc.android.apps.soldier.BuildConfig;

/**
 * Created by YuGang Yang on 03 14, 2016.
 * Copyright 20015-2016 honc.tech. All rights reserved.
 */
public final class OSSUtils {

  public OSS oss;

  public OSSUtils() {
    OSSCredentialProvider credentialProvider =
        new OSSPlainTextAKSKCredentialProvider(BuildConfig.OSS_ACCESS_KEY_ID,
            BuildConfig.OSS_ACCESS_KEY_SECRET);

    ClientConfiguration conf = new ClientConfiguration();
    conf.setConnectionTimeout(30 * 1000); // 连接超时，默认15秒
    conf.setSocketTimeout(30 * 1000); // socket超时，默认15秒
    conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
    conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
    OSSLog.enableLog();
    oss = new OSSClient(StarterKitApp.appContext(), BuildConfig.OSS_ENDPOINT, credentialProvider,
        conf);
  }
}
