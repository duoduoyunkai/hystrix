package cache.hystrix;

import cache.hystrix.util.HttpClientUtils;

public class CircuitBreakerTest {

	public static void main(String[] args) throws Exception {
		long currentTimeMillis = System.currentTimeMillis();
		for(int i = 0; i < 10; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8080/getOrder?orderNumber=001");  
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);  
		}
		Thread.sleep(7000);
		for(int i = 0; i < 15; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8080/getOrder?orderNumber=000");  
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);  
		}
		Thread.sleep(3000);
		long currentTimeMillis2 = System.currentTimeMillis();
		System.out.println("统计时间是===="+(currentTimeMillis2-currentTimeMillis));
		// 等待了3s后，时间窗口统计了，发现异常比例太多，就短路了
		for(int i = 0; i < 10; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8080/getOrder?orderNumber=001");  
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);  //尽管订单号是001 单其实已经走了fallback.
		}
		// 统计单位，有一个时间窗口的，我们必须要等到那个时间窗口过了以后，才会说，hystrix看一下最近的这个时间窗口
		// 比如说，最近的10秒内，有多少条数据，其中异常的数据有没有到一定的比例
		// 如果到了一定的比例，那么才会去短路
		System.out.println("尝试等待3秒钟。。。。。。");  
		Thread.sleep(3000); 
		for(int i = 0; i < 10; i++) {
			String response = HttpClientUtils.sendGetRequest("http://localhost:8080/getOrder?orderNumber=002");  
			System.out.println("第" + (i + 1) + "次请求，结果为：" + response);  
		}
	}
}
