package cn.calendo.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * random validate code generating
 */

@Component
public class ValidateCodeUtils {

    /**
     * random generate v-code
     *
     * @param length length=4/6
     * @return
     */
    public Integer generateValidateCode(int length) {
        Integer code = null;
        if (length == 4) {
            //generating random number, max=9999
            code = new Random().nextInt(9999);
            //ensure random number is 4 digit
            if (code < 1000) {
                code = code + 1000;
            }
        } else if (length == 6) {
            //generating random number, max=999999
            code = new Random().nextInt(999999);
            if (code < 100000) {
                code = code + 100000;//保证随机数为6位数字
            }
        } else {
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * random generate string v-code by designing length
     *
     * @param length v-code length
     * @return
     */
    public String generateValidateCode4String(int length) {
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        return hash1.substring(0, length);
    }
}
