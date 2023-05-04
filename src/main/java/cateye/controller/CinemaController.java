package cateye.controller;

import cateye.bean.dto.CinemaListDTO;
import cateye.response.ResultResponse;
import cateye.service.impl.BrandService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/cinema")
public class CinemaController {
    @Resource
    private BrandService brandService;
    @PostMapping("/_select")
    public Object cinemaList(){
        CinemaListDTO cinemaListDTO = new CinemaListDTO();
        cinemaListDTO.setBrandList(brandService.selectAll());
        cinemaListDTO.setCinemaList(null);
        return ResultResponse.success(cinemaListDTO);

    }

}
