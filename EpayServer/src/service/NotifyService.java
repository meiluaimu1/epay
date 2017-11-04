package service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.grain.httpserver.HttpConfig;
import org.grain.msg.IMsgListener;
import org.grain.msg.MsgPacket;
import org.grain.thread.AsyncThreadManager;

import action.OrderRecordAction;
import config.OrderRecordConfig;
import dao.model.ext.OrderRecordExt;
import msg.MsgOpCodePayCenter;
import protobuf.msg.AddNotifyOuterClass.AddNotify;
import tool.TimeUtils;

public class NotifyService implements IMsgListener {
	private Map<Integer, NotifyClient> notifyClientMap;

	public NotifyService(int asyncThreadNum) {
		Date date = new Date();
		String dateStart = TimeUtils.getDateStartString(date);

		List<OrderRecordExt> orderRecordList = OrderRecordAction.getOrderRecordList(null, null, dateStart, null, OrderRecordConfig.PAY_STATUS_ALREADY, 0, null, 0, null, OrderRecordConfig.NOTIFY_RESULT_NOT, 0, 0);
		List<OrderRecordExt> orderRecordListFail = OrderRecordAction.getOrderRecordList(null, null, dateStart, null, OrderRecordConfig.PAY_STATUS_UNQUALIFIED, 0, null, 0, null, OrderRecordConfig.NOTIFY_RESULT_NOT, 0, 0);
		notifyClientMap = new HashMap<>();
		for (int i = 1; i < asyncThreadNum; i++) {
			NotifyClient notifyClient = new NotifyClient();
			notifyClientMap.put(i, notifyClient);
		}
		for (int i = 0; i < orderRecordList.size(); i++) {
			OrderRecordExt orderRecord = orderRecordList.get(i);
			int[] threadId = AsyncThreadManager.getRandomThread();
			notifyClientMap.get(threadId[0]).addNotifyOrder(orderRecord.getOrderRecordId());
		}
		for (int i = 0; i < orderRecordListFail.size(); i++) {
			OrderRecordExt orderRecord = orderRecordListFail.get(i);
			int[] threadId = AsyncThreadManager.getRandomThread();
			notifyClientMap.get(threadId[0]).addNotifyOrder(orderRecord.getOrderRecordId());
		}
		for (int i = 1; i < asyncThreadNum; i++) {
			NotifyClient notifyClient = notifyClientMap.get(i);
			AsyncThreadManager.addCycle(notifyClient, i, 1);
		}
	}

	@Override
	public Map<String, String> getMsgs() throws Exception {
		HashMap<String, String> map = new HashMap<>();
		map.put(MsgOpCodePayCenter.ADD_NOTIFY, "addNotifyHandle");
		return map;
	}

	public void addNotifyHandle(MsgPacket msgPacket) {
		AddNotify message = (AddNotify) msgPacket.getData();
		HttpConfig.log.info("接到推送请求orderRecordId：" + message.getOrderRecordId());
		int[] threadId = AsyncThreadManager.getRandomThread();
		NotifyClient notifyClient = notifyClientMap.get(threadId[0]);
		notifyClient.addNotifyOrder(message.getOrderRecordId());
		HttpConfig.log.info("推送请求orderRecordId：" + message.getOrderRecordId() + "到线程" + threadId[0]);
	}

}
