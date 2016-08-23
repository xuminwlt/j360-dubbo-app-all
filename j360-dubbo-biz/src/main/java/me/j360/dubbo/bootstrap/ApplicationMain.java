package me.j360.dubbo.bootstrap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ApplicationMain {
	public static void main(String[] args) throws IOException {
		 ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
		 "META-INF/spring/application-service.xml",
		 "META-INF/spring/application-system.xml",
		 "META-INF/spring/application-validation.xml",
		 "META-INF/spring/application-dal.xml",
		 "META-INF/spring/application-index.xml",
		 "META-INF/spring/application-keygen.xml",
		 "META-INF/spring/application-manager.xml",
		 "META-INF/spring/application-proxy.xml",
		 "META-INF/spring/application-repository.xml",
		 "META-INF/spring/application-service.xml",
		 "META-INF/spring/application-import.xml",
         "META-INF/spring/application-builder.xml",
         "META-INF/spring/application-tfs.xml",
		 "META-INF/spring/application-export.xml"});
	     context.start();
	     System.in.read();
	}
}
