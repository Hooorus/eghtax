package cn.calendo.controller;

import cn.calendo.common.R;
import cn.calendo.dto.SetmealDto;
import cn.calendo.pojo.Category;
import cn.calendo.pojo.Setmeal;
import cn.calendo.service.SetmealService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 接收并保存新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> saveMeal(@RequestBody SetmealDto setmealDto) {
        setmealService.saveNewMeal(setmealDto);
        return R.success("新套餐添加成功！");
    }

    /**
     * 套餐管理页的分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        Page mealPage = setmealService.mealPage(page, pageSize, name);
        return R.success(mealPage);
    }

    /**
     * 套餐管理页的删除套餐
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(String id) {
        setmealService.deleteMeal(id);
        return R.success("套餐删除成功");
    }

    /**
     * 修改起售/停售状态
     *
     * @param setmeal
     * @return
     */
    @PutMapping("/status")
    public R<String> updateStatus(Setmeal setmeal) {
        setmealService.updateStatus(setmeal);
        return R.success("状态修改成功");
    }

    /**
     * 根据id查询菜品，套餐，回显到修改套餐上
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getMeal(@PathVariable Long id) {
        SetmealDto mealDish = setmealService.getMealDish(id);
        return R.success(mealDish);
    }

    /**
     * 修改套餐与菜品
     *
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> putSetmeal(@RequestBody SetmealDto setmealDto) {
        setmealService.updateSetmealWithDish(setmealDto);
        return R.success("成功修改套餐");
    }

    /**
     * get front page meal's list
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> getMealList(Setmeal setmeal) {
        List<Setmeal> mealList = setmealService.getMealList(setmeal);
        return R.success(mealList);
    }

    @GetMapping("/dish/{id}")
    public R<SetmealDto> getMeal2Order(@PathVariable Long id) {
        SetmealDto mealDish = setmealService.getMealDish(id);
        return R.success(mealDish);
    }
}
