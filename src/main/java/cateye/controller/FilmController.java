package cateye.controller;

import cateye.response.ResultResponse;
import cateye.service.impl.CategoryService;
import cateye.service.impl.FilmRegionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    /**
     * 影片列表接口
     * @return 响应
     */
    @PostMapping("/_select")
    public Object filmList(){
        Map<String,Object> data = new HashMap<>(8);
        // 获取类型列表
        data.put("categoryList",categoryService.selectAll());
        // 获取拍摄地列表
        data.put("regionList", filmRegionService.selectAll());
        // 获取影片列表
        data.put("filmList", null );
        return ResultResponse.success(data);
    }

}
