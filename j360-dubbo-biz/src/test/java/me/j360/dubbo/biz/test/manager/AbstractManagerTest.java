package me.j360.dubbo.biz.test.manager;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/META-INF/spring/application-system.xml",
        "classpath*:/META-INF/spring/application-dal.xml",
        "classpath*:/META-INF/spring/application-service.xml"})
public abstract class AbstractManagerTest {

    protected void print(Object obj) {
        System.out.println(obj.toString());
    }
}
