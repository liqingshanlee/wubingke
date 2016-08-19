package tech.honc.android.apps.soldier.utils.toolsutils;

import android.annotation.TargetApi;
import android.os.Build;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import tech.honc.android.apps.soldier.R;

/**
 * Created by MrJiang
 * on 2016/4/19.
 */
public class ColorsUtils {

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) public static int getColor(int position) {
    ArrayList<Integer> colors = new ArrayList<>();
    colors.add(R.color.color_one);
    colors.add(R.color.color_two);
    colors.add(R.color.color_three);
    colors.add(R.color.color_four);
    return colors.get(ThreadLocalRandom.current().nextInt(((position + 3) % (position + 1) + 1)));
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) public static int getWomanColor(int position) {
    ArrayList<Integer> colors = new ArrayList<>();
    colors.add(R.color.color_woman_one);
    colors.add(R.color.color_woman_two);
    colors.add(R.color.color_woman_three);
    colors.add(R.color.color_woman_four);
    return colors.get(ThreadLocalRandom.current().nextInt(((position + 3) % (position + 1) + 1)));
  }
}
