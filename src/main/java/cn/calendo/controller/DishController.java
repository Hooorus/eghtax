package cn.calendo.controller;

import cn.calendo.common.R;
import cn.calendo.dto.DishDto;
import cn.calendo.pojo.Dish;
import cn.calendo.service.DishService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> saveDish(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        //操作两张表了，去service里面扩展以下方法
        dishService.saveWithFlavor(dishDto);
        return R.success("菜品新增成功");
    }

    /**
     * 分页查询菜品（需要BU双表查询）
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        Page dishPage = dishService.page(page, pageSize, name);
        return R.success(dishPage);
    }

    /**
     * 根据id查询菜品信息和对应口味信息回显到修改套餐上
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getDish(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> putDish(@RequestBody DishDto dishDto) {
        //操作两张表了，去service里面扩展以下方法
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功");
    }

    /**
     * 删除/批量删除菜品
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteDish(String id) {
        dishService.deleteDish(id);
        return R.success("菜品删除成功");
    }

    /**
     * 启用/停用
     *
     * @param dish
     * @return
     */
    @PutMapping("/status")
    public R<String> updateStatus(Dish dish) {
        dishService.updateStatus(dish);
        return R.success("状态修改成功");
    }

    /**
     * 添加套餐内添加菜品里每种菜品的分类
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> getDishList(Dish dish) {
        List<DishDto> dishes = dishService.selectDishList(dish);
        return R.success(dishes);
    }

    /**
     * 添加套餐内添加菜品里每种菜品的分类
     *
     * @param name
     * @return
     */
    @GetMapping("/qry")
    public R<List<Dish>> qryDishList(String name) {
        List<Dish> dishes = dishService.queryDishListByKey(name);
        return R.success(dishes);
    }
}
