package pneum4.board.articleread.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ViewClientTest {
    @Autowired
    ViewClient viewClient;

    @Test
    void readCacheableTest() throws InterruptedException {
        Long articleId = 1L;

        long count1 = viewClient.count(articleId); // 로그 출력
        long count2 = viewClient.count(articleId); // 미출력
        long count3 = viewClient.count(articleId); // 미출력

        assertEquals(count1, count2);
        assertEquals(count1, count3);

//        Thread.sleep(3000);
        TimeUnit.SECONDS.sleep(3);

        long count4 = viewClient.count(articleId); // 로그 출력
    }

    @Test
    void readCacheableMultiThreadTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        viewClient.count(1L); // init cache

        for(int i=0;i <5; i++) {
            CountDownLatch latch = new CountDownLatch(5);
            for(int j=0;j<5;j++) {
                executorService.submit(() -> {
                    viewClient.count(1L);
                    latch.countDown();
                });
            }
            latch.await();
            TimeUnit.SECONDS.sleep(2);
            System.out.println("=== cache expired ===");
        }
    }
}