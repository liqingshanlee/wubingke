package tech.honc.android.apps.soldier.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import tech.honc.android.apps.soldier.R;

/**
 * Created by Administrator on 2016/4/21.
 */
public class AgreementActivity extends BaseActivity {

  @Bind(R.id.agreement_webview) WebView mWebView;
  @Bind(R.id.agreement_progress_bar) MaterialProgressBar mProgressBar;
  public static final String url = "file:///android_asset/userAgreement.html";
  private WebSettings mWebSettings;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_agreement);
    initWebView(url);
  }

  private void initWebView(String url) {

    mWebView.setWebChromeClient(new WebChromeClient() {
      @Override public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
          if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.setProgress(0);
          }
        } else {
          if (mProgressBar != null) {
            if (View.GONE == mProgressBar.getVisibility()) {
              mProgressBar.setVisibility(View.VISIBLE);
            }
            mProgressBar.setProgress(newProgress);
          }
        }

        super.onProgressChanged(view, newProgress);
      }
    });
    mWebView.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return super.shouldOverrideUrlLoading(view, url);
      }
    });
    mWebSettings = mWebView.getSettings();
    mWebSettings.setJavaScriptEnabled(true);
    mWebSettings.setDefaultTextEncodingName("UTF-8");
    mWebSettings.setBuiltInZoomControls(false);
    mWebView.loadUrl(url);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (mWebView != null) {
      mWebView.removeAllViews();
      mWebView.destroy();
    }
  }
}
