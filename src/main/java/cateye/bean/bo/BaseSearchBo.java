package cateye.bean.bo;

/**
 * 搜索业务模型基础类
 */
/**
 * 搜索业务模型超类
 * */
public class BaseSearchBo {
    // -- 分页条件 --
    private Long currentPage = 1L;   // 当前页码         <= 请求参数
    private Long pageSize = 10L;      // 每页显示记录数    <= 请求参数
    private Long startIndex = 0L;    // 起始记录索引      <= ( currentPage - 1 ) * pageSize
    private Long resultCount;   // 总记录数         <= 数据源中查询出来
    private Long pageCount;     // 总页数          <= resultCount % pageSize == 0 ? resultCount / pageSize : resultCount / pageSize + 1

    // -- 排序条件 --
    private String orderColumn; // 排序列名        <= 请求参数
    private String orderType;   // 排序方式        <= 请求参数

    // -- 搜索关键字 --
    private String keyword;     // 搜索关键字      <= 请求参数

    // getters and setters
    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        // 设置 当前页码
        this.currentPage = currentPage;
        // 根据 当前页码、每页显示记录数 ，计算 起始记录索引
        setStartIndex((this.currentPage - 1) * this.pageSize);
    }

    public Long getPageSize() {
        return pageSize;
    }
    public void setPageSize(Long pageSize) {
        // 设置 每页显示记录数
        this.pageSize = pageSize;
        // 根据 当前页码、每页显示记录数 ，计算 起始记录索引
        setStartIndex((this.currentPage - 1) * this.pageSize);
    }

    public Long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Long startIndex) {
        this.startIndex = startIndex;
    }

    public Long getResultCount() {
        return resultCount;
    }

    public void setResultCount(Long resultCount) {
        // 设置 总记录数
        this.resultCount = resultCount;
        // 根据 总记录数、每页显示记录数 ，计算 总页数
        setPageCount(
                this.resultCount % this.pageSize == 0 ?
                        this.resultCount / this.pageSize :
                        this.resultCount / this.pageSize + 1);
    }

    public Long getPageCount() {
        return pageCount;
    }

    public void setPageCount(Long pageCount) {
        // 设置 总页数
        this.pageCount = pageCount;
        // 控制 当前页码 的 数据有效性
        if (this.currentPage > this.pageCount) {
            setCurrentPage(this.pageCount);
        }
        if (this.currentPage < 1) {
            setCurrentPage(1L);
        }
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}