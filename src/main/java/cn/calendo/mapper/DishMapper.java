package cn.calendo.mapper;

import cn.calendo.pojo.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单mapper(dao)层映射接口
 */

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
