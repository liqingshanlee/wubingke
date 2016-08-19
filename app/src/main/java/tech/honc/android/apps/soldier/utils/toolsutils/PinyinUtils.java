package tech.honc.android.apps.soldier.utils.toolsutils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.regex.Pattern;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * author MrJiang on 2016/1/28.
 */
public class PinyinUtils {
  /**
   * 获取拼音的首字母（大写）
   */
  public static String getFirstLetter(final String pinyin) {
    if (TextUtils.isEmpty(pinyin)) return "#";
    String c = pinyin.substring(0, 1);
    Pattern pattern = Pattern.compile("^[A-Za-z]+$");
    if (pattern.matcher(c).matches()) {
      return c.toUpperCase();
    }
    return "";
  }

  @Deprecated @NonNull public static String getPinYinDeprecated(String word) {
    char c = word.charAt(0);
    String[] pinyin = null;
    try {
      HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
      defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
      defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
      pinyin = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
    } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
      badHanyuPinyinOutputFormatCombination.printStackTrace();
    }
    if (pinyin != null && pinyin.length != 0) {
      return String.valueOf(pinyin[0]);
    }
    return "";
  }

  public static String getPinYin(String hanzi) {
    ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(hanzi);
    StringBuilder sb = new StringBuilder();
    if (tokens != null && tokens.size() > 0) {
      for (HanziToPinyin.Token token : tokens) {
        if (HanziToPinyin.Token.PINYIN == token.type) {
          sb.append(token.target);
        } else {
          sb.append(token.source);
        }
      }
    }

    return sb.toString().toUpperCase();
  }
}
