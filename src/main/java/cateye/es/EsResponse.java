package cateye.es;

import java.util.List;
public class EsResponse<T> {
    /**
     实体模型对象集合
     */
    private List<T> data;
    /**
     总命中数
     */
    private long resultCount;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getResultCount() {
        return resultCount;
    }

    public void setResultCount(long resultCount) {
        this.resultCount = resultCount;
    }
}
