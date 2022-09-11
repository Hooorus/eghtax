package cn.calendo.config;

import cn.calendo.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
//springboot建这个项目的写这个mvc配置类要实现WebMvcConfigurer接口，而不是继承WebMvcConfigurationSupport
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 扩展mvc框架的消息转换器，可以兼容前端js与后端java的数据类型，处理精度丢失等问题
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("创建转换器");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用jackson将java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器内
        converters.add(0, messageConverter);
    }
}
