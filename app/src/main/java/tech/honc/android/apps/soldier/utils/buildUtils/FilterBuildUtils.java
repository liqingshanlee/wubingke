package tech.honc.android.apps.soldier.utils.buildUtils;

import java.util.ArrayList;
import support.ui.cells.CellModel;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.City;
import tech.honc.android.apps.soldier.model.Profession;
import tech.honc.android.apps.soldier.model.Region;
import tech.honc.android.apps.soldier.model.Soldiers;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.ui.app.SoldierApp;
import tech.honc.android.apps.soldier.ui.fragment.FilterFemaleSoldierFragment;
import tech.honc.android.apps.soldier.ui.fragment.FilterSoldierFragment;
import tech.honc.android.apps.soldier.utils.data.SearchData;

/**
 * Created by MrJiang
 * on 4/15/2016.
 */
public class FilterBuildUtils {

  private FilterBuildUtils() {
  }

  public static ArrayList<CellModel> buildMaleDatas(SearchData searchData) {
    ArrayList<CellModel> items = new ArrayList<>();
    if (searchData!=null) {
      items.add(CellModel.settingCell("性别")
          .value(searchData.getSex() != null ? searchData.getSex() : SoldierApp.appResources().getString(R.string.please_choice))
          .needDivider(true)
          .enabled(true)
          .tag(FilterSoldierFragment.TAG_SEX)
          .build());
      items.add(CellModel.settingCell("年龄")
          .value(searchData.getAge() != null ? searchData.getAge() : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .tag(FilterSoldierFragment.TAG_AGE)
          .build());
      items.add(CellModel.shadowCell().build());
      items.add(CellModel.settingCell("身高")
          .value(searchData.getHeight() != null ? searchData.getHeight()
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .needDivider(true)
          .tag(FilterSoldierFragment.TAG_HEIGHT)
          .build());
      items.add(CellModel.settingCell("体重")
          .value(searchData.getWeight() != null ? searchData.getWeight()
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .tag(FilterSoldierFragment.TAG_WEIGHT)
          .build());
      items.add(CellModel.shadowCell().build());
      items.add(CellModel.settingCell("兵种")
          .value(searchData.getArmyType() != null ? searchData.getArmyType().name
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .needDivider(true)
          .tag(FilterSoldierFragment.TAG_SOLDIER_TYPE)
          .build());
      items.add(CellModel.settingCell("兵龄")
          .value(searchData.getArmyYear() != null ? searchData.getArmyYear()
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .tag(FilterSoldierFragment.TAG_SOLDIER_AGE)
          .build());
      items.add(CellModel.shadowCell().build());
      items.add(CellModel.settingCell("服役军区")
          .value(searchData.getArmyArea() != null ? searchData.getArmyArea().name
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .tag(FilterSoldierFragment.TAG_SOLDIER_AREA)
          .build());
      items.add(CellModel.shadowCell().build());
      items.add(CellModel.settingCell("入伍时间")
          .value(searchData.getStartTime() != null ? searchData.getStartTime()
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .needDivider(true)
          .tag(FilterSoldierFragment.TAG_SOLDIER_COME)
          .build());
      items.add(CellModel.settingCell("退伍时间")
          .value(searchData.getEndTime() != null ? searchData.getEndTime()
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .tag(FilterSoldierFragment.TAG_SOLDIER_GO)
          .build());
    }
    return items;
  }

  public static ArrayList<CellModel> buildFemaleDatas(SearchData searchData) {
    ArrayList<CellModel> items = new ArrayList<>();
    if (searchData!=null) {
      items.add(CellModel.settingCell("年龄")
          .value((searchData.getAge() != null) ? searchData.getAge()
              : SoldierApp.appResources().getString(R.string.please_choice))
          .needDivider(true)
          .enabled(true)
          .tag(FilterFemaleSoldierFragment.TAG_AGE)
          .build());
      items.add(CellModel.settingCell("身高")
          .value((searchData.getHeight() != null) ? searchData.getHeight()
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .tag(FilterFemaleSoldierFragment.TAG_HEIGHT)
          .build());
      items.add(CellModel.shadowCell().build());
      items.add(CellModel.settingCell("体重")
          .value((searchData.getWeight() != null) ? searchData.getWeight()
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .needDivider(true)
          .tag(FilterFemaleSoldierFragment.TAG_WEIGHT)
          .build());
      items.add(CellModel.settingCell("职业")
          .value((searchData.getProfession() != null) ? searchData.getProfession().name
              : SoldierApp.appResources().getString(R.string.please_input))
          .enabled(true)
          .needDivider(true)
          .tag(FilterFemaleSoldierFragment.TAG_WORK)
          .build());
      items.add(CellModel.shadowCell().build());
      items.add(CellModel.settingCell("学历")
          .value((searchData.getEducation() != null) ? searchData.getEducation()
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .needDivider(true)
          .tag(FilterFemaleSoldierFragment.TAG_GRADE)
          .build());
      items.add(CellModel.settingCell("婚姻状况")
          .value((searchData.getMarry() != null) ? searchData.getMarry()
              : SoldierApp.appResources().getString(R.string.please_choice))
          .enabled(true)
          .needDivider(true)
          .tag(FilterFemaleSoldierFragment.TAG_MARRY)
          .build());
    }
    return items;
  }

  public static Integer getMarray(String marry) {
    if (marry != null) {
      if (marry.equals("已婚")) {
        return 1;
      }
      return 0;
    }
    return null;
  }

  public static Integer getProfessions(ArrayList<Profession> professions, ArrayList<String> list,
      String content) {
    if (professions != null && list != null && content != null) {
      return professions.get(list.indexOf(content)).id;
    }
    return null;
  }

  public static String getSex(String sex) {
    if (sex != null) {
      if (sex.equals("男")) {
        return GenderType.MALE.toString();
      }
      return GenderType.FEMALE.toString();
    }
    return null;
  }

  public static Integer getRegion(ArrayList<Region> regions, ArrayList<String> strings,
      String content) {
    if (regions != null && strings != null && content != null) {
      return regions.get(strings.indexOf(content)).id;
    }
    return null;
  }

  public static Integer getArmyType(ArrayList<Soldiers> soldiers, ArrayList<String> strings,
      String content) {
    if (soldiers != null && strings != null && content != null) {
      return soldiers.get(strings.indexOf(content)).id;
    }
    return null;
  }

  public static Integer getAreaType(ArrayList<City> soldiers, ArrayList<String> strings,
      String content) {
    if (soldiers != null && strings != null && content != null) {
      return soldiers.get(strings.indexOf(content)).id;
    }
    return null;
  }

  public static Integer getMillitaryType(ArrayList<Region> soldiers, ArrayList<String> strings,
      String content) {
    if (soldiers != null && strings != null && content != null) {
      return soldiers.get(strings.indexOf(content)).id;
    }
    return null;
  }
  public static Integer getDutyType(ArrayList<Profession> soldiers, ArrayList<String> strings,
      String content) {
    if (soldiers != null && strings != null && content != null) {
      return soldiers.get(strings.indexOf(content)).id;
    }
    return null;
  }
}
