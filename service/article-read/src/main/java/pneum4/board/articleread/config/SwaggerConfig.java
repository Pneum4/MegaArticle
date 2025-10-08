package pneum4.board.articleread.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Article-read Service API",
                version = "1.0",
                description = "빠른 게시글 조회를 위한 Query 서비스 API 명세서"
        )
)
public class SwaggerConfig {
}
