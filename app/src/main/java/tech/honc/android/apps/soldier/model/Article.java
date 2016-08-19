package tech.honc.android.apps.soldier.model;

import android.net.Uri;
import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/4/21.
 * 资讯
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Article extends Entity {

  public Integer id;
  public String title;
  public String thumbnail;
  //资讯
  @JsonProperty("article_id") public Integer articleId;
  @JsonProperty("published_at") public Integer publishedAt;
  @JsonProperty("created_at") public Integer createdAt;
  @JsonProperty("like_times") public Integer likeTimes;
  @JsonProperty("comment_times") public Integer commentTimes;
  @JsonProperty("category_name") public String categoryName;
  public String content;
  @JsonProperty("collection_times") public Integer collectionTimes;
  @JsonProperty("is_liked") public boolean isLiked;
  @JsonProperty("is_collected") public boolean isCollected;



  public Article() {
  }

  public Uri uri() {
    if (thumbnail != null) {
      return Uri.parse(thumbnail);
    }
    return null;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.title);
    dest.writeString(this.thumbnail);
    dest.writeValue(this.articleId);
    dest.writeValue(this.publishedAt);
    dest.writeValue(this.createdAt);
    dest.writeValue(this.likeTimes);
    dest.writeValue(this.commentTimes);
    dest.writeString(this.categoryName);
    dest.writeString(this.content);
    dest.writeValue(this.collectionTimes);
    dest.writeByte(isLiked ? (byte) 1 : (byte) 0);
    dest.writeByte(isCollected ? (byte) 1 : (byte) 0);
  }

  protected Article(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.title = in.readString();
    this.thumbnail = in.readString();
    this.articleId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.publishedAt = (Integer) in.readValue(Integer.class.getClassLoader());
    this.createdAt = (Integer) in.readValue(Integer.class.getClassLoader());
    this.likeTimes = (Integer) in.readValue(Integer.class.getClassLoader());
    this.commentTimes = (Integer) in.readValue(Integer.class.getClassLoader());
    this.categoryName = in.readString();
    this.content = in.readString();
    this.collectionTimes = (Integer) in.readValue(Integer.class.getClassLoader());
    this.isLiked = in.readByte() != 0;
    this.isCollected = in.readByte() != 0;
  }

  public static final Creator<Article> CREATOR = new Creator<Article>() {
    @Override public Article createFromParcel(Parcel source) {
      return new Article(source);
    }

    @Override public Article[] newArray(int size) {
      return new Article[size];
    }
  };
}
