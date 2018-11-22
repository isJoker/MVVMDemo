package cn.com.minimvvm.net;

/**
 * Created by JokerWan on 2018/11/21.
 * Function:
 */

public class ResponseThrowableException extends Exception {
    public int code;
    public String message;

    public ResponseThrowableException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
}
