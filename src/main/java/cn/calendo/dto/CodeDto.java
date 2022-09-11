package cn.calendo.dto;

import cn.calendo.pojo.User;
import lombok.Data;

/**
 * code dto: use for validate code receive
 */

@Data
public class CodeDto extends User {

    private String code;

}
