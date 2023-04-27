package cateye.aop;

import cateye.aop.annotation.RedisCache;
import cateye.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis缓存策略 切面类
 * 代理对象
 * */
@Component
@Aspect
public class RedisAspect {
    Logger logger = LoggerFactory.getLogger(RedisAspect.class);

    @Resource
    private RedisUtils redisUtil;

    /**
     * 环绕增强器
     * @param joinPoint 连接点，通过连接点，可以获取到代理目标方法的信息
     * @param redisCache 注解验证，含有该注解的方法才会被代理
     * @return 代理对象的返回值
     * */
    @Around( value = "@annotation(redisCache)" )
    public Object proceedAround( ProceedingJoinPoint joinPoint , RedisCache redisCache )
            throws Throwable {
        logger.debug( "==> {} => 开启Redis缓存策略",joinPoint.getSignature());
        ////////  --  缓存Key的生成规则  --  ////////
        // 缓存Key的颗粒度：类签名+方法名+参数
        Map<String,Object> keyPayload = new HashMap<>(8);
        keyPayload.put( "class" , joinPoint.getSignature().getDeclaringTypeName() );
        keyPayload.put( "method" , joinPoint.getSignature().getName() );
        keyPayload.put( "params" , joinPoint.getArgs() );
        // 将缓存key的载荷数据 序列化为 json 字符串 ，作为缓存的key
        String key = JSON.toJSONString( keyPayload );
        ////////  --  判断缓存是否命中  --  判断Redis中是否含有该key  ////////
        if( redisUtil.hasKey( key ) ){
            logger.info( "==> {} => 缓存命中，直接返回缓存数据" ,joinPoint.getSignature());
            // 获取 Redis 中 该 key 的 value 值
            Object cacheValue = "null".equals( redisUtil.get( key ).toString() ) ?
                    null :
                    redisUtil.get( key );
            // 直接返回缓存数据
            return cacheValue;
        }
        ////////  --  缓存未命中  --  ////////
        logger.info( "==> {} => 缓存未命中",joinPoint.getSignature());
        // 缓存失效后,通过分布式锁来控制读写缓存的线程数,某个key只允许一个线程访问,其他线程等待
        if( redisUtil.setnx( "Mutrix - " + key , 5 ) ) {
            logger.debug("==> {} => 争夺到分布式锁",joinPoint.getSignature());
            logger.debug("==> {} => 去数据源查询数据",joinPoint.getSignature());
            ////////  --  调用代理目标方法  --  去 数据源 查询数据  ////////
            Object data = joinPoint.proceed();
            ////////  --  生成缓存  --  将 数据源中查询到的数据 添加到 Redis缓存中  ////////
            logger.debug("==> {} => 生成缓存",joinPoint.getSignature());
            if (data == null) {
                redisUtil.set(key, "null", 5);
            } else {
                redisUtil.set(key, data,
                        // 缓存的有效期 基底时间 + 随机部分 ，为了防止缓存雪崩
                        redisCache.duration() + (long) (Math.random() * redisCache.duration() / 10)
                );
            }
            ////////  --  返回数据源中查询到的数据  --  ////////
            return data;
        }else {
            // 没有 争夺到分布式锁 等待一段时间 重新 判断缓存是否命中
            logger.debug( "==> {} => 没有争夺到分布式锁",joinPoint.getSignature() );
            // 循环延时等待，直到Redis缓存成功生成
            while( true ) {
                // 当前线程睡眠
                Thread.sleep(100);
                // 重新判断缓存是否命中
                if (redisUtil.hasKey(key)) {
                    // 获取 Redis 中 该 key 的 value 值
                    Object cacheValue = "null".equals(redisUtil.get(key).toString()) ?
                            null :
                            redisUtil.get(key);
                    // 直接返回缓存数据
                    return cacheValue;
                }
            }
        }
    }
}
