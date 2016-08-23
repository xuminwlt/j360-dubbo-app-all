package me.j360.dubbo.api;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class UserServiceIT {

	ClassPathXmlApplicationContext appContext = null;
	CategoryService categoryService = null;
	Long categoryId =1L;
	
	@BeforeClass
	public void setup() {
		this.appContext = new ClassPathXmlApplicationContext(
				"classpath:META-INF/spring/user-service-consumer.xml");
		this.appContext.start();

		this.categoryService = ((CategoryService) this.appContext
				.getBean("categoryService"));
	}
	
	@AfterClass
	public void teardown() {
		this.appContext.close();
	}
	

	@Test(description="查询商品类目树")
	public void getCategorysTreeTest() {
		CategoryTreeResult result = this.categoryService.getCategorysTree();
		CategoryDO category = result.getTree();
		Assert.assertTrue(result.isSuccess());
		this.categoryId = category.getId(); 
	}
	
	@Test(description="查询除根节点的商品类目森林")
	public void getCategorysListTest() {
		CategoryQryResult result = this.categoryService.getCategorysList();
		List<CategoryDO> categoryList = result.getCategroyDOList();
		Assert.assertTrue(result.isSuccess());
		Assert.assertTrue(categoryList.size()> 0);			
	}
	
	@Test(description="根据商品类目主键，查询商品类目",dependsOnMethods={"getCategorysTreeTest"})
	public void getCategoryTest() {		
		CategoryResult result = this.categoryService.getCategory(this.categoryId);
		Assert.assertTrue(result.isSuccess());		
	}
}
