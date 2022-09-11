package cn.calendo.service;

import cn.calendo.pojo.Category;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category> {

    //按id删除分类
    void removeCategory(Long id);

    //管理页分页查询
    Page categoryPage(Integer page, Integer pageSize);

    //dish在菜品管理页面新增菜品时根据条件来查询分类数据
    List<Category> addDishList(Category category);
}
