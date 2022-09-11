package cn.calendo.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * user info entity
 */

@Data
public class User implements Serializable {

    private static long serialVersionUID = 1L;

    //Snowflake algorithm id
    private Long id;

    //name
    private String name;

    //email
    private String email;

    //gender
    private String sex;

    //avatar_icon
    private String avatar;

    //status: 0 :deny, 1: available
    private Integer status;
}
