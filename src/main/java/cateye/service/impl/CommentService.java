package cateye.service.impl;

import cateye.bean.po.Comment;
import cateye.es.EsResponse;
import cateye.es.EsUtils;
import cateye.service.ICommentService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class CommentService implements ICommentService {
    @Resource
    private EsUtils esUtils;
    @Override
    public List<Comment> selectListByFilmId(Integer filmId) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("film_id",filmId));
        searchSourceBuilder.query(boolQueryBuilder);
        EsResponse<Comment> commentEsResponse = esUtils.search("comment", searchSourceBuilder, Comment.class);
        return commentEsResponse.getData();


    }
}
