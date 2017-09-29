package com.dist.bdf.service.file.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter{

	 @Bean
	    public Docket api() { 
	        return new Docket(DocumentationType.SWAGGER_2)  
	          .groupName("file-web-api") //  组名
	          .select()  
	          .apis(RequestHandlerSelectors.basePackage("com.dist.bdf"))       
	          .paths(PathSelectors.any())      // PathSelectors.any() | PathSelectors.ant("/rest/sysservice/*")  
	                                                        // 不做限制，否则带有{}的参数接口识别不了 
	          .build()
	          .apiInfo(apiInfo());                                           
	    }
	 
	 private ApiInfo apiInfo() {
		 ApiInfo apiInfo = new ApiInfo(
				    "小智平台文件上传下载 API 导航",//大标题
	                "API Document管理",//小标题
	                "1.0",//版本
	                "NO terms of service",
	                new Contact("weifj", "", "weifj@dist.com.cn"),//作者
	                "",//链接显示文字
	                ""//网站链接
	        );
		 return apiInfo;
		}

	 @Override
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
	     registry.addResourceHandler("swagger-ui.html")
	       .addResourceLocations("classpath:/META-INF/resources/");
	  
	     registry.addResourceHandler("/webjars/**")
	       .addResourceLocations("classpath:/META-INF/resources/webjars/");
	 }
	 
}
