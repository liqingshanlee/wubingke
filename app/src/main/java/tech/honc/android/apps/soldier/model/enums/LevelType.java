package tech.honc.android.apps.soldier.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MrJiang
 * on 2016/4/19.
 * 等级
 */
public enum LevelType {
  ONE("one"),
  TWO("two"),
  THREE("three"),
  FOUR("four"),
  FIVE("five"),
  SIX("six"),
  SEVEN("seven"),
  EIGHT("eight"),
  NINE("nine"),
  TEN("ten");

  private final String mValue;

  LevelType(String value) {
    mValue = value;
  }

  @Override @JsonValue public String toString() {
    return mValue;
  }

  private static final Map<String, LevelType> STRING_MAPPING = new HashMap<>();

  static {
    for (LevelType calorieType : LevelType.values()) {
      STRING_MAPPING.put(calorieType.toString().toUpperCase(), calorieType);
    }
  }

  @JsonCreator public static LevelType fromValue(String value) {
    return STRING_MAPPING.get(value.toUpperCase());
  }
}
