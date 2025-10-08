package pneum4.board.articleread.cache;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class OptimizedCacheAspect {
    private final OptimizedCacheManager optimizedCacheManager;

    @Around("@annotation(OptimizedCacheable)") // Pointcut 지정 (AOP를 어디에 넣을건지)
    //@Before("execution(* com.example.service.*.*(..))") 이렇게 패키지를 지정하고, 하위 모든 메서드에 적용도 가능
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable { // joinPoint: 실제 호출되는 메서드
        OptimizedCacheable cacheable = findAnnotation(joinPoint);
        return optimizedCacheManager.process(
                cacheable.type(),
                cacheable.ttlSeconds(),
                joinPoint.getArgs(), // 메서드 인자들
                findReturnType(joinPoint),
                () -> joinPoint.proceed()
        );
    }

    private OptimizedCacheable findAnnotation(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod().getAnnotation(OptimizedCacheable.class);
    }

    private Class<?> findReturnType(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getReturnType();
    }
}
