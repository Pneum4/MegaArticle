package pneum4.board.articleread.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import pneum4.board.common.dataserializer.DataSerializer;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OptimizedCacheManager {
    private final StringRedisTemplate redisTemplate;
    private final OptimizedCacheLockProvider optimizedCacheLockProvider;

    private static final String DELIMITER = "::";

    public Object process(String type, long ttlSeconds, Object[] args, Class<?> returnType,
                          OptimizedCacheOriginDataSupplier<?> originDataSupplier) throws Throwable {
        String key = generateKey(type, args);

        String cachedData = redisTemplate.opsForValue().get(key);
        if (cachedData == null) { // 없는경우
            return refresh(originDataSupplier, key, ttlSeconds); // 원본데이터 리턴
        }

        OptimizedCache optimizedCache = DataSerializer.deserialize(cachedData, OptimizedCache.class);
        if (optimizedCache == null) { // 역직렬화 실패시
            return refresh(originDataSupplier, key, ttlSeconds);
        }

        if (!optimizedCache.isExpired()) { // logicalTTL 만료 전
            return optimizedCache.parseData(returnType);
        }

        if (!optimizedCacheLockProvider.lock(key)) { // logicalTTL 만료 + 락 획득 못함
            return optimizedCache.parseData(returnType);
        }

        try { // logicalTTL 만료 + 락 획득
            return refresh(originDataSupplier, key, ttlSeconds);
        } finally {
            optimizedCacheLockProvider.unlock(key);
        }
    }

    private Object refresh(OptimizedCacheOriginDataSupplier<?> originDataSupplier, String key, long ttlSeconds) throws Throwable {
        Object result = originDataSupplier.get(); // 원본데이터

        OptimizedCacheTTL optimizedCacheTTL = OptimizedCacheTTL.of(ttlSeconds);
        OptimizedCache optimizedCache = OptimizedCache.of(result, optimizedCacheTTL.getLogicalTTL());

        redisTemplate.opsForValue()
                .set(
                        key,
                        DataSerializer.serialize(optimizedCache), // 원본 데이터 + logicalTTL
                        optimizedCacheTTL.getPhysicalTTL()
                );

        return result;
    }

    private String generateKey(String prefix, Object[] args) {
        return prefix + DELIMITER +
                Arrays.stream(args)
                        .map(String::valueOf)
                        .collect(Collectors.joining(DELIMITER));
    }

}

