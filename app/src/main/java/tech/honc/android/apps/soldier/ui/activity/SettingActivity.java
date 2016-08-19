package tech.honc.android.apps.soldier.ui.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.smartydroid.android.starter.kit.account.AccountManager;
import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import support.ui.cells.CellModel;
import support.ui.cells.CellsViewHolderFactory;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.feature.im.helper.LoginHelper;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.utils.settings.VersionCode;
import tech.honc.android.apps.soldier.utils.settings.VersionUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.DateFormat;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by Administrator on 2016/4/25.
 */
public class SettingActivity extends BaseActivity implements EasyViewHolder.OnItemClickListener
{
  @Bind(android.R.id.list) RecyclerView mRecyclerView;
  private EasyRecyclerAdapter mAdapter;
  public static final int MODIFICATION_PASSWROD = 1;
  public static final int FEED_BACK = 2;
  public static final int ABOUT_OURS = 3;
  public static final int TAG_USE_TERM = 4;
  public static final int CHECK_UPDATES = 5;
  private boolean isUpdating;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FIR.init(this);
    setContentView(R.layout.activity_setting_detail);
    setupRecyclerView();
  }

  private ArrayList<CellModel> buildAccountData() {
    ArrayList<CellModel> cellModels = new ArrayList<>();
    cellModels.add(CellModel.shadowCell().build());
    cellModels.add(CellModel.settingCell(getString(R.string.Change_Password))
        .tag(MODIFICATION_PASSWROD)
        .needDivider(true)
        .build());
    cellModels.add(CellModel.settingCell(getString(R.string.feed_back))
        .tag(FEED_BACK)
        .needDivider(true)
        .build());
    cellModels.add(CellModel.settingCell(getString(R.string.about_ours))
        .tag(ABOUT_OURS)
        .needDivider(true)
        .build());
    cellModels.add(CellModel.settingCell("使用规范").tag(TAG_USE_TERM).needDivider(true).build());
    cellModels.add(CellModel.settingCell("检查更新").tag(CHECK_UPDATES).build());
    return cellModels;
  }

  private void setupRecyclerView() {
    mAdapter = new EasyRecyclerAdapter(this);
    mAdapter.appendAll(buildAccountData());
    mAdapter.viewHolderFactory(new CellsViewHolderFactory(this));
    mAdapter.setOnClickListener(this);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override public void onItemClick(int position, View view) {
    if (mAdapter.get(position).getClass().getSimpleName().equals("CellModel")) {
      CellModel model = (CellModel) mAdapter.get(position);
      switch (model.tag) {
        case MODIFICATION_PASSWROD:
          User mUser = AccountManager.getCurrentAccount();
          if (mUser.mobile.length() != 11) {
            Toast.makeText(this, "三方登陆不可以改密码", Toast.LENGTH_SHORT).show();
            return;
          }
          Navigator.startModificationActivity(this);
          break;

        case FEED_BACK:
          Navigator.startFeedBackActivity(this);
          break;

        case ABOUT_OURS:
          Navigator.startAboutoursActivity(this);
          break;
        case TAG_USE_TERM:
          Navigator.startUseTermsActivity(this);
          break;
        case CHECK_UPDATES:
          checkUpdate();
          break;
      }
    }
  }

  @OnClick({ R.id.btn_out_login }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_out_login: {
        showHud("正在注销...");
        AccountManager.logout();
        LoginHelper.getInstance().loginOut();
        dismissHud();
        Navigator.startMainActivity(SettingActivity.this);
        break;
      }
    }
  }

  private void checkUpdate() {
    if (isUpdating) {
      return;
    }
    isUpdating = true;
    FIR.checkForUpdateInFIR("e381b756c07aad93cf00f982ca40fd00", new VersionCheckCallback()
    {
      public void onSuccess(String versionJson) {
        Log.i("fir", "check from fir.im success! " + "\n" + versionJson);
        delUpdateJson(versionJson);
      }

      @Override public void onFail(Exception exception) {
        Log.i("fir", "check fir.im fail! " + "\n" + exception.getMessage());
        isUpdating = false;
      }

      @Override public void onStart() {
        //Toast.makeText(getApplicationContext(), "正在获取", Toast.LENGTH_SHORT).show();
      }

      @Override public void onFinish() {
        isUpdating = false;
        //Toast.makeText(getApplicationContext(), "获取完成", Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void delUpdateJson(String versionJson) {
    try {
      JSONObject jsonObject = new JSONObject(versionJson);
      int remoteVersionCode = jsonObject.getInt("version");
      int test= VersionUtil.getVersionCode();
      Log.e("test",test+"");
      if (remoteVersionCode > VersionUtil.getVersionCode()) {
        showNewVersion(jsonObject);
      } else {
        SnackBarUtil.showTextByToast(this, "当前已经是最新版本", Gravity.CENTER);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void showNewVersion(final JSONObject object) throws JSONException {
    new MaterialDialog.Builder(this).title("发现新版本")
        .
            content("更新日期:"
                + DateFormat.getRelativeTime(object.getLong("updated_at"))
                + "\n"
                +
                "版本号:"
                + object.getString("versionShort")
                + "\n"
                + "安装包大小:"
                + VersionUtil.bytes2kb((object.getJSONObject("binary")).getLong("fsize"))
                + "\n"
                + "更新日志："
                + object.getString("changelog"))
        .negativeText("暂不更新")
        .positiveText("现在更新")
        .autoDismiss(false)
        .onNegative(new MaterialDialog.SingleButtonCallback()
        {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            dialog.dismiss();
          }
        })
        .onPositive(new MaterialDialog.SingleButtonCallback()
        {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL
                | DownloadManager.STATUS_RUNNING
                | DownloadManager.STATUS_PAUSED
                | DownloadManager.STATUS_PENDING);
            Cursor cursor = downloadManager.query(query);
            ArrayList<Long> ids = new ArrayList<>();
            while (cursor != null && cursor.moveToNext()) {
              if (cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME))
                  .endsWith(".apk")) {
                ids.add(cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID)));
              }
            }
            for (Long item : ids) {
              downloadManager.remove(item);
            }
            File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "update.apk");
            if (file.exists()) {
              file.deleteOnExit();
            }
            DownloadManager.Request request = null;
            try {
              request = new DownloadManager.Request(Uri.parse(object.getString("installUrl")));
            } catch (JSONException e) {
              e.printStackTrace();
              return;
            }
            request.setDestinationUri(Uri.fromFile(file));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setTitle("下载更新...");
            request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            request.setAllowedOverRoaming(true);
            request.allowScanningByMediaScanner();
            try {
              long downloadId = downloadManager.enqueue(request);
            } catch (IllegalArgumentException e) {
              SnackBarUtil.showTextByToast(SettingActivity.this, "检测到您的系统中下载组件被移除，请重置手机以恢复该组件。",
                  Gravity.CENTER);
            }
            dialog.dismiss();
          }
        })
        .build()
        .show();
  }
}
