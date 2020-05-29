# 项目背景与简介
- 初版作品，基于SMM框架，主要完成了店铺、商品的管理与添加，首页展示模块的功能。适合个人练手以及熟悉组件技术之用。

# 主要技术栈及使用版本
- 前端：阿里巴巴SUI MObile前端UI库（[https://sui.ctolog.com/](https://sui.ctolog.com/ "https://sui.ctolog.com/")）/js
- 后端：Spring（5.0.2） + SpringMVC（5.0.2） + Mybatis（3.5.4）
- 数据库与缓存：MySQL（5.5）/Redis（5.0.8）
- 其他组件：Json数据处理框架jackson（2.11.0）/图片处理工具thumbnailator（0.4.11）/验证码工具包kaptcha（2.3.2）/日志记录工具logback（1.2.3）
- redis相关组件：Jedis（2.9.0）/spring-data-redis（2.1.9）
- 单元测试：Junit
- 部署：谷歌云

# 线上演示地址：[http://chmpersonal.top/](http://chmpersonal.top/ "http://chmpersonal.top/")
- 请不要随意修改演示地址数据，感谢配合
- 由于没有添加过多数据，只在美食饮品分区下的二哥奶茶店中添加了商品
- 商家注册和账号注册页面阉割了，后端是实现好的，自行添加即可
- 商品详情页面在基于SSM的V1.0中没有开发，迁移至SpringBoot后才开发的，这里可以自行补充
- 商家账号：testusername   密码：newtest


# QuickStart
- 数据库建库恢复o2oshop.sql数据
- 进入/src/main/java/cn/chm/o2o/util/PathUtil.java，修改getImgBasePath()方法中图片存放目录地址为你自己的地址
- 修改/src/main/resources/jdbc.properties中的数据库链接地址和用户名密码为你自己的数据库
- IDEA导入项目，部署，编译，开始访问吧
- 首页地址：XXXX/frontend/index
