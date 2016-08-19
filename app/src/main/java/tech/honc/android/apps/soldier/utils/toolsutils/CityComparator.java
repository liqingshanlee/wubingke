package tech.honc.android.apps.soldier.utils.toolsutils;

import java.util.Comparator;
import tech.honc.android.apps.soldier.model.City;

/**
 * Created by MrJiang on 2016/4/25.
 */
public class CityComparator implements Comparator<City> {
  @Override public int compare(City lhs, City rhs) {
    String a = lhs.sort;
    String b = rhs.sort;
    return a.compareTo(b);
  }
}
