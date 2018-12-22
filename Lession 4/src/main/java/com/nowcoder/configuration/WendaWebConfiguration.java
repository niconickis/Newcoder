package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequiredInterceptor;
import com.nowcoder.interceptor.MyInterceptor;
import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by nowcoder on 2016/7/3.
 */
@Component
// 我还是不懂这个注解是干啥的？？？？？？？？？？？
public class WendaWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;
    // 拦截器的注册与先后顺序
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 下面才是把刚刚写的拦截器注册到整个链路上中，才开始正式生效
        registry.addInterceptor(passportInterceptor);
        // 拦截器的顺序
        // 为什么会有拦截器的顺这个概念呢，因为HostHolder是在passportInterceptor首先被用到的（加入用户user到HostHolder）,
        // 如果先调用passportInterceptor这个拦截器，则HostHolder永远为空起不到作用了，座椅要将它放在本拦截器之前

        // addPathPatterns():表示在特定url路径才使用这个拦截器
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");

        registry.addInterceptor(myInterceptor);
        
        super.addInterceptors(registry);
    }
    
}
