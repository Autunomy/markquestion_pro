package com.hty.markquestion.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hty.markquestion.pojo.Advice;
import com.hty.markquestion.pojo.Question;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdviceMapper extends BaseMapper<Advice> {

    /***
     * 按照推荐名称搜索
     * @return
     */
    public List<Advice> searchAdvice(@Param("search") String search,
                                         @Param("currentPage") Integer currentPage,
                                         @Param("pageSize") Integer pageSize);

    /***
     * 统计搜索出来的条数
     */
    public Integer searchCount(@Param("search") String search);
}
