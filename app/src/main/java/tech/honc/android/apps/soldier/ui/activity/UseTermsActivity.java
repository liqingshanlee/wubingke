package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import butterknife.Bind;
import butterknife.ButterKnife;
import tech.honc.android.apps.soldier.R;

/**
 * Created by MrJiang on 2016/6/7.
 */
public class UseTermsActivity extends BaseActivity {

  @Bind(R.id.ui_use_terms) WebView mUiUseTerms;
  public static final String url = "file:///android_asset/standard.html";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_use_terms);
    ButterKnife.bind(this);
    WebSettings webSetting = mUiUseTerms.getSettings();
    webSetting.setJavaScriptEnabled(true);
    webSetting.setDefaultTextEncodingName("UTF-8");
    webSetting.setBuiltInZoomControls(false);
    mUiUseTerms.loadUrl(url);
  }
}
