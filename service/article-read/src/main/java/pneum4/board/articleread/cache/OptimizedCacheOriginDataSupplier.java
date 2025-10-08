package pneum4.board.articleread.cache;

@FunctionalInterface
public interface OptimizedCacheOriginDataSupplier<T> {
    T get() throws Throwable; // 예외 + error 객체를 전부 던질 수 있음
    // error는 개발자가 대처불가능한 심각한 오류고, excpeption은 대처가능한 오류
}
