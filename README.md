# 文档生成器
## 说明
   生成器基于spring boot和vue。设置好参数后一键生成文档，并打成可运行jar包，只需在命令行中执行java -jar
   api-doc.jar，即可在浏览器中访问:localhost:18080/api-doc。配置了负载服务器的信息后会自动上传到服务器并运行。
## 使用
  1. 配置参数：
        * 打开File->Setting->Order Settings->api-generator
        * 选择接口文件所在的目录，推荐选择直接的父目录
        * 选择输出的位置目录。注意输出目录必须有写权限
        * 如果有额外的markdown类型的接口文档要引用，则选择该目录，**没有请置空**（目前新版本暂不支持）
        * 基本类型枚举作用详情请参阅：[github](https://github.com/Casper-Mars/idea-plugin-api-generator)
  2. 生成文档：
        * 点击菜单栏的Tools，再点击Generate api doc选择即可生成文档到配置的输出目录
        * 或者直接按快捷键：shift+ctrl+alt+G
   
## 接口解析规则
   * 根据注释解析
   * @param 表示参数，后接参数描述。如果没有注释指定，则直接取方法的入参。例子：@param name 姓名
   * @return 表示返回值，后接返回值类型全称。如果没有注释指定，则直接取方法的回参。例子：@return java.lang.String
   * 集合类型只支持数组
   * 对于基本类（jdk自带的）和部分不需要解析的第三方类需要在设置界面设置好，设置格式为类名之间逗号隔开。例子：Boolean,Integer
## 例子
     /**
     * 添加
     * @param info 信息
     * @return java.lang.String[]
     */
     @PostMapping("/add")
     public List<String> hello(@RequestBody InfoDTO info){
     }
如上例子会解析出接口：
  * 名称：hello
  * 描述：添加
  * 请求方式：POST
  * 参数：
       * 名称：info
       * 描述：信息
       * 请求类型：body
       * 参数格式：json
  * 返回值：
       * 类型：String
       * 格式：array

## 模式：debug和生产
  * debug模式：在此模式下，会保留程序运行过程中的临时文件，包括java源文件、class文件、依赖jar包和容器jar包。debug模式只有开发时才能用，一旦部署后就是生产模式了。
  * 生产模式：在此模式下，只会保留最终的产物
  
## 关于基本类型枚举的解析

   接口参数或者返回值涉及开发者或者第三方包的pojo类时，插件会把pojo类的属性解析出来。例如pojo类参数：TestDTO{name;age} test 会被解析成
   test：{name;age}。
   如果指定了TestDTO是基本类型，则解析的结果是test:TestDTO，不会把pojo的属性解析出来。
   常见的使用场景：文件上传接口，接口参数是MultipartFile，这是第三方包的类，是不需要解析出来的