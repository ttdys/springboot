import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 我们可以通过一些配置将HTTP Basic认证修改为基于表单的认证方式。
 * <p>
 * 创建一个配置类BrowserSecurityConfig继承
 * org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
 * 这个抽象类并重写configure(HttpSecurity http)方法。
 * WebSecurityConfigurerAdapter是由Spring Security提供的Web应用安全配置的适配器：
 */
@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() // 表单方式
                //       http.basic()// 普通方式
                .and()
                .authorizeRequests() // 授权配置
                .anyRequest()  // 所有请求
                .authenticated(); // 都需要认证
    }
}
