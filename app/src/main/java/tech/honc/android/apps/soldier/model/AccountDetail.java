package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by MrJiang on 2016/4/20.
 * 个人详细资料
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class AccountDetail extends Entity {

  public Integer id;
  @JsonProperty("account_id") public Integer accountId;
  @JsonProperty("real_name") public String realName;
  public String age;
  public String education;
  public String weight;
  public String height;
  public String profession;
  public Integer married;
  public Integer followers;
  public Integer followings;
  public Integer helps;
  @JsonProperty("for_helps") public Integer forHelps;
  public String require;
  @JsonProperty("profession_id") public Integer professionid;
  public String nickname;
  @JsonProperty("open_im_id")public String openImId;



  public String getMarraied() {
    if (married == 0) {
      return "未婚";
    }
    return "已婚";
  }

  public AccountDetail() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeValue(this.accountId);
    dest.writeString(this.realName);
    dest.writeString(this.age);
    dest.writeString(this.education);
    dest.writeString(this.weight);
    dest.writeString(this.height);
    dest.writeString(this.profession);
    dest.writeValue(this.married);
    dest.writeValue(this.followers);
    dest.writeValue(this.followings);
    dest.writeValue(this.helps);
    dest.writeValue(this.forHelps);
    dest.writeString(this.require);
    dest.writeValue(this.professionid);
    dest.writeString(this.nickname);
    dest.writeString(this.openImId);
  }

  protected AccountDetail(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.accountId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.realName = in.readString();
    this.age = in.readString();
    this.education = in.readString();
    this.weight = in.readString();
    this.height = in.readString();
    this.profession = in.readString();
    this.married = (Integer) in.readValue(Integer.class.getClassLoader());
    this.followers = (Integer) in.readValue(Integer.class.getClassLoader());
    this.followings = (Integer) in.readValue(Integer.class.getClassLoader());
    this.helps = (Integer) in.readValue(Integer.class.getClassLoader());
    this.forHelps = (Integer) in.readValue(Integer.class.getClassLoader());
    this.require = in.readString();
    this.professionid = (Integer) in.readValue(Integer.class.getClassLoader());
    this.nickname = in.readString();
    this.openImId = in.readString();
  }

  public static final Creator<AccountDetail> CREATOR = new Creator<AccountDetail>() {
    @Override public AccountDetail createFromParcel(Parcel source) {
      return new AccountDetail(source);
    }

    @Override public AccountDetail[] newArray(int size) {
      return new AccountDetail[size];
    }
  };
}
