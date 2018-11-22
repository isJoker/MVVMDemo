package cn.com.minimvvm.utils;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.minimvvm.R;


/**
 * toast 提示框
 */

public class ToastUtil {


    public static void show(Context context, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, int msgId) {
        show(context, context.getResources().getString(msgId));
    }

    public static void show(Context context, int imgRid, int msgRId) {
        show(context, imgRid, context.getResources().getString(msgRId));
    }

    /**
     * 自定义样式的toast
     * @param context
     * @param imgRid
     * @param msg
     */
    public static void show(Context context, int imgRid, String msg) {
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.dialog_common, null);
        ImageView imageView = toastRoot.findViewById(R.id.imageView);
        if (imgRid != 0) {
            imageView.setImageResource(imgRid);
        }
        TextView tvPrompt = toastRoot.findViewById(R.id.tv_prompt);
        if (!TextUtils.isEmpty(msg)) {
            tvPrompt.setText(msg);
        }
        Toast toast = new Toast(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastRoot);
            toast.show();
        }
    }

    public static Toast showToast(Context context, int msgResId) {
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.dialog_toast, null);
        TextView mTextView = toastRoot.findViewById(R.id.tv_prompt);
        mTextView.setText(msgResId);
        Toast toast = new Toast(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(toastRoot);
            toast.show();
        }

        return toast;
    }

}
