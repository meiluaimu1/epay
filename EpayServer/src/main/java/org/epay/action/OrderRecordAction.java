package org.epay.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.epay.config.OrderRecordConfig;
import org.epay.dao.base.OrderGoodsMapper;
import org.epay.dao.base.OrderRecordMapper;
import org.epay.dao.ext.OrderRecordMapperExt;
import org.epay.model.base.App;
import org.epay.model.base.Notify;
import org.epay.model.base.OrderGoods;
import org.epay.model.base.OrderGoodsCriteria;
import org.epay.model.base.OrderRecord;
import org.epay.model.base.OrderRecordCriteria;
import org.epay.model.ext.OrderRecordExt;
import org.epay.protobuf.http.OrderRecordProto.OrderGoodsData;
import org.epay.protobuf.http.OrderRecordProto.OrderGoodsSendData;
import org.epay.protobuf.http.OrderRecordProto.OrderRecordData;
import org.epay.tool.StringUtil;
import org.epay.tool.TimeUtils;
import org.epay.util.IdUtil;
import org.grain.mariadb.MybatisManager;

public class OrderRecordAction {
	public static OrderRecordExt createOrderRecord(String appId, String orderRecordOrderId, double orderRecordTotalPrice, String orderRecordUserId, String orderRecordOrderDetail, String orderRecordUserName, String orderRecordReturnUrl, String orderRecordNotifyUrl, List<OrderGoodsSendData> orderGoodsArray) {
		if (StringUtil.stringIsNull(appId) || StringUtil.stringIsNull(orderRecordOrderId) || StringUtil.stringIsNull(orderRecordUserId)) {
			return null;
		}
		Date date = new Date();
		OrderRecordExt orderRecord = new OrderRecordExt();
		orderRecord.setOrderRecordId(IdUtil.getUuid());
		orderRecord.setAppId(appId);
		orderRecord.setOrderRecordOrderId(orderRecordOrderId);
		orderRecord.setOrderRecordTotalPrice(orderRecordTotalPrice);
		orderRecord.setOrderRecordCreateTime(date);
		orderRecord.setOrderRecordUpdateTime(date);
		orderRecord.setOrderRecordPayStatus((byte) OrderRecordConfig.PAY_STATUS_UNPAID);
		orderRecord.setOrderRecordStatus((byte) OrderRecordConfig.STATUS_USABLE);
		orderRecord.setOrderRecordUserId(orderRecordUserId);
		if (!StringUtil.stringIsNull(orderRecordOrderDetail)) {
			orderRecord.setOrderRecordOrderDetail(orderRecordOrderDetail);
		}
		if (!StringUtil.stringIsNull(orderRecordUserName)) {
			orderRecord.setOrderRecordUserName(orderRecordUserName);
		}
		if (!StringUtil.stringIsNull(orderRecordReturnUrl)) {
			orderRecord.setOrderRecordReturnUrl(orderRecordReturnUrl);
		}
		if (!StringUtil.stringIsNull(orderRecordNotifyUrl)) {
			orderRecord.setOrderRecordNotifyUrl(orderRecordNotifyUrl);
		}
		orderRecord.setOrderRecordNotifyResult((byte) OrderRecordConfig.NOTIFY_RESULT_NOT);
		orderRecord.setOrderRecordNotifyTime((byte) 0);
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			OrderRecordMapper orderRecordMapper = sqlSession.getMapper(OrderRecordMapper.class);
			OrderGoodsMapper orderGoodsMapper = sqlSession.getMapper(OrderGoodsMapper.class);
			int result = orderRecordMapper.insert(orderRecord);
			if (result != 1) {
				MybatisManager.log.warn("创建OrderRecord失败");
				throw new Exception("创建OrderRecord错误");
			}
			for (int i = 0; i < orderGoodsArray.size(); i++) {
				OrderGoodsSendData orderGoodsSendData = orderGoodsArray.get(i);
				OrderGoods orderGoods = new OrderGoods();
				orderGoods.setOrderGoodsId(IdUtil.getUuid());
				orderGoods.setOrderRecordId(orderRecord.getOrderRecordId());
				orderGoods.setOrderGoodsGoodsId(orderGoodsSendData.getOrderGoodsGoodsId());
				orderGoods.setOrderGoodsName(orderGoodsSendData.getOrderGoodsName());
				orderGoods.setOrderGoodsPrice(orderGoodsSendData.getOrderGoodsPrice());
				orderGoods.setOrderGoodsNumber(orderGoodsSendData.getOrderGoodsNumber());
				if (!StringUtil.stringIsNull(orderGoodsSendData.getOrderGoodsDetail())) {
					orderGoods.setOrderGoodsDetail(orderGoodsSendData.getOrderGoodsDetail());
				}
				if (!StringUtil.stringIsNull(orderGoodsSendData.getOrderGoodsUrl())) {
					orderGoods.setOrderGoodsUrl(orderGoodsSendData.getOrderGoodsUrl());
				}
				result = orderGoodsMapper.insert(orderGoods);
				if (result != 1) {
					MybatisManager.log.warn("创建OrderGoods失败");
					throw new Exception("创建OrderGoods错误");
				}
				orderRecord.addOrderGoods(orderGoods);
			}
			sqlSession.commit();
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("创建OrderRecord失败", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return orderRecord;
	}

	public static OrderRecordData.Builder getOrderRecordBuilder(OrderRecordExt orderRecord) {
		OrderRecordData.Builder dataBuilder = OrderRecordData.newBuilder();
		dataBuilder.setOrderRecordId(orderRecord.getOrderRecordId());
		dataBuilder.setAppId(orderRecord.getAppId());
		dataBuilder.setOrderRecordOrderId(orderRecord.getOrderRecordOrderId());
		dataBuilder.setOrderRecordTotalPrice(orderRecord.getOrderRecordTotalPrice());
		dataBuilder.setOrderRecordCreateTime(TimeUtils.dateToString(orderRecord.getOrderRecordCreateTime()));
		dataBuilder.setOrderRecordUpdateTime(TimeUtils.dateToString(orderRecord.getOrderRecordUpdateTime()));
		dataBuilder.setOrderRecordPayStatus(orderRecord.getOrderRecordPayStatus());
		dataBuilder.setOrderRecordStatus(orderRecord.getOrderRecordStatus());
		dataBuilder.setOrderRecordUserId(orderRecord.getOrderRecordUserId());
		if (orderRecord.getOrderRecordPayChannel() != null) {
			dataBuilder.setOrderRecordPayChannel(orderRecord.getOrderRecordPayChannel());
		}
		if (orderRecord.getOrderRecordOrderDetail() != null) {
			dataBuilder.setOrderRecordOrderDetail(orderRecord.getOrderRecordOrderDetail());
		}
		if (orderRecord.getOrderRecordUserName() != null) {
			dataBuilder.setOrderRecordUserName(orderRecord.getOrderRecordUserName());
		}
		if (orderRecord.getOrderRecordReturnUrl() != null) {
			dataBuilder.setOrderRecordReturnUrl(orderRecord.getOrderRecordReturnUrl());
		}
		if (orderRecord.getOrderRecordNotifyUrl() != null) {
			dataBuilder.setOrderRecordNotifyUrl(orderRecord.getOrderRecordNotifyUrl());
		}
		dataBuilder.setOrderRecordNotifyResult(orderRecord.getOrderRecordNotifyResult());
		dataBuilder.setOrderRecordNotifyTime(orderRecord.getOrderRecordNotifyTime());
		if (orderRecord.getTradeNo() != null) {
			dataBuilder.setTradeNo(orderRecord.getTradeNo());
		}
		if (orderRecord.getBuyerEmail() != null) {
			dataBuilder.setBuyerEmail(orderRecord.getBuyerEmail());
		}
		if (orderRecord.getNotifyTime() != null) {
			dataBuilder.setNotifyTime(TimeUtils.dateToString(orderRecord.getNotifyTime()));
		}
		if (orderRecord.getBuyerId() != null) {
			dataBuilder.setBuyerId(orderRecord.getBuyerId());
		}
		if (orderRecord.getTotalFee() != null) {
			dataBuilder.setTotalFee(orderRecord.getTotalFee());
		}
		if (orderRecord.getTradeStatus() != null) {
			dataBuilder.setTradeStatus(orderRecord.getTradeStatus());
		}
		for (int i = 0; i < orderRecord.orderGoodsArray.size(); i++) {
			OrderGoods orderGoods = orderRecord.orderGoodsArray.get(i);
			OrderGoodsData.Builder goodsBuilder = OrderGoodsData.newBuilder();
			goodsBuilder.setOrderGoodsId(orderGoods.getOrderGoodsId());
			goodsBuilder.setOrderRecordId(orderGoods.getOrderRecordId());
			goodsBuilder.setOrderGoodsGoodsId(orderGoods.getOrderGoodsGoodsId());
			goodsBuilder.setOrderGoodsName(orderGoods.getOrderGoodsName());
			goodsBuilder.setOrderGoodsPrice(orderGoods.getOrderGoodsPrice());
			goodsBuilder.setOrderGoodsNumber(orderGoods.getOrderGoodsNumber());
			if (orderGoods.getOrderGoodsDetail() != null) {
				goodsBuilder.setOrderGoodsDetail(orderGoods.getOrderGoodsDetail());
			}
			if (orderGoods.getOrderGoodsUrl() != null) {
				goodsBuilder.setOrderGoodsUrl(orderGoods.getOrderGoodsUrl());
			}
			dataBuilder.addOrderGoodsArray(goodsBuilder);
		}
		return dataBuilder;
	}

	public static OrderRecordExt getOrderRecordById(String orderRecordId) {
		if (StringUtil.stringIsNull(orderRecordId)) {
			return null;
		}
		SqlSession sqlSession = null;
		OrderRecordExt orderRecord;
		try {
			sqlSession = MybatisManager.getSqlSession();
			OrderRecordMapperExt orderRecordMapper = sqlSession.getMapper(OrderRecordMapperExt.class);
			orderRecord = orderRecordMapper.selectByPrimaryKey(orderRecordId);
			if (orderRecord == null) {
				MybatisManager.log.warn("通过orderRecordId:" + orderRecordId + "获取支付记录为空");
			}
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("获取支付记录异常", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return orderRecord;
	}

	public static OrderRecordExt updateOrderRecord(String orderRecordId, int orderRecordPayStatus, int orderRecordStatus, int orderRecordPayChannel, int orderRecordNotifyResult, int orderRecordNotifyTime) {
		if (StringUtil.stringIsNull(orderRecordId)) {
			return null;
		}
		OrderRecordExt orderRecord = getOrderRecordById(orderRecordId);
		if (orderRecord == null) {
			return null;
		}
		Date date = new Date();
		OrderRecord orderRecordNew = new OrderRecord();
		orderRecordNew.setOrderRecordId(orderRecordId);
		orderRecordNew.setOrderRecordUpdateTime(date);

		if (orderRecordPayStatus == OrderRecordConfig.PAY_STATUS_UNPAID || orderRecordPayStatus == OrderRecordConfig.PAY_STATUS_ALREADY || orderRecordPayStatus == OrderRecordConfig.PAY_STATUS_UNQUALIFIED) {
			orderRecordNew.setOrderRecordPayStatus((byte) orderRecordPayStatus);
		}
		if (orderRecordStatus == OrderRecordConfig.STATUS_USABLE || orderRecordStatus == OrderRecordConfig.STATUS_DELETE) {
			orderRecordNew.setOrderRecordStatus((byte) orderRecordStatus);
		}
		if (orderRecordPayChannel == OrderRecordConfig.PAY_CHANNEL_ALIPAY) {
			orderRecordNew.setOrderRecordPayChannel((byte) orderRecordPayChannel);
		}
		if (orderRecordNotifyResult == OrderRecordConfig.NOTIFY_RESULT_YES || orderRecordNotifyResult == OrderRecordConfig.NOTIFY_RESULT_NOT) {
			orderRecordNew.setOrderRecordNotifyResult((byte) orderRecordNotifyResult);
		}
		if (orderRecordNotifyTime > 0) {
			orderRecordNew.setOrderRecordNotifyTime((byte) orderRecordNotifyTime);
		}
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			OrderRecordMapper orderRecordMapper = sqlSession.getMapper(OrderRecordMapper.class);
			int result = orderRecordMapper.updateByPrimaryKeySelective(orderRecordNew);
			if (result != 1) {
				MybatisManager.log.warn("修改支付记录失败");
				return null;
			}
			sqlSession.commit();
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("修改支付记录异常", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return getOrderRecordById(orderRecordNew.getOrderRecordId());
	}

	public static List<OrderGoods> getOrderGoodsListByOrderRecordId(String orderRecordId) {
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			OrderGoodsMapper orderGoodsMapper = sqlSession.getMapper(OrderGoodsMapper.class);
			OrderGoodsCriteria orderGoodsCriteria = new OrderGoodsCriteria();
			OrderGoodsCriteria.Criteria criteria = orderGoodsCriteria.createCriteria();
			criteria.andOrderRecordIdEqualTo(orderRecordId);
			List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByExample(orderGoodsCriteria);
			return orderGoodsList;
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("获取支付记录商品失败", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	public static long getOrderRecordListCount(String appId, String orderRecordOrderId, String orderRecordCreateTimeGreaterThan, String orderRecordCreateTimeLessThan, int orderRecordPayStatus, int orderRecordStatus, String orderRecordUserId, int orderRecordPayChannel, String orderRecordOrderDetail, int orderRecordNotifyResult) {
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			OrderRecordMapper orderRecordMapper = sqlSession.getMapper(OrderRecordMapper.class);
			OrderRecordCriteria orderRecordCriteria = new OrderRecordCriteria();
			OrderRecordCriteria.Criteria criteriaOrderRecord = orderRecordCriteria.createCriteria();
			if (!StringUtil.stringIsNull(appId)) {
				criteriaOrderRecord.andAppIdEqualTo(appId);
			}
			if (!StringUtil.stringIsNull(orderRecordOrderId)) {
				criteriaOrderRecord.andOrderRecordOrderIdEqualTo(orderRecordOrderId);
			}
			if (!StringUtil.stringIsNull(orderRecordCreateTimeGreaterThan)) {
				Date orderRecordCreateTimeGreaterThanDate = TimeUtils.stringToDate(orderRecordCreateTimeGreaterThan);
				if (orderRecordCreateTimeGreaterThanDate != null) {
					criteriaOrderRecord.andOrderRecordCreateTimeGreaterThanOrEqualTo(orderRecordCreateTimeGreaterThanDate);
				}
			}
			if (!StringUtil.stringIsNull(orderRecordCreateTimeLessThan)) {
				Date orderRecordCreateTimeLessThanDate = TimeUtils.stringToDate(orderRecordCreateTimeLessThan);
				if (orderRecordCreateTimeLessThanDate != null) {
					criteriaOrderRecord.andOrderRecordCreateTimeLessThanOrEqualTo(orderRecordCreateTimeLessThanDate);
				}
			}
			if (orderRecordPayStatus == OrderRecordConfig.PAY_STATUS_UNPAID || orderRecordPayStatus == OrderRecordConfig.PAY_STATUS_ALREADY || orderRecordPayStatus == OrderRecordConfig.PAY_STATUS_UNQUALIFIED) {
				criteriaOrderRecord.andOrderRecordPayStatusEqualTo((byte) orderRecordPayStatus);
			}
			if (orderRecordStatus == OrderRecordConfig.STATUS_USABLE || orderRecordStatus == OrderRecordConfig.STATUS_DELETE) {
				criteriaOrderRecord.andOrderRecordStatusEqualTo((byte) orderRecordStatus);
			}
			if (!StringUtil.stringIsNull(orderRecordUserId)) {
				criteriaOrderRecord.andOrderRecordUserIdEqualTo(orderRecordUserId);
			}
			if (orderRecordPayChannel == OrderRecordConfig.PAY_CHANNEL_ALIPAY) {
				criteriaOrderRecord.andOrderRecordPayChannelEqualTo((byte) orderRecordPayChannel);
			}
			if (orderRecordNotifyResult == OrderRecordConfig.NOTIFY_RESULT_YES || orderRecordNotifyResult == OrderRecordConfig.NOTIFY_RESULT_NOT) {
				criteriaOrderRecord.andOrderRecordNotifyResultEqualTo((byte) orderRecordNotifyResult);
			}
			orderRecordCriteria.setOrderByClause("order_record_create_time asc");
			long count = orderRecordMapper.countByExample(orderRecordCriteria);
			return count;
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("查询OrderRecordlistcount失败", e);
			return -1;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	public static List<OrderRecordExt> getOrderRecordList(String appId, String orderRecordOrderId, String orderRecordCreateTimeGreaterThan, String orderRecordCreateTimeLessThan, int orderRecordPayStatus, int orderRecordStatus, String orderRecordUserId, int orderRecordPayChannel, String orderRecordOrderDetail, int orderRecordNotifyResult, int limitStart, int pageSize) {
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			OrderRecordMapperExt orderRecordMapper = sqlSession.getMapper(OrderRecordMapperExt.class);
			OrderRecordCriteria orderRecordCriteria = new OrderRecordCriteria();
			OrderRecordCriteria.Criteria criteriaOrderRecord = orderRecordCriteria.createCriteria();
			if (!StringUtil.stringIsNull(appId)) {
				criteriaOrderRecord.andAppIdEqualTo(appId);
			}
			if (!StringUtil.stringIsNull(orderRecordOrderId)) {
				criteriaOrderRecord.andOrderRecordOrderIdEqualTo(orderRecordOrderId);
			}
			if (!StringUtil.stringIsNull(orderRecordCreateTimeGreaterThan)) {
				Date orderRecordCreateTimeGreaterThanDate = TimeUtils.stringToDate(orderRecordCreateTimeGreaterThan);
				if (orderRecordCreateTimeGreaterThanDate != null) {
					criteriaOrderRecord.andOrderRecordCreateTimeGreaterThanOrEqualTo(orderRecordCreateTimeGreaterThanDate);
				}
			}
			if (!StringUtil.stringIsNull(orderRecordCreateTimeLessThan)) {
				Date orderRecordCreateTimeLessThanDate = TimeUtils.stringToDate(orderRecordCreateTimeLessThan);
				if (orderRecordCreateTimeLessThanDate != null) {
					criteriaOrderRecord.andOrderRecordCreateTimeLessThanOrEqualTo(orderRecordCreateTimeLessThanDate);
				}
			}
			if (orderRecordPayStatus == OrderRecordConfig.PAY_STATUS_UNPAID || orderRecordPayStatus == OrderRecordConfig.PAY_STATUS_ALREADY || orderRecordPayStatus == OrderRecordConfig.PAY_STATUS_UNQUALIFIED) {
				criteriaOrderRecord.andOrderRecordPayStatusEqualTo((byte) orderRecordPayStatus);
			}
			if (orderRecordStatus == OrderRecordConfig.STATUS_USABLE || orderRecordStatus == OrderRecordConfig.STATUS_DELETE) {
				criteriaOrderRecord.andOrderRecordStatusEqualTo((byte) orderRecordStatus);
			}
			if (!StringUtil.stringIsNull(orderRecordUserId)) {
				criteriaOrderRecord.andOrderRecordUserIdEqualTo(orderRecordUserId);
			}
			if (orderRecordPayChannel == OrderRecordConfig.PAY_CHANNEL_ALIPAY) {
				criteriaOrderRecord.andOrderRecordPayChannelEqualTo((byte) orderRecordPayChannel);
			}
			if (orderRecordNotifyResult == OrderRecordConfig.NOTIFY_RESULT_YES || orderRecordNotifyResult == OrderRecordConfig.NOTIFY_RESULT_NOT) {
				criteriaOrderRecord.andOrderRecordNotifyResultEqualTo((byte) orderRecordNotifyResult);
			}
			orderRecordCriteria.setOrderByClause("order_record_create_time asc");
			if (pageSize > 0) {
				orderRecordCriteria.setLimitStart(limitStart);
				orderRecordCriteria.setPageSize(pageSize);
			}
			List<OrderRecordExt> orderRecordList = orderRecordMapper.selectByExample(orderRecordCriteria);
			return orderRecordList;
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("查询OrderRecordlist失败", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	// 设置订单不合法
	public static boolean setOrderRecordPriceNotSame(String orderRecordId, double totalFee, String buyerEmail, String tradeNo, String notifyTime, String buyerId, String tradeStatus) {
		if (StringUtil.stringIsNull(orderRecordId)) {
			return false;
		}
		Date date = new Date();
		OrderRecord orderRecordNew = new OrderRecord();
		orderRecordNew.setTotalFee(totalFee);
		orderRecordNew.setOrderRecordUpdateTime(date);
		if (!StringUtil.stringIsNull(buyerEmail)) {
			orderRecordNew.setBuyerEmail(buyerEmail);
		}
		if (!StringUtil.stringIsNull(tradeNo)) {
			orderRecordNew.setTradeNo(tradeNo);
		}
		if (!StringUtil.stringIsNull(notifyTime)) {
			Date notifyTimeDate = TimeUtils.stringToDate(notifyTime);
			if (notifyTimeDate != null) {
				orderRecordNew.setNotifyTime(notifyTimeDate);
			}
		}
		if (!StringUtil.stringIsNull(buyerId)) {
			orderRecordNew.setBuyerId(buyerId);
		}
		if (!StringUtil.stringIsNull(tradeStatus)) {
			orderRecordNew.setTradeStatus(tradeStatus);
		}
		orderRecordNew.setOrderRecordPayStatus((byte) OrderRecordConfig.PAY_STATUS_UNQUALIFIED);
		orderRecordNew.setOrderRecordPayChannel((byte) OrderRecordConfig.PAY_CHANNEL_ALIPAY);
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			OrderRecordMapper orderRecordMapper = sqlSession.getMapper(OrderRecordMapper.class);
			OrderRecordCriteria orderRecordCriteria = new OrderRecordCriteria();
			OrderRecordCriteria.Criteria criteriaOrderRecord = orderRecordCriteria.createCriteria();
			criteriaOrderRecord.andOrderRecordIdEqualTo(orderRecordId);
			// 如果订单已经完成或者订单已经不合法，就不做修改了。
			List<Byte> list = new ArrayList<>();
			list.add((byte) OrderRecordConfig.PAY_STATUS_ALREADY);
			list.add((byte) OrderRecordConfig.PAY_STATUS_UNQUALIFIED);
			criteriaOrderRecord.andOrderRecordPayStatusNotIn(list);
			int result = orderRecordMapper.updateByExampleSelective(orderRecordNew, orderRecordCriteria);
			if (result != 1) {
				MybatisManager.log.warn("修改支付记录失败");
				return false;
			}
			sqlSession.commit();
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("修改支付记录异常", e);
			return false;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return true;
	}

	// 设置订单完成
	public static boolean setOrderRecordSuccess(String orderRecordId, String buyerEmail, String tradeNo, String notifyTime, String buyerId, String tradeStatus) {
		if (StringUtil.stringIsNull(orderRecordId)) {
			return false;
		}
		Date date = new Date();
		OrderRecord orderRecordNew = new OrderRecord();
		orderRecordNew.setOrderRecordUpdateTime(date);
		if (!StringUtil.stringIsNull(buyerEmail)) {
			orderRecordNew.setBuyerEmail(buyerEmail);
		}
		if (!StringUtil.stringIsNull(tradeNo)) {
			orderRecordNew.setTradeNo(tradeNo);
		}
		if (!StringUtil.stringIsNull(notifyTime)) {
			Date notifyTimeDate = TimeUtils.stringToDate(notifyTime);
			if (notifyTimeDate != null) {
				orderRecordNew.setNotifyTime(notifyTimeDate);
			}
		}
		if (!StringUtil.stringIsNull(buyerId)) {
			orderRecordNew.setBuyerId(buyerId);
		}
		if (!StringUtil.stringIsNull(tradeStatus)) {
			orderRecordNew.setTradeStatus(tradeStatus);
		}
		orderRecordNew.setOrderRecordPayStatus((byte) OrderRecordConfig.PAY_STATUS_ALREADY);
		orderRecordNew.setOrderRecordPayChannel((byte) OrderRecordConfig.PAY_CHANNEL_ALIPAY);
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			OrderRecordMapper orderRecordMapper = sqlSession.getMapper(OrderRecordMapper.class);
			OrderRecordCriteria orderRecordCriteria = new OrderRecordCriteria();
			OrderRecordCriteria.Criteria criteriaOrderRecord = orderRecordCriteria.createCriteria();
			criteriaOrderRecord.andOrderRecordIdEqualTo(orderRecordId);
			// 如果订单完成或者不合法不做修改了。
			List<Byte> list = new ArrayList<>();
			list.add((byte) OrderRecordConfig.PAY_STATUS_UNQUALIFIED);
			list.add((byte) OrderRecordConfig.PAY_STATUS_ALREADY);
			criteriaOrderRecord.andOrderRecordPayStatusNotIn(list);
			int result = orderRecordMapper.updateByExampleSelective(orderRecordNew, orderRecordCriteria);
			if (result != 1) {
				MybatisManager.log.warn("修改支付记录失败");
				return false;
			}
			sqlSession.commit();
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("修改支付记录异常", e);
			return false;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return true;
	}

	public static String getSubject(OrderRecordExt orderRecord) {
		String subject = "";
		for (int i = 0; i < orderRecord.orderGoodsArray.size(); i++) {
			OrderGoods orderGoods = orderRecord.orderGoodsArray.get(i);
			if (i == orderRecord.orderGoodsArray.size() - 1) {
				subject += orderGoods.getOrderGoodsName() + "x" + orderGoods.getOrderGoodsNumber();
			} else {
				subject += orderGoods.getOrderGoodsName() + "x" + orderGoods.getOrderGoodsNumber() + ",";
			}
		}
		return subject;
	}

	public static String getReturnUrl(OrderRecordExt orderRecord, Notify notify) {
		String returnUrl = orderRecord.getOrderRecordReturnUrl();
		if (StringUtil.stringIsNull(returnUrl)) {
			App app = AppAction.getAppById(orderRecord.getAppId());
			if (app == null) {
				return null;
			}
			returnUrl = app.getAppReturnUrl();
		}
		if (StringUtil.stringIsNull(returnUrl)) {
			return null;
		}
		returnUrl += "?orderRecordId=" + orderRecord.getOrderRecordId() + "&orderRecordOrderId=" + orderRecord.getOrderRecordOrderId() + "&orderRecordPayStatus=" + orderRecord.getOrderRecordPayStatus() + "&notifyId=" + notify.getNotifyId();
		return returnUrl;
	}

	public static String getNotifyUrl(OrderRecordExt orderRecord, Notify notify) {
		String notifyUrl = orderRecord.getOrderRecordNotifyUrl();
		if (StringUtil.stringIsNull(notifyUrl)) {
			App app = AppAction.getAppById(orderRecord.getAppId());
			if (app == null) {
				return null;
			}
			notifyUrl = app.getAppNotifyUrl();
		}
		if (StringUtil.stringIsNull(notifyUrl)) {
			return null;
		}
		notifyUrl += "?orderRecordId=" + orderRecord.getOrderRecordId() + "&orderRecordOrderId=" + orderRecord.getOrderRecordOrderId() + "&orderRecordPayStatus=" + orderRecord.getOrderRecordPayStatus() + "&notifyId=" + notify.getNotifyId();
		return notifyUrl;
	}
}
