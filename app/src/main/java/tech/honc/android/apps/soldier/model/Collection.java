package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by MrJiang on 2016/5/3.
 */
public class Collection extends Entity {

  @JsonProperty("account_id") public Integer accountId;
  @JsonProperty("article_id") public String articleId;
  @JsonProperty("created_at") public Integer createAt;
  public Integer id;
  @JsonProperty("is_collected") public boolean isCollected;
  @JsonProperty("category_type")public String categoryType;
  public String title;
  public String summary;
  public String thumbnail;
  @JsonProperty("deleted_at") public String deletedAt;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.accountId);
    dest.writeString(this.articleId);
    dest.writeValue(this.createAt);
    dest.writeValue(this.id);
    dest.writeByte(isCollected ? (byte) 1 : (byte) 0);
    dest.writeString(this.categoryType);
    dest.writeString(this.title);
    dest.writeString(this.summary);
    dest.writeString(this.thumbnail);
    dest.writeString(this.deletedAt);
  }

  public Collection() {
  }

  protected Collection(Parcel in) {
    this.accountId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.articleId = in.readString();
    this.createAt = (Integer) in.readValue(Integer.class.getClassLoader());
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.isCollected = in.readByte() != 0;
    this.categoryType = in.readString();
    this.title = in.readString();
    this.summary = in.readString();
    this.thumbnail = in.readString();
    this.deletedAt = in.readString();
  }

  public static final Creator<Collection> CREATOR = new Creator<Collection>() {
    @Override public Collection createFromParcel(Parcel source) {
      return new Collection(source);
    }

    @Override public Collection[] newArray(int size) {
      return new Collection[size];
    }
  };
}
