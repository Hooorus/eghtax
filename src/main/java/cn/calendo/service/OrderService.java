package cn.calendo.service;

import cn.calendo.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrderService extends IService<Orders> {

    void submit(Orders orders);

}
