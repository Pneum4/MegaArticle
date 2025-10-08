package pneum4.board.articleread.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pneum4.board.articleread.cache.OptimizedCacheable;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewClient {
    private RestClient restClient;
    @Value("${endpoints.pneum4-board-view-service.url}")
    private String viewServiceUrl;

    @PostConstruct
    public void initRestClient() {
        restClient = RestClient.create(viewServiceUrl);
    }

    // 일단 redis 캐시 1초
    // 레디스 캐시가 있으면 count호출 안함, 레디스 데이터 없으면 count 메서드 호출
    // count 메서드 호출 시, 레디스 캐시에 1초 동안 저장 + 값 반환
    // @Cacheable(value = "articleViewCount", key = "#articleId")
    // 레디스 키값 : articleViewCount::articleId(ex. articleViewCount::123)
    @OptimizedCacheable(type = "articleViewCount", ttlSeconds = 1)
    public long count(Long articleId) { // joinPoint.getArgs로 key값으로 articleId 사용
        log.info("[ViewClient.count] articleId={}", articleId);
        try {
            return restClient.get()
                    .uri("/v1/article-views/articles/{articleId}/count", articleId)
                    .retrieve()
                    .body(Long.class);
        } catch (Exception e) {
            log.error("[ViewClient.count] articleId={}", articleId, e);
            return 0;
        }
    }

}
