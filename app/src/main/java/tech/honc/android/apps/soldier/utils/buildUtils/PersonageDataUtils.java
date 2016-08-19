package tech.honc.android.apps.soldier.utils.buildUtils;

import com.smartydroid.android.starter.kit.account.AccountManager;
import java.util.ArrayList;
import support.ui.cells.CellModel;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.ui.activity.PersonageDataActivity;
import tech.honc.android.apps.soldier.utils.data.PersonageData;

/**
 * Created by Administrator on 2016/4/27.
 */
public final class PersonageDataUtils
{
  private PersonageDataUtils() {
  }

  public static ArrayList<CellModel> buildManData(PersonageData personageData) {
    User user = AccountManager.getCurrentAccount();
    ArrayList<CellModel> cellModels = new ArrayList<>();
    cellModels.add(CellModel.shadowCell().build());
    cellModels.add(CellModel.headCell("服役信息").build());
    cellModels.add(CellModel.settingCell("服役地区")
        .value(personageData.getArea() != null ? personageData.getArea().name
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_SOLDIER_AREA)
        .build());
    cellModels.add(CellModel.settingCell("服役军区")
        .value(personageData.getMilitary() != null ? personageData.getMilitary().name : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_SOLDIER_MILLITARY)
        .build());
    cellModels.add(CellModel.settingCell("入伍时间")
        .value(personageData.getJoin() != null ? personageData.getJoin()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_SOLDIER_COME)
        .build());
    cellModels.add(CellModel.settingCell("退伍时间")
        .value(personageData.getRetire() != null ? personageData.getRetire()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_SOLDIER_GO)
        .build());

    cellModels.add(CellModel.shadowCell().build());
    cellModels.add(CellModel.headCell("基本信息").build());
    cellModels.add(CellModel.settingCell("姓名")
        .value(PersonageDataActivity.mName == null ? "" : PersonageDataActivity.mName)
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_NAME)
        .build());
    cellModels.add(CellModel.settingCell("年龄")
        .value(personageData.getAge() != null ? personageData.getAge()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_AGE)
        .build());
    cellModels.add(CellModel.settingCell("昵称")
        .value(PersonageDataActivity.mNiCheng == null ? "" : PersonageDataActivity.mNiCheng)
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_NICHENG)
        .build());

    cellModels.add(CellModel.settingCell("所在地")
        .value(personageData.getLocation() != null ? personageData.getLocation()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_AREA)
        .build());
    cellModels.add(CellModel.settingCell("学历")
        .value(personageData.getEducation() != null ? personageData.getEducation()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_EDUCATION)
        .build());
    cellModels.add(CellModel.settingCell("身高")
        .value(personageData.getHeight() != null ? personageData.getHeight()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_HEIGHT)
        .build());
    cellModels.add(CellModel.settingCell("体重")
        .value(personageData.getWeight() != null ? personageData.getWeight()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_WEIGHT)
        .build());
    cellModels.add(CellModel.settingCell("职业")
        .value(personageData.getProfession() != null ? personageData.getProfession().name
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_DUTY)
        .build());
    cellModels.add(CellModel.settingCell("兵种")
        .value(personageData.getmArm() != null ? personageData.getmArm().name
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_ARM)
        .build());
    cellModels.add(CellModel.settingCell("婚姻状况")
        .value(personageData.getMarriage() != null ? personageData.getMarriage()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_MARRIGE)
        .build());

    cellModels.add(CellModel.shadowCell().build());
    cellModels.add(CellModel.headCell("其他").build());
    cellModels.add(CellModel.settingCell("嗜好")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_HOBBY)
        .build());
    return cellModels;
  }

  public static ArrayList<CellModel> buildWumanData(PersonageData personageData) {
    User user = AccountManager.getCurrentAccount();
    ArrayList<CellModel> cellModels = new ArrayList<>();
    cellModels.add(CellModel.shadowCell().build());
    cellModels.add(CellModel.headCell("基本信息").build());
    cellModels.add(CellModel.settingCell("姓名")
        .value(PersonageDataActivity.mName == null ? "" : PersonageDataActivity.mName)
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_NAME)
        .build());
    cellModels.add(CellModel.settingCell("年龄")
        .value(personageData.getAge() != null ? personageData.getAge()
            :"")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_AGE)
        .build());
    cellModels.add(CellModel.settingCell("昵称")
        .value(PersonageDataActivity.mNiCheng == null ? "" : PersonageDataActivity.mNiCheng)
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_NICHENG)
        .build());

    cellModels.add(CellModel.settingCell("所在地")
        .value(personageData.getLocation() != null ? personageData.getLocation()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_AREA)
        .build());
    cellModels.add(CellModel.settingCell("学历")
        .value(personageData.getEducation() != null ? personageData.getEducation()
            :"")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_EDUCATION)
        .build());
    cellModels.add(CellModel.settingCell("身高")
        .value(personageData.getHeight() != null ? personageData.getHeight()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_HEIGHT)
        .build());

    cellModels.add(CellModel.settingCell("体重")
        .value(personageData.getWeight() != null ? personageData.getWeight()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_WEIGHT)
        .build());
    cellModels.add(CellModel.settingCell("职业")
        .value(personageData.getProfession() != null ? personageData.getProfession().name
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_DUTY)
        .build());

    cellModels.add(CellModel.settingCell("婚姻状况")
        .value(personageData.getMarriage() != null ? personageData.getMarriage()
            : "")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_MARRIGE)
        .build());

    cellModels.add(CellModel.shadowCell().build());
    cellModels.add(CellModel.headCell("其他").build());
    cellModels.add(CellModel.settingCell("嗜好")
        .needDivider(true)
        .enabled(true)
        .tag(PersonageDataActivity.TAG_HOBBY)
        .build());
    return cellModels;
  }
}