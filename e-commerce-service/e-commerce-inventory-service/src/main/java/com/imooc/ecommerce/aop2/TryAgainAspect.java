package com.imooc.ecommerce.aop2;

import com.imooc.ecommerce.exception.GoodsNotFoundException;
import com.imooc.ecommerce.exception.TryAgainException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.Transactional;


/**
 * 更新失败，尝试重试切片
 * @author  rstyro
 */
@Aspect
@Configuration
public class TryAgainAspect implements Ordered {

	/**
	 * 默认重试几次
	 */
	private static final int    DEFAULT_MAX_RETRIES = 10;

	private int                 maxRetries          = DEFAULT_MAX_RETRIES;
	private int                 order               = 1;

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public int getOrder() {
		return this.order;
	}

	@Pointcut("@annotation(IsTryAgain)")
	public void retryOnOptFailure() {
		// pointcut mark
	}

	@Around("retryOnOptFailure()")
	@Transactional(rollbackFor = Exception.class)
	public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
		int numAttempts = 0;
		do {
			numAttempts++;
			try {
				//再次执行业务代码
				Object proceed = pjp.proceed();
				System.out.println("==重试成功==");
				return proceed;
			} catch (TryAgainException ex) {
				if (numAttempts > maxRetries) {
					//log failure information, and throw exception
//					如果大于 默认的重试机制 次数，我们这回就真正的抛出去了
					throw new GoodsNotFoundException("11111");
				}else{
					//如果 没达到最大的重试次数，将再次执行
					System.out.println("=====正在重试====="+numAttempts+"次");
				}
			}
		} while (numAttempts <= this.maxRetries);

		return null;
	}
}
