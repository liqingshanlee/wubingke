package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by MrJiang on 2016/4/25.
 * 城市列表
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class City extends Entity {

  public Integer id;
  public String name;
  public String sort;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.name);
    dest.writeString(this.sort);
  }

  public City() {
  }

  protected City(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.name = in.readString();
    this.sort = in.readString();
  }

  public static final Creator<City> CREATOR = new Creator<City>() {
    @Override public City createFromParcel(Parcel source) {
      return new City(source);
    }

    @Override public City[] newArray(int size) {
      return new City[size];
    }
  };
}
