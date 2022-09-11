package cn.calendo.service.impl;

import cn.calendo.common.CustomException;
import cn.calendo.mapper.CategoryMapper;
import cn.calendo.pojo.Category;
import cn.calendo.pojo.Dish;
import cn.calendo.pojo.Setmeal;
import cn.calendo.service.CategoryService;
import cn.calendo.service.DishService;
import cn.calendo.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类管理实现类
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，但是删除之前需要判断
     *
     * @param id
     */
    @Override
    public void removeCategory(Long id) {
        //制造查询结果
        QueryWrapper<Dish> qw = new QueryWrapper<>();
        qw.eq("category_id", id);
        int dishCount = dishService.count(qw);
        //查询当前分类是否关联菜品，如果已经关联，那就抛出一个业务异常给运行异常控制器处理
        if (dishCount > 0) {
            throw new CustomException("当前分类下关联了菜品，无法删除");
        }
        //查询当前分类是否关联套餐，如果已经关联，那就抛出一个业务异常给运行异常控制器处理
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealService.count(lqw);
        if (setmealCount > 0) {
            throw new CustomException("当前分类下关联了套餐，无法删除");
        }
        //正常删除分类
        super.removeById(id);
    }

    /**
     * 管理页分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Page categoryPage(Integer page, Integer pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        QueryWrapper<Category> qw = new QueryWrapper<>();
        //添加排序条件
        qw.orderByAsc("sort");
        //分页查询
        return this.page(pageInfo, qw);
    }

    /**
     * dish在菜品管理页面新增菜品时根据条件来查询分类数据
     * @param category
     * @return
     */
    @Override
    public List<Category> addDishList(Category category) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(category.getType() != null, Category::getType, category.getType());
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        return this.list(lqw);
    }

}
