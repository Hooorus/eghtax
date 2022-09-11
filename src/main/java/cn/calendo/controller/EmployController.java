package cn.calendo.controller;

import cn.calendo.common.R;
import cn.calendo.pojo.Employee;
import cn.calendo.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 员工信息controller层
 */

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查询数据库
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        String queryUsername = employee.getUsername();
        QueryWrapper<Employee> eq = queryWrapper.eq("username", queryUsername);
        //由于username字段已经唯一，所以使用getOne进行查询
        Employee loginRes = employeeService.getOne(eq);
        //3.判断没有查到的结果后返回登录失败
        if (loginRes == null) {
            return R.error("用户名或密码错误");
        }
        //4.比对密码，如果不一致则返回结果
        if (!loginRes.getPassword().equals(password)) {
            return R.error("用户名或密码错误");
        }
        //5.查看员工状态，若员工已被禁用，则返回登录失败结果
        if (loginRes.getStatus() == 0) {
            return R.error("账号已被禁用");
        }
        //6.登录成功，将员工id存入session，并返回登录结果
        request.getSession().setAttribute("employee", loginRes.getId());
        return R.success(loginRes);
    }

    /**
     * 员工登出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理session中保存的登录员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息:{}", employee.toString());
        //由于新增的员工没有密码，所以要给一个md5加密的初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工页面分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        QueryWrapper<Employee> qw = new QueryWrapper<>();
        //添加过滤条件，column必须是数据库字段名，而不是映射名
        qw.like(!StringUtils.isEmpty(name), "name", name);
        //添加排序条件
        qw.orderByDesc("update_time");
        //执行查询
        employeeService.page(pageInfo, qw);
        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
        log.info(employee.toString());
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询并回显员工信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询并回显员工信息");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("未查询到员工信息");
    }
}
