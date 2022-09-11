package cn.calendo.controller;

import cn.calendo.common.BaseContext;
import cn.calendo.common.R;
import cn.calendo.pojo.ShoppingCart;
import cn.calendo.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        ShoppingCart shoppingList = shoppingCartService.addShoppingList(shoppingCart);
        return R.success(shoppingList);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        ShoppingCart shoppingList = shoppingCartService.subShoppingList(shoppingCart);
        return R.success(shoppingList);
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        lqw.orderByAsc(ShoppingCart::getUserId);
        List<ShoppingCart> list = shoppingCartService.list(lqw);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    private R<String> clean() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(lqw);
        return R.success("清空完毕");
    }
}
