package cache.hystrix.controller;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixObservableCommand;

import cache.hystrix.command.GetNameCommand;
import cache.hystrix.command.GetOrderInfoCommand;
import cache.hystrix.command.GetOrderInfosCommand;
import cache.hystrix.model.OrderVo;

@Controller
public class HelloController {

	@RequestMapping("/hello")
	@ResponseBody
	public String hello(String name) {
		return "hello, " + name;
	}
	
	
	
	@RequestMapping("/getOrder")
	@ResponseBody
	public OrderVo order(@RequestParam String orderNumber) {
		//把每一次的调用封装到command里面  会扔到GetOrderInfoCommandGroup 对应的线程池里面 即是是超出了总数量 也不会暂用其他的资源，只会隔离在当前的线程池里面。
		HystrixCommand<OrderVo> getOrderCommand = new GetOrderInfoCommand(orderNumber);
		OrderVo orderVo = getOrderCommand.execute();//同步执行的
//		getOrderCommand.execute();  //同步执行
//		getOrderCommand.queue();//异步执行
//		getOrderCommand.observe();//hot 已经执行过
//		getOrderCommand.toObservable();//cold 还没有执行
		//信号量
		String soldToCustomerNumber = orderVo.getSoldToCustomerNumber();
		GetNameCommand getNameCommand = new GetNameCommand(soldToCustomerNumber);
		String name = getNameCommand.execute();
		orderVo.setCustomerName(name);
		return orderVo;
	}
	
	/**
	 * 一次性批量查询多条商品数据的请求
	 */
	@RequestMapping("/getOrders")
	@ResponseBody
	public List<OrderVo> getProductInfos(@RequestParam String orderNumbers) {
		HystrixObservableCommand<OrderVo> getProductInfosCommand = 
				new GetOrderInfosCommand(orderNumbers.split(","));  
		Observable<OrderVo> observable = getProductInfosCommand.observe();//立即执行 command construct里面内容 下面的subscribe只是去拿结果
		List<OrderVo> result = new ArrayList<OrderVo>();
		Subscription subscribe = observable.subscribe(new Observer<OrderVo>() {//only to get result

			public void onCompleted() {
				System.out.println("获取完了所有的商品数据");
			}
			public void onError(Throwable e) {
				e.printStackTrace();
			}
			public void onNext(OrderVo orderVo) {
				System.out.println(orderVo);  
				result.add(orderVo);
			}
			
		});
		return result;
	}
	
	
	/**
	 * 一次性批量查询多条商品数据的请求
	 */
	@RequestMapping("/getOrders2")
	@ResponseBody
	public List<OrderVo> getProductInfos_2(@RequestParam String orderNumbers) {
		HystrixObservableCommand<OrderVo> getProductInfosCommand = 
				new GetOrderInfosCommand(orderNumbers.split(","));  
		Observable<OrderVo> observable = getProductInfosCommand.toObservable(); // 还没有执行
		List<OrderVo> result = new ArrayList<OrderVo>();
		Subscription subscribe = observable.subscribe(new Observer<OrderVo>() { // 等到调用subscribe然后才会执行

			public void onCompleted() {
				System.out.println("获取完了所有的商品数据");
			}
			public void onError(Throwable e) {
				e.printStackTrace();
			}
			public void onNext(OrderVo orderVo) {
				System.out.println(orderVo);  
				result.add(orderVo);
			}
			
		});
		return result;
	}
	
//	资源隔离---线程池 和信号量
	
	/**
	 * 一次性批量查询多条商品数据的请求
	 */
	@RequestMapping("/request/cache")
	@ResponseBody
	public List<OrderVo> requestCache(@RequestParam String orderNumbers) {
		List<OrderVo> result = new ArrayList<OrderVo>();

		for(String orderNumber : orderNumbers.split(",")) {
			GetOrderInfoCommand getOrderInfoCommand = new GetOrderInfoCommand(orderNumber);
			OrderVo orderVo = getOrderInfoCommand.execute();
			System.out.println(orderVo);
			System.out.println(orderNumber+"---是否从缓存--"+getOrderInfoCommand.isResponseFromCache()); 
			result.add(orderVo);
		}
		return result;
	}
}
