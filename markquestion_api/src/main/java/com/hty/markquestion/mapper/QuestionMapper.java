package com.hty.markquestion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hty.markquestion.pojo.Question;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMapper extends BaseMapper<Question> {

    /***
     * 搜索题解 按照 id  题目名称 题目来源  作者查询
     * @return
     */
    public List<Question> searchQuestion(@Param("search") String search,
                                         @Param("currentPage") Integer currentPage,
                                         @Param("pageSize") Integer pageSize);

    /***
     * 统计搜索出来的条数
     */
    public Integer searchCount(@Param("search") String search);
}
