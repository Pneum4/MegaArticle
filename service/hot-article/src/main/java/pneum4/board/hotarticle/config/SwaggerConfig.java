package pneum4.board.hotarticle.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Hot-article Service API",
                version = "1.0",
                description = "인기글 조회를 위한 서비스 API 명세서"
        )
)
public class SwaggerConfig {
}
