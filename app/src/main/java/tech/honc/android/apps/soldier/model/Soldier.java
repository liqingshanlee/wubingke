package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by MrJiang on 2016/4/20.
 * 军人信息
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Soldier extends Entity {

  public Integer id;
  public String region;
  @JsonProperty("region_id") public Integer regionId;
  public String area;
  @JsonProperty("area_id") public Integer areaId;
  @JsonProperty("enlistment_time") public Integer enlistmentTime;
  @JsonProperty("retired_time") public Integer retiredtime;
  @JsonProperty("army_kind") public String armyKind;
  @JsonProperty("army_kind_id") public Integer armyKindId;
  public Integer years;

  public Soldier() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.region);
    dest.writeValue(this.regionId);
    dest.writeString(this.area);
    dest.writeValue(this.areaId);
    dest.writeValue(this.enlistmentTime);
    dest.writeValue(this.retiredtime);
    dest.writeString(this.armyKind);
    dest.writeValue(this.armyKindId);
    dest.writeValue(this.years);
  }

  protected Soldier(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.region = in.readString();
    this.regionId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.area = in.readString();
    this.areaId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.enlistmentTime = (Integer) in.readValue(Integer.class.getClassLoader());
    this.retiredtime = (Integer) in.readValue(Integer.class.getClassLoader());
    this.armyKind = in.readString();
    this.armyKindId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.years = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public static final Creator<Soldier> CREATOR = new Creator<Soldier>()
  {
    @Override public Soldier createFromParcel(Parcel source) {
      return new Soldier(source);
    }

    @Override public Soldier[] newArray(int size) {
      return new Soldier[size];
    }
  };
}
