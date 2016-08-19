package tech.honc.android.apps.soldier.utils.data;

import android.os.Parcel;
import com.smartydroid.android.starter.kit.model.entity.Entity;
import tech.honc.android.apps.soldier.model.Profession;
import tech.honc.android.apps.soldier.model.Region;
import tech.honc.android.apps.soldier.model.Soldiers;

/**
 * Created by MrJiang on 2016/4/26.
 */
public class SearchData extends Entity {
  private String age;
  private String height;
  private String weight;
  private Profession profession;
  private String education;
  private String marry;
  private String sex;
  private Soldiers armyType;
  private String armyYear;
  private Region armyArea;
  private String startTime;
  private String endTime;

  public Soldiers getArmyType() {
    return armyType;
  }

  public void setArmyType(Soldiers armyType) {
    this.armyType = armyType;
  }

  public Region getArmyArea() {
    return armyArea;
  }

  public void setArmyArea(Region armyArea) {
    this.armyArea = armyArea;
  }

  public static Creator<SearchData> getCREATOR() {
    return CREATOR;
  }

  public Profession getProfession() {
    return profession;
  }

  public void setProfession(Profession profession) {
    this.profession = profession;
  }

  public SearchData() {
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getArmyYear() {
    return armyYear;
  }

  public void setArmyYear(String armyYear) {
    this.armyYear = armyYear;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getWeight() {
    return weight;
  }

  public void setWeight(String weight) {
    this.weight = weight;
  }

  public String getEducation() {
    return education;
  }

  public void setEducation(String education) {
    this.education = education;
  }

  public String getMarry() {
    return marry;
  }

  public void setMarry(String marry) {
    this.marry = marry;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.age);
    dest.writeString(this.height);
    dest.writeString(this.weight);
    dest.writeParcelable(this.profession, flags);
    dest.writeString(this.education);
    dest.writeString(this.marry);
    dest.writeString(this.sex);
    dest.writeParcelable(this.armyType, flags);
    dest.writeString(this.armyYear);
    dest.writeParcelable(this.armyArea, flags);
    dest.writeString(this.startTime);
    dest.writeString(this.endTime);
  }

  protected SearchData(Parcel in) {
    this.age = in.readString();
    this.height = in.readString();
    this.weight = in.readString();
    this.profession = in.readParcelable(Profession.class.getClassLoader());
    this.education = in.readString();
    this.marry = in.readString();
    this.sex = in.readString();
    this.armyType = in.readParcelable(Soldiers.class.getClassLoader());
    this.armyYear = in.readString();
    this.armyArea = in.readParcelable(Region.class.getClassLoader());
    this.startTime = in.readString();
    this.endTime = in.readString();
  }

  public static final Creator<SearchData> CREATOR = new Creator<SearchData>() {
    @Override public SearchData createFromParcel(Parcel source) {
      return new SearchData(source);
    }

    @Override public SearchData[] newArray(int size) {
      return new SearchData[size];
    }
  };
}
