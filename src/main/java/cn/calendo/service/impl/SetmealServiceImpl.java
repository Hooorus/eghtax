package cn.calendo.service.impl;

import cn.calendo.common.CustomException;
import cn.calendo.dto.SetmealDto;
import cn.calendo.mapper.SetmealMapper;
import cn.calendo.pojo.Category;
import cn.calendo.pojo.Setmeal;
import cn.calendo.pojo.SetmealDish;
import cn.calendo.service.CategoryService;
import cn.calendo.service.SetmealDishService;
import cn.calendo.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐service实现类
 */

@Service
@Slf4j
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 套餐页面分页查询（BU双页查询）
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page mealPage(Integer page, Integer pageSize, String name) {
        //分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> stemealDtopage = new Page<>();
        //条件构造器
        QueryWrapper<Setmeal> qw = new QueryWrapper<>();
        //模糊查询
        qw.like(name != null, "name", name);
        //排序条件
        qw.orderByDesc("id").orderByDesc("category_id").orderByDesc("name");
        //执行分页查询
        this.page(pageInfo, qw);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, stemealDtopage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        stemealDtopage.setRecords(list);
        return stemealDtopage;
    }

    /**
     * 新增套餐，同时保存菜品与套餐的关联关系
     *
     * @param setmealDto
     */
    @Override
    public void saveNewMeal(SetmealDto setmealDto) {
        //保存套餐的基本信息到setmeal里
        this.save(setmealDto);
        //获取到setmeal的id
        Long mealId = setmealDto.getId();
        //获取到setmeal的相关菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //stream流设置相关菜品的dishId为setmeal的id
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(mealId);
            return item;
        }).collect(Collectors.toList());
        //保存套餐的菜品到setmeal_dish表里
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 修改起售/停售状态
     *
     * @param setmeal
     */
    @Override
    @CacheEvict(value = "mealListCache", allEntries = true)
    public void updateStatus(@NonNull Setmeal setmeal) {
        QueryWrapper<Setmeal> qw = new QueryWrapper<>();
        qw.eq("id", setmeal.getId());
        this.updateById(setmeal);
    }

    /**
     * 根据id查询菜品，套餐，分类，回显到修改套餐上
     *
     * @param id
     * @return
     */
    @Override
    public SetmealDto getMealDish(Long id) {
        //查询套餐信息
        Setmeal meal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(meal, setmealDto);
        //查询套餐的内含菜品信息，从setmeal_dish表里查询，得到的是一个list
        QueryWrapper<SetmealDish> qw = new QueryWrapper<>();
        qw.eq("setmeal_id", meal.getId());
        List<SetmealDish> setmealDishes = setmealDishService.list(qw);
        setmealDto.setSetmealDishes(setmealDishes);
        return setmealDto;
    }

    /**
     * 修改套餐与菜品
     *
     * @param setmealDto
     */
    @Override
    @CacheEvict(value = "mealListCache", allEntries = true)
    public void updateSetmealWithDish(SetmealDto setmealDto) {
        //更新stemeal表
        this.updateById(setmealDto);
        //删除旧setmeal_dish套餐内菜品数据
        QueryWrapper<SetmealDish> qw = new QueryWrapper<>();
        qw.eq("setmeal_id", setmealDto.getId());
        setmealDishService.remove(qw);
        //添加新setmeal_dish套餐内菜品数据
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 套餐管理页面根据id删除套餐
     *
     * @param id
     */
    @Override
    @CacheEvict(value = "mealListCache", allEntries = true)
    public void deleteMeal(@NonNull String id) {
        String[] splitId = id.split(",");
        //看看下面有没有菜品，能不能删除
        QueryWrapper<Setmeal> qw1 = new QueryWrapper<>();
        for (String cnt : splitId) {
            qw1.in("id", cnt);
            qw1.eq("status", 1);
        }
        int count = this.count(qw1);
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        for (String tmp : splitId) {
            //清理setmeal_dish套餐的菜单表
            QueryWrapper<SetmealDish> qw = new QueryWrapper<>();
            qw.eq("setmeal_id", Long.valueOf(tmp));
            setmealDishService.remove(qw);
            //清理套餐
            this.removeById(Long.valueOf(tmp));
        }
    }

    /**
     * get front page meal's list
     *
     * @param setmeal
     * @return
     */
    @Override
    @Cacheable(value = "mealListCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    public List<Setmeal> getMealList(Setmeal setmeal) {
        QueryWrapper<Setmeal> qw = new QueryWrapper<>();
        qw.eq("category_id", setmeal.getCategoryId());
        qw.eq("status", setmeal.getStatus());
        return this.list(qw);
    }
}
