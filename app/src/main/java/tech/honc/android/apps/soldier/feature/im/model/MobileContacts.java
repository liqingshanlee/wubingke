package tech.honc.android.apps.soldier.feature.im.model;

import android.net.Uri;
import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by MrJiang on 2016/6/3.
 * 读取手机号码
 */
public class MobileContacts extends Entity {

  public Integer id;
  public String avatar;
  public String mobile;
  public String nickname;
  @JsonProperty("open_im_id") public String openImId;
  public boolean hasAccount;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.avatar);
    dest.writeString(this.mobile);
    dest.writeString(this.nickname);
    dest.writeString(this.openImId);
    dest.writeByte(this.hasAccount ? (byte) 1 : (byte) 0);
  }

  public MobileContacts() {
  }

  protected MobileContacts(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.avatar = in.readString();
    this.mobile = in.readString();
    this.nickname = in.readString();
    this.openImId = in.readString();
    this.hasAccount = in.readByte() != 0;
  }

  public static final Creator<MobileContacts> CREATOR = new Creator<MobileContacts>() {
    @Override public MobileContacts createFromParcel(Parcel source) {
      return new MobileContacts(source);
    }

    @Override public MobileContacts[] newArray(int size) {
      return new MobileContacts[size];
    }
  };

  public Uri uri(){
    if (avatar!=null){
      return Uri.parse(avatar);
    }
    return null;
  }
}
