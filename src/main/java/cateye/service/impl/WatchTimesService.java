package cateye.service.impl;

import cateye.bean.po.WatchTimes;
import cateye.es.EsResponse;
import cateye.es.EsUtils;
import cateye.mapper.WatchTimesMapper;
import cateye.service.IWatchTimeService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WatchTimesService implements IWatchTimeService {
    @Resource
    private EsUtils esUtils;


    @Override
    public WatchTimes selectOne(String watchTimesId) {
        // 从es中获取场次数据,用于数据分析
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("wt_id",watchTimesId));
        searchSourceBuilder.query(boolQueryBuilder);
        EsResponse<WatchTimes> watchTimesEsResponse = esUtils.search("watch_times", searchSourceBuilder, WatchTimes.class);
        return watchTimesEsResponse.getResultCount() == 0 ? null : watchTimesEsResponse.getData().get(0);


    }
}
