package cn.calendo.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
    private Integer userStatus;

    //下单用户id
    private Long userId;

    //地址id
    private String userAddId;

    //下单时间
    private LocalDateTime userOrderTime;

    //支付方式 1微信，2支付宝
    private Integer userPayMethod;

    //实收金额
    private BigDecimal userAmount;

    //备注
    private String userRemark;

    //手机号
    private String userPhone;

    //地址
    private String userAddress;

    //用户名
    private String userName;

    //收货人
    private String userConsignee;
}
