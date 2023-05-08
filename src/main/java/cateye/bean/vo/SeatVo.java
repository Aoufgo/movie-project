package cateye.bean.vo;

/**
 * 座位视图模型类
 */
public class SeatVo {
    /**
     * 座位编号
     */
    private String site_no;
    /**
     * 行
     */
    private String site_row;
    /**
     * 列
     */
    private String  site_column;
    /**
     * 状态
     */
    private String site_state;

    public String getSite_no() {
        return site_no;
    }

    public void setSite_no(String site_no) {
        this.site_no = site_no;
    }

    public String getSite_row() {
        return site_row;
    }

    public void setSite_row(String site_row) {
        this.site_row = site_row;
    }

    public String getSite_column() {
        return site_column;
    }

    public void setSite_column(String site_column) {
        this.site_column = site_column;
    }

    public String getSite_state() {
        return site_state;
    }

    public void setSite_state(String site_state) {
        this.site_state = site_state;
    }
}
