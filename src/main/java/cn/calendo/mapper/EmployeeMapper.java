package cn.calendo.mapper;

import cn.calendo.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工信息mapper(dao)层映射接口
 */

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
