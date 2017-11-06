package org.epay.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epay.action.AppAction;
import org.epay.action.OrderRecordAction;
import org.epay.action.PCErrorPack;
import org.epay.data.UserData;
import org.epay.http.HOpCodePayCenter;
import org.epay.model.base.App;
import org.epay.model.base.OrderGoods;
import org.epay.model.ext.OrderRecordExt;
import org.epay.protobuf.http.OrderRecordProto.CreateOrderRecordC;
import org.epay.protobuf.http.OrderRecordProto.CreateOrderRecordS;
import org.epay.protobuf.http.OrderRecordProto.GetOrderRecordC;
import org.epay.protobuf.http.OrderRecordProto.GetOrderRecordListC;
import org.epay.protobuf.http.OrderRecordProto.GetOrderRecordListS;
import org.epay.protobuf.http.OrderRecordProto.GetOrderRecordS;
import org.epay.protobuf.http.OrderRecordProto.UpdateOrderRecordC;
import org.epay.protobuf.http.OrderRecordProto.UpdateOrderRecordS;
import org.epay.protobuf.http.PCErrorProto.PCError;
import org.epay.protobuf.http.PCErrorProto.PCErrorCode;
import org.epay.tool.PageFormat;
import org.epay.tool.PageObj;
import org.grain.httpserver.HttpException;
import org.grain.httpserver.HttpPacket;
import org.grain.httpserver.IHttpListener;

public class OrderRecordService implements IHttpListener {

	@Override
	public Map<String, String> getHttps() {
		HashMap<String, String> map = new HashMap<>();
		map.put(HOpCodePayCenter.CREATE_ORDER_RECORD, "createOrderRecordHandle");
		// map.put(HOpCodePayCenter.UPDATE_ORDER_RECORD, "updateOrderRecordHandle");
		map.put(HOpCodePayCenter.GET_ORDER_RECORD, "getOrderRecordHandle");
		map.put(HOpCodePayCenter.GET_ORDER_RECORD_LIST, "getOrderRecordListHandle");
		return map;
	}

	public HttpPacket createOrderRecordHandle(HttpPacket httpPacket) {
		CreateOrderRecordC message = (CreateOrderRecordC) httpPacket.getData();
		String appId = message.getAppId();
		App app = AppAction.getAppById(appId);
		if (app == null) {
			return null;
		}
		OrderRecordExt orderRecord = OrderRecordAction.createOrderRecord(appId, message.getOrderRecordOrderId(), message.getOrderRecordTotalPrice(), message.getOrderRecordUserId(), message.getOrderRecordOrderDetail(), message.getOrderRecordUserName(), message.getOrderRecordReturnUrl(), message.getOrderRecordNotifyUrl(), message.getOrderGoodsArrayList());
		if (orderRecord == null) {
			return null;
		}
		CreateOrderRecordS.Builder builder = CreateOrderRecordS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		builder.setOrderRecordData(OrderRecordAction.getOrderRecordBuilder(orderRecord));
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket updateOrderRecordHandle(HttpPacket httpPacket) {
		UpdateOrderRecordC message = (UpdateOrderRecordC) httpPacket.getData();
		OrderRecordExt orderRecord = OrderRecordAction.updateOrderRecord(message.getOrderRecordId(), message.getOrderRecordPayStatus(), message.getOrderRecordStatus(), message.getOrderRecordPayChannel(), message.getOrderRecordNotifyResult(), message.getOrderRecordNotifyTime());
		if (orderRecord == null) {
			return null;
		}
		List<OrderGoods> orderGoodsList = OrderRecordAction.getOrderGoodsListByOrderRecordId(orderRecord.getOrderRecordId());
		if (orderGoodsList == null) {
			return null;
		}
		orderRecord.orderGoodsArray = orderGoodsList;
		UpdateOrderRecordS.Builder builder = UpdateOrderRecordS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		builder.setOrderRecordData(OrderRecordAction.getOrderRecordBuilder(orderRecord));
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket getOrderRecordHandle(HttpPacket httpPacket) throws HttpException {
		GetOrderRecordC message = (GetOrderRecordC) httpPacket.getData();
		OrderRecordExt orderRecord = OrderRecordAction.getOrderRecordById(message.getOrderRecordId());
		if (orderRecord == null) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_1, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		// 该订单是否是此用户的
		UserData userData = (UserData) httpPacket.hSession.otherData;
		if (!userData.getUserId().equals(orderRecord.getOrderRecordUserId())) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_2, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		List<OrderGoods> orderGoodsList = OrderRecordAction.getOrderGoodsListByOrderRecordId(orderRecord.getOrderRecordId());
		if (orderGoodsList == null) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_3, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		orderRecord.orderGoodsArray = orderGoodsList;
		GetOrderRecordS.Builder builder = GetOrderRecordS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		builder.setOrderRecordData(OrderRecordAction.getOrderRecordBuilder(orderRecord));
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket getOrderRecordListHandle(HttpPacket httpPacket) {
		GetOrderRecordListC message = (GetOrderRecordListC) httpPacket.getData();
		long count = OrderRecordAction.getOrderRecordListCount(message.getAppId(), message.getOrderRecordOrderId(), message.getOrderRecordCreateTimeGreaterThan(), message.getOrderRecordCreateTimeLessThan(), message.getOrderRecordPayStatus(), message.getOrderRecordStatus(), message.getOrderRecordUserId(), message.getOrderRecordPayChannel(), message.getOrderRecordOrderDetail(), message.getOrderRecordNotifyResult());
		if (count == -1) {
			return null;
		}
		PageObj pageObj = PageFormat.getStartAndEnd(message.getCurrentPage(), message.getPageSize(), (int) count);
		List<OrderRecordExt> orderRecordList = OrderRecordAction.getOrderRecordList(message.getAppId(), message.getOrderRecordOrderId(), message.getOrderRecordCreateTimeGreaterThan(), message.getOrderRecordCreateTimeLessThan(), message.getOrderRecordPayStatus(), message.getOrderRecordStatus(), message.getOrderRecordUserId(), message.getOrderRecordPayChannel(), message.getOrderRecordOrderDetail(), message.getOrderRecordNotifyResult(), pageObj.start, pageObj.pageSize);
		if (orderRecordList == null) {
			return null;
		}
		GetOrderRecordListS.Builder builder = GetOrderRecordListS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		for (int i = 0; i < orderRecordList.size(); i++) {
			OrderRecordExt orderRecord = orderRecordList.get(i);
			List<OrderGoods> orderGoodsList = OrderRecordAction.getOrderGoodsListByOrderRecordId(orderRecord.getOrderRecordId());
			orderRecord.orderGoodsArray = orderGoodsList;
			builder.addOrderRecordList(OrderRecordAction.getOrderRecordBuilder(orderRecord));
		}
		builder.setCurrentPage(pageObj.currentPage);
		builder.setAllNum(pageObj.allNum);
		builder.setPageSize(pageObj.pageSize);
		builder.setTotalPage(pageObj.totalPage);
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}

}
