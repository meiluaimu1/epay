package service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.grain.httpserver.HttpException;
import org.grain.httpserver.HttpPacket;
import org.grain.httpserver.IHttpListener;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;

import action.NotifyAction;
import action.OrderRecordAction;
import action.PCErrorPack;
import config.CommonConfigPayCenter;
import config.NotifyConfig;
import config.OrderRecordConfig;
import dao.model.base.Notify;
import dao.model.base.OrderGoods;
import dao.model.ext.OrderRecordExt;
import data.UserData;
import http.HOpCodePayCenter;
import protobuf.http.PCErrorProto.PCError;
import protobuf.http.PCErrorProto.PCErrorCode;
import protobuf.http.PayProto.GetPayHTMLC;
import protobuf.http.PayProto.GetPayHTMLS;
import protobuf.http.PayProto.GetReturnUrlC;
import protobuf.http.PayProto.GetReturnUrlS;
import protobuf.http.PayProto.VerifyNotifyC;
import protobuf.http.PayProto.VerifyNotifyS;

public class PayService implements IHttpListener {

	@Override
	public Map<String, String> getHttps() {
		HashMap<String, String> map = new HashMap<>();
		map.put(HOpCodePayCenter.GET_PAY_HTML, "getPayHTMLHandle");
		map.put(HOpCodePayCenter.GET_RETURN_URL, "getReturnUrlHandle");
		map.put(HOpCodePayCenter.VERIFY_NOTIFY, "verifyNotifyHandle");
		return map;
	}

	public HttpPacket verifyNotifyHandle(HttpPacket httpPacket) throws HttpException {
		VerifyNotifyC message = (VerifyNotifyC) httpPacket.getData();
		Notify notify = NotifyAction.getNotify(message.getNotifyId());
		if (notify != null && notify.getAppId().equals(message.getAppId())) {
			Date date = new Date();
			if (date.getTime() < notify.getNotifyExpireTime().getTime()) {
				VerifyNotifyS.Builder builder = VerifyNotifyS.newBuilder();
				builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
				builder.setResult(1);
				HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
				return packet;
			}
		}
		VerifyNotifyS.Builder builder = VerifyNotifyS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		builder.setResult(2);
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket getReturnUrlHandle(HttpPacket httpPacket) throws HttpException {
		GetReturnUrlC message = (GetReturnUrlC) httpPacket.getData();
		OrderRecordExt orderRecord = OrderRecordAction.getOrderRecordById(message.getOrderRecordId());
		if (orderRecord == null) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_1, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		// 该订单必须完成或者失败
		if (orderRecord.getOrderRecordPayStatus().intValue() == OrderRecordConfig.PAY_STATUS_UNPAID) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_5, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		// 该订单是否是此用户的
		UserData userData = (UserData) httpPacket.hSession.otherData;
		httpPacket.runMonitor.putMonitor("userId:" + userData.getUserId());
		httpPacket.runMonitor.putMonitor("orderRecord.getOrderRecordUserId():" + orderRecord.getOrderRecordUserId());
		if (!userData.getUserId().equals(orderRecord.getOrderRecordUserId())) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_2, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		Notify notify = NotifyAction.createNotify(orderRecord.getAppId(), orderRecord.getOrderRecordId(), NotifyConfig.TYPE_RETURN);
		if (notify == null) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_6, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		String returnUrl = OrderRecordAction.getReturnUrl(orderRecord, notify);
		if (returnUrl == null) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_7, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}

		GetReturnUrlS.Builder builder = GetReturnUrlS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		builder.setReturnUrl(returnUrl);
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket getPayHTMLHandle(HttpPacket httpPacket) throws HttpException {
		GetPayHTMLC message = (GetPayHTMLC) httpPacket.getData();
		OrderRecordExt orderRecord = OrderRecordAction.getOrderRecordById(message.getOrderRecordId());
		if (orderRecord == null) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_1, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		List<OrderGoods> orderGoodsList = OrderRecordAction.getOrderGoodsListByOrderRecordId(orderRecord.getOrderRecordId());
		if (orderGoodsList == null) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_3, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		orderRecord.orderGoodsArray = orderGoodsList;
		// 该订单是否是此用户的
		UserData userData = (UserData) httpPacket.hSession.otherData;
		if (!userData.getUserId().equals(orderRecord.getOrderRecordUserId())) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_2, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		String subject = OrderRecordAction.getSubject(orderRecord);
		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.service);
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("seller_id", AlipayConfig.seller_id);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", AlipayConfig.notify_url);
		sParaTemp.put("return_url", AlipayConfig.return_url);
		sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
		sParaTemp.put("out_trade_no", orderRecord.getOrderRecordId());
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", orderRecord.getOrderRecordTotalPrice().toString());
		sParaTemp.put("body", "none");
		// 其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
		// 如sParaTemp.put("参数名","参数值");

		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "submit");
		GetPayHTMLS.Builder builder = GetPayHTMLS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		builder.setPayHtml(sHtmlText);
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public PayService() {
		AlipayConfig.partner = CommonConfigPayCenter.ALIPAY_PARTNER;
		AlipayConfig.seller_id = CommonConfigPayCenter.ALIPAY_SELLER_ID;
		AlipayConfig.notify_url = CommonConfigPayCenter.ALIPAY_NOTIFY_URL;
		AlipayConfig.return_url = CommonConfigPayCenter.ALIPAY_RETURN_URL;
		AlipayConfig.key = CommonConfigPayCenter.ALIPAY_KEY;
		AlipayConfig.sign_type = CommonConfigPayCenter.ALIPAY_ENCRYPT_TYPE;
		AlipayConfig.private_key = CommonConfigPayCenter.ALIPAY_PRIVATE_KEY;
		AlipayConfig.alipay_public_key = CommonConfigPayCenter.ALIPAY_ALIPAY_PUBLIC_KEY;
	}

}
