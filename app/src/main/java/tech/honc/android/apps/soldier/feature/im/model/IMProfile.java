package tech.honc.android.apps.soldier.feature.im.model;

import android.os.Parcel;
import com.smartydroid.android.starter.kit.model.entity.Entity;
import tech.honc.android.apps.soldier.model.enums.GenderType;
import tech.honc.android.apps.soldier.model.enums.LevelType;
import tech.honc.android.apps.soldier.model.enums.RoleType;

/**
 * Created by Mrjiang on 2016/5/31.
 * 查询用户信息
 */
public class IMProfile extends Entity {

  public Integer id;
  public String avatar;
  public String nickname;
  public LevelType level;
  public GenderType gender;
  public String signature;
  public RoleType role;
  public String open_im_id;
  public boolean firend;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.avatar);
    dest.writeString(this.nickname);
    dest.writeInt(this.level == null ? -1 : this.level.ordinal());
    dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
    dest.writeString(this.signature);
    dest.writeInt(this.role == null ? -1 : this.role.ordinal());
    dest.writeString(this.open_im_id);
    dest.writeByte(this.firend ? (byte) 1 : (byte) 0);
  }

  public IMProfile() {
  }

  protected IMProfile(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.avatar = in.readString();
    this.nickname = in.readString();
    int tmpLevel = in.readInt();
    this.level = tmpLevel == -1 ? null : LevelType.values()[tmpLevel];
    int tmpGender = in.readInt();
    this.gender = tmpGender == -1 ? null : GenderType.values()[tmpGender];
    this.signature = in.readString();
    int tmpRole = in.readInt();
    this.role = tmpRole == -1 ? null : RoleType.values()[tmpRole];
    this.open_im_id = in.readString();
    this.firend = in.readByte() != 0;
  }

  public static final Creator<IMProfile> CREATOR = new Creator<IMProfile>() {
    @Override public IMProfile createFromParcel(Parcel source) {
      return new IMProfile(source);
    }

    @Override public IMProfile[] newArray(int size) {
      return new IMProfile[size];
    }
  };
}
