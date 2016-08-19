package tech.honc.android.apps.soldier.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MrJiang on 2016/5/10.
 * 帮助是否有用
 */
public enum TaskCommentType {
  NO_USE("no_use"),
  USEFUL("helpful");
  private final String mValue;

  TaskCommentType(String value) {
    mValue = value;
  }

  @Override @JsonValue public String toString() {
    return mValue;
  }

  private static final Map<String, TaskCommentType> STRING_MAPPING = new HashMap<>();

  static {
    for (TaskCommentType calorieType : TaskCommentType.values()) {
      STRING_MAPPING.put(calorieType.toString().toUpperCase(), calorieType);
    }
  }

  @JsonCreator public static TaskCommentType fromValue(String value) {
    return STRING_MAPPING.get(value.toUpperCase());
  }

  /**
   * 获取资源
   */
  public static String getIconResource(TaskCommentType genderType) {
    String resource = "";
    switch (genderType) {
      case NO_USE:
        break;
      case USEFUL:
        resource = "被选中";
        break;
    }
    return resource;
  }
}
