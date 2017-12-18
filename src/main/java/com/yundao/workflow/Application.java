package com.yundao.workflow;

import com.alibaba.druid.pool.DruidDataSource;
import com.yundao.core.utils.ConfigUtils;
import com.yundao.core.utils.DesUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import javax.sql.DataSource;

/**
 *
 * @author 云道
 * @version 1.0.0
 * @blog http://blog.didispace.com
 *
 */
@SpringBootApplication
@EnableCaching
@ImportResource(locations = "classpath:config/spring/springRootContext.xml")
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

//	@Bean
//	public DataSource dataSource() throws Exception {
//		DruidDataSource dataSource = new DruidDataSource();
//		dataSource.setUrl(ConfigUtils.getValue("spring.datasource.url"));
//		String userName = DesUtils.decrypt(ConfigUtils.getValue("spring.datasource.username"));
//		String password = DesUtils.decrypt(ConfigUtils.getValue("spring.datasource.password"));
//		dataSource.setUsername(userName);//用户名
//		dataSource.setPassword(password);//密码
//		String initialSize = ConfigUtils.getValue("spring.datasource.initialSize","5");
//		dataSource.setInitialSize(Integer.valueOf(initialSize));
//		String maxActive = ConfigUtils.getValue("spring.datasource.maxActive","20");
//		dataSource.setMaxActive(Integer.valueOf(maxActive));
//		String minIdle = ConfigUtils.getValue("spring.datasource.minIdle","5");
//		dataSource.setMinIdle(Integer.valueOf(minIdle));
//		String maxwait = ConfigUtils.getValue("spring.datasource.maxWait","60000");
//		dataSource.setMaxWait(Integer.valueOf(maxwait));
//		String query = ConfigUtils.getValue("spring.datasource.validationQuery","SELECT 1 FROM DUAL");
//		dataSource.setValidationQuery(query);
//		String millis = ConfigUtils.getValue("spring.datasource.minEvictableIdleTimeMillis","300000");
//		dataSource.setMinEvictableIdleTimeMillis(Integer.valueOf(millis));
//		String whileIdle = ConfigUtils.getValue("spring.datasource.testWhileIdle","true");
//		dataSource.setTestWhileIdle("true".equals(whileIdle));
//		String borrow = ConfigUtils.getValue("spring.datasource.testOnBorrow","false");
//		dataSource.setTestOnBorrow("true".equals(borrow));
//		String onReturn = ConfigUtils.getValue("spring.datasource.testOnReturn","false");
//		dataSource.setTestOnReturn("true".equals(onReturn));
//		String statements = ConfigUtils.getValue("spring.datasource.poolPreparedStatements","true");
//		dataSource.setPoolPreparedStatements("true".equals(statements));
//		String connectionSize = ConfigUtils.getValue("spring.datasource.maxPoolPreparedStatementPerConnectionSize","20");
//		dataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.valueOf(connectionSize));
//		return dataSource;
//	}
}
