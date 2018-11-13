package com.caozhihu.tmall.util;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MybatisGenerator {

    public static void main (String[] args) throws Exception{
        String today = "2018-11-10";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = simpleDateFormat.parse(today);
        Date date = new Date();

        if (date.getTime() > now.getTime() + 1000 * 60 * 60 * 24) {
            System.err.println("-----未能成功运行-----");
            System.err.println("-----未能成功运行-----");
            System.err.println("本程序具有重置作用，应该只运行一次，如果必须再次运行，请修改today变量为今天，如：" + simpleDateFormat.format(new Date()));
            return;
        }

        if (false) {
            return;
        }

        List<String> warnnings = new ArrayList<>();
        boolean overwrite = true;
        InputStream is = MybatisGenerator.class.getClassLoader().getResource("generatorConfig.xml").openStream();//获取配置文件对应路径的输入流
        ConfigurationParser configurationParser = new ConfigurationParser(warnnings);
        Configuration configuration = configurationParser.parseConfiguration(is);
        is.close();

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, callback, warnnings);
        myBatisGenerator.generate(null);

        System.out.println("生成代码成功，只能执行一次，以后执行会覆盖掉mapper,pojo,xml 等文件上做的修改");
    }
}
