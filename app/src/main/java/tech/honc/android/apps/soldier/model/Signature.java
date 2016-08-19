package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/5/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Signature extends Entity
{

  public int status;
  public String sign;

  public Signature() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.status);
    dest.writeString(this.sign);
  }

  protected Signature(Parcel in) {
    this.status = in.readInt();
    this.sign = in.readString();
  }

  public static final Creator<Signature> CREATOR = new Creator<Signature>()
  {
    @Override public Signature createFromParcel(Parcel source) {
      return new Signature(source);
    }

    @Override public Signature[] newArray(int size) {
      return new Signature[size];
    }
  };
}
