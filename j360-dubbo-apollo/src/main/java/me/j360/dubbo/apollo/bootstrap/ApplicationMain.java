package me.j360.dubbo.apollo.bootstrap;

import me.j360.dubbo.apollo.common.CacheConfiguration;
import me.j360.dubbo.apollo.repository.UserRepository;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApplicationMain {
	public static void main(String[] args) throws IOException, InterruptedException {
		 ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
		 "META-INF/spring/application-service.xml",
				 "META-INF/spring/application-dal.xml"});
	     context.start();


		UserRepository userRepository = (UserRepository) context.getBean("userRepository");
		CacheConfiguration cacheConfiguration = (CacheConfiguration) context.getBean("cacheConfiguration");


		while (true) {
			String name = userRepository.getUserCacheable(1L);
			System.out.println(String.format("name=%s", name));

			System.out.println(String.format("timeout=%s", cacheConfiguration.getTimeout()));
			TimeUnit.SECONDS.sleep(5);
		}
	}
}
