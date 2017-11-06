package org.epay.http;

import org.epay.protobuf.http.AppProto.CreateAppC;
import org.epay.protobuf.http.AppProto.CreateAppS;
import org.epay.protobuf.http.AppProto.GetAppC;
import org.epay.protobuf.http.AppProto.GetAppListC;
import org.epay.protobuf.http.AppProto.GetAppListS;
import org.epay.protobuf.http.AppProto.GetAppS;
import org.epay.protobuf.http.AppProto.UpdateAppC;
import org.epay.protobuf.http.AppProto.UpdateAppS;
import org.epay.protobuf.http.LoginProto.LoginC;
import org.epay.protobuf.http.LoginProto.LoginS;
import org.epay.protobuf.http.OrderRecordProto.CreateOrderRecordC;
import org.epay.protobuf.http.OrderRecordProto.CreateOrderRecordS;
import org.epay.protobuf.http.OrderRecordProto.GetOrderRecordC;
import org.epay.protobuf.http.OrderRecordProto.GetOrderRecordListC;
import org.epay.protobuf.http.OrderRecordProto.GetOrderRecordListS;
import org.epay.protobuf.http.OrderRecordProto.GetOrderRecordS;
import org.epay.protobuf.http.OrderRecordProto.UpdateOrderRecordC;
import org.epay.protobuf.http.OrderRecordProto.UpdateOrderRecordS;
import org.epay.protobuf.http.PCErrorProto.PCError;
import org.epay.protobuf.http.PayProto.GetPayHTMLC;
import org.epay.protobuf.http.PayProto.GetPayHTMLS;
import org.epay.protobuf.http.PayProto.GetReturnUrlC;
import org.epay.protobuf.http.PayProto.GetReturnUrlS;
import org.epay.protobuf.http.PayProto.VerifyNotifyC;
import org.epay.protobuf.http.PayProto.VerifyNotifyS;
import org.grain.httpserver.HttpManager;

public class HOpCodePayCenter {

	public static String PC_ERROR = "199";

	public static String CREATE_APP = "200";
	public static String UPDATE_APP = "201";
	public static String GET_APP = "202";
	public static String GET_APP_LIST = "203";

	public static String CREATE_ORDER_RECORD = "210";
	public static String UPDATE_ORDER_RECORD = "211";
	public static String GET_ORDER_RECORD = "212";
	public static String GET_ORDER_RECORD_LIST = "213";

	public static String GET_PAY_HTML = "220";
	public static String GET_RETURN_URL = "221";
	public static String VERIFY_NOTIFY = "222";

	public static String LOGIN = "230";

	public static void init() {

		HttpManager.addMapping(PC_ERROR, null, PCError.class);
		HttpManager.addMapping(CREATE_APP, CreateAppC.class, CreateAppS.class);
		HttpManager.addMapping(UPDATE_APP, UpdateAppC.class, UpdateAppS.class);
		HttpManager.addMapping(GET_APP, GetAppC.class, GetAppS.class);
		HttpManager.addMapping(GET_APP_LIST, GetAppListC.class, GetAppListS.class);
		HttpManager.addMapping(CREATE_ORDER_RECORD, CreateOrderRecordC.class, CreateOrderRecordS.class);
		HttpManager.addMapping(UPDATE_ORDER_RECORD, UpdateOrderRecordC.class, UpdateOrderRecordS.class);
		HttpManager.addMapping(GET_ORDER_RECORD, GetOrderRecordC.class, GetOrderRecordS.class);
		HttpManager.addMapping(GET_ORDER_RECORD_LIST, GetOrderRecordListC.class, GetOrderRecordListS.class);
		HttpManager.addMapping(GET_PAY_HTML, GetPayHTMLC.class, GetPayHTMLS.class);
		HttpManager.addMapping(GET_RETURN_URL, GetReturnUrlC.class, GetReturnUrlS.class);
		HttpManager.addMapping(VERIFY_NOTIFY, VerifyNotifyC.class, VerifyNotifyS.class);
		HttpManager.addMapping(LOGIN, LoginC.class, LoginS.class);

	}
}
