package tech.honc.android.apps.soldier.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MrJiang
 * on 2016/4/19.
 */
public enum RoleType {
  SOLDIER("solider"),
  JUNSAO("junsao");
  private final String mValue;

  RoleType(String value) {
    mValue = value;
  }

  @Override @JsonValue public String toString() {
    return mValue;
  }

  private static final Map<String, RoleType> STRING_MAPPING = new HashMap<>();

  static {
    for (RoleType calorieType : RoleType.values()) {
      STRING_MAPPING.put(calorieType.toString().toUpperCase(), calorieType);
    }
  }

  @JsonCreator public static RoleType fromValue(String value) {
    return STRING_MAPPING.get(value.toUpperCase());
  }
}
