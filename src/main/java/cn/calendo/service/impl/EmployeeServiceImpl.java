package cn.calendo.service.impl;

import cn.calendo.mapper.EmployeeMapper;
import cn.calendo.pojo.Employee;
import cn.calendo.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 员工信息service实现类
 */

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
