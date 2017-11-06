package org.epay.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.epay.action.OrderRecordAction;
import org.epay.action.PayNotifyLogAction;
import org.epay.config.OrderRecordConfig;
import org.epay.log.LogManagerPayCenter;
import org.epay.model.ext.OrderRecordExt;
import org.epay.msg.MsgOpCodePayCenter;
import org.epay.protobuf.msg.AddNotifyOuterClass.AddNotify;
import org.epay.tool.StringUtil;
import org.grain.log.RunMonitor;
import org.grain.threadmsg.ThreadMsgManager;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;

public class AlipayNotifyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {

	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		RunMonitor runMonitor = new RunMonitor("http", "alipay_notify");
		runMonitor.putMonitor("alipay_notify参数:" + requestParams.toString());
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		runMonitor.putMonitor("alipay_notify解析参数:" + params.toString());
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		// 商户订单号
		if (StringUtil.stringIsNull(request.getParameter("out_trade_no"))) {
			runMonitor.putMonitor("alipay_notify(out_trade_no):为空");
			LogManagerPayCenter.alipayLog.error(runMonitor.toString("alipay_notify"));
			response.getWriter().print("fail");
			return;
		}
		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
		runMonitor.putMonitor("alipay_notify(out_trade_no):" + out_trade_no);
		// 支付宝交易号
		if (StringUtil.stringIsNull(request.getParameter("trade_no"))) {
			runMonitor.putMonitor("alipay_notify(trade_no):为空");
			LogManagerPayCenter.alipayLog.error(runMonitor.toString("alipay_notify"));
			response.getWriter().print("fail");
			return;
		}
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
		runMonitor.putMonitor("alipay_notify(trade_no):" + trade_no);
		// 交易状态
		if (StringUtil.stringIsNull(request.getParameter("trade_status"))) {
			runMonitor.putMonitor("alipay_notify(trade_status):为空");
			LogManagerPayCenter.alipayLog.error(runMonitor.toString("alipay_notify"));
			response.getWriter().print("fail");
			return;
		}
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
		runMonitor.putMonitor("alipay_notify(trade_status):" + trade_status);
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		if (AlipayNotify.verify(params)) {// 验证成功
			runMonitor.putMonitor("alipay_notify验证成功");
			PayNotifyLogAction.createPayNotifyLog(out_trade_no, params.toString());
			OrderRecordExt orderRecord = OrderRecordAction.getOrderRecordById(out_trade_no);
			if (orderRecord == null) {
				runMonitor.putMonitor("订单id为：" + out_trade_no + "不存在");
				LogManagerPayCenter.alipayLog.error(runMonitor.toString("alipay_notify"));
				response.getWriter().print("fail");
				return;
			}
			if (StringUtil.stringIsNull(params.get("seller_id"))) {
				runMonitor.putMonitor("seller_id不存在");
				LogManagerPayCenter.alipayLog.error(runMonitor.toString("alipay_notify"));
				response.getWriter().print("fail");
				return;
			}
			if (!params.get("seller_id").equals(AlipayConfig.seller_id)) {
				runMonitor.putMonitor("seller_id:" + params.get("seller_id") + "，跟支付中心不匹配");
				LogManagerPayCenter.alipayLog.error(runMonitor.toString("alipay_notify"));
				response.getWriter().print("fail");
				return;
			}
			if (StringUtil.stringIsNull(params.get("total_fee"))) {
				runMonitor.putMonitor("total_fee不存在");
				LogManagerPayCenter.alipayLog.error(runMonitor.toString("alipay_notify"));
				response.getWriter().print("fail");
				return;
			}
			if (Double.valueOf(params.get("total_fee")).doubleValue() != orderRecord.getOrderRecordTotalPrice().doubleValue()) {
				runMonitor.putMonitor("total_fee:" + params.get("total_fee") + "，与订单价格：" + orderRecord.getOrderRecordTotalPrice() + "不相符");
				LogManagerPayCenter.alipayLog.error(runMonitor.toString("alipay_notify"));
				boolean result = OrderRecordAction.setOrderRecordPriceNotSame(orderRecord.getOrderRecordId(), Double.valueOf(params.get("total_fee")).doubleValue(), params.get("buyer_email"), params.get("trade_no"), params.get("notify_time"), params.get("buyer_id"), params.get("trade_status"));
				// 第一次修改成功，推送
				if (result) {
					AddNotify.Builder builder = AddNotify.newBuilder();
					builder.setOrderRecordId(orderRecord.getOrderRecordId());
					ThreadMsgManager.dispatchThreadMsg(MsgOpCodePayCenter.ADD_NOTIFY, builder.build(), null);
				}
				response.getWriter().print("fail");
				return;
			}
			//////////////////////////////////////////////////////////////////////////////////////////
			// 请在这里加上商户的业务逻辑程序代码

			// ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

			if (trade_status.equals("TRADE_FINISHED")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
				boolean result = OrderRecordAction.setOrderRecordSuccess(orderRecord.getOrderRecordId(), params.get("buyer_email"), params.get("trade_no"), params.get("notify_time"), params.get("buyer_id"), params.get("trade_status"));
				runMonitor.putMonitor("修改订单结果：" + result);
				// 第一次修改成功，推送
				if (result) {
					AddNotify.Builder builder = AddNotify.newBuilder();
					builder.setOrderRecordId(orderRecord.getOrderRecordId());
					ThreadMsgManager.dispatchThreadMsg(MsgOpCodePayCenter.ADD_NOTIFY, builder.build(), null);
					runMonitor.putMonitor("发送推送请求：" + orderRecord.getOrderRecordId());
				}
				orderRecord = OrderRecordAction.getOrderRecordById(out_trade_no);
				if (orderRecord.getOrderRecordPayStatus().intValue() == OrderRecordConfig.PAY_STATUS_ALREADY) {
					LogManagerPayCenter.alipayLog.info(runMonitor.toString("alipay_notify"));
					response.getWriter().print("success"); // 请不要修改或删除
					return;
				}
			} else if (trade_status.equals("TRADE_SUCCESS")) {
				// 判断该笔订单是否在商户网站中已经做过处理
				// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				// 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
				// 如果有做过处理，不执行商户的业务程序

				// 注意：
				// 付款完成后，支付宝系统发送该交易状态通知
				boolean result = OrderRecordAction.setOrderRecordSuccess(orderRecord.getOrderRecordId(), params.get("buyer_email"), params.get("trade_no"), params.get("notify_time"), params.get("buyer_id"), params.get("trade_status"));
				runMonitor.putMonitor("修改订单结果：" + result);
				// 第一次修改成功，推送
				if (result) {
					AddNotify.Builder builder = AddNotify.newBuilder();
					builder.setOrderRecordId(orderRecord.getOrderRecordId());
					ThreadMsgManager.dispatchThreadMsg(MsgOpCodePayCenter.ADD_NOTIFY, builder.build(), null);
					runMonitor.putMonitor("发送推送请求：" + orderRecord.getOrderRecordId());
				}
				orderRecord = OrderRecordAction.getOrderRecordById(out_trade_no);
				if (orderRecord.getOrderRecordPayStatus().intValue() == OrderRecordConfig.PAY_STATUS_ALREADY) {
					LogManagerPayCenter.alipayLog.info(runMonitor.toString("alipay_notify"));
					response.getWriter().print("success"); // 请不要修改或删除
					return;
				}
			}
			// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			LogManagerPayCenter.alipayLog.error(runMonitor.toString("alipay_notify"));
			response.getWriter().print("fail"); // 请不要修改或删除

			//////////////////////////////////////////////////////////////////////////////////////////
		} else {// 验证失败
			runMonitor.putMonitor("alipay_notify验证失败");
			PayNotifyLogAction.createPayNotifyLog(out_trade_no, params.toString());

			LogManagerPayCenter.alipayLog.error(runMonitor.toString("alipay_notify"));
			response.getWriter().print("fail");
		}
	}

}
