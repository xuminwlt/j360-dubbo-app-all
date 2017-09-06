package me.j360.dubbo.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Package: cn.paomiantv.common.util
 * User: min_xu
 * Date: 2017/6/22 下午6:42
 * 说明：
 */
public class ModelUtil {



    /**
     * 对List<CompletableFuture> 解析成 List<T>
     * @param futures
     * @param <T>
     * @return
     */
    public static <T> CompletableFuture<List<T>> listFuture(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.<T>toList()));
    }

}
