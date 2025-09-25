package pneum4.board.comment.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pneum4.board.common.snowflake.Snowflake;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;
import pneum4.board.comment.CommentApplication;
import pneum4.board.comment.entity.Comment;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(classes = CommentApplication.class)
public class DataInitializer {
    static final int BULK_INSERT_SIZE = 2000;
    static final int EXECUTE_COUNT = 6000;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired 
    TransactionTemplate transactionTemplate;
    Snowflake snowflake = new Snowflake();
    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    @Test
    void initialize() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < EXECUTE_COUNT; ++i) {
            executorService.submit(() -> {
                insert();
                System.out.println("latch.getCount() = " + latch.getCount());
                latch.countDown();
            });
        }
        latch.await();
        executorService.shutdown();
    }

    void insert() {
        transactionTemplate.executeWithoutResult(status -> {
            Comment prev = null;
            for (int i = 0; i < BULK_INSERT_SIZE; i++) {
                Comment comment = Comment.create(
                        snowflake.nextId(),
                        "comment content" + i,
                        i%2==0? null : prev.getCommentId(),
                        1L,
                        1L
                );
                prev = comment;

                entityManager.persist(comment);
            }
        });
    }
}
