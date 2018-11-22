package cn.com.minimvvm.base;

/**
 * Created by JokerWan on 2018/11/20.
 * Function:
 */

public interface IBaseActivity{
    /**
     * 初始化界面传递参数
     */
    void initParam();
    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();
}
