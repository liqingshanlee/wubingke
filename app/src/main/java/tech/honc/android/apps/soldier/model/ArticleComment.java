package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by MrJiang on 2016/4/28.
 * 文章评论model
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class ArticleComment implements Parcelable {

  public Integer id;
  public String content;
  @JsonProperty("created_at") public Integer createdAt;
  public User account;
  public Article article;

  public ArticleComment() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeString(this.content);
    dest.writeValue(this.createdAt);
    dest.writeParcelable(this.account, flags);
    dest.writeParcelable(this.article, flags);
  }

  protected ArticleComment(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.content = in.readString();
    this.createdAt = (Integer) in.readValue(Integer.class.getClassLoader());
    this.account = in.readParcelable(User.class.getClassLoader());
    this.article = in.readParcelable(Article.class.getClassLoader());
  }

  public static final Creator<ArticleComment> CREATOR = new Creator<ArticleComment>() {
    @Override public ArticleComment createFromParcel(Parcel source) {
      return new ArticleComment(source);
    }

    @Override public ArticleComment[] newArray(int size) {
      return new ArticleComment[size];
    }
  };
}
