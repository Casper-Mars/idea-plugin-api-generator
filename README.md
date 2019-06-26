# 文档生成器
## 说明
   生成器基于spring boot、swagger2和肖玉明的swagger-ui。设置好参数后一键生成文档，并打成可运行jar包，只需在命令行中执行java -jar
   container.jar，即可在浏览器中访问:localhost:8080/doc.html
## 使用
  1. 配置参数：
        * 打开File->Setting->Order Settings->api-generator
        * 选择接口文件所在的目录，推荐选择直接的父目录
        * 选择输出的位置目录。注意输出目录必须有写权限
        * 如果有额外的markdown类型的接口文档要引用，则选择该目录
        * 基本类型枚举作用详情请参阅：
  2. 生成文档：
        * 点击菜单栏的Tools，再点击Generate api doc选择即可生成文档到配置的输出目录
        * 或者直接按快捷键：shift+ctrl+alt+G
## 技术难点记录
   * idea提供的文档不足，好多api不会用，又查不到
   * 读取jar包中的文件
   * 插件的classpath问题
   * 动态编译
   * 对jar包操作
   
## 接口解析规则
   * 根据注释解析
   * @param 表示参数，后接参数描述。如果没有注释指定，则直接取方法的入参。例子：@param name 姓名
   * @return 表示返回值，后接返回值类型全称。如果没有注释指定，则直接取方法的回参。例子：@return java.lang.String
   * 集合类型只支持数组
   * 泛型只支持一元泛型，并会解析为数组，二元或更高的认为是object
   * 对于基本类（jdk自带的）和部分不需要解析的第三方类需要在设置界面设置好，设置格式为类名之间逗号隔开。例子：Boolean,Integer
   * 注意：一定要规范好基本类的设置，避免出现深度解析卡死线程
   * 注意：接口的入参和回参如果是实体类，应该注意避免出现自引用的情况，目前的版本是忽略自引用的属性
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