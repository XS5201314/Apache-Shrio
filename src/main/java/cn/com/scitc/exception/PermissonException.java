package cn.com.scitc.exception;
/**
*@author xiaoxie
*@date create 2019/9/6   update  2019/9/11
*@return
 * 自定义接口异常
*/
public class PermissonException extends RuntimeException{

    public PermissonException() {
        super();
    }

    public PermissonException(String message) {
        super(message);
    }

    public PermissonException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissonException(Throwable cause) {
        super(cause);
    }

    protected PermissonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
