# Pay

[![Build Status](https://travis-ci.org/dianbaer/Pay.svg?branch=master)](https://travis-ci.org/dianbaer/Pay)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ee6a61826df447279701b6b9584084a4)](https://www.codacy.com/app/232365732/Pay?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dianbaer/Pay&amp;utm_campaign=Badge_Grade)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

# Pay是一个支付平台。

Pay由PayServer(服务器)与PayClient(客户端)两个项目组成。

### 基于grain RPC框架

https://github.com/dianbaer/grain

	grain-httpserver
	grain-httpclient
	grain-mariadb
	grain-threadmsg


## 依赖身份系统Identity：


https://github.com/dianbaer/Identity


## 打版本：在项目根目录下，执行

	ant


## 配置：


dist/PayClient/js/app/Url.js-----访问支付服务器与身份系统服务器

	function Url() {
		//支付平台地址
		this.url = "http://localhost:8081/PayServer/s";
		//身份系统地址
		this.ucUrl = "http://localhost:8080/IdentityServer/s";
		//支付客户端本项目地址
		this.clientUrl = "http://localhost/PayClient/"
	}
	$T.url = new Url();


dist/PayConfig/mybatis-config.xml---访问支付数据库


dist/PayServer.properties----PayConfig在服务器路径及一些参数

	#mybatis-config.xml在服务器的地址
	config_dir = C:/Users/admin/Desktop/github/Pay/trunk/PayConfig
	#支付宝partner
	alipay_partner=
	#支付宝seller_id
	alipay_seller_id=
	#支付宝md5
	alipay_key=
	#支付宝通知地址
	alipay_notify_url=http://localhost:8081/PayServer/alipay_notify
	#支付宝回调地址
	alipay_return_url=http://localhost:8081/PayServer/alipay_return
	#使用RSA还是MD5加密
	alipay_encrypt_type=RSA
	#RSA私钥
	alipay_private_key=
	#RSA共钥
	alipay_alipay_public_key=
	#身份系统地址
	ucenter_url=http://localhost:8080/IdentityServer/s
	#支付成功后，跳转的支付客户端地址
	pay_success_url=http://localhost/PayClient/paySuccess.html
	#支付失败后，跳转的支付客户端地址
	pay_fail_url=http://localhost/PayClient/payFail.html
	#通知过期时间
	notify_expire_time=300000
	#最大通知次数
	max_notify_time=5
	#通知间隔
	notify_interval=60000



## 推荐环境：

>快捷部署 https://github.com/dianbaer/deployment-server

	jdk-8u121

	apache-tomcat-8.5.12

	MariaDB-10.1.22

	CentOS-7-1611


## 发布项目：

>1、该项目依赖Identity，需要先部署身份系统，具体详见：

	https://github.com/dianbaer/Identity

>2、安装数据库
	
	create database pay
	
	source ****/pay.sql

>3、将PayConfig放入服务器某个路径，例如
	
	/home/PayConfig

>4、将PayServer.properties放入tomcat根目录下，例如
	
	/home/tomcat/PayServer.properties
	
	并修改config_dir对应的PayConfig路径

>5、将PayClient与PayServer.war放入tomcat/webapps，例如
	
	/home/tomcat/webapps/PayServer.war
	
	/home/tomcat/webapps/PayClient


## Pay功能：

>1、创建支付应用（通过支付应用id号，进行支付）：
	
	创建、修改、获取支付应用，获取支付应用列表。
	
>2、创建订单（通过支付应用id与该应用自己产生的订单号，创建订单）：

	创建、修改、获取订单，获取订单列表。
	
>3、通过token与支付中心产生的订单号进行支付（基于Identity的token，如果对接其他身份系统需要二次开发）
	
	例如：https://xxx/loginPayCenter.html?token=248778127e2f4937b570825a5f80b61a&orderRecordId=2d67e747337240e98dbc202dbe92538e
	
>4、支付环节（目前只支持支付宝）

	与支付宝进行的支付环节，支持RSA或MD5
	
>5、支付成功回调及通知（基于服务器统一线程模型，高效且线程安全）

	创建订单时，填写回调地址与通知地址，支付成功后会回调相应URL并通知相应URL
	例如：https://xxx/success.html?orderRecordId=3440682343a646fc804cc79014375345&orderRecordOrderId=F264DA7A-1DED-474B-A559-51826316D02E&orderRecordPayStatus=2&notifyId=44bc0a7ff7b1428991b2df7420c59779

