# o365
O365管理系统是一个以java语言开发的基于Microsoft Graph Restful API的多全局管理系统，理论上支持任何Office全局的管理(A1,A3,A1P,E3,E5等)，你可以很方便的使用它来添加，删除，启用，禁用，搜索和查看用户，提升和收回管理员权限，更新密钥，查看订阅，分配订阅(创新用户时)，查看多全局报告

使用说明请参考
https://hostloc.com/thread-846732-1-1.html


# Docker 
基于v1.1.1版的docker
```bash
docker push 448261751/java-hqr-o365
docker run -d -p 9527:9527 448261751/java-hqr-o365
```
