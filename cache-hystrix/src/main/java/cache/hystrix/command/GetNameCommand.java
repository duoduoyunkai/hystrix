package cache.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;

import cache.hystrix.local.cache.LocationCache;
import cache.hystrix.model.OrderVo;

public class GetNameCommand extends HystrixCommand<String>{

	private String soldToCustomerNumber;
	
	public GetNameCommand(String soldToCustomerNumber) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetNameGroup"))
		        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
		               .withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE) //信号量
		               .withExecutionIsolationSemaphoreMaxConcurrentRequests(50)
		               .withFallbackIsolationSemaphoreMaxConcurrentRequests(50) //控制fallback的数量 否则会报GetNameCommand fallback execution rejected.
//		               .withCircuitBreakerRequestVolumeThreshold(10)
//		               .withCircuitBreakerErrorThresholdPercentage(40)
//		               .withCircuitBreakerSleepWindowInMilliseconds(5)
		        		));
		this.soldToCustomerNumber = soldToCustomerNumber;
	}
	
	@Override
	protected String run() throws Exception {
//		int a = 1/0;//fallback会使用到
		return LocationCache.getName(soldToCustomerNumber);
	}
	/**
	 * 信号量不管timeout  一般用信号量来处理本项目内的一些代码 逻辑
	 * 
	 * 50%异常 断路器开启；5秒之后  断路器 处于半开闭状态
	 */
	
	
	/**
	 * 降级处理
	 */
	@Override
	protected String getFallback() {
		// TODO Auto-generated method stub
		return "default name";
	}
}
