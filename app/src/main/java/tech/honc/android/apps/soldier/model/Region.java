package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/4/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Region extends Entity {

  public Integer id;
  public String name;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.name);
  }

  public Region() {
  }

  protected Region(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.name = in.readString();
  }

  public static final Creator<Region> CREATOR = new Creator<Region>() {
    @Override public Region createFromParcel(Parcel source) {
      return new Region(source);
    }

    @Override public Region[] newArray(int size) {
      return new Region[size];
    }
  };
}
