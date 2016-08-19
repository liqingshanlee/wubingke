package tech.honc.android.apps.soldier.feature.im.interfaces;

import com.alibaba.mobileim.contact.IYWContact;
import java.util.List;

/**
 * Created by wangh on 2016-5-20-0020.
 */
public interface ISelectContactListener {
  void onSelectCompleted(List<IYWContact> contacts);
}
