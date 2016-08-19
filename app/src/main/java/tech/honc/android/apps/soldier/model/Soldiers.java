package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/4/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Soldiers extends Entity {

  public Integer id;
  public String name;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.name);
  }

  public Soldiers() {
  }

  protected Soldiers(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.name = in.readString();
  }

  public static final Creator<Soldiers> CREATOR = new Creator<Soldiers>() {
    @Override public Soldiers createFromParcel(Parcel source) {
      return new Soldiers(source);
    }

    @Override public Soldiers[] newArray(int size) {
      return new Soldiers[size];
    }
  };
}
