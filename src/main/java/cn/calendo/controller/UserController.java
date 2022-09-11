package cn.calendo.controller;

import cn.calendo.common.R;
import cn.calendo.dto.CodeDto;
import cn.calendo.pojo.User;
import cn.calendo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * user app
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * send email-v-code by email
     *
     * @param user user entity
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> getVCode(@RequestBody User user) {
        return userService.getVCode(user);
    }

    /**
     * mobile client user login
     *
     * @param codeDto codeDto Dto entity
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody CodeDto codeDto) {
        return userService.login(codeDto);
    }
}