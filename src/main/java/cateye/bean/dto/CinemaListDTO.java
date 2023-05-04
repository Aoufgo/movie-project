package cateye.bean.dto;

import cateye.bean.po.Brand;
import cateye.bean.po.China;
import cateye.bean.po.Cinema;
import cateye.bean.po.SpecialHall;
import java.util.List;

public class CinemaListDTO {
    private List<Brand> brandList;              // 品牌列表
    private List<China> chinaList;              // 地区列表
    private List<SpecialHall> specialHallList;  // 放映厅类型列表
    private List<Cinema> cinemaList;            // 影院列表

    // getters and setters
    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public List<China> getChinaList() {
        return chinaList;
    }

    public void setChinaList(List<China> chinaList) {
        this.chinaList = chinaList;
    }

    public List<SpecialHall> getSpecialHallList() {
        return specialHallList;
    }

    public void setSpecialHallList(List<SpecialHall> specialHallList) {
        this.specialHallList = specialHallList;
    }

    public List<Cinema> getCinemaList() {
        return cinemaList;
    }

    public void setCinemaList(List<Cinema> cinemaList) {
        this.cinemaList = cinemaList;
    }
}
