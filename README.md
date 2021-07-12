# o365
O365管理系统是一个以java语言开发的基于Microsoft Graph Restful API的多全局管理系统，理论上支持任何Office全局的管理(A1,A3,A1P,E3,E5等)，你可以很方便的使用它来批量添加，批量删除，批量启用，批量禁用，搜索和查看用户，提升和收回管理员权限，更新密钥，查看订阅，分配订阅(创新用户时)，查看单全局或多全局报告，登录同时需要微信许可（此功能默认关闭）

## 最低环境需求
| 类型 | - |
| ---- | ----|
| CPU | 1C |
| RAM | 0.5G |
| 硬盘 | 5GB |


## 功能介绍
### 首页预览  
  ![alt 首页](https://github.com/vanyouseea/o365/blob/master/pic/001.PNG)
  登录后请根据首页介绍，先创建APP，然后赋予以下6个权限，需要注意的是，API权限请务必选择**应用程序**
  | API名 | 大致作用 |
  | ---- | ----|
  | Application.ReadWrite.All | 用于新增密钥的功能 |
  | Application.ReadWrite.OwnedBy | 用于新增密钥的功能 |
  | Directory.ReadWrite.All | 用于订阅，域名，用户管理的功能 |
  | RoleManagement.ReadWrite.Directory | 用于特权角色的管理 |
  | User.ManageIdentities.All | 用于用户的管理 |
  | User.ReadWrite.All | 用于用户的管理 |

### [配置和报告] -> Office配置  
  - **新增**  
    将刚才得到的3个要素填入
    ![alt 填入APP信息](https://github.com/vanyouseea/o365/blob/master/pic/002.PNG)  
    PS 配置根据备注排序，如果你想要有序，可以设置备注  
  - **删除**，**修改**，**刷新**不做介绍  
  - **导入**  
    如果不想麻烦，也可以使用导入功能，先下载模板，填写好信息后，可以批量导入  
    ![alt 导入](https://github.com/vanyouseea/o365/blob/master/pic/003.PNG)  
  - **导出**  
    将所有APP信息导出为csv文件  
  - **切换全局**  
    选中一行后，点解切换全局，O365很多的TAB工作的前提是需要有一个当前全局为是的APP，被选中的行将会以浅蓝色标记  
    ![alt 切换全局](https://github.com/vanyouseea/o365/blob/master/pic/004.PNG)  
  - **更新密钥**  
    将创建一个过期时间为2099-12-31的新密钥，成功后会替换现有密钥，若不想更改现有密钥，请取消  
    ![alt 更新密钥](https://github.com/vanyouseea/o365/blob/master/pic/005.PNG)  
  - **校验**  
    校验只验证APP 3要素的正确性(**不包含权限的校验**)
  - **生成报告**  
    选一行后，点击此按钮，会生成总览报告，此报告可以在Office总览报告中找到  
    ![alt 更新密钥](https://github.com/vanyouseea/o365/blob/master/pic/006.PNG)  
  - **On/Off**  
    显示/隐藏APPID和密钥  
  - **帮助**  
    有什么问题，你可以尝试点击帮助了解更多  
  
### [用户] -> 管理用户  
  ![alt 用户首页](https://github.com/vanyouseea/o365/blob/master/pic/007.PNG)  
  - **添加**  
    ![alt 添加](https://github.com/vanyouseea/o365/blob/master/pic/007.5.PNG)  
  - **批量添加**  
    批量添加有2中策略，一种是随机5字符，另外一种是自增长数字
    ![alt 批量添加](https://github.com/vanyouseea/o365/blob/master/pic/008.PNG)  
  - **删除**，**刷新**不做介绍  
  - **启用**，**禁用**  
    分别为启用和禁用选中的用户  
  - **提权**，**撤权**  
    分别为提升或撤销一个用户为全局管理员  
    同时他们支持更多角色的权限赋予与收回， 你可以在[配置和报告] -> 系统配置中修改关键字DEFAULT_ADMIN_ROLE_ID的值为其他的admin的role，比如通过这种方式，你可以提升一个用户为用户管理员  
    ![alt 批量添加](https://github.com/vanyouseea/o365/blob/master/pic/009.PNG)  
  - **搜索**  
    可以搜索名字和邮箱前缀  
  - **帮助**  
    有什么问题，你可以尝试点击帮助了解更多  
  
### [用户] -> 查看特权用户  
  ![alt 查看特权用户](https://github.com/vanyouseea/o365/blob/master/pic/010.PNG)  
### [许可证] -> 查看许可证  
  ![alt 查看许可证](https://github.com/vanyouseea/o365/blob/master/pic/011.PNG)  
### [配置和报告] -> Office总览报告  
  - **手动执行**  
    生成所有全局的总览报告，此报告包含总用户数，管理员数和SPO可用性  
    ![alt 查看许可证](https://github.com/vanyouseea/o365/blob/master/pic/012.PNG)  
    + 灰色代表无效全局  
    + 红色代表代表全局无管理员或者无管理员存活的全局  
    + 橙色代表SPO为0的全局  
    
### [配置和报告] -> 系统配置  
   
  


# Docker 
基于v1.5.3版的docker
```bash
docker pull vanyouseea/o365
docker run -d -p 9527:9527 vanyouseea/o365

#如果你之前使用过，但是不想手动迁移数据，那么可以这样做，/root/o365/data是你本地的文件夹
#建议使用这种方法，以后你如果想要迁移数据，直接拷贝/root/o365/data文件就行了
docker run -d -p 9527:9527 -v /root/o365/data:/data vanyouseea/o365
```
