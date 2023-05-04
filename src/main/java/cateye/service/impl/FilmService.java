package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.bo.FilmSearchBo;
import cateye.bean.po.Film;
import cateye.es.EsResponse;
import cateye.es.EsUtils;
import cateye.service.IFilmService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FilmService implements IFilmService {
    @Resource
    private EsUtils esUtils;
    @Override
    @RedisCache(duration = 60*60)
    public List<Film> selectList(FilmSearchBo filmSearchBo) {
        // es资源查询构造器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 逻辑条件构造器
        // QueryBuilders.termQuery() 搜索条件完全相等
        // QueryBuilders.matchQuery() 使用分词器
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // boolQueryBuilder.must() 条件之间的逻辑关系为and
        // boolQueryBuilder.should() 条件之间的逻辑关系为or
        // 判断 影片类型 是否有筛选要求
        if( filmSearchBo.getCategoryId() != null ){
            // => and `film_cate_query` = filmSearchBo.getCateId()
            boolQueryBuilder.must( QueryBuilders.termQuery("film_cate_query" , filmSearchBo.getCategoryId() ) );
        }
        // 判断 影片拍摄地 是否有筛选要求
        if( filmSearchBo.getRegionId() != null ){
            // => and `film_region_query` = filmSearchBo.getFilmReId()
            boolQueryBuilder.must( QueryBuilders.termQuery("film_region_query" , filmSearchBo.getRegionId() ) );
        }
        // 判断 上映年代 是否有筛选要求
        if( filmSearchBo.getYear() != null && !filmSearchBo.getYear().isBlank() ){
            // => and `film_years` = filmSearchBo.getFilm_years()
            boolQueryBuilder.must( QueryBuilders.termQuery("film_years" , filmSearchBo.getYear() ) );
        }
        // 判断 是否搜索词 的 筛选要求
        if( filmSearchBo.getKeyword() != null && !filmSearchBo.getKeyword().isBlank() ){
            // => and `film_name` like '%filmSearchBo.getKeyword()%'
            boolQueryBuilder.must( QueryBuilders.matchQuery("film_name" , filmSearchBo.getKeyword() ) );
        }
        // 给 搜索建造者 添加 筛选条件
        searchSourceBuilder.query( boolQueryBuilder );
        // 搜索建造者 设置 排序条件 => order by 子句
        if( filmSearchBo.getOrderColumn() != null && !filmSearchBo.getOrderColumn().isBlank() ){
            searchSourceBuilder.sort(
                    filmSearchBo.getOrderColumn(),
                    filmSearchBo.getOrderType() != null && "desc".equalsIgnoreCase(filmSearchBo.getOrderType()) ?  SortOrder.DESC : SortOrder.ASC );
        }
        // 分页
        searchSourceBuilder.from((int)(long)filmSearchBo.getStartIndex());
        searchSourceBuilder.size((int)(long)filmSearchBo.getPageSize());
        // 使用 ElasticSearch 操作帮助类 发送查询 得到结果
        EsResponse<Film> esResponse = esUtils.search( "film" , searchSourceBuilder , Film.class );
        // 获取 EsResponse 对象中的 总记录数
        filmSearchBo.setResultCount( esResponse.getResultCount() );
        // 获取 EsResponse 对象中的 实体模型集合
        return esResponse.getData();
    }

    @Override
    public Film selectOne(Integer filmId) {
        // 使用es获取影片详情,为了分析用户行为
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("film_id",filmId));
        searchSourceBuilder.query(boolQueryBuilder);
        EsResponse<Film> esResponse = esUtils.search("film", searchSourceBuilder, Film.class);
        // 判断是否存在满足条件的数据
        if(esResponse.getResultCount()>0){
            return esResponse.getData().get(0);
        }
        return null;
    }
}
