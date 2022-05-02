### 1. 课程介绍

#### 1.1. 技术介绍

- 前端：Thymeleaf、Bootstrap、Jquery
- 后端：SpringBoot、MyBatisPlus、Lombok
- 中间件：RabbitMQ、Redis

​	说明：项目没有前后端分离是因为把重点集中在后端秒杀上

------

#### 1.2. 课程介绍

- 项目搭建
- 分布式 Session
- 秒杀功能
- 压力测试
- 页面优化
- 服务优化
- 接口安全

------

### 2. 学习目标

主要学习如何应对**大并发**（即如何使用缓存和异步），如何编写**优雅的代码**（即如何封装代码实现复用），如何解决**超卖**（秒杀解决的主要痛点）问题，具体目标如下：

1. 分布式会话：用户登录、**共享 Session**
2. 功能开发：商品列表、商品详情、**秒杀**、订单详情
3. 系统压测：Jemeter 入门、自定义变量、**正式压测**
4. 页面优化：**缓存**、静态化分离
5. 服务优化：**RabbitMQ 消息队列、接口优化、分布式锁**
6. 安全优化（防止黄牛）：隐藏秒杀地址、验证码、接口限流

------

### 3. 秒杀设计思路

秒杀主要解决两个问题：**并发读和并发写**

1. 并发读的优化思路：减少请求读的次数，或者读少量数据
2. 并发写的优化思路：单独做一个数据库层

秒杀主要的特点：**高性能、一致性、高可用**

1. 高性能：大量并发读和并发写，必须支持高并发访问，解决方案有：动静分离、热点的发现与隔离、请求的削峰与分层过滤、服务端的优化等
2. 一致性：实现并发情况下**减库存**这个修改操作的一致性，使多个用户看到相同的库存量
3. 高可用：防止宕机和意外情况使服务不可用

------

### 4. 项目搭建

#### 4.1. 创建项目

- idea 创建项目

<img src="/Users/yangwu/Library/Application Support/typora-user-images/image-20220501152717663.png" alt="image-20220501152717663" style="zoom:25%;" />



<img src="/Users/yangwu/Library/Application Support/typora-user-images/image-20220501152856863.png" alt="image-20220501152856863" style="zoom:25%;" />

- 添加 MyBatisPlus 依赖到 pom.xml

  ```xml
  <dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.1</version>
  </dependency>
  ```

- 配置 aplication.yml 文件

- 在 resource 目录下创建 mapper 目录

- 在 java 目录下创建 controller、service、service.impl、mapper、pojo 目录

- 在启动类中添加 @MapperScan 注解

------

### 5. 分布式会话

#### 1. 登录功能

##### 创建用户表

- 创建数据库 seckill，使用 utf8mb4 编码
- 创建用户表 t_user

------

##### 两次 MD5 加密

- 前端到后端：MD5(明文密码 + salt)，防止明文密码传输
- 后端到数据库：MD5(加密密码 + salt)，防止数据库被盗反向获取明文密码
- 引入 MD5 依赖
- 创建 utils 目录
- 编写 MD5Util 工具类

------

##### 逆向工程

- 新建逆向工程项目

- 编写生成工具类
- 生成用户表对应的实体类、service 接口、mapper 文件等

------

##### 功能实现

- 编写 LoginController 进行页面跳转

- 拷贝 login.html 到 templates 目录
- 拷贝静态资源到 static 目录
- 创建 vo 目录
  - 编写公共返回对象 RespBean 
  - 及其枚举 RespBeanEnum 
  - 安装启动 idea lombok 插件
- 编写登录请求处理方法 doLogin()
- 在 vo 目录下创建 LoginVo 对象封装登录请求参数
- 测试后端是否能接受参数以及 md5 加密是否正确
- 在 UserServiceImpl 实现登录逻辑 doLogin()

------

#### 2. 参数校验

##### 添加 validataion 依赖

- controller 中添加 @Valid 
- LoginVo 中添加注解

------

##### 创建自定义 @IsMobile 注解

- 创建 validator 目录
- 创建 IsMobile 注解
- 编写手机号码校验规则 IsMobileValidator 

------

##### 统一异常处理

- 创建 exception 目录
- 创建全局异常类 GlobalException
- 创建异常处理类 GlobalExceptionHandler

------

#### 3. Sesson 处理

- 添加工具类 CookieUtil 和 UUIDUtil

- 添加 HttpServletRequest 和 HttpServletResponse 参数给 doLogin

  用户第一次登录时，服务端生成 cookie 标识该用户，将 cookie-user 键值对保存在 request 的 session 中，用户接下来的所有请求都可以从 session 中获取 user 来识别用户身份

- 登录成功后跳转到商品列表页

  - 在 GoodsController 中编写 toList 跳转处理器方法
  - 编写 goodsList.html

------

##### 分布式 Session

​	把 session 存在 redis 中，所有前端请求统一从 redis 中获取 session

​	下面使用 spring session 自动解决分布式 session 问题（存入的是二进制数据）

- 添加 pom 依赖
  - spring-boot-starter-data-redis 依赖
  - commons-pool2 对象池依赖
  - spring-session-data-redis 依赖

- 配置 redis 

------

##### Redis 存储用户信息

​	第二种是不使用 spring session 自动存入 redis，而是手动将用户信息存入 redis 

- 删除 spring-session-data-redis 依赖
- 创建 config 包，准备 RedisConfig  配置类，实现用户数据的序列化
- UserServiceImpl doLogin() 将 user 对象存入 redis
- 创建 getUserByCookie 方法，从 redis 中获取 user

------

##### 优化登录功能

​	问题：每个处理方法都会提前通过 ticket 判断用户是否登录，出现大量重复

​	解决：在传入到 controller 方法前统一进行参数判断，使用 MVC 参数解析器，springmvc 先进行方法参数解析，再调用方法

- 创建 WebConfig 配置 MVC，重写 addArgumentResolvers 方法

- 创建自定义用户参数 UserArgumentResolver



未完待续... 详细步骤参看 Java 秒杀方案.pdf

------

### 6. 秒杀功能

- 准备四张表：商品表、订单表、秒杀商品表、秒杀订单表，单独设计秒杀商品表是为了单独处理秒杀逻辑
- 设计倒计时：页面显示秒杀状态和秒杀按钮，秒杀前后秒杀后按钮置灰，秒杀中按钮可用
- 秒杀逻辑：判断商品库存够不够 -> 判断是否重复抢购 -> 减秒杀商品表库存 -> 生产订单 -> 生产秒杀订单 



### 7. 压测

- QPS：query per second，每秒查询数，一次服务器查询就是一个 QPS，比如 DNS 服务器查询

- TPS：transactions per second，每秒事务数，一次页面访问就是一个 TPS，可能包含多个 QPS

- 在 linux 服务器上进行压测：maven 打包本地应用 -> 上传到服务器 -> 服务器安装 jdk jmeter mysql -> 压测生产报告

- 带参数的压测：遇到一个 bug，maxos maxproc 为 4000 左右，设置 jmeter 线程数量不能超过它

- 不同用户秒杀压测：

  - 工具类生产不同用户的，登录产生 cookie ticket，写入 config 文件，启动 jmeter 压测秒杀接口

  - 工具类生产不同的用户：
    1. 生成 User 列表，创建并初始化 User 对象
    2. 插入数据库：获取数据库连接，编写 sql，准备 statement，执行 sql
    3. 模拟登录：根据请求 url 创建 URL 对象，注意接口是 doLogin 不是 toLogin，调试了 2 小时 (获取 B 站弹幕)，获取 HTTP 连接对象 conn，通过流发送请求和读取响应并写入文件，注意必须在第 2 步完成后单独执行第 3 步
    4. 理解 HttpUrlConnecton 的设计：封装了 request、response、inputstrem、outputstream 等对象，从中可以获取 response 对象中的 cookie（ 调试了 2 小时），但是从 RespBean 中获取更加简单







### 秒杀优化

- 做缓存的条件：频繁读取且变更少的数据
- 缓存粒度

#### 1. 页面缓存

​	SpringMCV 在返回 Controller 返回 ModelAndView 后，将进行视图解和模型渲染析工作，

​	对于那些经常不变的页面可以缓存在 Redis 中，直接返回 html 给前端，省略渲染解析步骤

- 首先读 redis 查页面，如果存在直接返回，如果不存在手动解析页面，更新 redis，同时设置页面失效
- 一般缓存前几页



#### 2. 对象缓存

​	数据更新需要查数据库，而数据库是系统的瓶颈，对此可将对象缓存在 Redis 中，减少数据库访问

- 更新数据库和更新缓存的关系：先更新数据库再更新缓存，比如更新密码，先通过缓存获取用户，再修改用户密码，更新到数据库，最后删除缓存，重新登录时先查数据库再缓存



#### 3. 商品详情页静态化

​	对于前后端分离的项目，页面大部分内容是不变的，把不变的数据缓存在浏览器中，服务器只需要	传输页面中变化的数据就行，不需要传输整个页面，即不是通过 Model 而是通过 Ajax 传数据

- VO、DTO 几乎是相等的，都用来传输数据
- Controller 返回 View 一个 Model 对象，里面保存页面渲染的数据，用于数据传输
- 替换 thymeleaf 模板，前端就是一个 html，通过 Ajax 请求后端数据



#### 4. 解决库存超卖

- 使数据库更新操作时使用排它锁，即 innodb 行锁
- 解决库存超卖问题：使用 SQL 语句减库存，同时判断库存是否大于 0
- 解决同一用户重复抢购问题：使用数据库索引，用户id + 商品id



#### 5. 数据库优化

- redis 预减库存减少数据库访问：系统初始化时，把数据库加载到 redis

- 内存标记减少 redis 访问

- 异步下单，大量请求进入 mq 队列，后期在慢慢处理，实现流量消峰

  

#### 6. Redis 锁

- setIfAbsent 函数实现 Redis 锁功能
- 通过设置超时时间释放锁，当出现超时，当前线程的锁被提前释放，会导致后面一个线程的锁会被当前线程释放掉
- 给当前锁设置随机值，然后通过 lua 脚本实现锁释放的原子性



#### 7. 秒杀接口地址隐藏

- 返回秒杀接口地址，防止脚本抢单



#### 8. 项目总结

- 项目搭建：
  - 使用 SpringBoot Thymeleaf 搭建项目
  - 使用 RespBean 统一封装响应对象
  - 使用 MyBatis  生成数据库查询
- 分布式会话
  - 用户登录功能
    - 设计数据库
    - 明文密码二次 MD5 加密
    - 参数校验 + 全局异常处理
  - 共享 Session
    - SpringSession
    - Redis 
- 功能开发
  - 商品列表
  - 商品详情
  - 秒杀
  - 订单详情
- 系统压测
  - JMeter
  - 自定义变量模拟多用户
  - JMeter 命令行使用
  - 正式压测
    - 商品列表
    - 秒杀
- 优化
  - 页面缓存 + URL 缓存 + 对象缓存
  - 页面静态化，前后端分离
  - 静态资源优化
  - CDN 优化
- 接口优化
  - Redis 预减库存减少数据库访问
  - 内存标记减少 Redis 访问
  - RabbitMQ 异步下单
    - SpringBooot 整合 RabbitMQ
    - 交换机
- 安全优化
  - 秒杀接口地址隐藏
  - 算术验证码
  - 接口防刷
