package com.caozhihu.tmall.service.impl;

import com.caozhihu.tmall.mapper.ProductMapper;
import com.caozhihu.tmall.pojo.Category;
import com.caozhihu.tmall.pojo.Product;
import com.caozhihu.tmall.pojo.ProductExample;
import com.caozhihu.tmall.pojo.ProductImage;
import com.caozhihu.tmall.service.CategoryService;
import com.caozhihu.tmall.service.ProductImageService;
import com.caozhihu.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductImageService productImageService;

    @Override
    public void add(Product p) {
        int cid = p.getCid();
        Category c = categoryService.get(cid);
        p.setCategory(c);
        productMapper.insert(p);

    }

    @Override
    public void delete(int id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product p) {
        productMapper.updateByPrimaryKeySelective(p);
    }

    @Override
    public Product get(int id) {
        Product p = productMapper.selectByPrimaryKey(id);
        setCategory(p);
        setFirstProductImage(p);
        return p;
    }

    @Override
    public List list(int cid) {
        ProductExample example = new ProductExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        List<Product> result = productMapper.selectByExample(example);
        Category c = categoryService.get(cid);
        setCategory(result);
        setFirstProductImage(result);

        return result;
    }

    public void setCategory(List<Product> ps) {
        for (Product p : ps)
            setCategory(p);
    }

    public void setCategory(Product p) {
        int cid = p.getCid();
        Category c = categoryService.get(cid);
        p.setCategory(c);
    }

    @Override
    public void setFirstProductImage(Product p) {//根据pid和type取出productImage集合
        List<ProductImage> pis = productImageService.list(p.getId(), ProductImageService.type_single);
        if (!pis.isEmpty()) {
            ProductImage pi = pis.get(0);
            p.setFirstProductImage(pi);
        }
    }

    public void setFirstProductImage(List<Product> ps) {
        for (Product p : ps) {
            this.setFirstProductImage(p);
        }
    }
}
