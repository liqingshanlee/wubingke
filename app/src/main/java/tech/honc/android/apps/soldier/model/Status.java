package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/4/28.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Status extends Entity {

  public static final Creator<Status> CREATOR = new Creator<Status>() {
    @Override public Status createFromParcel(Parcel source) {
      return new Status(source);
    }

    @Override public Status[] newArray(int size) {
      return new Status[size];
    }
  };
  public String message;
  @JsonProperty("status_code") public Integer statusCode;

  public Status() {
  }

  protected Status(Parcel in) {
    this.message = in.readString();
    this.statusCode = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.message);
    dest.writeValue(this.statusCode);
  }
}
