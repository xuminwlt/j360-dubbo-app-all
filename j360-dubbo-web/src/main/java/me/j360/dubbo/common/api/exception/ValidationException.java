package me.j360.dubbo.common.api.exception;

import org.springframework.validation.BindingResult;

/**
 * Created by ft on 16/9/13.
 * <p>
 * 验证异常
 *
 * @author ft
 */
public class ValidationException extends RuntimeException {

    private BindingResult bindingResult;

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(Throwable cause, BindingResult bindingResult) {
        super(cause);
        this.bindingResult = bindingResult;
    }

    public ValidationException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public final BindingResult getBindingResult() {
        return this.bindingResult;
    }
}
