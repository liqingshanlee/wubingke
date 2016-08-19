package tech.honc.android.apps.soldier.utils.data;

import android.os.Parcel;
import com.smartydroid.android.starter.kit.model.entity.Entity;

/**
 * Created by MrJiang on 4/22/2016.
 */
public class Letter extends Entity {

  public String letter;

  public Letter(String letter) {
    this.letter = letter;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.letter);
  }

  protected Letter(Parcel in) {
    this.letter = in.readString();
  }

  public static final Creator<Letter> CREATOR = new Creator<Letter>() {
    @Override public Letter createFromParcel(Parcel source) {
      return new Letter(source);
    }

    @Override public Letter[] newArray(int size) {
      return new Letter[size];
    }
  };
}


