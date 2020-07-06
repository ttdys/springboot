package com.zjh.security.config;


import com.zjh.security.fileter.ValidateCodeFilter;
import com.zjh.security.handler.MyAuthenticationAccessDeniedHandler;
import com.zjh.security.handler.MyAuthenticationFailureHandler;
import com.zjh.security.handler.MyAuthenticationSuccessHandler;
import com.zjh.security.handler.MyLogOutSuccessHandler;
import com.zjh.security.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author zjh on 2020/7/5
 */
@Component
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启权限注解
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MyAuthenticationAccessDeniedHandler myAuthenticationAccessDeniedHandler;

    @Autowired
    private MyLogOutSuccessHandler myLogOutSuccessHandler;

    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加验证码校验过滤器
                 .exceptionHandling()
                 .accessDeniedHandler(myAuthenticationAccessDeniedHandler)
                 .and()
                 .formLogin() // 表单登录
                // http.httpBasic() // HTTP Basic
               .loginPage("/authentication/require") // 登录跳转 URL
                .loginProcessingUrl("/login") // 处理表单登录 URL
                .failureHandler(authenticationFailureHandler) // 处理登录失败
                .successHandler(authenticationSuccessHandler)
                 .and()
                 .rememberMe() // 启用rememberMe
                 .tokenRepository(persistentTokenRepository()) // 配置 token 持久化仓库
                 .tokenValiditySeconds(3600) // remember 过期时间，单为秒
                 .userDetailsService(userDetailService) // 处理自动登录逻辑
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/authentication/require",
                        "/login.html",
                        "/code/image",
                        "/signout/success").permitAll() // 无需认证的请求路径
                .anyRequest()  // 所有请求
                .authenticated() // 都需要认证
                 .and()
                 .logout()
                 .logoutUrl("/signout") // 退出登录的url
                 // .logoutSuccessUrl("/signout/success")
                 .logoutSuccessHandler(myLogOutSuccessHandler) // 配置退出成功处理骑
                 .deleteCookies("JSESSIONID") // 删除名为'JSESSIONID'的cookie
                .and().csrf().disable();
    }
}
