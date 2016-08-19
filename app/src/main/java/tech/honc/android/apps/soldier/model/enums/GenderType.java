package tech.honc.android.apps.soldier.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import tech.honc.android.apps.soldier.R;

/**
 * Created by MrJiang
 * on 2016/4/19.
 */
public enum GenderType {
  MALE("male"),
  FEMALE("female");
  private final String mValue;

  GenderType(String value) {
    mValue = value;
  }

  @Override @JsonValue public String toString() {
    return mValue;
  }

  private static final Map<String, GenderType> STRING_MAPPING = new HashMap<>();

  static {
    for (GenderType calorieType : GenderType.values()) {
      STRING_MAPPING.put(calorieType.toString().toUpperCase(), calorieType);
    }
  }

  @JsonCreator public static GenderType fromValue(String value) {
    return STRING_MAPPING.get(value.toUpperCase());
  }

  public static int getIconResource(GenderType genderType) {
    if (genderType != null && genderType.equals(MALE)) {
      return R.mipmap.ic_sex_man;
    }
    return R.mipmap.ic_sex_woman;
  }
}
