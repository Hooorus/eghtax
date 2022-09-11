package cn.calendo.service;

import cn.calendo.common.R;
import cn.calendo.dto.CodeDto;
import cn.calendo.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {

    //send email-v-code by email
    R<String> getVCode(User user);

    //mobile client user login
    R<User> login(CodeDto codeDto);

}
