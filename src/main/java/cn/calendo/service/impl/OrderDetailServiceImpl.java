package cn.calendo.service.impl;

import cn.calendo.mapper.OrderDetailMapper;
import cn.calendo.pojo.OrderDetail;
import cn.calendo.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
