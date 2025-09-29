package pneum4.board.common.outboxmessagerelay;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {
//    WHERE o.shardKey = :shardKey
//    AND o.createdAt <= :from
//    ORDER BY o.createdAt ASC
    //그 뒤에 오는 파라미터의 이름은 신경 쓰지 않고, "순서"로 매핑합니다.
    List<Outbox> findAllByShardKeyAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
            Long shardKey,
            LocalDateTime from,
            Pageable pageable
    );
}
