package cache.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import cache.hystrix.filter.HystrixRequestContextServletFilter;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
public class Application {

//    @Bean
//    @ConfigurationProperties(prefix="spring.datasource")
//    public DataSource dataSource() {
//        return new org.apache.tomcat.jdbc.pool.DataSource();
//    }

//    @Bean
//    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource());
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis/*.xml"));
//        return sqlSessionFactoryBean.getObject();
//    }

//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(dataSource());
//    }
	
	
	@Bean
	public FilterRegistrationBean indexFilterRegistration() {
	    FilterRegistrationBean registration = new FilterRegistrationBean(new HystrixRequestContextServletFilter());
	    registration.addUrlPatterns("/*");
	    return registration;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
