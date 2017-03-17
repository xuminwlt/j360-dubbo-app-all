package me.j360.dubbo.common.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiResponse<D> extends BaseResponse {

    @Getter
    protected D data;

    public static Builder newBuilder() {
        return new Builder();
    }

    public static ApiResponse createResponse() {
        return newBuilder().data(null).status(ApiStatus.SUCCESS).error("").build();
    }

    public static <D> ApiResponse createResponse(D data) {
        return newBuilder().data(data).status(ApiStatus.SUCCESS).error("").build();
    }

    protected ApiResponse(D data, int status, String error) {
        super(status, error);
        this.data = data;
    }

    public static class Builder<D> extends BaseResponse.Builder {

        private D data;

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public ApiResponse build() {
            return new ApiResponse(data, status, error);
        }

        public Builder data(D data) {
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
    }



}
