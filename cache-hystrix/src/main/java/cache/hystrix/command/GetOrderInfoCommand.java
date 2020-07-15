package cache.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

import cache.hystrix.model.OrderVo;
import cache.hystrix.util.HttpClientUtils;

public class GetOrderInfoCommand extends HystrixCommand<OrderVo> {

	private String orderNumber;

	public GetOrderInfoCommand(String orderNumber) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("OrderInfoService"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("GetOrderInfoCommand"))
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetOrderInfoThread"))
				.andThreadPoolPropertiesDefaults(
						HystrixThreadPoolProperties.Setter()
						.withCoreSize(1)
						.withMaxQueueSize(8)
						.withQueueSizeRejectionThreshold(6)//实际等待对列的数目是 withMaxQueueSize 和withQueueSizeRejectionThreshold的最小值。
						)
//				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withFallbackIsolationSemaphoreMaxConcurrentRequests(2)) //控制fallback并发数量 GetOrderInfoCommand fallback execution rejected.
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withCircuitBreakerRequestVolumeThreshold(20) //10秒内 20个请求
						.withCircuitBreakerErrorThresholdPercentage(40) //失败率40%
						.withCircuitBreakerSleepWindowInMilliseconds(3000) //3秒
						.withExecutionTimeoutInMilliseconds(20000)
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(30) 
						) //3秒
				);
		this.orderNumber = orderNumber;
	}

	@Override
	protected OrderVo run() throws Exception {
		System.out.println("请求的订单号===========："+orderNumber);
		if("000".equals(orderNumber)) {
			throw new Exception("订单号不能为000！");
		}
		if("003".equals(orderNumber)) {
			Thread.sleep(1000);
		}
		String url = "http://127.0.0.1:8082/orders?orderNumber=" + orderNumber;
		String response = HttpClientUtils.sendGetRequest(url);
		return JSONObject.parseObject(response, OrderVo.class);
	}

	/**
	 * override this method to open request cache
	 */
//	@Override
//	protected String getCacheKey() {
//		// TODO Auto-generated method stub
////		return super.getCacheKey();
//		return "order_info" + this.orderNumber;
//	}

	/**
	 * 降级处理
	 */
	@Override
	protected OrderVo getFallback() {
		// TODO Auto-generated method stub
		OrderVo result = new OrderVo();
		result.setSalesOrderNumber("-1");
		result.setSoldToCustomerNumber("000000");
		return result;
	}

}
