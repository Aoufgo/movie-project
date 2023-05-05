package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.bo.CinemaSearchBo;
import cateye.bean.po.Cinema;
import cateye.es.EsResponse;
import cateye.es.EsUtils;
import cateye.service.ICinemaService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CinemaService implements ICinemaService {
    @Resource
    private EsUtils esUtils;
    @Override
    @RedisCache(duration = 60 * 60 * 24)
    public List<Cinema> selectList(CinemaSearchBo cinemaSearchBo) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 筛选品牌
        if (cinemaSearchBo.getBrandId() != null) {
            boolQueryBuilder.must(QueryBuilders.termQuery("brand_id", cinemaSearchBo.getBrandId()));
        }
        // 筛选行政区
        if (cinemaSearchBo.getChinaName() != null && !cinemaSearchBo.getChinaName().isBlank()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("cma_address", cinemaSearchBo.getChinaName()));
        }
        // 筛选放映厅类型
        if (cinemaSearchBo.getSpecialHallId() != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("cma_halls_query", cinemaSearchBo.getSpecialHallId()));
        }
        // 筛选服务类型
        if (cinemaSearchBo.getServiceId() != null){
            boolQueryBuilder.must(QueryBuilders.matchQuery("cma_service_query",cinemaSearchBo.getServiceId()));
        }
        // 筛选影院名
        if (cinemaSearchBo.getKeyword() != null && !cinemaSearchBo.getKeyword().isBlank()){
            boolQueryBuilder.must(QueryBuilders.matchQuery("cma_name",cinemaSearchBo.getKeyword()));
        }
        // 将逻辑查询构建器加到资源管理器中
        searchSourceBuilder.query(boolQueryBuilder);
        // 排序
        if (cinemaSearchBo.getOrderColumn() != null && !cinemaSearchBo.getOrderColumn().isBlank()){
            searchSourceBuilder.sort(cinemaSearchBo.getOrderColumn(),
                    cinemaSearchBo.getOrderType() != null && "desc".equalsIgnoreCase(cinemaSearchBo.getOrderType()) ?  SortOrder.DESC : SortOrder.ASC);
        }
        // 分页
        searchSourceBuilder.from((int)(long)cinemaSearchBo.getStartIndex());
        searchSourceBuilder.size((int)(long)cinemaSearchBo.getPageSize());
        // 查询
        EsResponse<Cinema> cinemaEsResponse = esUtils.search("cinema", searchSourceBuilder, Cinema.class);
        // 设置总记录数
        cinemaSearchBo.setResultCount(cinemaEsResponse.getResultCount());
        return cinemaEsResponse.getData();
    }

    @Override
    public Cinema selectOne(Integer cinemaId) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("cma_id",cinemaId));
        searchSourceBuilder.query(boolQueryBuilder);
        EsResponse<Cinema> cinemaEsResponse = esUtils.search("cinema", searchSourceBuilder, Cinema.class);
        if (cinemaEsResponse.getResultCount()>0){
            return cinemaEsResponse.getData().get(0);
        }
        return null;
    }
}
