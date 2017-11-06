package org.epay.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.grain.httpclient.HttpUtil;
import org.grain.httpserver.HttpConfig;

public class NotifyUtil {
	public static String send(String url) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse httpResponse = null;
		try {
			if (url.startsWith("https")) {
				httpClient = HttpClients.custom().setSSLSocketFactory(HttpUtil.sslSocketFactory).build();
			} else {
				httpClient = HttpClients.createDefault();
			}
			HttpGet httpGet = new HttpGet(url);
			HttpConfig.log.info("发送地址：" + url);
			httpResponse = httpClient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity responseEntity = httpResponse.getEntity();
				if (responseEntity != null) {
					byte[] result = EntityUtils.toByteArray(responseEntity);
					String jsonStr = new String(result, HttpConfig.ENCODE);
					return jsonStr;
				} else {
					HttpConfig.log.warn("responseEntity为空");
					return null;
				}
			} else {
				HttpConfig.log.warn("http返回码为：" + statusCode + "请注意");
				return null;
			}

		} catch (Exception e) {
			HttpConfig.log.error("http请求异常", e);
			return null;
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				HttpConfig.log.error("关闭httpClient异常", e);
			}
			try {
				if (httpResponse != null) {
					httpResponse.close();
				}
			} catch (IOException e) {
				HttpConfig.log.error("关闭httpResponse异常", e);
			}
		}

	}
}
