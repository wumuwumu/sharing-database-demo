package cn.sciento.wumu.sharingdatabase;

import cn.sciento.wumu.sharingdatabase.config.DynamicDataSourceContextHolder;
import cn.sciento.wumu.sharingdatabase.config.DynamicRoutingDataSource;
import cn.sciento.wumu.sharingdatabase.entity.User;
import cn.sciento.wumu.sharingdatabase.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SharingDatabaseApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private DynamicRoutingDataSource dynamicRoutingDataSource;

	@Test
	public void contextLoads() {
//		DynamicDataSourceContextHolder.setDataSourceType("ds1");

		User user = new User();
		user.setAge(12);
		user.setName("毛工");
		int count = userService.add(user);
		System.out.println("返回值："+count);


//

		Map<String,String> map = new HashMap<>();
		map.put("driverClassName","com.mysql.jdbc.Driver");
		map.put("url", "jdbc:mysql://XXXX:3306/sharing_2?useSSL=false");
		map.put("username", "XXXX");
		map.put("password", "XXXX");
		map.put("key","ds1");
		boolean en = dynamicRoutingDataSource.addDataSource(map);
		System.out.println(en);
		DynamicDataSourceContextHolder.setDataSourceKey("ds1");
		int count1 = userService.add(user);
		System.out.println("返回值1："+count1);
	}

}
