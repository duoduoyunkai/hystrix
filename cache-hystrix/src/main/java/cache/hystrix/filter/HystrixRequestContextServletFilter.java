package cache.hystrix.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class HystrixRequestContextServletFilter implements Filter {
	public void init(FilterConfig config) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HystrixRequestContext initializeContext = HystrixRequestContext.initializeContext();
		try {
			chain.doFilter(request, response);

		} finally {
			initializeContext.shutdown();
		}
	}

	public void destroy() {

	}

}
