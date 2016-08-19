package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by kevin on 2016/6/23.
 */
public class BeanCertification extends Entity {

  public String title;
  public String value;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.title);
    dest.writeString(this.value);
  }

  public BeanCertification() {
  }

  protected BeanCertification(Parcel in) {
    this.title = in.readString();
    this.value = in.readString();
  }

  public static final Creator<BeanCertification> CREATOR = new Creator<BeanCertification>() {
    @Override public BeanCertification createFromParcel(Parcel source) {
      return new BeanCertification(source);
    }

    @Override public BeanCertification[] newArray(int size) {
      return new BeanCertification[size];
    }
  };
}
