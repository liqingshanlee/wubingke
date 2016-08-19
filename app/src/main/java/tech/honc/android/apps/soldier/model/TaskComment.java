package tech.honc.android.apps.soldier.model;

import android.net.Uri;
import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartydroid.android.starter.kit.model.entity.Entity;
import tech.honc.android.apps.soldier.model.enums.TaskCommentType;

/**
 * Created by MrJiang on 2016/5/10.
 * 我的任务
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class TaskComment extends Entity {

  public Integer id;
  @JsonProperty("account_id") public Integer accountId;
  public String avatar;
  public String nickname;
  public String content;
  public TaskCommentType status;
  @JsonProperty("created_at") public Long createdAt;

  public Uri uri() {
    if (avatar != null) {
      return Uri.parse(avatar);
    }
    return null;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.id);
    dest.writeValue(this.accountId);
    dest.writeString(this.avatar);
    dest.writeString(this.nickname);
    dest.writeString(this.content);
    dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    dest.writeValue(this.createdAt);
  }

  public TaskComment() {
  }

  protected TaskComment(Parcel in) {
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.accountId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.avatar = in.readString();
    this.nickname = in.readString();
    this.content = in.readString();
    int tmpStatus = in.readInt();
    this.status = tmpStatus == -1 ? null : TaskCommentType.values()[tmpStatus];
    this.createdAt = (Long) in.readValue(Long.class.getClassLoader());
  }

  public static final Creator<TaskComment> CREATOR = new Creator<TaskComment>() {
    @Override public TaskComment createFromParcel(Parcel source) {
      return new TaskComment(source);
    }

    @Override public TaskComment[] newArray(int size) {
      return new TaskComment[size];
    }
  };
}
