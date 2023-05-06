package cateye.service.impl;

import cateye.aop.annotation.RedisCache;
import cateye.bean.bo.FilmSearchBo;
import cateye.bean.po.CinemaFilmRel;
import cateye.bean.po.Film;
import cateye.bean.po.WatchTimes;
import cateye.bean.vo.FilmVo;
import cateye.bean.vo.WatchDateVo;
import cateye.es.EsResponse;
import cateye.es.EsUtils;
import cateye.mapper.CinemaFilmRelMapper;
import cateye.mapper.FilmMapper;
import cateye.mapper.WatchTimesMapper;
import cateye.service.IFilmService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService implements IFilmService {
    @Resource
    private EsUtils esUtils;
    @Resource
    private CinemaFilmRelMapper cinemaFilmRelMapper;
    @Resource
    private FilmMapper filmMapper;
    @Resource
    private WatchTimesMapper watchTimesMapper;
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

    @Override
    @RedisCache(duration = 60 * 5)
    public List<FilmVo> selectFilmVoListByCinemaId(Integer cinemaId) {
        List<FilmVo> filmVoList = new ArrayList<>();
        // 根据影院id获取电影列表
        LambdaQueryWrapper<CinemaFilmRel> filmLambdaQueryWrapper = Wrappers.lambdaQuery();
        filmLambdaQueryWrapper.eq(CinemaFilmRel::getCmaId,cinemaId);
        List<CinemaFilmRel> cinemaFilmRelList = cinemaFilmRelMapper.selectList(filmLambdaQueryWrapper);
        // 根据关联表获取filmId
        for (CinemaFilmRel cinemaFilmRel : cinemaFilmRelList) {
            Integer filmId = cinemaFilmRel.getFilmId();
            // filmVo
            FilmVo filmVo = new FilmVo();
            // 获取film信息
            Film film = filmMapper.selectById(filmId);
            // 根据filmId和cinemaId获取排片表
            LambdaQueryWrapper<WatchTimes> watchTimesWrapper = Wrappers.lambdaQuery();
            watchTimesWrapper.eq(WatchTimes::getCmaId,cinemaId);
            watchTimesWrapper.eq(WatchTimes::getFilmId,filmId);
            List<WatchTimes> watchTimesList = watchTimesMapper.selectList(watchTimesWrapper);
            // 实例化watchDate
            List<WatchDateVo> watchDateVoList = new ArrayList<>();
            for (WatchTimes watchTimes : watchTimesList) {
                // 设置flag是否存在该日期
                boolean isNewDay = true;
                for (WatchDateVo watchDateVo : watchDateVoList) {
                    if (watchTimes.getWdDate().equals(watchDateVo.getDate())){
                         watchDateVo.getWatchTimeList().add(watchTimes);
                         isNewDay = false;
                         break;
                    }
                }
                if (isNewDay){
                    WatchDateVo watchDateVo = new WatchDateVo();
                    watchDateVo.setDate(watchTimes.getWdDate());
                    // 实例化一个WatchTimeList
                    watchDateVo.setWatchTimeList(new ArrayList<>());
                    watchDateVo.getWatchTimeList().add(watchTimes);
                    // 添加vo
                    watchDateVoList.add(watchDateVo);
                }
            }
            filmVo.setFilm(film);
            filmVo.setWatchDateVoList(watchDateVoList);
            filmVoList.add(filmVo);

        }
        return filmVoList;
    }
}
