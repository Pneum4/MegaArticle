package pneum4.board.common.snowflake;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

class SnowflakeTest {
	Snowflake snowflake = new Snowflake();

	@Test
	void nextIdTest() throws ExecutionException, InterruptedException {
		// given
		ExecutorService executorService = Executors.newFixedThreadPool(10); //thread_pool 10개 생성
		List<Future<List<Long>>> futures = new ArrayList<>();
		int repeatCount = 1000; // 1000번 반복
		int idCount = 1000; // 한번에 1000개

		// when
		for (int i = 0; i < repeatCount; i++) {
			futures.add(executorService.submit(() -> generateIdList(snowflake, idCount)));
		}

		// then
		List<Long> result = new ArrayList<>();
		for (Future<List<Long>> future : futures) {
			List<Long> idList = future.get();
			for (int i = 1; i < idList.size(); i++) { // 오름차순인지 확인
				assertThat(idList.get(i)).isGreaterThan(idList.get(i - 1));
			}
			result.addAll(idList);
		}
		// 생성개수 100만개 확인
		assertThat(result.stream().distinct().count()).isEqualTo(repeatCount * idCount);

		executorService.shutdown();
	}

	List<Long> generateIdList(Snowflake snowflake, int count) {
		List<Long> idList = new ArrayList<>();
		while (count-- > 0) {
			idList.add(snowflake.nextId());
		}
		return idList;
	}

	//100만개 생성속도 측정
	@Test
	void nextIdPerformanceTest() throws InterruptedException {
		// given
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		int repeatCount = 1000;
		int idCount = 1000;
		CountDownLatch latch = new CountDownLatch(repeatCount);

		// when
		long start = System.nanoTime();
		for (int i = 0; i < repeatCount; i++) {
			executorService.submit(() -> {
				generateIdList(snowflake, idCount);
				latch.countDown();
			});
		}

		latch.await();

		long end = System.nanoTime();
		System.out.println("times = %s ms".formatted((end - start) / 1_000_000));

		executorService.shutdown();
	}
}