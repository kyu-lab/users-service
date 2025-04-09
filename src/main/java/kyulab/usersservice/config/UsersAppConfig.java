package kyulab.usersservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;

@Configuration
public class UsersAppConfig {

	@Value("${jwt.refresh-expiredTime:36000}")
	private long refreshTokenTime;

	@Value("${gateway.key:}")
	private String gatewayKey;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CacheManager rcm(RedisConnectionFactory redisConnectionFactory) {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
				.entryTtl(Duration.ofMinutes(30L)) // 캐시 항목의 TTL을 30분으로 설정
				.disableCachingNullValues();  // Null 값은 캐싱 비활성화

		// 리프레쉬 토큰 시간 설정
		RedisCacheConfiguration rConfig = redisCacheConfiguration.entryTtl(Duration.ofHours(refreshTokenTime));

		return RedisCacheManager
				.RedisCacheManagerBuilder
				.fromConnectionFactory(redisConnectionFactory)
				.cacheDefaults(redisCacheConfiguration)
				.withCacheConfiguration("refresh", rConfig)
				.build();
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
			HttpHeaders headers = request.getHeaders();
			headers.add("X-GATE-WAY-KEY", gatewayKey);
			headers.setContentType(MediaType.APPLICATION_JSON);
			return execution.execute(request, body);
		}));
		return restTemplate;
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}
