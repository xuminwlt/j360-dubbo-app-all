package me.j360.dubbo.bootstrap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ApplicationMain {
	public static void main(String[] args) throws IOException {
		 ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
		 "META-INF/spring/application-service.xml"});
	     context.start();
	     System.in.read();
	}
}
