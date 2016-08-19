package tech.honc.android.apps.soldier.model;

import android.net.Uri;
import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by kevin on 2016/6/23.
 * 用户认证
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Certification extends Entity {

  public String url;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.url);
  }

  public Certification() {
  }

  protected Certification(Parcel in) {
    this.url = in.readString();
  }

  public static final Creator<Certification> CREATOR = new Creator<Certification>() {
    @Override public Certification createFromParcel(Parcel source) {
      return new Certification(source);
    }

    @Override public Certification[] newArray(int size) {
      return new Certification[size];
    }
  };

  public String getKey() {
    if (url != null) {
      return url.replace("oss", "img");
    }
    return null;
  }

  public Uri getUri() {
    return Uri.parse(getKey());
  }
}
