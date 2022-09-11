package cn.calendo.service;

import cn.calendo.pojo.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ShoppingCartService extends IService<ShoppingCart> {
    //add shopping to shoppingCart
    ShoppingCart addShoppingList(ShoppingCart shoppingCart);

    //sub shopping to shoppingCart
    ShoppingCart subShoppingList(ShoppingCart shoppingCart);
}
