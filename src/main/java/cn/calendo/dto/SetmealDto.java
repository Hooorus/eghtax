package cn.calendo.dto;

import cn.calendo.pojo.Setmeal;
import cn.calendo.pojo.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
