package config;

import java.util.Properties;

public class CommonConfigPayCenter {

	public static String ALIPAY_PARTNER;
	public static String ALIPAY_SELLER_ID;
	public static String ALIPAY_KEY;
	public static String ALIPAY_NOTIFY_URL;
	public static String ALIPAY_RETURN_URL;
	public static String ALIPAY_ENCRYPT_TYPE;
	public static String ALIPAY_PRIVATE_KEY;
	public static String ALIPAY_ALIPAY_PUBLIC_KEY;

	public static String UCENTER_URL;

	public static String PAY_SUCCESS_URL;
	public static String PAY_FAIL_URL;

	public static int NOTIFY_EXPIRE_TIME;

	public static int MAX_NOTIFY_TIME;

	public static int NOTIFY_INTERVAL;

	public static void init(Properties properties) {

		ALIPAY_PARTNER = properties.getProperty("alipay_partner");
		ALIPAY_SELLER_ID = properties.getProperty("alipay_seller_id");
		ALIPAY_KEY = properties.getProperty("alipay_key");
		ALIPAY_NOTIFY_URL = properties.getProperty("alipay_notify_url");
		ALIPAY_RETURN_URL = properties.getProperty("alipay_return_url");
		ALIPAY_ENCRYPT_TYPE = properties.getProperty("alipay_encrypt_type");
		ALIPAY_PRIVATE_KEY = properties.getProperty("alipay_private_key");
		ALIPAY_ALIPAY_PUBLIC_KEY = properties.getProperty("alipay_alipay_public_key");

		UCENTER_URL = properties.getProperty("ucenter_url");
		PAY_SUCCESS_URL = properties.getProperty("pay_success_url");
		PAY_FAIL_URL = properties.getProperty("pay_fail_url");

		NOTIFY_EXPIRE_TIME = Integer.valueOf(properties.getProperty("notify_expire_time"));

		MAX_NOTIFY_TIME = Integer.valueOf(properties.getProperty("max_notify_time"));

		NOTIFY_INTERVAL = Integer.valueOf(properties.getProperty("notify_interval"));

	}
}
