package cn.calendo.mapper;

import cn.calendo.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类管理mapper(dao)层映射接口
 */

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
