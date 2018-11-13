package com.caozhihu.tmall.mapper;

import com.caozhihu.tmall.pojo.Category;
import com.caozhihu.tmall.pojo.CategoryExample;
import java.util.List;

public interface CategoryMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
}