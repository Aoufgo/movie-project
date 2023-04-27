package cateye.bean.bo;

/**
 * 影片业务查询
 */
public class FilmSearchBo extends BaseSearchBo{
    private Integer categoryId;
    private Integer regionId;
    private String year;

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
