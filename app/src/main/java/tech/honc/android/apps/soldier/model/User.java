package tech.honc.android.apps.soldier.model;

import android.net.Uri;
import android.os.Parcel;
import android.text.TextUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.account.Account;
import com.smartydroid.android.starter.kit.model.entity.Entity;
import java.util.ArrayList;
import tech.honc.android.apps.soldier.BuildConfig;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.model.enums.LevelType;
import tech.honc.android.apps.soldier.model.enums.RoleType;
import tech.honc.android.apps.soldier.utils.toolsutils.JsonUtils;

/**
 * Created by MrJiang
 * on 2016/4/19.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class User extends Entity implements Account {

  public Integer id;
  public String mobile;
  @JsonProperty("account_id") public Integer accountId;
  public String avatar;
  public String nickname;
  public String signature;
  public LevelType level;
  public String city;
  public GenderType gender;
  public RoleType role;
  public String geohash;
  public ArrayList<Image> albums;
  public Feed feed;
  public AccountDetail accountDetail;
  public Soldier army;
  public String token;
  public boolean focus;
  public String content;
  @JsonProperty("created_at") public Integer createdAt;
  public int followers;
  public int followings;
  public int helps;
  public Double latitude;
  public Double longitude;
  public OpenIm openIm;
  @JsonProperty("open_im_id")  public String openImId;
  @JsonProperty("object_id") public String objectId;
  public int friend;



  @Override public String token() {
    return token;
  }

  @Override public Object key() {
    return id;
  }

  @Override public String toJson() {
    return JsonUtils.get().toJson(this);
  }

  public Uri uri() {
    if (TextUtils.isEmpty(avatar)) return null;
    if (avatar.startsWith("http://")) {
      return Uri.parse(avatar);
    }
    return Uri.parse(BuildConfig.OSS_IMAGE_ENDPOINT + avatar);
  }

  public Uri uri(int w, int h) {
    if (avatar != null) {
      return Uri.parse(getKey() + "@" + w + "w_" + h + "h_1e");
    }
    return null;
  }

  public Uri thumbUri() {
    if (avatar != null) {
      return Uri.parse(getKey() + BuildConfig.THUNB_LEVEL);
    }
    return null;
  }

  public String getKey() {
    if (avatar != null) {
      return avatar.replace("oss", "img");
    }
    return null;
  }

  public String getSex() {
    if (gender.toString().equals(GenderType.FEMALE.toString())) {
      return "女";
    } else {
      return "男";
    }
  }

  public User() {
  }

  /**
   * 模糊处理
   */
  public Uri blurryUri() {
    if (avatar != null) {
      return Uri.parse(getKey() + "@" + "3-2bl");
    }
    return null;
  }

  @Override public String toString() {
    return "User{" +
        "id=" + id +
        ", mobile='" + mobile + '\'' +
        ", accountId=" + accountId +
        ", avatar='" + avatar + '\'' +
        ", nickname='" + nickname + '\'' +
        ", signature='" + signature + '\'' +
        ", level=" + level +
        ", city='" + city + '\'' +
        ", gender=" + gender +
        ", role=" + role +
        ", geohash='" + geohash + '\'' +
        ", albums=" + albums +
        ", feed=" + feed +
        ", accountDetail=" + accountDetail +
        ", army=" + army +
        ", token='" + token + '\'' +
        ", focus=" + focus +
        ", content='" + content + '\'' +
        ", createdAt=" + createdAt +
        ", followers=" + followers +
        ", followings=" + followings +
        ", helps=" + helps +
        ", latitude=" + latitude +
        ", longitude=" + longitude +
        ", objectId='" + objectId + '\'' +
        '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.mobile);
    dest.writeValue(this.accountId);
    dest.writeString(this.avatar);
    dest.writeString(this.nickname);
    dest.writeString(this.signature);
    dest.writeInt(this.level == null ? -1 : this.level.ordinal());
    dest.writeString(this.city);
    dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
    dest.writeInt(this.role == null ? -1 : this.role.ordinal());
    dest.writeString(this.geohash);
    dest.writeTypedList(this.albums);
    dest.writeParcelable(this.feed, flags);
    dest.writeParcelable(this.accountDetail, flags);
    dest.writeParcelable(this.army, flags);
    dest.writeString(this.token);
    dest.writeByte(this.focus ? (byte) 1 : (byte) 0);
    dest.writeString(this.content);
    dest.writeValue(this.createdAt);
    dest.writeInt(this.followers);
    dest.writeInt(this.followings);
    dest.writeInt(this.helps);
    dest.writeValue(this.latitude);
    dest.writeValue(this.longitude);
    dest.writeParcelable(this.openIm, flags);
    dest.writeString(this.openImId);
    dest.writeString(this.objectId);
    dest.writeInt(this.friend);
  }

  protected User(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.mobile = in.readString();
    this.accountId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.avatar = in.readString();
    this.nickname = in.readString();
    this.signature = in.readString();
    int tmpLevel = in.readInt();
    this.level = tmpLevel == -1 ? null : LevelType.values()[tmpLevel];
    this.city = in.readString();
    int tmpGender = in.readInt();
    this.gender = tmpGender == -1 ? null : GenderType.values()[tmpGender];
    int tmpRole = in.readInt();
    this.role = tmpRole == -1 ? null : RoleType.values()[tmpRole];
    this.geohash = in.readString();
    this.albums = in.createTypedArrayList(Image.CREATOR);
    this.feed = in.readParcelable(Feed.class.getClassLoader());
    this.accountDetail = in.readParcelable(AccountDetail.class.getClassLoader());
    this.army = in.readParcelable(Soldier.class.getClassLoader());
    this.token = in.readString();
    this.focus = in.readByte() != 0;
    this.content = in.readString();
    this.createdAt = (Integer) in.readValue(Integer.class.getClassLoader());
    this.followers = in.readInt();
    this.followings = in.readInt();
    this.helps = in.readInt();
    this.latitude = (Double) in.readValue(Double.class.getClassLoader());
    this.longitude = (Double) in.readValue(Double.class.getClassLoader());
    this.openIm = in.readParcelable(OpenIm.class.getClassLoader());
    this.openImId = in.readString();
    this.objectId = in.readString();
    this.friend = in.readInt();
  }

  public static final Creator<User> CREATOR = new Creator<User>() {
    @Override public User createFromParcel(Parcel source) {
      return new User(source);
    }

    @Override public User[] newArray(int size) {
      return new User[size];
    }
  };
}
