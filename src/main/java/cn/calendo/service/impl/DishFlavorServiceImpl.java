package cn.calendo.service.impl;

import cn.calendo.mapper.DishFlavorMapper;
import cn.calendo.pojo.DishFlavor;
import cn.calendo.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
