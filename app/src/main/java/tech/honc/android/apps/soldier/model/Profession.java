package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by MrJiang on 2016/4/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Profession extends Entity {

  public Integer id;
  public String name;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.name);
  }

  public Profession() {
  }

  protected Profession(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.name = in.readString();
  }

  public static final Creator<Profession> CREATOR = new Creator<Profession>() {
    @Override public Profession createFromParcel(Parcel source) {
      return new Profession(source);
    }

    @Override public Profession[] newArray(int size) {
      return new Profession[size];
    }
  };
}
