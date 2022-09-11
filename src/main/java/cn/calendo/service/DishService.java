package cn.calendo.service;

import cn.calendo.dto.DishDto;
import cn.calendo.pojo.Dish;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DishService extends IService<Dish> {

    //分页查询菜品（需要BU双表查询）
    Page page(Integer page, Integer pageSize, String name);

    //新增菜品，同时插入菜品的口味数据（2张表）
    void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品的基本信息和对应口味信息
    DishDto getByIdWithFlavor(Long id);

    //修改菜品
    void updateWithFlavor(DishDto dishDto);

    //启用/停用菜品
    void updateStatus(Dish dish);

    //添加套餐内添加菜品里每种菜品的分类
    List<DishDto> selectDishList(Dish dish);

    //添加套餐内添加菜品的查询菜品
    List<Dish> queryDishListByKey(String name);

    //删除菜品
    void deleteDish(String id);

}
