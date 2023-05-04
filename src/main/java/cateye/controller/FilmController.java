package cateye.controller;

import cateye.bean.bo.FilmSearchBo;
import cateye.response.ResultResponse;
import cateye.service.impl.CategoryService;
import cateye.service.impl.CommentService;
import cateye.service.impl.FilmRegionService;
import cateye.service.impl.FilmService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/film")
public class FilmController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private FilmRegionService filmRegionService;
    @Resource
    private CommentService commentService;
    @Resource
    private FilmService filmService;
    /**
     * 影片列表接口
     * @return 响应
     */
    @PostMapping("/_select")
    public Object filmList(FilmSearchBo filmSearchBo){
        Map<String,Object> data = new HashMap<>(8);
        // 获取类型列表
        data.put("categoryList",categoryService.selectAll());
        // 获取拍摄地列表
        data.put("regionList", filmRegionService.selectAll());
        // 获取影片列表
        data.put("filmList", filmService.selectList(filmSearchBo));
        // 查询条件
        data.put("filmSearchBo",filmSearchBo);
        return ResultResponse.success(data);
    }

    /**
     * 影片详情
     * @param id id
     * @return 响应报文
     */
    @GetMapping("/{id}")
    public Object film(@PathVariable Integer id){
        Map<String,Object> data = new HashMap<>(8);
        // 影片详情
        data.put("film",filmService.selectOne(id));
        // 评论列表
        data.put("commentList",commentService.selectListByFilmId(id));
        return ResultResponse.success(data);
    }

}
