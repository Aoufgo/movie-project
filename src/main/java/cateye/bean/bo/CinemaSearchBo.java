package cateye.bean.bo;

/**
 * 影院查询业务
 */
public class CinemaSearchBo extends BaseSearchBo{
    /**
     * 筛选下级地区条件,父级地区编号
     */
    private Integer parentId;
    /**
     * 选中的品牌编号
     */
    private Integer brandId;
    /**
     * 选中的地区名称
     */
    private String chinaName;
    /**
     * 选中的放映厅类型编号
     */
    private Integer specialHallId;
    /**
     * 选中的影院服务编号
     */
    private Integer serviceId;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getChinaName() {
        return chinaName;
    }

    public void setChinaName(String chinaName) {
        this.chinaName = chinaName;
    }

    public Integer getSpecialHallId() {
        return specialHallId;
    }

    public void setSpecialHallId(Integer specialHallId) {
        this.specialHallId = specialHallId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
}
