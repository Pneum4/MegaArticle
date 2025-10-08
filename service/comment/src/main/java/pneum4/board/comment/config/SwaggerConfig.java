package pneum4.board.comment.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Article-comment Service API",
                version = "1.0",
                description = "게시글의 댓글 서비스 API 명세서"
        )
)
public class SwaggerConfig {
}
