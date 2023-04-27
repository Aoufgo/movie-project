package cateye.aop.annotation;

import java.lang.annotation.*;

/**
 * Redis缓存策略
 * */
@Documented
@Target( ElementType.METHOD )
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {
    long duration() default 0;
}
