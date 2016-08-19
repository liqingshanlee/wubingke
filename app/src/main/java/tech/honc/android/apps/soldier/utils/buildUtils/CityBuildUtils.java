package tech.honc.android.apps.soldier.utils.buildUtils;

import java.util.ArrayList;
import tech.honc.android.apps.soldier.model.City;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;

/**
 * Created by MrJiang on 4/22/2016.
 */
public class CityBuildUtils {

  public static final int TYPE_LOCATION = 100;
  public static final int TYPE_HOT = 200;
  public static final int TYPE_ALL = 300;

  public static ArrayList<String> buildLetterCity() {
    ArrayList<String> listItems = new ArrayList<>();
    listItems.add("#");
    listItems.add("A");
    listItems.add("B");
    listItems.add("C");
    listItems.add("D");
    listItems.add("E");
    listItems.add("F");
    listItems.add("G");
    listItems.add("H");
    listItems.add("I");
    listItems.add("G");
    listItems.add("K");
    listItems.add("L");
    listItems.add("M");
    listItems.add("N");
    listItems.add("O");
    listItems.add("P");
    listItems.add("Q");
    listItems.add("R");
    listItems.add("S");
    listItems.add("T");
    listItems.add("U");
    listItems.add("V");
    listItems.add("W");
    listItems.add("X");
    listItems.add("Y");
    listItems.add("Z");
    return listItems;
  }

  public static ArrayList<SettingItems> buildCityDatas(String locations, ArrayList<City> allCity) {
    ArrayList<SettingItems> items = new ArrayList<>();
    items.add(new SettingItems.Builder().text(locations)
        .itemViewType(SettingItems.VIEW_TYPE_CITY_LOCATION)
        .type(TYPE_LOCATION)
        .build());

    items.add(new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_HOT_CITY)
        .type(TYPE_HOT)
        .build());

    items.add(new SettingItems.Builder().mCityList(allCity)
        .itemViewType(SettingItems.VIEW_TYPE_ALL_CITY)
        .type(TYPE_ALL)
        .build());
    return items;
  }
}
