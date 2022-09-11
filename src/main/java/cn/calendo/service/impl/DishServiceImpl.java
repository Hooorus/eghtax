package cn.calendo.service.impl;

import cn.calendo.common.CustomException;
import cn.calendo.dto.DishDto;
import cn.calendo.mapper.DishMapper;
import cn.calendo.pojo.Category;
import cn.calendo.pojo.Dish;
import cn.calendo.pojo.DishFlavor;
import cn.calendo.pojo.SetmealDish;
import cn.calendo.service.CategoryService;
import cn.calendo.service.DishFlavorService;
import cn.calendo.service.DishService;
import cn.calendo.service.SetmealDishService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单service实现类
 */

@Service
@Slf4j
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品的分页查询（需要BU双表查询）
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page page(Integer page, Integer pageSize, String name) {
        //分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        QueryWrapper<Dish> qw = new QueryWrapper<>();
        //模糊查询
        qw.like(name != null, "name", name);
        //排序条件
        qw.orderByDesc("id").orderByDesc("category_id").orderByDesc("name");
        //执行分页查询
        this.page(pageInfo, qw);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return dishDtoPage;
    }

    /**
     * 新增菜品，同时保存对应的口味数据
     *
     * @param dishDto
     */
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 根据id查询菜品的基本信息和对应口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询菜品当前口味信息，从dish_flavor表里面查询
        QueryWrapper<DishFlavor> qw = new QueryWrapper<>();
        qw.eq("dish_id", dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(qw);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        this.updateById(dishDto);
        //若setmeal_dish内包含此菜品那就也更新它
        QueryWrapper<SetmealDish> qwid = new QueryWrapper<>();
        qwid.eq("dish_id", dishDto.getId());
        if (setmealDishService.count(qwid) > 0) {
            //get setmeal_dish list
            SetmealDish setmealDish1 = setmealDishService.getOne(qwid);
            String name = dishDto.getName();//set name from dishDto to setmeal_dish
            BigDecimal price = dishDto.getPrice();//set price from dishDto to setmeal_dish
            setmealDish1.setName(name);
            setmealDish1.setPrice(price);
            setmealDishService.updateById(setmealDish1);
        }
        //清理菜品对应的口味数据——dish_flavor
        QueryWrapper<DishFlavor> qw = new QueryWrapper<>();
        qw.eq("dish_id", dishDto.getId());
        dishFlavorService.remove(qw);
        //添加当前提交过来的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 起售/停售
     *
     * @param dish
     */
    @Override
    public void updateStatus(Dish dish) {
        QueryWrapper<Dish> qw = new QueryWrapper<>();
        dish.setStatus(dish.getStatus());
        qw.eq("id", dish.getId());
        this.updateById(dish);
    }

    /**
     * 添加套餐内添加菜品里每种菜品的分类（前后台复用）
     * @param dish
     * @return
     */
    @Override
    public List<DishDto> selectDishList(Dish dish) {
        QueryWrapper<Dish> qw = new QueryWrapper<>();
        qw.eq(dish.getCategoryId() != null, "category_id", dish.getCategoryId());
        qw.orderByAsc("sort").orderByDesc("update_time");
        List<Dish> dishList = this.list(qw);
        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            QueryWrapper<DishFlavor> qw2 = new QueryWrapper<>();
            qw2.eq("dish_id", dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(qw2);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        return dishDtoList;
    }

    /**
     * 使用关键字输入框查询数据
     *
     * @param name
     * @return
     */
    @Override
    public List<Dish> queryDishListByKey(String name) {
        QueryWrapper<Dish> qw = new QueryWrapper<>();
        qw.like(name != null, "name", name);
        return this.list(qw);
    }

    /**
     * 根据id删除菜品，在删除前查看套餐是否含有此菜品
     *
     * @param id
     */
    @Override
    public void deleteDish(String id) {
        String[] splitId = id.split(",");
        for (String tmp : splitId) {
            //清理dish_flavor口味表，在清理时统计setmeal_dish表内是否包含此菜品，若包含则不能删除
            QueryWrapper<DishFlavor> qw = new QueryWrapper<>();
            QueryWrapper<SetmealDish> qw2 = new QueryWrapper<>();
            qw2.eq("dish_id", Long.valueOf(tmp));
            qw.eq("dish_id", Long.valueOf(tmp));
            int haveCount = setmealDishService.count(qw2);
            if (haveCount > 0) {
                throw new CustomException("菜品已被套餐包含！");
            }
            dishFlavorService.remove(qw);
            //删除此菜品
            this.removeById(Long.valueOf(tmp));
        }
    }

}
