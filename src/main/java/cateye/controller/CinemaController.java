package cateye.controller;

import cateye.bean.bo.CinemaSearchBo;
import cateye.bean.dto.CinemaDTO;
import cateye.bean.dto.CinemaListDTO;
import cateye.response.ResultResponse;
import cateye.service.impl.BrandService;
import cateye.service.impl.ChinaService;
import cateye.service.impl.CinemaService;
import cateye.service.impl.SpecialHallService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/cinema")
public class CinemaController {
    @Resource
    private BrandService brandService;
    @Resource
    private ChinaService chinaService;
    @Resource
    private SpecialHallService specialHallService;
    @Resource
    private CinemaService cinemaService;
    @PostMapping("/_select")
    public Object cinemaList(CinemaSearchBo cinemaSearchBo){
        CinemaListDTO cinemaListDTO = new CinemaListDTO();
        cinemaListDTO.setBrandList(brandService.selectAll());
        cinemaListDTO.setChinaList(chinaService.selectListByParentId(cinemaSearchBo.getParentId()));
        cinemaListDTO.setSpecialHallList(specialHallService.selectAll());
        cinemaListDTO.setCinemaList(cinemaService.selectList(cinemaSearchBo));
        cinemaListDTO.setCinemaSearchBo(cinemaSearchBo);
        return ResultResponse.success(cinemaListDTO);

    }
    @GetMapping("/{id}")
    public Object cinema(@PathVariable Integer id){
        CinemaDTO cinemaDTO = new CinemaDTO();
        // 获取影院信息
        cinemaDTO.setCinema(cinemaService.selectOne(id));
        return ResultResponse.success(cinemaDTO);

    }

}
