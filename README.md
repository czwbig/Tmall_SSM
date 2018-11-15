
# Tmall_SSM

技术栈 Spring MVC+ Mybatis + Spring + Jsp + Tomcat , 是 Java Web 入门非常好的练手项目  
##### 效果展示：  
[模仿天猫前台](http://caozhihu.com/tmall)    
[模仿天猫后台](http://caozhihu.com/tmall/admin_category_list)    

### 项目简介

`关联项目`  
**[github - 天猫 JavaEE 项目](https://github.com/czwbig/Tmall_JavaEE)**  
**[github - 天猫 SSH 项目](https://github.com/czwbig/Tmall_SSH)**  
**[github - 天猫 SSM 项目](https://github.com/czwbig/Tmall_SSM)**  

之前使用 JavaEE 整套技术和 SSH 框架来作为解决方案，实现模仿天猫网站的各种业务场景，现在开始使用 SSM 框架技术。

> 项目用到的技术如下：  
**Java:  `Java SE基础`
前端： `HTML`,`CSS`, `JavaScript`, `JQuery`,`AJAX`, `Bootstrap`  
J2EE：`Tomcat`, `Servlet`, `JSP`, `Filter`  
框架：`Spring`，`Spring MVC`，`Mybatis`，`SSM整合`  
数据库：`MySQL`  
开发工具: `IDEA` ,`Maven`**  
>

![项目结构](https://upload-images.jianshu.io/upload_images/14923529-19c2f8b095450fd0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


### 表结构

[建表sql](https://github.com/czwbig/Tmall_SSM/blob/master/sql/tmall_ssm.sql)  已经放在 Github 项目的 /sql 文件夹下

|表名 |中文含义 |介绍 |
| - |:-:| -|
| Category           |分类表         |存放分类信息，如女装，平板电视，沙发等 
| Property            |属性表         |存放属性信息，如颜色，重量，品牌，厂商，型号等 
| Product             |产品表         |存放产品信息，如LED40EC平板电视机，海尔EC6005热水器 
| PropertyValue   |属性值表     |存放属性值信息，如重量是900g,颜色是粉红色 
| ProductImage   |产品图片表   |存放产品图片信息，如产品页显示的5个图片 
| Review              |评论表           |存放评论信息，如买回来的蜡烛很好用，么么哒 
| User                   |用户表         |存放用户信息，如斩手狗，千手小粉红 
| Order                 |订单表         |存放订单信息，包括邮寄地址，电话号码等信息 
| OrderItem         |订单项表       | 存放订单项信息，包括购买产品种类，数量等

![表关系](https://upload-images.jianshu.io/upload_images/14923529-8645ee131490206f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

|一|多|
|-|-|
| Category-分类	|Product-产品
| Category-分类	|Property-属性
| Property-属性	|PropertyValue-属性值
| Product-产品	      |PropertyValue-属性值
| Product-产品	      | ProductImage-产品图片
| Product-产品	     | Review-评价
| User-用户	    | Order-订单
| Product-产品	    | OrderItem-订单项
| User-用户	    | OrderItem-订单项
| Order-订单	    | OrderItem-订单项
| User-用户	    | User-评价

以上直接看可能暂时无法完全理解，结合后面具体到项目的业务流程就明白了。

----

### 开发流程

首先使用经典的 SSM 模式进行由浅入深地开发出第一个分类管理模块 ，
然后分析这种方式的弊端，再对其进行项目重构，使得框架更加紧凑，后续开发更加便利和高效率。 

### 分类管理

###### Category 实体类

准备 Category 实体类，并用 Hibernate 注解标示其对应的表，字段等信息。
举个例子，对于 `分类 / category` 的 实体类 和 表结构 设计如下：

![](https://upload-images.jianshu.io/upload_images/14923529-859590e49c301251.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---

###### Mapper 接口

`public interface CategoryMapper {
    List<Category> list();
} 
 `

---

###### CategoryMapper.xml

com.how2java.tmall.mapper.CategoryMapper 对应上面的 Mapper 接口。mybatis 的 sql 是手打的，还好有逆向工程，后面重构会讲。

```
<mapper namespace="com.caozhihu.tmall.mapper.CategoryMapper">
  <select id="list" resultType="Category">
    select * from   category order by id desc
  </select>
</mapper>
```

---

###### CategoryService  接口


`
public interface CategoryService{
    List<Category> list();
}
`

---

###### CategoryServiceImpl 实现类

```
@Service
public class CategoryServiceImpl  implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    public List<Category> list(){
        return categoryMapper.list();
    };
}
```

在 list() 方法中，通过其自动装配的一个 CategoryMapper 对象的 list() 获取所有的分类对象。

---

###### CategoryController 类

```
@Controller //声明当前类是一个控制器
@RequestMapping("") //访问的时候无需额外的地址
public class CategoryController {
    @Autowired //自动装配进 categoryService 接口
    CategoryService categoryService;
    @RequestMapping("admin_category_list")
    public String list(Model model){
        List<Category> cs= categoryService.list();
        model.addAttribute("cs", cs); 
        return "admin/listCategory";
    }
}
```

在list方法中，通过 categoryService.list() 获取所有的 Category 对象，然后放在 "cs" 中，并服务端跳转到“admin/listCategory” 视图。

---

###### jdbc.properties
```
#数据库配置文件
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/tmall_ssm?useUnicode=true&characterEncoding=utf8
jdbc.username=root
jdbc.password=admin
```

---

###### applicationContext.xml

```
<beans>
    <!-- 启动对注解的识别 -->
    <context:annotation-config/>
    <context:component-scan base-package="com.caozhihu.tmall.service"/>

    <!-- 导入数据库配置文件 -->
    <context:property-placeholder location="classpath:jdbc.properties"/>
    <!-- 配置数据库连接池 -->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
        <!-- 基本属性 url、user、password -->
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
        <!-- 配置初始化大小、最小、最大 -->
        <property name="initialSize" value="1"/>
        <property name="minIdle" value="1"/>
        <property name="maxActive" value="20"/>
        <!-- 配置获取连接等待超时的时间 -->
        <property name="maxWait" value="60000"/>
        <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
        <property name="timeBetweenEvictionRunsMillis" value="60000"/>
        <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
        <property name="minEvictableIdleTimeMillis" value="300000"/>
        <property name="validationQuery" value="SELECT 1"/>
        <property name="testWhileIdle" value="true"/>
        <property name="testOnBorrow" value="false"/>
        <property name="testOnReturn" value="false"/>
        <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
        <property name="poolPreparedStatements" value="true"/>
        <property name="maxPoolPreparedStatementPerConnectionSize"
                  value="20"/>
    </bean>

    <!--Mybatis的SessionFactory配置-->
    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="typeAliasesPackage" value="com.how2java.tmall.pojo"/>
        <property name="dataSource" ref="dataSource"/>
        <property name="mapperLocations">
            <array>
                <value>classpath:com/caozhihu/tmall/mapper/*.xml</value>
                <value>classpath:mapper/*.xml</value>
            </array>
        </property>
        <!--分页插件-->
        <property name="plugins">
            <array>
                <bean class="com.github.pagehelper.PageInterceptor">
                    <property name="properties">
                        <value>
                        </value>
                    </property>
                </bean>
            </array>
        </property>
    </bean>

    <!--Mybatis的Mapper文件识别,mybatis-spring提供了MapperScannerConfigurer这个类，
    它将会查找类路径下的映射器并自动将它们创建成MapperFactoryBean-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.caozhihu.tmall.mapper"/>
    </bean>
</beans>
```
这里只放了核心配置部分，头部命名空间已省略

---

###### springMVC.xml
 ```
<beans>
    <!--启动注解识别-->
    <context:annotation-config/>
    <context:component-scan base-package="com.caozhihu.tmall.controller">
        <context:include-filter type="annotation"
                                expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
    <mvc:annotation-driven/>

    <!--开通静态资源的访问-->
    <mvc:default-servlet-handler/>

    <!-- 视图定位 例如 admin/listCategory 会被定位成 /WEB-INF/jsp/admin/listCategory.jsp-->
    <bean
            class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="viewClass"
                  value="org.springframework.web.servlet.view.JstlView"/>
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

    <!-- 对上传文件的解析-->
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver"/>
</beans>
```

---

###### web.xml

修改web.xml，主要提供如下功能

1. 指定spring的配置文件为classpath下的applicationContext.xml
2. 设置中文过滤器
3. 指定spring mvc配置文件为classpath下的springMVC.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         version="2.5">

    <!-- spring的配置文件-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!--中文过滤器-->
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!-- spring mvc核心：分发servlet -->
    <servlet>
        <servlet-name>mvc-dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- spring mvc的配置文件 -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springMVC.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>mvc-dispatcher</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

---

###### 访问 jsp 显示数据

Controller 中的 Model 携带数据跳转到 jsp ，作为视图，担当的角色是显示数据，借助 JSTL 的 c:forEach 标签遍历从 CategoryController.list() 传递过来的集合。

![listCategory.jsp 部分代码](https://upload-images.jianshu.io/upload_images/14923529-bc0021816eaf1202.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![localhost/admin_category_list 访问效果](https://upload-images.jianshu.io/upload_images/14923529-767eeb3d143ed003.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

管理分类剩下部分就不展开了  
完整的`CategoryMapper.xml`代码如下
```
<mapper namespace="com.how2java.tmall.mapper.CategoryMapper">
    <select id="list" resultType="Category">
        select * from   category         order by id desc
        <if test="start!=null and count!=null">
            limit #{start},#{count}
        </if>
    </select>

    <select id="total" resultType="int">
        select count(*) from category
    </select>

    <insert id="add"  keyProperty="id"  useGeneratedKeys="true" parameterType="Category" >
        insert into category ( name ) values (#{name})
    </insert>

    <delete id="delete">
        delete from category where id= #{id}
    </delete>
 
    <select id="get" resultType="Category">
        select * from category  where id= #{id}
    </select>
 
    <update id="update" parameterType="Category" >
        update category set name=#{name} where id=#{id}
    </update>
</mapper>
```

---

完整的`CategoryMapper`接口代码如下
```
public interface CategoryMapper {
     List<Category> list(Page page);
     int total();
     void add(Category category);
     void delete(int id);
     Category get(int id);
     void update(Category category);
}
```

---

完整的`CategoryService`接口代码如下
```
public interface CategoryService{
    int total();
    List<Category> list(Page page);
    void add(Category category);
    void delete(int id);
    Category get(int id);
     void update(Category category);
}
```

---

完整的`CategoryServiceImpl`实现类代码就不放着了，只是实现了每个方法，并在其中调用对应的 CategoryMapper 方法而已  
如： ` public List<Category> list(Page page) { return categoryMapper.list(page); }`

###### 思路流程图

![思路流程图](https://upload-images.jianshu.io/upload_images/14923529-1271966830811f35.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 项目重构

分类管理中的CategoryMapper.xml使用很直接的SQL语句开发出来，这样的好处是简单易懂，便于理解。
可是，随着本项目功能的展开和复杂度的提升，使用这种直接的SQL语句方式的开发效率较低，需要自己手动写每一个SQL语句，而且其维护起来也比较麻烦。
所以我们做进一步的改进，主要是在分页方式和逆向工程方面做了重构。
1. 分页方式
目前的分页方式是自己写分页对应的limit SQL语句，并且提供一个获取总数的count(*) SQL。 不仅如此, mapper, service, service.impl 里都要提供两个方法：
`list(Page page);`
`count();`
分类是这么做的，后续其他所有的实体类要做分页管理的时候都要这么做，所以为了提高开发效率，把目前的分页方式改为使用 pageHelper 分页插件来实现。  

2. 逆向工程
目前分类管理中 Mybatis 中相关类都是自己手动编写的，包括：Category.java, CategoryMapper.java和CategoryMapper.xml。  
尤其是CategoryMapper.xml里面主要是SQL语句，可以预见在接下来的开发任务中，随着业务逻辑的越来越复杂，SQL语句也会越来越复杂，进而导致开发速度降低，出错率增加，维护成本上升等问题。
为了解决手动编写SQL语句效率低这个问题，我们对 Mybatis 部分的代码，使用逆向工程进行重构。 
所谓的逆向工程，就是在已经存在的数据库表结构基础上，通过工具，自动生成Category.java, CategoryMapper.java和CategoryMapper.xml，想想就很美好是吧。

---

##### pageHelper 分页

**`CategoryMapper.xml`**
1. 去掉total SQL语句
2. 修改list SQL语句，去掉其中的limit
```
<mapper namespace="com.how2java.tmall.mapper.CategoryMapper">
    <select id="list" resultType="Category">
        select * from   category         order by id desc
    </select>
 
    <insert id="add"  keyProperty="id"  useGeneratedKeys="true" parameterType="Category" >
        insert into category ( name ) values (#{name})
    </insert>

    <delete id="delete">
        delete from category where id= #{id}
    </delete>
 
    <select id="get" resultType="Category">
        select * from category  where id= #{id}
    </select>
 
    <update id="update" parameterType="Category" >
        update category set name=#{name} where id=#{id}
    </update>
</mapper>
```

---

**`CategoryMapper 接口` /`CategoryService 接口` / `CategoryServiceImpl 类`** 都是  
1. 去掉total()方法
2. 去掉list(Page page)方法
3. 新增list() 方法

---

**`CategoryController.list()方法 `**
```
@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
   
    @RequestMapping("admin_category_list")
    public String list(Model model,Page page){
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Category> cs= categoryService.list();
        int total = (int) new PageInfo<>(cs).getTotal();
        page.setTotal(total);
        model.addAttribute("cs", cs);
        model.addAttribute("page", page);
        return "admin/listCategory";
    }
}
```
---

**`applicationContext.xml 配置 pagehelper 插件`**
之前放的已经是修改过的了。。  
就是在`<!--Mybatis的SessionFactory配置-->
    <bean id="sqlSession" class="org.mybatis.spring.SqlSessionFactoryBean">`里面加入
```
<property name="plugins">
            <array>
                <bean class="com.github.pagehelper.PageInterceptor">
                    <property name="properties">
                        <value>
                        </value>
                    </property>
                </bean>
            </array>
</property>
```
---

##### Mybatis 逆向工程
1. OverIsMergeablePlugin  
MybatisGenerator 插件是 Mybatis 官方提供的，这个插件存在一个问题 ，即当第一次生成了CategoryMapper.xml 之后，再次运行会导致 CategoryMapper.xml 生成重复内容，而影响正常的运行。
为了解决这个问题，需要自己写一个小插件类 OverIsMergeablePlugin 。  这是复制别人的，具体原理还没研究。
```
public class OverIsMergeablePlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        try {
            Field field = sqlMap.getClass().getDeclaredField("isMergeable");
            field.setAccessible(true);
            field.setBoolean(sqlMap, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
```

2. generatorConfig.xml
这里提供一部分代码，具体的在 [github-generatorConfig.xml]( )

```
<generatorConfiguration>
    <context id="DB2Tables" targetRuntime="MyBatis3">
        <!--避免生成重复代码的插件-->
        <plugin type="com.caozhihu.tmall.util.OverIsMergeablePlugin"/>
        <!--是否在代码中显示注释-->
        <commentGenerator>
            <property name="suppressDate" value="true"/>
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <!--数据库链接地址账号密码-->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver" connectionURL="jdbc:mysql://localhost/tmall_ssm"
                        userId="root" password="admin">
        </jdbcConnection>
        <!--该属性可以控制是否强制DECIMAL和NUMERIC类型的字段转换为Java类型的java.math.BigDecimal,默认值为false，一般不需要配置。-->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>
        <!--生成pojo类存放位置-->
        <javaModelGenerator targetPackage="com.caozhihu.tmall.pojo" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <!--生成xml映射文件存放位置-->
        <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>
        <!--生成mapper类存放位置-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.caozhihu.tmall.mapper" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>

        <!--生成对应表及类名-->
        <table tableName="category" domainObjectName="Category" enableCountByExample="false"
               enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="true"
               selectByExampleQueryId="false">
            <property name="my.isgen.usekeys" value="true"/>
            <property name="useActualColumnNames" value="true"/>
            <generatedKey column="id" sqlStatement="JDBC"/>
        </table>
```
3. MybatisGenerator  
运行即生成mapper,pojo,xml 文件，核心代码如下  
```
List<String> warnnings = new ArrayList<>();
        boolean overwrite = true;
        InputStream is = MybatisGenerator.class.getClassLoader().getResource("generatorConfig.xml").openStream();//获取配置文件对应路径的输入流
        ConfigurationParser configurationParser = new ConfigurationParser(warnnings);
        Configuration configuration = configurationParser.parseConfiguration(is);
        is.close();

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration, callback, warnnings);
        myBatisGenerator.generate(null);
```
---

**自动生成的 CategoryMapper.xml 如下，**

![](https://upload-images.jianshu.io/upload_images/14923529-5182704f28925e59.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这是插件自动生成的xml，与我们自己手动写的也差不了多少，主要区别在于提供了一个 id="Example_Where_Clause"的SQL，借助这个可以进行多条件查询。

---

**pojo 如下，**

![](https://upload-images.jianshu.io/upload_images/14923529-74643bd40d2402fc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

MybatisGenerator会生成一个类叫做XXXXExample的。 它的作用是进行排序，条件查询的时候使用。
在分类管理里用到了排序，但是没有使用到其条件查询，在后续的属性管理里就会看到其条件查询的用法了。

---

**mapper 接口**  
![image.png](https://upload-images.jianshu.io/upload_images/14923529-2fdec1b6c547303d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

与手动编写的CategoryMapper比起来，CategoryMapper也是提供CURD一套，不过方法名发生了变化，比如：  
`delete`叫做`deleteByPrimaryKey`, `update`叫做`updateByPrimaryKey`。
除此之外，修改还提供了一个`updateByPrimaryKeySelective` ，其作用是只修改变化了的字段，未变化的字段就不修改了。

还有个改动是查询 list ,变成了`selectByExample(CategoryExample example);`

**CategoryServiceImpl 实现类**  
因为 CategoryMapper 的方法名发生了变化，所以 CategoryServiceImpl 要做相应的调整。
值得一提的是list方法：
```
public List<Category> list() {
        CategoryExample example =new CategoryExample();
        example.setOrderByClause("id desc");
        return categoryMapper.selectByExample(example);
    }
```
按照这种写法，传递一个example对象，这个对象指定按照id倒排序来查询  
我查看了 xml 里的映射， 在对应的查询语句 selectByExample 里面，
会判断 orderByClause 是否为空，如果不为空就追加 `order by ${orderByClause}`  
这样如果设置了 orderByClause 的值为“id desc” ，执行的 sql 则会是 `order by id desc`   

![](https://upload-images.jianshu.io/upload_images/14923529-f5c674ea3863f384.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


然后，我们再根据数据库字段，一次性生成所有的 实体类，example 类，mapper 和 xml，如果需要定制，直接在生成的东西上修改就行了，真是舒服啊。

---

后台还有其他管理页面的，比如属性管理、产品管理等，由于篇幅原因，具体的请移步[github-Tmall_SSM项目](https://github.com/czwbig/Tmall_SSM)。


#####   前台页面展示

此处是 SSH 跑起来截的图，SSM 版本目前只做了后台，前台未做，敬请期待...
![前台首页](https://upload-images.jianshu.io/upload_images/14923529-6be73ccdb1dec779.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![产品页](https://upload-images.jianshu.io/upload_images/14923529-d7a5bfc9f920b5e8.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

本文所讲不足整个项目的 1/10 ，有兴趣的朋友请移步 [github 项目的地址](https://github.com/czwbig/Tmall_SSM) 。


### 参考
**[天猫SSM整站学习教程](http://how2j.cn/k/tmall_ssm/tmall_ssm-1546/1546.html?p=55563)** 里面除了本项目，还有 Java 基础，前端，Tomcat 及其他中间件等教程， 可以注册一个账户，能保存学习记录。
