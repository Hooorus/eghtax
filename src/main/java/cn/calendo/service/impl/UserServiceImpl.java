package cn.calendo.service.impl;

import cn.calendo.common.R;
import cn.calendo.dto.CodeDto;
import cn.calendo.mapper.UserMapper;
import cn.calendo.pojo.User;
import cn.calendo.service.UserService;
import cn.calendo.utils.VCodeMailSenderUtils;
import cn.calendo.utils.ValidateCodeUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * interface user's implementing
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private VCodeMailSenderUtils vCodeMailSenderUtils;

    @Autowired
    private ValidateCodeUtils validateCodeUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    public static RedisTemplate redis;

    @PostConstruct
    public void getRedisTemplate() {
        redis = this.redisTemplate;
    }

    /**
     * send email-v-code by email
     *
     * @param user user entity
     * @return
     */
    @Override
    public R<String> getVCode(User user) {
        //get email address
        String s = JSON.toJSONString(user);
        JSONObject jsonObject = JSON.parseObject(s);
        String email = jsonObject.getString("email");
        //generate & send 6 digit v-code
        if (email != null) {
            Integer code = validateCodeUtils.generateValidateCode(6);
            String from = "2479711422@qq.com";
            String codeMail = vCodeMailSenderUtils.sendVCodeMail(code, from, email);
            //inject validate code (key-value pair) to redis set 10min
            redis.opsForValue().set("VCode", codeMail, 10, TimeUnit.MINUTES);
            return R.success(codeMail);
        } else {
            return R.error("can not send vcode mail!");
        }
    }

    /**
     * mobile client user login
     *
     * @param codeDto
     * @return
     */
    @Override
    public R<User> login(CodeDto codeDto) {
        //get request JSON, split validate code & email address
        String s = JSON.toJSONString(codeDto);
        JSONObject jsonObject = JSON.parseObject(s);
        String code = jsonObject.getString("code");
        String email = jsonObject.getString("email");
        //reject validate code (key-value pair) from redis
        String vCode = redis.opsForValue().get("VCode").toString();
        //validate code matching
        User user = new User();
        if (code.equals(vCode)) {
            //if email is a new account, auto register
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("email", email);
            int count = userService.count(qw);
            if (count == 0) {
                user.setEmail(email);
                user.setStatus(1);
                userService.save(user);
            }
        } else {
            return R.error("验证码错误！");
        }
        //if login success, delete vcode in redis
        redis.delete("VCode");
        return R.success(user);
    }
}
