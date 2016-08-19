package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/4/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Likes extends Entity {

  public int id;
  @JsonProperty("account") public User user;
  @JsonProperty("article") public Article article;



  public Likes() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeParcelable(this.user, flags);
    dest.writeParcelable(this.article, flags);
  }

  protected Likes(Parcel in) {
    this.id = in.readInt();
    this.user = in.readParcelable(User.class.getClassLoader());
    this.article = in.readParcelable(Article.class.getClassLoader());
  }

  public static final Creator<Likes> CREATOR = new Creator<Likes>() {
    @Override public Likes createFromParcel(Parcel source) {
      return new Likes(source);
    }

    @Override public Likes[] newArray(int size) {
      return new Likes[size];
    }
  };
}
