package pneum4.board.hotarticle.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HotArticleListRepository {
    private final StringRedisTemplate redisTemplate;

    // {yyyyMMdd}
    private static final String KEY_FORMAT = "hot-article::list::%s";

    // key
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    // value (articleId, 저장날짜, 점수, 인기글 개수, 유지시간)
    public void add(Long articleId, LocalDateTime time, Long score, Long limit, Duration ttl) {
        redisTemplate.executePipelined((RedisCallback<?>) action-> {
            StringRedisConnection conn = (StringRedisConnection) action;
            String key = generateKey(time);
            // 동일 articleId가 주입되면 덮어쓰기됨
            conn.zAdd(key, score, String.valueOf(articleId));

            // 오름차순 정렬 되어있는 리스트를 0 ~ n-limit-1 까지 지우고, n-limit ~ n 을 반환
            conn.zRemRange(key, 0, -limit-1);
            conn.expire(key, ttl.toSeconds());
            return null;
        });
    }

    public void remove(Long articleId, LocalDateTime time) {
        redisTemplate.opsForZSet().remove(generateKey(time), String.valueOf(articleId));
    }

    private String generateKey(LocalDateTime time) {
        return generateKey(TIME_FORMATTER.format(time));
    }

    private String generateKey(String dateStr) {
        return KEY_FORMAT.formatted(dateStr);
    }

    public List<Long> readAll(String dateStr) {
        return redisTemplate.opsForZSet()
                // 0~ n-1 순위 까지 역순으로 반환
                .reverseRangeWithScores(generateKey(dateStr), 0, -1).stream()
                // 최종 연산이 호출되었을때만, 중간 과정의 요소를 확인
                .peek(tuple->log.info("[HotArticleListRepository.readAll] articleId={}, score={}", tuple.getValue(), tuple.getScore()))
                .map(ZSetOperations.TypedTuple::getValue)
                .map(Long::valueOf)
                .toList();
    }
}
