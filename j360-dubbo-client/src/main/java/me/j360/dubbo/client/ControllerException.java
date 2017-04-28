package me.j360.dubbo.client;

/**
 * Package: me.j360.dubbo.client
 * User: min_xu
 * Date: 2017/4/28 上午11:47
 * 说明：
 */
public class ControllerException extends RuntimeException {

    public ControllerException(){
        super();
    }

    public ControllerException(String message) {
        super(message);
    }
}
