package org.epay.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.epay.config.CommonConfigPayCenter;
import org.epay.http.HOpCodePayCenter;
import org.epay.http.filter.TokenHttpFilter;
import org.epay.log.MariadbLog;
import org.epay.msg.MsgOpCodePayCenter;
import org.epay.service.AppService;
import org.epay.service.LoginService;
import org.epay.service.NotifyService;
import org.epay.service.OrderRecordService;
import org.epay.service.PayService;
import org.grain.httpclient.HttpUtil;
import org.grain.httpserver.HttpConfig;
import org.grain.httpserver.HttpManager;
import org.grain.httpserver.IExpandServer;
import org.grain.mariadb.MybatisManager;
import org.grain.msg.MsgManager;
import org.grain.thread.AsyncThreadManager;

public class Expand implements IExpandServer {

	@Override
	public void init(HttpServlet servlet) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		ServletContext servletContext = servlet.getServletContext();
		String configFileName = servletContext.getInitParameter("configFileName");
		Properties properties = loadConfig(configFileName);
		MybatisManager.init(properties.getProperty("config_dir"), "mybatis-config.xml", new MariadbLog());
		HttpUtil.init("UTF-8", null);
		AsyncThreadManager.init(100, 10, 3, 0, null);
		AsyncThreadManager.start();
		MsgManager.init(true, null);
		CommonConfigPayCenter.init(properties);
		HOpCodePayCenter.init();

		MsgOpCodePayCenter.init();
		HttpManager.addFilter(new TokenHttpFilter());
		HttpManager.addHttpListener(new AppService());
		HttpManager.addHttpListener(new OrderRecordService());
		HttpManager.addHttpListener(new PayService());
		HttpManager.addHttpListener(new LoginService());
		MsgManager.addMsgListener(new NotifyService(10));
	}

	private Properties loadConfig(String configFileName) throws Exception {
		HttpConfig.log.info("初始化基础配置文件");
		InputStream inputStream = null;
		URL url = this.getClass().getClassLoader().getResource(configFileName);
		if (url != null) {
			HttpConfig.log.info("Init.class.getClassLoader().getResource找到配置文件，路径为：" + url.getPath());
			inputStream = this.getClass().getClassLoader().getResourceAsStream(configFileName);
		} else {
			HttpConfig.log.info("Init.class.getClassLoader().getResource：" + this.getClass().getClassLoader().getResource("").getPath() + "，未找到配置文件：" + configFileName);
		}
		if (inputStream == null) {
			File file = new File(System.getProperty("catalina.base") + "/" + configFileName);
			if (file.exists()) {
				HttpConfig.log.info("System.getProperty(\"catalina.base\")找到配置文件，路径为" + System.getProperty("catalina.base") + "/" + configFileName);
				inputStream = new FileInputStream(file);
			} else {
				HttpConfig.log.info("System.getProperty(\"catalina.base\")：" + System.getProperty("catalina.base") + "，未找到配置文件：" + configFileName);
			}
		}
		if (inputStream == null) {
			File file = new File(configFileName);
			if (file.exists()) {
				HttpConfig.log.info("找到配置文件，路径为" + file.getAbsolutePath());
				inputStream = new FileInputStream(file);
			} else {
				HttpConfig.log.info("未找到配置文件：" + configFileName);
			}
		}
		if (inputStream != null) {
			Properties properties = new Properties();
			properties.load(inputStream);
			HttpConfig.log.info("初始化基础配置文件完成");
			inputStream.close();
			return properties;
		} else {
			HttpConfig.log.warn("未找到配置文件：" + configFileName);
			throw new Exception("未找到配置文件" + configFileName);
		}
	}

}
