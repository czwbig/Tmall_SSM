package com.caozhihu.tmall.service;

import com.caozhihu.tmall.pojo.Category;
import com.caozhihu.tmall.util.Page;

import java.util.List;

public interface CategoryService {

    List<Category> list();
    void add(Category category);
    void delete(int id);

    Category get(int id);

    void update(Category category);
}
