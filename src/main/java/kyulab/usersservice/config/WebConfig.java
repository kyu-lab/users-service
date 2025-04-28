package kyulab.usersservice.config;

import kyulab.usersservice.interceptor.GatewayInterceptor;
import kyulab.usersservice.interceptor.ServiceInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final GatewayInterceptor gatewayInterceptor;
	private final ServiceInterceptor serviceInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(gatewayInterceptor)
				.addPathPatterns("/gateway/**");

		registry.addInterceptor(serviceInterceptor)
				.addPathPatterns("/api/**");
	}

}
