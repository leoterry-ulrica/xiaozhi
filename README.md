# xiaozhi-server
主要用于小智服务端开发，当前版本：1.2.0-SNAPSHOT。

## 主要功能

* **主业务功能**：负责服务端主要业务功能开发。


## 文档资料


## 版本迭代

* **1.2.0-SNAPSHOT**

>  [2017.04.10]
1. 修复公众版模块中企业审计问题；
2. 在父pom文件中添加各种profiles环境变量，自定义参数profiles.server，配置文件服务器地址。用于适用不同的环境。  
        命令：mvn clean package -Pproduct-cloud -Dmaven.test.skip=true  
        或者显式指定ip：mvn clean package -Pproduct-cloud -Dmaven.test.skip=true -Dprofiles.server=127.0.0.1  
        说明：-P表示显式指定profile，-Dmaven.test.skip表示跳过单元测试 
        
>  [2017.04.11]
1. 添加markdown语法的README文件;
2. 添加bean之间转换的优雅工具类：dozer
3. 修复任务汇总bug；
4. 优化获取微作信息接口：添加发布者编码；
5. 项目中涉及到IP配置独立出来，放到maven构建参数中；

>  [2017.04.12]
1. 添加权限模板；
2. 修复企业审计信息；
3. 修复全文检索排序问题；
4. 重构代码：去掉冗余代码；
5. 添加发布者信息；

>  [2017.04.13]
1. web-公共页：根据用户编码获取它相关其它成员列表;
2. web-公共页：添加删除微作接口；
3. web-公共页：企业审计信息同步；
4. PC端：文档重命名；

>  [2017.04.14]
1. 修改上传版本接口；
2. 完成任务汇总的综合过滤，导出；
3. 部署版本到aws；

>  [2017.04.15]
1. 开发公众版的个人简历上传（进行中...）；

* **1.2.2-SNAPSHOT**

>  [2017.04.20]
1. 重构代码，调整代码结构，添加通用处理层：bdp-manager；
2. 修复任务筛选bug； 
3. 优化spring session用户登录信息缓存（redis）；

* **1.3.0-SNAPSHOT**

>  [2017.04.21]
1. 重构代码，以dcm命名业务线；