# o365
O365管理系统是一个以java语言开发的基于Microsoft Graph Restful API的多全局管理系统，理论上支持任何Office全局的管理(A1,A3,A1P,E3,E5等)，你可以很方便的使用它来批量添加，批量删除，批量启用，批量禁用，搜索和查看用户，提升和收回管理员权限，更新密钥，查看订阅，分配订阅(创新用户时)，查看单全局或多全局报告，登录同时需要微信许可（此功能默认关闭）

一般示例请参考
https://hostloc.com/thread-846732-1-1.html


# Docker 
基于v1.5.3版的docker
```bash
docker pull vanyouseea/o365
docker run -d -p 9527:9527 vanyouseea/o365

#如果你之前使用过，但是不想手动迁移数据，那么可以这样做，/root/o365/data是你本地的文件夹
#建议使用这种方法，以后你如果想要迁移数据，直接拷贝/root/o365/data文件就行了
docker run -d -p 9527:9527 -v /root/o365/data:/data vanyouseea/o365
```
