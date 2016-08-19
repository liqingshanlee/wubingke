package tech.honc.android.apps.soldier.utils.toolsutils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.Calendar;
import tech.honc.android.apps.soldier.R;

/**
 * Created by MrJiang
 * on 2016/4/18.
 */
public class DialogUtil {

  public static ItemsView mItemsView;
  public static ItemsTag mItemsTag;

  private DialogUtil() {
  }

  /**
   * 筛选工具类
   * int array
   */
  // TODO: 16-5-22 有内存泄露
  public static void showFilterDialog(Activity activity, MaterialDialog.ListCallback callback,
      int array, View v) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(activity).items(array);
    if (callback != null) {
      builder.itemsCallback(callback);
      mItemsView.sendItemView(v);
    }
    builder.show();
  }

  public static void showFilterDialog(Activity activity, MaterialDialog.ListCallback callback,
      int array, int tag) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(activity).items(array);
    if (callback != null) {
      builder.itemsCallback(callback);
      mItemsTag.sendItemTag(tag);
    }
    builder.show();
  }

  /**
   * 筛选工具类
   */
  public static void showFilterDialog(Activity activity, MaterialDialog.ListCallback callback,
      ArrayList<String> list, View v) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(activity).items(list);
    if (callback != null) {
      builder.itemsCallback(callback);
      mItemsView.sendItemView(v);
    }
    builder.show();
  }

  public static void showFilterDialog(Activity activity, MaterialDialog.ListCallback callback,
      ArrayList<String> list, int tag) {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(activity).items(list);
    if (callback != null) {
      builder.itemsCallback(callback);
      mItemsTag.sendItemTag(tag);
    }
    builder.show();
  }

  /**
   * 显示DatePickDialog
   */
  public static void showDatePickerDialog(Context context,
      MaterialDialog.SingleButtonCallback callback) {
    boolean wrapInScrollView = false;
    MaterialDialog dialog = new MaterialDialog.Builder(context).title(R.string.date_picker_title)
        .customView(R.layout.list_item_datepicker, wrapInScrollView)
        .positiveText(R.string.date_picker_ok)
        .negativeText(R.string.date_picker_cancel)
        .onPositive(callback)
        .onNegative(callback)
        .show();
  }

  /**
   * 显示输入框
   */
  public static void showInputDialog(Context context, MaterialDialog.InputCallback callback,
      String title, String content, String pref) {
    new MaterialDialog.Builder(context).title(title)
        .content(content)
        .inputType(InputType.TYPE_CLASS_TEXT)
        .input(title, pref, callback)
        .show();
  }

  public static void setItemsViewListener(ItemsView itemsViewListener) {
    mItemsView = itemsViewListener;
  }

  public static void setItemTagListener(ItemsTag itemTagListener) {
    mItemsTag = itemTagListener;
  }

  public static void showDatePicker(Activity activity, DatePickerDialog.OnDateSetListener listener,
      int tag) {
    final Calendar calendar = Calendar.getInstance();
    DatePickerDialog dialog = new DatePickerDialog(activity, listener, calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
    dialog.show();
    mItemsTag.sendItemTag(tag);
  }

  /**
   * 没有限制
   */
  public static void showDatePicker(Activity activity, DatePickerDialog.OnDateSetListener listener,
      int tag, boolean isLimit) {
    final Calendar calendar = Calendar.getInstance();
    DatePickerDialog dialog = new DatePickerDialog(activity, listener, calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
    dialog.show();
    mItemsTag.sendItemTag(tag);
  }

  public interface ItemsView {
    public void sendItemView(View v);
  }

  public interface ItemsTag {
    public void sendItemTag(int tag);
  }

  /**
   * delete
   */
  public static void relationDeleteDialogImage(Activity activity,
      MaterialDialog.ListCallback callback) {
    MaterialDialog.Builder builder =
        new MaterialDialog.Builder(activity).items(R.array.delete_image);
    if (callback != null) {
      builder.itemsCallback(callback);
    }
    builder.show();
  }
}
