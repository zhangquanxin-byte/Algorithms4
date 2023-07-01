# 跨域统一解决方案

## 后端统一配置 zuul

```java
@SpringBootConfiguration
public class ShopCorsConfig {
    private static Set<String> set = new HashSet<>();

    static {
        set.add("http://api.shop.com");
        set.add("http://manager.shop.com");
        set.add("http://www.shop.com");
        set.add("http://search.shop.com");
        set.add("http://item.shop.com");
        set.add("http://sso.shop.com");
        set.add("http://cart.shop.com");
        set.add("http://order.shop.com");
    }

    @Bean
    public CorsFilter corsFilter() {
        // CorsConfiguration
        CorsConfiguration config = new CorsConfiguration();
        //允许跨域
        for (String s : set) {
            config.addAllowedOrigin(s);
        }
        //允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        //允许cookie
        config.setAllowCredentials(true);
        //允许的method
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        // UrlBasedCorsConfigurationSource
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

## 前端静态页面配置 nginx 

```nginx
    server {
        listen       80;
        server_name  res.shop.com;

        location / {
            
			add_header 'Access-Control-Allow-Headers' 		'*';
			add_header 'Access-Control-Allow-Methods' 		'*';
			add_header 'Access-Control-Allow-Origin' 		'*';
			add_header 'Access-Control-Allow-Credentials' 	'true';
			
            root   绝对路径;
        }
    }
```

