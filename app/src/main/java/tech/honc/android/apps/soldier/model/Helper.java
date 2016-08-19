package tech.honc.android.apps.soldier.model;

import android.net.Uri;
import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;
import java.util.ArrayList;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.model.enums.TaskType;

/**
 * Created by MrJiang on 2016/5/10.
 * 我的求助
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Helper extends Entity {

  public Integer id;
  @JsonProperty("account_id") public Integer accountId;
  public String avatar;
  public String nickname;
  public GenderType gender;
  public String address;
  public String content;
  public TaskType status;
  @JsonProperty("comment_times") public Integer commentTimes;
  @JsonProperty("interest_times") public Integer interestTimes;
  @JsonProperty("create_at") public Long createAt;
  public Integer distance;
  public ArrayList<Image> images;

  public Uri uri() {
    if (avatar != null) {
      return Uri.parse(avatar);
    }
    return null;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeValue(this.accountId);
    dest.writeString(this.avatar);
    dest.writeString(this.nickname);
    dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
    dest.writeString(this.address);
    dest.writeString(this.content);
    dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    dest.writeValue(this.commentTimes);
    dest.writeValue(this.interestTimes);
    dest.writeValue(this.createAt);
    dest.writeValue(this.distance);
    dest.writeTypedList(images);
  }

  public Helper() {
  }

  protected Helper(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.accountId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.avatar = in.readString();
    this.nickname = in.readString();
    int tmpGender = in.readInt();
    this.gender = tmpGender == -1 ? null : GenderType.values()[tmpGender];
    this.address = in.readString();
    this.content = in.readString();
    int tmpStatus = in.readInt();
    this.status = tmpStatus == -1 ? null : TaskType.values()[tmpStatus];
    this.commentTimes = (Integer) in.readValue(Integer.class.getClassLoader());
    this.interestTimes = (Integer) in.readValue(Integer.class.getClassLoader());
    this.createAt = (Long) in.readValue(Long.class.getClassLoader());
    this.distance = (Integer) in.readValue(Integer.class.getClassLoader());
    this.images = in.createTypedArrayList(Image.CREATOR);
  }

  public static final Creator<Helper> CREATOR = new Creator<Helper>() {
    @Override public Helper createFromParcel(Parcel source) {
      return new Helper(source);
    }

    @Override public Helper[] newArray(int size) {
      return new Helper[size];
    }
  };
}
