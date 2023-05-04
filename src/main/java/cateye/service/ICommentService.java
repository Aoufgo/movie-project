package cateye.service;

import cateye.bean.po.Comment;

import java.util.List;

public interface ICommentService {
    /**
     * 根据影片编号查询该影片的影评信息
     * @param filmId 要查询的影片编号
     * @return 影评实体模型对象集合
     * */
    List<Comment> selectListByFilmId( Integer filmId );
}
