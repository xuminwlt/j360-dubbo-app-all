package me.j360.dubbo.base.model.result;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.j360.dubbo.base.constant.BaseErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Package: com.app.api.base
 * User: min_xu
 * Date: 16/8/19 下午3:01
 * 说明：
 */
@Slf4j
public class DefaultPageResult<D> extends DefaultResult {

    private DefaultPageResult(Object data, boolean success , int code, String msg) {
        super(data, success, code, msg);
    }

    @Override
    public ListData<D> getData() {
        return (ListData<D>) super.getData();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static <D> DefaultPageResult success(Integer total, List<D> list) {
        return newBuilder().success(true).data(new ListData(total,list)).code(BaseErrorCode.SUCCESS_CODE).msg("").build();
    }
    public static <D> DefaultPageResult success(List<D> list) {
        list = null==list? new ArrayList<D>():list;
        return newBuilder().success(true).data(new ListData(list.size(),list)).code(BaseErrorCode.SUCCESS_CODE).msg("").build();
    }

    public static DefaultPageResult fail(int code, String msg) {
        return newBuilder().success(false).data(null).code(code).msg(msg).build();
    }

    public static <D> DefaultPageResult fail(Integer total, List<D> list, int code, String msg) {
        return newBuilder().success(false).data(new ListData(total,list)).code(code).msg(msg).build();
    }

    public static DefaultPageResult fail(BaseErrorCode errorCode) {
        return newBuilder().success(false).data(null).code(errorCode.getErrorCode()).msg(errorCode.getErrorMsg()).build();
    }

    public static class Builder<D> extends DefaultResult.Builder {

        private ListData<D> data;

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder data(ListData data) {
            this.data = data;
            return this;
        }

        @Override
        public Builder code(Integer code) {
            this.code = code;
            return this;
        }
        @Override
        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        @Override
        public Builder success(Boolean success) {
            this.success = success;
            return this;
        }

        @Override
        public DefaultPageResult build() {
            return new DefaultPageResult(data, success ,code, msg);
        }

    }


    private static class ListData<D> {

        @Getter
        private Integer total;

        @Getter
        private List<D> list;

        private ListData(List<D> list) {
            this(list.size(), list);
        }

        private ListData(Integer total, List<D> list) {
            this.total = total;
            this.list = list;
        }
    }
}
