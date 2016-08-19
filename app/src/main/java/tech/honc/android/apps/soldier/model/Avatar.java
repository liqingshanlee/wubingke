package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/5/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Avatar extends Entity
{

  public String message;
  public int status_code;

  protected Avatar() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.message);
    dest.writeInt(this.status_code);
  }

  protected Avatar(Parcel in) {
    this.message = in.readString();
    this.status_code = in.readInt();
  }

  public static final Creator<Avatar> CREATOR = new Creator<Avatar>()
  {
    @Override public Avatar createFromParcel(Parcel source) {
      return new Avatar(source);
    }

    @Override public Avatar[] newArray(int size) {
      return new Avatar[size];
    }
  };
}
