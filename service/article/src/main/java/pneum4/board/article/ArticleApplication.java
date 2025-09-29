package pneum4.board.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "pneum4.board")
@EnableJpaRepositories(basePackages = "pneum4.board")
// 이 파일 하위의 모든 등록된 컴포넌트를 스캔 -> @EnableScheduling이 활성화 -> 스케줄링 시작
@SpringBootApplication
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }
}
