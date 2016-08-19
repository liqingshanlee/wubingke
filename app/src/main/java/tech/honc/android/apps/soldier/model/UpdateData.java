package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/5/3.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class UpdateData extends Entity
{
  public String message;
  @JsonProperty("status_code") public int statuscode;

  public UpdateData() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.message);
    dest.writeInt(this.statuscode);
  }

  protected UpdateData(Parcel in) {
    this.message = in.readString();
    this.statuscode = in.readInt();
  }

  public static final Creator<UpdateData> CREATOR = new Creator<UpdateData>()
  {
    @Override public UpdateData createFromParcel(Parcel source) {
      return new UpdateData(source);
    }

    @Override public UpdateData[] newArray(int size) {
      return new UpdateData[size];
    }
  };
}
