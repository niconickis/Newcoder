package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by nowcoder on 2016/7/10.
 */
@Aspect//通过注解的方式表示是切面
@Component//没听懂为啥要加component...
//现在是想在调用IndexController和SettingController之前调用 beforeMethod ；之后都调用afterMethod
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.nowcoder.controller.*Controller.*(..))")//第一个*表示两个controller的返回值；com.nowcoder.controller表示包的位置；*Controller表示所有的controller
    public void beforeMethod(JoinPoint joinPoint) {                  //*(..)表示某个Controller的所有方法
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("before method:" + sb.toString());
    }

    @After("execution(* com.nowcoder.controller.IndexController.*(..))")
    public void afterMethod() {
        logger.info("after method" + new Date());
    }
}
