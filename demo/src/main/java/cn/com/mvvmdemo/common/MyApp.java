package cn.com.mvvmdemo.common;

import android.app.Application;
import android.content.Context;

import cn.com.minimvvm.crash.CaocConfig;
import cn.com.minimvvm.utils.LogUtils;
import cn.com.mvvmdemo.BuildConfig;
import cn.com.mvvmdemo.R;
import cn.com.mvvmdemo.news.ui.FeedActivity;

/**
 * Created by JokerWan on 2018/11/21.
 * Function:
 */

public class MyApp extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        initCrash();
        LogUtils.init(BuildConfig.DEBUG);
    }

    public static Context getContext() {
        return mContext;
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                //背景模式,开启沉浸式
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT)
                //是否启动全局异常捕获
                .enabled(true)
                //是否显示错误详细信息
                .showErrorDetails(true)
                //是否显示重启按钮
                .showRestartButton(true)
                //是否跟踪Activity
                .trackActivities(true)
                //崩溃的间隔时间(毫秒)
                .minTimeBetweenCrashesMs(2000)
                //错误图标
                .errorDrawable(R.mipmap.ic_launcher)
                //重新启动后的activity
                .restartActivity(FeedActivity.class)
                //崩溃后的错误activity
//                .errorActivity(YourCustomErrorActivity.class)
                //崩溃后的错误监听
//                .eventListener(YourCustomEventListener.class)
                .apply();
    }
}
