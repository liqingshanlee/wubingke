package tech.honc.android.apps.soldier.model;

import android.os.Parcel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by Administrator on 2016/5/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true) public class Token extends Entity
{
  public String token;

  public Token() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.token);
  }

  protected Token(Parcel in) {
    this.token = in.readString();
  }

  public static final Creator<Token> CREATOR = new Creator<Token>()
  {
    @Override public Token createFromParcel(Parcel source) {
      return new Token(source);
    }

    @Override public Token[] newArray(int size) {
      return new Token[size];
    }
  };
}
