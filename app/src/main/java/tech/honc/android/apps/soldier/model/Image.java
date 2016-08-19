package tech.honc.android.apps.soldier.model;

import android.net.Uri;
import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by MrJiang on 4/17/2016.
 * 相册
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Image extends Entity {
  public int id;
  @JsonProperty("url") public String url;
  @JsonProperty("feed_id") public Integer feedId;
  public String status;

  public Image() {

  }

  public Image(String url) {
    this.url = url;
  }

  public Uri uri() {
    if (url != null) {
      return Uri.parse(getKey());
    }
    return null;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.url);
    dest.writeValue(this.feedId);
    dest.writeString(this.status);
  }

  protected Image(Parcel in) {
    this.id = in.readInt();
    this.url = in.readString();
    this.feedId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.status = in.readString();
  }

  public static final Creator<Image> CREATOR = new Creator<Image>() {
    @Override public Image createFromParcel(Parcel source) {
      return new Image(source);
    }

    @Override public Image[] newArray(int size) {
      return new Image[size];
    }
  };

  public String getKey() {
    if (url != null) {
      return url.replace("oss", "img");
    }
    return null;
  }

  public Uri uri(int w, int h) {
    if (url != null) {
      return Uri.parse(getKey() + "@" + w + "w_" + h + "h_1e");
    }
    return null;
  }
}
