package tech.honc.android.apps.soldier.utils.toolsutils;

import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.enums.LevelType;

/**
 * Created by MrJiang on 2016/4/20.
 */
public class LevelUtils {
  /**
   * 战友
   */
  public static int getManMipmapFromLevel(LevelType level) {
    if (level != null) {
      switch (level.toString()) {
        case "zero":
          return R.mipmap.ic_rank_one;
        case "one":
          return R.mipmap.ic_rank_one;
        case "two":
          return R.mipmap.ic_rank_two;
        case "three":
          return R.mipmap.ic_rank_three;
        case "four":
          return R.mipmap.ic_rank_four;
        case "five":
          return R.mipmap.ic_rank_five;
        case "six":
          return R.mipmap.ic_rank_six;
        case "seven":
          return R.mipmap.ic_rank_seven;
        case "eight":
          return R.mipmap.ic_rank_eight;
        case "nine":
          return R.mipmap.ic_rank_nine;
        case "ten":
          return R.mipmap.ic_rank_ten;
        default:
          return R.mipmap.ic_rank_one;
      }
    }
    return R.mipmap.ic_rank_one;
  }

  /**
   * 军嫂
   */
  public static int getWoManMipmapFromLevel(LevelType level) {
    if (level != null) {
      switch (level.toString()) {
        case "zero":
          return R.mipmap.ic_rank_one;
        case "one":
          return R.mipmap.ic_rank_one;
        case "two":
          return R.mipmap.ic_rank_two;
        case "three":
          return R.mipmap.ic_rank_three;
        case "four":
          return R.mipmap.ic_rank_four;
        case "five":
          return R.mipmap.ic_rank_five;
        default:
          return R.mipmap.ic_rank_one;
      }
    }
    return R.mipmap.ic_rank_one;
  }
}
