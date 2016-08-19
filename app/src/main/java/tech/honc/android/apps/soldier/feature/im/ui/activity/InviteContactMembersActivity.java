package tech.honc.android.apps.soldier.feature.im.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.smartydroid.android.starter.kit.utilities.RecyclerViewUtils;
import java.util.ArrayList;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.FriendService;
import tech.honc.android.apps.soldier.feature.im.model.MobileContacts;
import tech.honc.android.apps.soldier.feature.im.ui.adapter.MobileContactsAdapter;
import tech.honc.android.apps.soldier.feature.im.utils.MobileContactsComparator;
import tech.honc.android.apps.soldier.feature.im.widget.SideBar;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;
import tech.honc.android.apps.soldier.ui.appInterface.ContactOnItemClickListener;
import tech.honc.android.apps.soldier.utils.toolsutils.PinyinUtils;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by MrJiang on 2016/6/3.
 * 邀请通讯录成员
 */
public class InviteContactMembersActivity extends BaseActivity
    implements ContactOnItemClickListener {

  private static final int REQUEST_CODE = 100;
  @Bind(R.id.recycler_members) RecyclerView mRecyclerMembers;
  @Bind(R.id.ui_view_bubble) TextView mUiViewBubble;
  @Bind(R.id.ui_sidebar) SideBar mUiSidebar;
  private MobileContactsAdapter mEasyRecyclerAdapter;
  private FriendService mFriendService;
  private ArrayList<User> mFriendsDatas;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_invite_contact_members);
    ButterKnife.bind(this);
    initValues();
    setUpView();
    //读取通讯录内容
    requestPermission();
  }

  /**
   * 初始化变量
   */
  private void initValues() {
    mFriendService = ApiService.createFriendService();
    mFriendsDatas = new ArrayList<>();
  }

  @Override protected void onResume() {
    super.onResume();
  }

  public void startUpdate() {
    mEasyRecyclerAdapter.notifyDataSetChanged();
  }

  private void setUpView() {
    mEasyRecyclerAdapter = new MobileContactsAdapter(this, readMobileContacts());
    mRecyclerMembers.addItemDecoration(RecyclerViewUtils.buildItemDecoration(this));
    mRecyclerMembers.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    mRecyclerMembers.setAdapter(mEasyRecyclerAdapter);
  }

  public void SendSMS(String phoneNumber, String message) {
    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
    intent.putExtra("sms_body", message);
    startActivity(intent);
  }

  /**
   * @return common data
   */
  public ArrayList<MobileContacts> readMobileContacts() {
    connectNetWorkFriends();
    ArrayList<MobileContacts> user = new ArrayList<>();
    Cursor phones =
        getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
            null, " display_name COLLATE LOCALIZED ");
    if (phones != null) {
      int position = 0;
      while (phones.moveToNext()) {
        String name = phones.getString(
            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String phoneNumber =
            phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        MobileContacts contacts = new MobileContacts();
        contacts.id = position;
        contacts.nickname = name;
        contacts.mobile = phoneNumber;
        user.add(contacts);
        position++;
      }
    }
    return filterContacts(mFriendsDatas, user);
  }

  /**
   * 联网获取我的好友
   */
  public void connectNetWorkFriends() {
    Call<ArrayList<User>> call = mFriendService.myFriends();
    call.enqueue(new Callback<ArrayList<User>>() {
      @Override
      public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
        if (response.isSuccessful()) {
          mFriendsDatas = response.body();
        }
      }

      @Override public void onFailure(Call<ArrayList<User>> call, Throwable t) {
        Toast.makeText(InviteContactMembersActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
      }
    });
  }

  /**
   * 对网络数据和本地通讯录进行删选
   *
   * @param user 网络数据
   * @param mobile 本地数据
   * @return filter
   */
  public ArrayList<MobileContacts> filterContacts(ArrayList<User> user,
      ArrayList<MobileContacts> mobile) {
    if (user.size() != 0 && mobile.size() != 0) {
      for (MobileContacts contacts : mobile) {
        for (User u : user) {
          if (u.mobile.equals(contacts.mobile)) {
            contacts.avatar = u.avatar;
            contacts.openImId = u.openImId;
            contacts.hasAccount = true;
            break;
          }
        }
      }
    }
    Collections.sort(mobile, new MobileContactsComparator());
    mUiSidebar.setUpCharList(sideBarDatas(mobile));
    mUiSidebar.setBubble(mUiViewBubble);
    mUiSidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
      @Override public void onTouchingLetterChanged(String s) {
        int position = mEasyRecyclerAdapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
          mRecyclerMembers.scrollToPosition(position);
        }
      }
    });
    return mobile;
  }

  public ArrayList<String> sideBarDatas(ArrayList<MobileContacts> mobile) {
    ArrayList<String> list = new ArrayList<>();
    for (MobileContacts m : mobile) {
      if (!list.contains(PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(m.nickname)))) {
        list.add(PinyinUtils.getFirstLetter(
            PinyinUtils.getFirstLetter(PinyinUtils.getPinYin(m.nickname))));
      }
    }
    return list;
  }

  @TargetApi(Build.VERSION_CODES.M) public void requestPermission() {
    //判断当前Activity是否已经获得了该权限
    String[] permissions =
        { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED) {

        //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
          SnackBarUtil.showText(this, "选择照片需要权限哦，请同意");
        } else {
          //进行权限请求
          ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }
      } else {
        startUpdate();
      }
    } else {
      startUpdate();
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch (requestCode) {
      case REQUEST_CODE: {
        // 如果请求被拒绝，那么通常grantResults数组为空
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          startUpdate();
        } else {
          SnackBarUtil.showText(this, "你没有权限哦");
        }
      }
    }
  }

  @Override public void onItemClick(MobileContacts contacts) {
    if (contacts != null) {
      SendSMS(contacts.mobile, "欢迎下载伍兵客APP，遇见你心仪的战友或军嫂\n");
    }
  }
}
