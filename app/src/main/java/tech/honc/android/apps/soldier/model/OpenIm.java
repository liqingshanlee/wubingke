package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by wangh on 2016-5-20-0020.
 */

@JsonIgnoreProperties(ignoreUnknown = true) public class OpenIm extends Entity {

  @JsonProperty("user_id") public String userId;
  public String password;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.userId);
    dest.writeString(this.password);
  }

  public OpenIm() {
  }

  protected OpenIm(Parcel in) {
    this.userId = in.readString();
    this.password = in.readString();
  }

  public static final Creator<OpenIm> CREATOR = new Creator<OpenIm>() {
    @Override public OpenIm createFromParcel(Parcel source) {
      return new OpenIm(source);
    }

    @Override public OpenIm[] newArray(int size) {
      return new OpenIm[size];
    }
  };
}
