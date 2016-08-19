package tech.honc.android.apps.soldier.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import tech.honc.android.apps.soldier.R;

/**
 * Created by MrJiang on 2016/5/10.
 * 任务类型
 */
public enum TaskType {

  GOING("going"),
  OVER("over"),
  EXPIRED("expired");
  private final String mValue;

  TaskType(String value) {
    mValue = value;
  }

  @Override @JsonValue public String toString() {
    return mValue;
  }

  private static final Map<String, TaskType> STRING_MAPPING = new HashMap<>();

  static {
    for (TaskType calorieType : TaskType.values()) {
      STRING_MAPPING.put(calorieType.toString().toUpperCase(), calorieType);
    }
  }

  @JsonCreator public static TaskType fromValue(String value) {
    return STRING_MAPPING.get(value.toUpperCase());
  }

  /**
   * 获取资源
   */
  public static int getIconResource(TaskType genderType) {
    int resource = 0;
    if (genderType != null) {
      switch (genderType)
      {
        case GOING:
          resource = R.mipmap.ic_ing;
          break;
        case OVER:
          resource = R.mipmap.ic_ending;
          break;
        case EXPIRED:
          resource = R.mipmap.ic_over;
          break;
        default:
          resource = R.mipmap.ic_ending;
          break;
      }
      return resource;
    }
    return R.mipmap.ic_ending;
  }
}
