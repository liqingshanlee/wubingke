package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/4/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Comments extends Entity {

  public static final Creator<Comments> CREATOR = new Creator<Comments>() {
    @Override public Comments createFromParcel(Parcel source) {
      return new Comments(source);
    }

    @Override public Comments[] newArray(int size) {
      return new Comments[size];
    }
  };
  public Integer id;
  @JsonProperty("parent_id") public int parentId;//0表示评论，非0表示回复
  public String content;
  @JsonProperty("author") public User Auser;
  @JsonProperty("parent") public User Puser;
  @JsonProperty("created_at") public int createdAt;

  public Comments() {
  }

  public Comments(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.parentId = in.readInt();
    this.content = in.readString();
    this.Auser = in.readParcelable(User.class.getClassLoader());
    this.Puser = in.readParcelable(User.class.getClassLoader());
    this.createdAt = in.readInt();
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeInt(this.parentId);
    dest.writeString(this.content);
    dest.writeParcelable(this.Auser, flags);
    dest.writeParcelable(this.Puser, flags);
    dest.writeInt(this.createdAt);
  }
}
