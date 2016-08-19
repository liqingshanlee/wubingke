package tech.honc.android.apps.soldier.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 2016/6/23.
 */
public enum ReportType {

  FEED("feed"),
  TASK("task"),
  FEED_COMMENT("feed_comment"),
  TASK_HELP("task_help");

  private final String mValue;

  ReportType(String value) {
    mValue = value;
  }

  @Override @JsonValue public String toString() {
    return mValue;
  }

  private static final Map<String, ReportType> STRING_MAPPING = new HashMap<>();

  static {
    for (ReportType calorieType : ReportType.values()) {
      STRING_MAPPING.put(calorieType.toString().toUpperCase(), calorieType);
    }
  }

  @JsonCreator public static ReportType fromValue(String value) {
    return STRING_MAPPING.get(value.toUpperCase());
  }
}
