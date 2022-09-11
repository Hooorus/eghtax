package cn.calendo.service.impl;

import cn.calendo.common.BaseContext;
import cn.calendo.mapper.ShoppingCartMapper;
import cn.calendo.pojo.ShoppingCart;
import cn.calendo.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Override
    public ShoppingCart addShoppingList(ShoppingCart shoppingCart) {
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getDishId, dishId);
        if (dishId != null) {
            lqw.eq(ShoppingCart::getDishId, dishId);
        } else {
            lqw.or().eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = this.getOne(lqw);
        if (shoppingCart1 != null) {
            //如果已经存在，就在原来数量的基础上加一
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number + 1);
            this.updateById(shoppingCart1);
        } else {
            //如果不存在，则添加到购物车，数量默认就是一
            shoppingCart.setNumber(1);
            this.save(shoppingCart);
            shoppingCart1 = shoppingCart;
            return shoppingCart1;
        }
        return null;
    }

    @Override
    public ShoppingCart subShoppingList(ShoppingCart shoppingCart) {
        //设置用户id，指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getDishId, dishId);
        if (dishId != null) {
            lqw.eq(ShoppingCart::getDishId, dishId);
        } else {
            lqw.or().eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = this.getOne(lqw);
        if (shoppingCart1.getNumber() >= 2) {
            //如果已经存在>=2，就在原来数量的基础上-1
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number - 1);
            this.updateById(shoppingCart1);
        } else {
            if (shoppingCart1.getNumber() == 1) {
                this.remove(lqw);
            }
        }
        return null;
    }
}
