package cateye.service.impl;

import cateye.bean.po.Category;
import cateye.mapper.CategoryMapper;
import cateye.service.ICategoryService;
import cateye.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService implements ICategoryService {
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public List<Category> selectAll() {
        // 生成缓存的key的规则
        // key的载荷数据
        Map<String,Object> keyPayload = new HashMap<>(8);
        // 载荷类签名
        keyPayload.put( "class" , "cateye.service.impl.CategoryService" );
        // 载荷方法名
        keyPayload.put( "method" , "selectAll" );
        // 载荷参数列表
        keyPayload.put( "params" , new Object[]{} );
        // 将 载荷数据 序列化成 json 字符串 作为 key
        String key = JSON.toJSONString( keyPayload );
        //////  -- 判断缓存是否命中 -- 判断Redis中是否存在这个key  ////////////////////////////////////////////////////////////
        if( redisUtils.hasKey( key ) ){
            System.out.println( "==> Redis 缓存 => 缓存命中。直接返回缓存数据。" );
            return (List<Category>)redisUtils.get( key );
        }
        //////  -- 缓存未命中 -- 去数据库查询数据 --   ///////////////////////////////////////////////////////////////////////
        System.out.println( "==> Redis 缓存 => 缓存未命中。" );
        System.out.println( "==> Redis 缓存 => 去数据库查询数据。" );
        // 去数据库查询category数据表中的所有记录
        // sql => select * from `category`
        List<Category> categoryList  = categoryMapper.selectList( null );
        // => select * from `category`
        //////  -- 将数据库查询到的数据 生成缓存 --  存入到Redis中   ///////////////////////////////////////////////////////////
        redisUtils.set(key, categoryList, 60 * 60);
        System.out.println( "==> Redis 缓存 => 将数据库中查询到的数据，生成缓存。" );
        //////  -- 返回数据库查询到的数据  --  //////////////////////////////////////////////////////////////////////////////
        return categoryList;
    }
}
