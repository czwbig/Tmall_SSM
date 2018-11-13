package com.caozhihu.tmall.service;

import com.caozhihu.tmall.pojo.User;

import java.util.List;

public interface UserService {
    void add(User c);

    void delete(int id);

    void update(User u);

    User get(int id);

    List list();
}
