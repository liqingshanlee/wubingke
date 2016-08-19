package tech.honc.android.apps.soldier.feature.im.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;

public class Notification {
	static Context mContext;
	
	private static Handler mHandler = new Handler(Looper.getMainLooper());
	
	/**
	 * 显示toast消息
	 * 
	 * @param msg
	 *            ：要显示的消息
	 */
	public static void showToastMsg(final Context context, final String msg) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, msg,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void showToastMsgLong(final Context context, final String msg) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, msg,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

    public static void showToastMsg(final Context context, final int resId){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
            }
        });
    }
	
	/**
	 * 显示图片
	 * @param data
	 */
	public static void showImageToast(final byte[] data){		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				Toast toast = Toast.makeText(mContext, "图片", Toast.LENGTH_SHORT);
				ImageView imageView = new ImageView(mContext);
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
				imageView.setImageBitmap(bm);
				toast.setView(imageView);
				toast.show();
			}
		});
	}
	
	public static void showDialog(Context context, final String message){
		AlertDialog dialog = new AlertDialog.Builder(context)
		.setTitle("对话框")
		.setMessage(message)
		.setNeutralButton("看完了", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.create();
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}
	
	public static void commonDialog(Context context, final String[] items){
		AlertDialog dialog = new AlertDialog.Builder(context)
		.setTitle("对话框")
		.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

            }
		})
		.create();
	}
	
	interface NotificationType{
		public static final long P2PMessage = 1;
		public static final long TribeMessage = 2;
	}
}
