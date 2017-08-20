package me.j360.dubbo.batch.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CountDownLatch;



@Slf4j
public class ApplicationBootstrap {


    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        context.start();

        log.info("service started successfully!");
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                log.info("Shutdown hook was invoked. Shutting down Service.");
                context.close();
            }
        });
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
