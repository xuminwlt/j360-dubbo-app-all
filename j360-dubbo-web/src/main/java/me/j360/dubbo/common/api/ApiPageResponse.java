package me.j360.dubbo.common.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ApiPageResponse<D> extends ApiResponse {

    private ApiPageResponse(Object data, int status, String error) {
        super(data, status, error);
    }

    @Override
    public ListData<D> getData() {
        return (ListData<D>) super.getData();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static <D> ApiPageResponse createResponse(Integer total, List<D> list) {
        return newBuilder().data(new ListData(total,list)).status(ApiStatus.SUCCESS).error("").build();
    }
    public static <D> ApiPageResponse createResponse(List<D> list) {
        list = null==list? new ArrayList<D>():list;
        return newBuilder().data(new ListData(list.size(),list)).status(ApiStatus.SUCCESS).error("").build();
    }
    public static class Builder<D> extends ApiResponse.Builder {

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
        public Builder status(Integer status) {
            this.status = status;
            return this;
        }
        @Override
        public Builder error(String error) {
            this.error = error;
            return this;
        }

        @Override
        public ApiPageResponse build() {
            return new ApiPageResponse(data, status, error);
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
