package pneum4.board.common.outboxmessagerelay;

import lombok.Getter;

import java.util.List;
import java.util.stream.LongStream;

@Getter
public class AssignedShard {
    // 어플리케이션에 할당된 샤드 번호 리스트
    private List<Long> shards;

    // 코디네이터가 실행중인 appId의 리스트를 넘겨준다
    public static AssignedShard of(String appId, List<String> appIds, long shardCount ) { // 샤드 총 개수
        AssignedShard assignedShard = new AssignedShard();
        assignedShard.shards = assign(appId, appIds, shardCount);
        return assignedShard;
    }

    private static List<Long> assign(String appId, List<String> appIds, long shardCount) {
        // 4shard 2app이면 appIndex는 0,1 2개이고
        // shard는 0~1, 2~3 이다
        int appIndex = findAppIndex(appId, appIds);
        if (appIndex == -1) {
            return List.of();
        }

        long start = appIndex * shardCount / appIds.size();
        long end = (appIndex + 1) * shardCount / appIds.size() - 1;

        return LongStream.rangeClosed(start, end).boxed().toList();
    }

    private static int findAppIndex(String appId, List<String> appIds) {
        for (int i=0; i < appIds.size(); i++) {
            if (appIds.get(i).equals(appId)) {
                return i;
            }
        }
        return -1;
    }
}
