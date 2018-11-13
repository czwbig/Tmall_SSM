package com.caozhihu.tmall.controller;

import com.caozhihu.tmall.pojo.Category;
import com.caozhihu.tmall.pojo.Property;
import com.caozhihu.tmall.service.CategoryService;
import com.caozhihu.tmall.service.PropertyService;
import com.caozhihu.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class PropertyController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyService propertyService;

    @RequestMapping("admin_property_add")
    public String add(Model model, Property p) {
        propertyService.add(p);
        return "redirect:admin_property_list?cid=" + p.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id) {
        Property p = propertyService.get(id);
        propertyService.delete(id);
        return "redirect:admin_property_list?cid=" + p.getCid();
    }

    @RequestMapping("admin_property_edit")
    public String  edit(Model model, int id) {
        Property p = propertyService.get(id);
        Category c = categoryService.get(p.getCid());//跳转到编辑页面，编辑好了之后需要跳转到对应的Category的Property列表,到时候需要知道Property的category属性
        p.setCategory(c);//property对象时从数据库get的，其没有category字段，所以需要自己手动获取，并赋值给property
        model.addAttribute("p", p);
        return "admin/editProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property p) {
        propertyService.update(p);//更新操作只需要cid和name字段，这两个字段对应editProperty.jsp里面的表单
        return "redirect:admin_property_list?cid=" + p.getCid();
    }

    @RequestMapping("admin_property_list")
    public String list(int cid, Model model, Page page){
        Category c = categoryService.get(cid);
        PageHelper.offsetPage(page.getStart(), page.getCount());
        List<Property> ps = propertyService.list(cid);

        int total = (int) new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        page.setParam("&cid=" + c.getId());

        model.addAttribute("ps", ps);
        //ps集合是根据category对象查询出的，按理说ps里面的每个p都有category对象了，
        // 这里为什么还要单独设置c？因为后续面包屑导航会用到显示分类名称，方便取值
        model.addAttribute("c", c);
        model.addAttribute("page", page);

        return "admin/listProperty";

    }
}
