package kyulab.usersservice.config;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;

@Configuration
public class UsersAppConfig {

	@Value("${jwt.refresh-expiredTime:36000}")
	private long refreshTokenTime;

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

}
