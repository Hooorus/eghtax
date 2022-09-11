package cn.calendo.config;

import cn.calendo.common.MyMetaObjectHandler;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class MybatisPlusConfig {

    /**
     * 分页查询插件的配置类
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

//    @Bean
//    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
//        MybatisSqlSessionFactoryBean mybatisPlus = new MybatisSqlSessionFactoryBean();
//        //加载数据源
//        mybatisPlus.setDataSource(dataSource);
//        //全局配置
//        GlobalConfig globalConfig = new GlobalConfig();
//        //配置填充器
//        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
//        mybatisPlus.setGlobalConfig(globalConfig);
//        return mybatisPlus;
//    }
}
