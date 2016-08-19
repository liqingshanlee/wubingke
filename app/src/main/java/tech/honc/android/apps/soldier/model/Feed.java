package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;
import java.util.ArrayList;
import tech.honc.android.apps.soldier.model.enums.TaskType;

/**
 * Created by Administrator on 2016/4/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Feed extends Entity {

  public Integer id;
  @JsonProperty("account") public User user;
  public String content;
  public ArrayList<Image> images;
  @JsonProperty("create_at") public Integer createdAt;
  @JsonProperty("like_times") public Integer likeTimes;
  @JsonProperty("comment_times") public Integer commentTimes;
  @JsonProperty("account_id") public Integer accountId;
  public TaskType status;
  public String address;
  public Integer distance;
  @JsonProperty("interest_times") public Integer interestTimes;
  public ArrayList<Likes> likes;
  public ArrayList<Comments> comments;
  public String city;
  @JsonProperty("is_liked") public Integer isLiked;
  public Long deadline;
  @JsonProperty("is_interest") public Integer isInterest;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeParcelable(this.user, flags);
    dest.writeString(this.content);
    dest.writeTypedList(images);
    dest.writeValue(this.createdAt);
    dest.writeValue(this.likeTimes);
    dest.writeValue(this.commentTimes);
    dest.writeValue(this.accountId);
    dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    dest.writeString(this.address);
    dest.writeValue(this.distance);
    dest.writeValue(this.interestTimes);
    dest.writeTypedList(likes);
    dest.writeTypedList(comments);
    dest.writeString(this.city);
    dest.writeValue(this.isLiked);
    dest.writeValue(this.deadline);
    dest.writeValue(this.isInterest);
  }

  public Feed() {
  }

  protected Feed(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.user = in.readParcelable(User.class.getClassLoader());
    this.content = in.readString();
    this.images = in.createTypedArrayList(Image.CREATOR);
    this.createdAt = (Integer) in.readValue(Integer.class.getClassLoader());
    this.likeTimes = (Integer) in.readValue(Integer.class.getClassLoader());
    this.commentTimes = (Integer) in.readValue(Integer.class.getClassLoader());
    this.accountId = (Integer) in.readValue(Integer.class.getClassLoader());
    int tmpStatus = in.readInt();
    this.status = tmpStatus == -1 ? null : TaskType.values()[tmpStatus];
    this.address = in.readString();
    this.distance = (Integer) in.readValue(Integer.class.getClassLoader());
    this.interestTimes = (Integer) in.readValue(Integer.class.getClassLoader());
    this.likes = in.createTypedArrayList(Likes.CREATOR);
    this.comments = in.createTypedArrayList(Comments.CREATOR);
    this.city = in.readString();
    this.isLiked = (Integer) in.readValue(Integer.class.getClassLoader());
    this.deadline = (Long) in.readValue(Long.class.getClassLoader());
    this.isInterest = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public static final Creator<Feed> CREATOR = new Creator<Feed>() {
    @Override public Feed createFromParcel(Parcel source) {
      return new Feed(source);
    }

    @Override public Feed[] newArray(int size) {
      return new Feed[size];
    }
  };
}
