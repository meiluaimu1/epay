package org.epay.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.epay.action.NotifyAction;
import org.epay.action.OrderRecordAction;
import org.epay.config.CommonConfigPayCenter;
import org.epay.config.NotifyConfig;
import org.epay.config.OrderRecordConfig;
import org.epay.http.NotifyUtil;
import org.epay.model.base.Notify;
import org.epay.model.ext.OrderRecordExt;
import org.grain.httpserver.HttpConfig;
import org.grain.thread.ICycle;

public class NotifyClient implements ICycle {
	private LinkedBlockingQueue<String> notifyOrderQueue = new LinkedBlockingQueue<String>();
	private ArrayList<String> handleNotifyOrder = new ArrayList<String>();

	public void addNotifyOrder(String orderRecordId) {
		try {
			notifyOrderQueue.put(orderRecordId);
		} catch (InterruptedException e) {
			HttpConfig.log.error("添加订单id失败", e);
		}
	}

	public ArrayList<String> getHandleNotifyOrder() {
		handleNotifyOrder.clear();
		notifyOrderQueue.drainTo(handleNotifyOrder);
		return handleNotifyOrder;
	}

	@Override
	public void cycle() throws Exception {
		ArrayList<String> notifyOrderArray = getHandleNotifyOrder();
		ArrayList<String> leaveArray = new ArrayList<>();
		Date date = new Date();
		for (int i = 0; i < notifyOrderArray.size(); i++) {
			String orderRecordId = notifyOrderArray.get(i);
			OrderRecordExt orderRecord = OrderRecordAction.getOrderRecordById(orderRecordId);
			// 不存在跳过
			if (orderRecord == null) {
				continue;
			}
			// 不是推送合理状态跳过
			if (orderRecord.getOrderRecordPayStatus().intValue() != OrderRecordConfig.PAY_STATUS_ALREADY && orderRecord.getOrderRecordPayStatus().intValue() != OrderRecordConfig.PAY_STATUS_UNQUALIFIED) {
				continue;
			}
			// 已经推送完成的跳过
			if (orderRecord.getOrderRecordNotifyResult().intValue() == OrderRecordConfig.NOTIFY_RESULT_YES) {
				continue;
			}
			// 大于最大推送次数的跳过
			if (orderRecord.getOrderRecordNotifyTime().intValue() > CommonConfigPayCenter.MAX_NOTIFY_TIME) {
				continue;
			}
			List<Notify> notifyList = NotifyAction.getNotifyList(null, orderRecord.getOrderRecordId(), NotifyConfig.TYPE_NOTIFY, 0, 1);
			if (notifyList == null) {
				leaveArray.add(orderRecordId);
				continue;
			}
			if (notifyList.size() > 0) {
				if (date.getTime() < notifyList.get(0).getNotifyCreateTime().getTime() + CommonConfigPayCenter.NOTIFY_INTERVAL) {
					leaveArray.add(orderRecordId);
					continue;
				}
			}
			Notify notify = NotifyAction.createNotify(orderRecord.getAppId(), orderRecord.getOrderRecordId(), NotifyConfig.TYPE_NOTIFY);
			if (notify == null) {
				leaveArray.add(orderRecordId);
				continue;
			}
			String notifyUrl = OrderRecordAction.getNotifyUrl(orderRecord, notify);
			if (notifyUrl == null) {
				continue;
			}
			String result = NotifyUtil.send(notifyUrl);
			OrderRecordExt updateOrderRecord;
			if (result == null) {
				HttpConfig.log.info("通知失败，未返回数据");
				updateOrderRecord = OrderRecordAction.updateOrderRecord(orderRecordId, 0, 0, 0, 0, orderRecord.getOrderRecordNotifyTime().intValue() + 1);
				leaveArray.add(orderRecordId);
				if (updateOrderRecord == null) {
					HttpConfig.log.warn("修改OrderRecord notifytime失败");
				} else {
					HttpConfig.log.info("修改OrderRecord notifytime成功");
				}
			} else if (!result.equals("success")) {
				HttpConfig.log.info("通知失败，返回数据为" + result);
				NotifyAction.updateNotify(notify.getNotifyId(), result);
				updateOrderRecord = OrderRecordAction.updateOrderRecord(orderRecordId, 0, 0, 0, 0, orderRecord.getOrderRecordNotifyTime().intValue() + 1);
				leaveArray.add(orderRecordId);
				if (updateOrderRecord == null) {
					HttpConfig.log.warn("修改OrderRecord notifytime失败");
				} else {
					HttpConfig.log.info("修改OrderRecord notifytime成功");
				}
			} else {
				HttpConfig.log.info("通知成功，返回数据为" + result);
				NotifyAction.updateNotify(notify.getNotifyId(), result);
				updateOrderRecord = OrderRecordAction.updateOrderRecord(orderRecordId, 0, 0, 0, OrderRecordConfig.NOTIFY_RESULT_YES, orderRecord.getOrderRecordNotifyTime().intValue() + 1);
				if (updateOrderRecord == null) {
					HttpConfig.log.warn("修改OrderRecord notifytime与状态失败");
				} else {
					HttpConfig.log.info("修改OrderRecord notifytime与状态成功");
				}
			}

		}
		notifyOrderQueue.addAll(leaveArray);
	}

	@Override
	public void onAdd() throws Exception {

	}

	@Override
	public void onRemove() throws Exception {

	}

}
