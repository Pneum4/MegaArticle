package pneum4.board.articleread.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pneum4.board.common.dataserializer.DataSerializer;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@ToString
public class OptimizedCache {
    private String data;
    private LocalDateTime expiredAt;

    public static OptimizedCache of(Object data, Duration ttl) {
        OptimizedCache optimizedCache = new OptimizedCache();
        optimizedCache.data = DataSerializer.serialize(data);
        optimizedCache.expiredAt = LocalDateTime.now().plus(ttl);
        return optimizedCache;
    }

    // jackson은 is__ 형태를 getter로 인식해서 json으로 만들면 Expired 필드가 추가됨 -> 이거를 방지
    @JsonIgnore
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    public <T> T parseData(Class<T> dataType) {
        return DataSerializer.deserialize(data, dataType);
    }
}
