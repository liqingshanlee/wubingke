package tech.honc.android.apps.soldier.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import butterknife.Bind;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.smartydroid.android.starter.kit.account.AccountManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import support.ui.cells.CellModel;
import support.ui.cells.CellsViewHolderFactory;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.CityService;
import tech.honc.android.apps.soldier.api.Service.FilterService;
import tech.honc.android.apps.soldier.api.Service.UpdateProfileService;
import tech.honc.android.apps.soldier.model.City;
import tech.honc.android.apps.soldier.model.Profession;
import tech.honc.android.apps.soldier.model.Profile;
import tech.honc.android.apps.soldier.model.Region;
import tech.honc.android.apps.soldier.model.Soldiers;
import tech.honc.android.apps.soldier.model.UpdateData;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.utils.buildUtils.FilterBuildUtils;
import tech.honc.android.apps.soldier.utils.buildUtils.PersonageDataUtils;
import tech.honc.android.apps.soldier.utils.data.PersonageData;
import tech.honc.android.apps.soldier.utils.data.WheelView;
import tech.honc.android.apps.soldier.utils.toolsutils.DateFormat;
import tech.honc.android.apps.soldier.utils.toolsutils.DialogUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by Administrator on 2016/4/27.
 */
public class PersonageDataActivity extends BaseActivity
    implements EasyViewHolder.OnItemClickListener, DialogUtil.ItemsTag, MaterialDialog.ListCallback,
    DatePickerDialog.OnDateSetListener
{
  public static final int TAG_SOLDIER_AREA = 1;
  public static final int TAG_SOLDIER_MILLITARY = 2;
  public static final int TAG_SOLDIER_COME = 3;
  public static final int TAG_SOLDIER_GO = 4;

  public static final int TAG_NAME = 5;
  public static final int TAG_AGE = 6;
  public static final int TAG_NICHENG = 8;
  public static final int TAG_SEX = 7;
  public static final int TAG_AREA = 9;
  public static final int TAG_EDUCATION = 10;
  public static final int TAG_HEIGHT = 11;
  public static final int TAG_WEIGHT = 12;
  public static final int TAG_ARM = 13;
  public static final int TAG_DUTY = 14;
  public static final int TAG_MARRIGE = 15;
  public static final int TAG_HOBBY = 16;
  public static final int REQUEST_AREA = 100;

  public static String mName = null;
  public static String mNiCheng = null;
  public static String mSoldierCome = null;
  public static String mSoldierGo = null;
  public static String mCity = null;
  public static String mEducation = null;
  public static String mMarriy = null;
  public static String mAge = null;

  public PersonageData mpersonageData;
  private int mTag;
  private EasyRecyclerAdapter mAdapter;
  private User mUser;
  private ArrayList<String> mProfessionsList = new ArrayList<>();
  private FilterService mFilterService;
  private CityService mCityService;
  private UpdateProfileService mUpdateProfileService;
  private ArrayList<Profession> mProfessions = new ArrayList<>();
  private ArrayList<String> mRegionStrings = new ArrayList<>();
  private ArrayList<Region> mRegionList = new ArrayList<>();
  private ArrayList<String> mCityArrayList = new ArrayList<>();
  public ArrayList<City> mAllCities = new ArrayList<>();
  private ArrayList mAgeList = new ArrayList<>();
  private ArrayList mWeightList = new ArrayList<>();
  private ArrayList mHeightList = new ArrayList<>();
  private ArrayList<String> mSoldierStrings = new ArrayList<>();
  private ArrayList<Soldiers> mSoldierList = new ArrayList<>();
  @Bind(R.id.person_recycler) RecyclerView mRecyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_personage);
    mpersonageData = new PersonageData();
    setupViews();
    setupRecyclerView();
    mFilterService = ApiService.createFilterService();
    mCityService = ApiService.createCityService();
    mUpdateProfileService = ApiService.createUpdateProfileService();
    getProfession();
    getMillitary();
    getCitys();
    getWeight();
    getHeight();
    getSearchData();
    clerndate();
    getProfile();
  }

  public void clerndate() {
    mName = null;
    mNiCheng = null;
    mSoldierCome = null;
    mSoldierGo = null;
    mCity = null;
    mEducation = null;
    mMarriy = null;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.save_ok) {
      submitDataInformation();
    } else if (item.getItemId() == android.R.id.home) {
      finish();
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_save, menu);
    return super.onCreateOptionsMenu(menu);
  }

  //获取个人资料信息
  private void getProfile() {
    Call<Profile> call = mUpdateProfileService.getProfile();
    call.enqueue(new Callback<Profile>()
    {
      @Override public void onResponse(Call<Profile> call, Response<Profile> response) {
        if (response.isSuccessful()) {
          upDateFile(response.body());
        }
      }

      @Override public void onFailure(Call<Profile> call, Throwable t) {
        Log.e("test", t.toString());
        dismissHud();
      }
    });
  }

  private void upDateFile(Profile profile) {
    Boolean mFile = true;
    if (mFile) {

      if (profile.army != null) {
        City soldiersArea = new City();
        soldiersArea.id = profile.army.areaId;
        soldiersArea.name = profile.army.area;
        mpersonageData.setArea(soldiersArea);
        Region soldiersMillitary = new Region();
        soldiersMillitary.id = profile.army.regionId;
        soldiersMillitary.name = profile.army.region;
        mpersonageData.setMilitary(soldiersMillitary);
        if (profile.army.enlistmentTime != null) {
          String d1 = (String) DateFormat.getRelativeTime(profile.army.enlistmentTime);
          mpersonageData.setJoin(d1);
        }
        if (profile.army.retiredtime != null) {
          String d2 = (String) DateFormat.getRelativeTime(profile.army.retiredtime);
          mpersonageData.setRetire(d2);
        }
        Soldiers soldiers = new Soldiers();
        soldiers.id = profile.army.armyKindId;
        soldiers.name = profile.army.armyKind;
        mpersonageData.setmArm(soldiers);
      }
      if (profile.accountdetail != null) {
        mName = profile.accountdetail.realName;
        mpersonageData.setEducation(profile.accountdetail.education);
        mpersonageData.setMarriage(getMarred(profile.accountdetail.married));
        mpersonageData.setAge(profile.accountdetail.age);
        mpersonageData.setHeight(profile.accountdetail.height);
        mpersonageData.setWeight(profile.accountdetail.weight);
        Profession soldiersDuty = new Profession();
        soldiersDuty.id = profile.accountdetail.professionid;
        soldiersDuty.name = profile.accountdetail.profession;
        mpersonageData.setProfession(soldiersDuty);
      }
      mpersonageData.setLocation(profile.city);
      mNiCheng = profile.nickname;
      mFile = false;
    }
    if (!mFile) {
      dismissHud();
      startUpdate();
    }
  }

  public static String getMarred(Integer marry) {
    if (marry != null) {
      if (marry.equals(1)) {
        return "已婚";
      }
      return "未婚";
    }
    return null;
  }

  //获取体重
  private void getWeight() {
    int i;
    for (i = 45; i < 100; i++) {
      mWeightList.add(i + "");
    }
  }

  //获取身高
  private void getHeight() {
    int i;
    for (i = 160; i < 210; i++) {
      mHeightList.add(i + "");
    }
  }

  //获取职业信息
  private void getProfession() {
    Call<ArrayList<Profession>> call = mFilterService.searchProfession();
    call.enqueue(new Callback<ArrayList<Profession>>()
    {
      @Override public void onResponse(Call<ArrayList<Profession>> call,
          Response<ArrayList<Profession>> response) {
        if (response.isSuccessful()) {
          mProfessions = response.body();
          for (Profession p : mProfessions) {
            mProfessionsList.add(p.name);
          }
        }
      }

      @Override public void onFailure(Call<ArrayList<Profession>> call, Throwable t) {

      }
    });
  }

  //获取军区
  private void getMillitary() {

    Call<ArrayList<Region>> callRegion = mFilterService.searchRegion();
    callRegion.enqueue(new Callback<ArrayList<Region>>()
    {
      @Override
      public void onResponse(Call<ArrayList<Region>> call, Response<ArrayList<Region>> response) {
        if (response.isSuccessful()) {
          if (response.isSuccessful()) {
            mRegionList = response.body();
            for (Region r : mRegionList) {
              mRegionStrings.add(r.name);
            }
          }
        }
      }

      @Override public void onFailure(Call<ArrayList<Region>> call, Throwable t) {

      }
    });
  }

  //获取兵种
  private void getSearchData() {
    Call<ArrayList<Soldiers>> callSoldier = mFilterService.searchSoldier();
    callSoldier.enqueue(new Callback<ArrayList<Soldiers>>()
    {
      @Override public void onResponse(Call<ArrayList<Soldiers>> call,
          Response<ArrayList<Soldiers>> response) {
        if (response.isSuccessful()) {
          mSoldierList = response.body();
          for (Soldiers r : mSoldierList) {
            mSoldierStrings.add(r.name);
          }
        }
      }

      @Override public void onFailure(Call<ArrayList<Soldiers>> call, Throwable t) {

      }
    });
  }

  //获取城市列表
  private void getCitys() {
    Call<ArrayList<City>> call = mCityService.getCitys();
    call.enqueue(new Callback<ArrayList<City>>()
    {
      @Override
      public void onResponse(Call<ArrayList<City>> call, Response<ArrayList<City>> response) {
        if (response.isSuccessful()) {
          mAllCities = response.body();
          for (City r : mAllCities) {
            mCityArrayList.add(r.name);
          }
        }
      }

      @Override public void onFailure(Call<ArrayList<City>> call, Throwable t) {

      }
    });
  }

  public void setupViews() {
    mAdapter = new EasyRecyclerAdapter(this);
    if (AccountManager.isLogin()) {
      mUser = AccountManager.getCurrentAccount();
      if (mUser.role != null) {
        switch (mUser.role) {
          case SOLDIER:
            mAdapter.appendAll(PersonageDataUtils.buildManData(mpersonageData));
            break;
          case JUNSAO:
            mAdapter.appendAll(PersonageDataUtils.buildWumanData(mpersonageData));
            break;
        }
      } else {
        SnackBarUtil.showText(this, "用户数据为空");
      }
    }
  }

  private void setupRecyclerView() {
    mAdapter.viewHolderFactory(new CellsViewHolderFactory(this));
    mAdapter.setOnClickListener(this);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override public void onItemClick(int position, View view) {
    if (mAdapter.get(position).getClass().getSimpleName().equals("CellModel")) {
      CellModel model = (CellModel) mAdapter.get(position);
      DialogUtil.setItemTagListener(this);
      switch (model.tag) {
        case TAG_SOLDIER_AREA:
          //DialogUtil.showFilterDialog(this, this, mCityArrayList, TAG_SOLDIER_AREA);
          Navigator.startSlodierAreaActivity(this);
          break;
        case TAG_SOLDIER_MILLITARY:
          DialogUtil.showFilterDialog(this, this, mRegionStrings, TAG_SOLDIER_MILLITARY);
          break;
        case TAG_SOLDIER_COME:
          DialogUtil.showDatePicker(this, this, TAG_SOLDIER_COME);
          break;
        case TAG_SOLDIER_GO:
          DialogUtil.showDatePicker(this, this, TAG_SOLDIER_GO);
          break;
        case TAG_NAME:
          new MaterialDialog.Builder(this).title("姓名")
              .input("请输入姓名", null, new MaterialDialog.InputCallback()
              {
                @Override public void onInput(MaterialDialog dialog, CharSequence input) {

                }
              })
              .negativeText("取消")
              .cancelable(false)
              .onNegative(new MaterialDialog.SingleButtonCallback()
              {
                @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                  dialog.dismiss();
                }
              })
              .onPositive(new MaterialDialog.SingleButtonCallback()
              {
                @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                  if (dialog.getInputEditText().getText().length() > 10 || dialog.getInputEditText()
                      .getText()
                      .toString()
                      .equals("")) {
                    Toast.makeText(getApplicationContext(), "输入内容不规范", Toast.LENGTH_LONG).show();
                  } else {
                    mName = dialog.getInputEditText().getText().toString();
                    startUpdate();
                    dialog.dismiss();
                  }
                }
              })
              .autoDismiss(false)
              .build()
              .show();
          break;
        case TAG_AGE:
          DialogUtil.showDatePicker(this, this, TAG_AGE);
          break;
        case TAG_NICHENG:
          new MaterialDialog.Builder(this).title("昵称")
              .input("请输入姓名", null, new MaterialDialog.InputCallback()
              {
                @Override public void onInput(MaterialDialog dialog, CharSequence input) {

                }
              })
              .negativeText("取消")
              .cancelable(false)
              .onNegative(new MaterialDialog.SingleButtonCallback()
              {
                @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                  dialog.dismiss();
                }
              })
              .onPositive(new MaterialDialog.SingleButtonCallback()
              {
                @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                  if (dialog.getInputEditText().getText().length() > 10 || dialog.getInputEditText()
                      .getText()
                      .toString()
                      .equals("")) {
                    Toast.makeText(getApplicationContext(), "输入内容不规范", Toast.LENGTH_LONG).show();
                  } else {
                    mNiCheng = dialog.getInputEditText().getText().toString();
                    startUpdate();
                    dialog.dismiss();
                  }
                }
              })
              .autoDismiss(false)
              .build()
              .show();
          break;
        case TAG_AREA:
          DialogUtil.showFilterDialog(this, this, mCityArrayList, TAG_AREA);
          break;
        case TAG_EDUCATION:
          DialogUtil.showFilterDialog(this, this, R.array.grade, TAG_EDUCATION);
          break;
        case TAG_HEIGHT:
          //DialogUtil.showFilterDialog(this, this, mHeightList, TAG_HEIGHT);
          View outerView = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
          WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
          wv.setOffset(2);
          wv.setItems(mHeightList);
          wv.setSeletion(3);
          wv.setOnWheelViewListener(new WheelView.OnWheelViewListener()
          {
            @Override public void onSelected(int selectedIndex, String item) {
              mpersonageData.setHeight(item);
              startUpdate();
            }
          });

          new AlertDialog.Builder(this).setTitle("请选择身高：")
              .setView(outerView)
              .setPositiveButton("OK", null)
              .show();
          startUpdate();
          break;
        case TAG_WEIGHT:
          //DialogUtil.showFilterDialog(this, this, mWeightList, TAG_WEIGHT);
          View outerView1 = LayoutInflater.from(this).inflate(R.layout.wheel_view, null);
          WheelView wv1 = (WheelView) outerView1.findViewById(R.id.wheel_view_wv);
          wv1.setOffset(2);
          wv1.setItems(mWeightList);
          wv1.setSeletion(3);
          wv1.setOnWheelViewListener(new WheelView.OnWheelViewListener()
          {
            @Override public void onSelected(int selectedIndex, String item) {
              mpersonageData.setWeight(item);
              startUpdate();
            }
          });

          new AlertDialog.Builder(this).setTitle("请选择体重：")
              .setView(outerView1)
              .setPositiveButton("OK", null)
              .show();
          startUpdate();
          break;
        case TAG_DUTY:
          DialogUtil.showFilterDialog(this, this, mProfessionsList, TAG_DUTY);
          break;
        case TAG_ARM:
          DialogUtil.showFilterDialog(this, this, mSoldierStrings, TAG_ARM);
          break;
        case TAG_MARRIGE:
          DialogUtil.showFilterDialog(this, this, R.array.marry, TAG_MARRIGE);
          break;
        case TAG_HOBBY:
          new MaterialDialog.Builder(this).title("嗜好")
              .input("请输入你的嗜好", null, new MaterialDialog.InputCallback()
              {
                @Override public void onInput(MaterialDialog dialog, CharSequence input) {

                }
              })
              .negativeText("取消")
              .cancelable(false)
              .onNegative(new MaterialDialog.SingleButtonCallback()
              {
                @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                  dialog.dismiss();
                }
              })
              .onPositive(new MaterialDialog.SingleButtonCallback()
              {
                @Override public void onClick(MaterialDialog dialog, DialogAction which) {
                  if (dialog.getInputEditText().getText().length() > 10 || dialog.getInputEditText()
                      .getText()
                      .toString()
                      .equals("")) {
                    Toast.makeText(getApplicationContext(), "输入内容不规范", Toast.LENGTH_LONG).show();
                  } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
                  }
                }
              })
              .autoDismiss(false)
              .build()
              .show();
          break;
      }
    }
  }

  @Override public void sendItemTag(int tag) {
    mTag = tag;
  }

  @Override
  public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
    switch (mTag) {
      case TAG_SOLDIER_AREA:
        City soldiersArea = new City();
        soldiersArea.name = text.toString();
        soldiersArea.id = FilterBuildUtils.getAreaType(mAllCities, mCityArrayList, text.toString());
        mpersonageData.setArea(soldiersArea);
        break;
      case TAG_SOLDIER_MILLITARY:
        Region soldiersMillitary = new Region();
        soldiersMillitary.name = text.toString();
        soldiersMillitary.id =
            FilterBuildUtils.getMillitaryType(mRegionList, mRegionStrings, text.toString());
        mpersonageData.setMilitary(soldiersMillitary);
        break;
      case TAG_SOLDIER_COME:

        break;
      case TAG_SOLDIER_GO:

        break;
      case TAG_NAME:

        break;
      case TAG_AGE:

        break;
      case TAG_NICHENG:

        break;
      case TAG_AREA:
        mpersonageData.setLocation(text.toString());
        mCity = text.toString();
        break;
      case TAG_EDUCATION:
        mpersonageData.setEducation(text.toString());
        mEducation = text.toString();
        break;
      case TAG_HEIGHT:
        mpersonageData.setHeight(text.toString());
        break;
      case TAG_WEIGHT:
        mpersonageData.setWeight(text.toString());
        break;
      case TAG_ARM:
        Soldiers soldiers = new Soldiers();
        soldiers.name = text.toString();
        soldiers.id = FilterBuildUtils.getArmyType(mSoldierList, mSoldierStrings, text.toString());
        mpersonageData.setmArm(soldiers);
        break;
      case TAG_DUTY:
        Profession soldiersDuty = new Profession();
        soldiersDuty.name = text.toString();
        soldiersDuty.id =
            FilterBuildUtils.getDutyType(mProfessions, mProfessionsList, text.toString());
        mpersonageData.setProfession(soldiersDuty);
        break;
      case TAG_MARRIGE:
        mpersonageData.setMarriage(text.toString());
        mMarriy = text.toString();
        break;
    }
    startUpdate();
  }

  @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
    String date = year + "-" + monthOfYear + "-" + dayOfMonth;
    switch (mTag) {
      case TAG_SOLDIER_COME:
        mpersonageData.setJoin(date);
        mSoldierCome = date;
        break;
      case TAG_SOLDIER_GO:
        mpersonageData.setRetire(date);
        mSoldierGo = date;
        break;
      case TAG_AGE:
        mpersonageData.setAge(date);
        Time time = new Time("GMT+8");
        time.setToNow();
        int time2 = time.year - year + 1;
        mAge = String.valueOf(time2);
        break;
    }
    startUpdate();
  }

  //修改后刷新adapter
  public void startUpdate() {
    mAdapter.clear();
    if (AccountManager.isLogin()) {
      mUser = AccountManager.getCurrentAccount();
      switch (mUser.gender) {
        case MALE:
          mAdapter.appendAll(PersonageDataUtils.buildManData(mpersonageData));
          break;
        case FEMALE:
          mAdapter.appendAll(PersonageDataUtils.buildWumanData(mpersonageData));
          break;
      }
    }
    mAdapter.notifyDataSetChanged();
  }

  //判断是否结婚

  public static Integer getMarray(String marry) {
    if (marry != null) {
      if (marry.equals("已婚")) {
        return 1;
      }
      return 0;
    }
    return null;
  }

  //上传资料
  public void submitDataInformation() {
    if (mSoldierCome == null && mSoldierGo != null) {
      Toast.makeText(this, "请选择入伍时间", Toast.LENGTH_SHORT).show();
      return;
    }

    if (mSoldierCome != null && mSoldierGo != null) {
      Date d1 = null;
      Date d2 = null;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      try {
        d1 = sdf.parse(mSoldierCome);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      try {
        d2 = sdf.parse(mSoldierGo);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      //比较
      if ((d1.getTime() >= d2.getTime())) {
        Toast.makeText(this, "退伍时间不符", Toast.LENGTH_SHORT).show();
        return;
      }
    }

    Call<UpdateData> checkCall = mUpdateProfileService.postUpdtaSoldier(
        mpersonageData.getArea() != null ? mpersonageData.getArea().name : null,
        mpersonageData.getArea() != null ? mpersonageData.getArea().id : 0,
        mpersonageData.getMilitary() != null ? mpersonageData.getMilitary().name : null,
        mpersonageData.getMilitary() != null ? mpersonageData.getMilitary().id : 0, mSoldierCome,
        mSoldierGo, mName, mAge, mNiCheng, mCity, mEducation,
        mpersonageData.getHeight() != null ? mpersonageData.getHeight() : null,
        mpersonageData.getWeight() != null ? mpersonageData.getWeight() : null,
        mpersonageData.getProfession() != null ? mpersonageData.getProfession().id : 0,
        mpersonageData.getmArm() != null ? mpersonageData.getmArm().name : null,
        mpersonageData.getmArm() != null ? mpersonageData.getmArm().id : 0, getMarray(mMarriy));
    showHud("提交修改中");
    checkCall.enqueue(new Callback<UpdateData>()
    {
      @Override public void onResponse(Call<UpdateData> call, Response<UpdateData> response) {
        if (response.isSuccessful()) {

          dismissHud();
          SnackBarUtil.showTextByToast(PersonageDataActivity.this, "提交修改成功.", Gravity.CENTER);
          PersonageDataActivity.this.finish();
        }
        return;
      }

      @Override public void onFailure(Call<UpdateData> call, Throwable t) {
        dismissHud();
        SnackBarUtil.showText(PersonageDataActivity.this, "发生错误导致提交失败,原因为:" + t.toString());
        Log.e("test", t.toString());
      }
    });
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    DialogUtil.mItemsTag = null;
    DialogUtil.mItemsView = null;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case REQUEST_AREA:
          callBackArea(data);
      }
      startUpdate();
    }
  }

  /**
   * 回调区域
   */
  public void callBackArea(Intent data) {
    String city = data.getStringExtra(SoldierAreaActivity.AREA_VALUE);
    City soldiersArea = new City();
    soldiersArea.name = city;
    soldiersArea.id = FilterBuildUtils.getAreaType(mAllCities, mCityArrayList, city);
    mpersonageData.setArea(soldiersArea);
  }
}
