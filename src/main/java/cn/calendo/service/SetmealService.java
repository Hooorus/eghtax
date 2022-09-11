package cn.calendo.service;

import cn.calendo.dto.SetmealDto;
import cn.calendo.pojo.Setmeal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;


public interface SetmealService extends IService<Setmeal> {

    //套餐页面分页查询（BU双页查询）
    Page mealPage(Integer page,Integer pageSize,String name);

    //新增套餐
    void saveNewMeal(SetmealDto setmealDto);

    //修改起售/停售状态
    void updateStatus(Setmeal setmeal);

    //回显菜品
    SetmealDto getMealDish(Long id);

    //修改套餐
    void updateSetmealWithDish(SetmealDto setmealDto);

    //删除指定套餐
    void deleteMeal(String id);

    //get front page meal's list
    List<Setmeal> getMealList(Setmeal setmeal);

}
