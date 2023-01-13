# o365
O365管理系统是一个以java语言开发的基于Microsoft Graph Restful API的多全局管理系统，理论上支持任何Office全局的管理(A1,A3,A1P,E3,E5等)，你可以很方便的使用它来批量添加，批量删除，批量启用，批量禁用，搜索和查看用户，绑定解绑域名，生成邀请码，邀请朋友注册，提升和收回管理员权限，更新密钥，查看订阅，分配订阅(创新用户时)，查看单全局或多全局报告，登录同时需要微信许可（此功能默认关闭）

## 最低环境需求
| 类型 | - |
| ---- | ----|
| CPU | 1C |
| RAM | 0.75G |
| 硬盘 | 5GB |

## heroku已不支持免费部署，故而删除体验网址  
## ~~体验o365 in heroku~~  
~~o365已部署于heroku,你可以访问以下路径体验最新版的o365,你也可以将工程fork到自己的仓库用自己的heroku账号进行部署（推荐）~~  
~~https://oo365.herokuapp.com~~  
  
~~**特别提示**~~  
~~heroku 超过30分钟不被访问数据就会被销毁，所以仅能用来体验o365的功能，有需求的话还是建议部署到自己的VPS，群晖或者杜甫上~~  


## 功能介绍
### 首页预览  
  ![alt 首页](https://github.com/vanyouseea/o365/blob/master/pic/001.PNG)
  登录后请根据首页介绍，先创建APP，然后赋予以下权限，需要注意的是，API权限请务必选择**应用程序**
  | API名 | 大致作用 |
  | ---- | ----|
  | Application.ReadWrite.All | 用于新增密钥的功能 |
  | Application.ReadWrite.OwnedBy | 用于新增密钥的功能 |
  | Directory.ReadWrite.All | 用于订阅，域名，用户管理的功能 |
  | RoleManagement.ReadWrite.Directory | 用于特权角色的管理 |
  | User.ManageIdentities.All | 用于用户的管理 |
  | User.ReadWrite.All | 用于用户的管理 |
  | Reports.Read.All | 用于生成Onedrive,Exchange详细报告 |
  | Sites.FullControl.All | 用于检验SPO |
  | Domain.ReadWrite.All | 用于域名操作 |

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
    选中一行后，点击切换全局，O365很多的TAB工作的前提是需要有一个当前全局为是的APP，被选中的行将会以浅蓝色标记  
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
    批量添加有2种策略，一种是随机5字符，另外一种是自增长数字  
    ![alt 批量添加](https://github.com/vanyouseea/o365/blob/master/pic/008.PNG)  
  - **删除**，**刷新**不做介绍  
  - **启用**，**禁用**  
    分别为启用和禁用选中的用户  
  - **提权**，**撤权**  
    分别为提升或撤销一个用户为全局管理员  
    同时他们支持更多角色的权限赋予与收回， 你可以在[配置和报告] -> 系统配置中修改关键字DEFAULT_ADMIN_ROLE_ID的值为其他的admin的role，比如通过这种方式，你可以提升一个用户为用户管理员  
    ![alt 批量添加](https://github.com/vanyouseea/o365/blob/master/pic/009.PNG)  
  - **域名**  
    你可以绑定或者解绑全局上的非默认域名，删除域名时是异步删除，如果域名依赖越多则花费越久，最多需要花费24小时，MS原话如下  
    > Prior to calling forceDelete, you must update or remove any references to Exchange as the provisioning service  
    > It can take up to 24 hours to remove a domain. Domains with many dependencies tend to take longer than others  
    
    PS: 如果域名托管在CF并且在O365系统中配置了正确的CF_AUTH_EMAIL和CF_AUTH_KEY，那么绑定域名时程序会自动添加TXT记录到CF并进行认证，无需人工干涉，实现域名一键绑定  
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
  - **下载Exchange报告**  
    生成选中全局的Exchange报告，此报告包含了所有用户的邮件使用情况   
  - **下载Onedrive报告**  
    生成选中全局的Onedrive报告，此报告包含了所有用户的Onedrive使用情况  
    由于微软更新了隐私策略，以上2个报告中的用户的信息和url会以乱码代替，如果想要查看真实的用户，请以管理员账号登录https://admin.microsoft.com/#/homepage
    选择**设置**->**组织设置**->**报告**，取消勾选 **在所有报告中，显示用户、组和站点的已取消识别的名称**即可显示真实信息  
 
  
### [配置和报告] -> 系统配置  
  WX_CALLBACK_IND  
  WX_CALLBACK_TOKEN  
  WX_CORPID  
  WX_CORPSECRET  
  WX_AGENTID  
  WX_CALLBACK_AESKEY  
  以上6个参数的设置请参考word文档, https://github.com/vanyouseea/o365/blob/master/docs/%E9%85%8D%E7%BD%AE%E5%BE%AE%E4%BF%A1%E5%93%8D%E5%BA%94.docx  
  **请谨慎使用，此6个变量的配置非必须，只是为了增强管理员账号的安全，如果未通过，请立即把WX_CALLBACK_IND设为N，避免把自己关外面**

# Docker 
### 基于v1.9.0版的docker
```bash
docker pull vanyouseea/o365
docker run -d -p 9527:9527 vanyouseea/o365

#如果你之前使用过，但是不想手动迁移数据，那么可以这样做，/root/o365/data是你本地的文件夹
#建议使用这种方法，以后你如果想要迁移数据，直接拷贝/root/o365/data文件就行了
docker run -d -p 9527:9527 -v /root/o365/data:/data vanyouseea/o365
```
### 手动构建master分支的docker  
docker build -t vanyouseea/o365 https://github.com/vanyouseea/o365.git#master  
