package pneum4.board.view.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Article-view Service API",
                version = "1.0",
                description = "게시글 조회수를 위한 API 명세서"
        )
)
public class SwaggerConfig {
}
