package action;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.grain.mariadb.MybatisManager;

import dao.dao.base.PayNotifyLogMapper;
import dao.model.base.PayNotifyLog;
import tool.StringUtil;
import util.IdUtil;

public class PayNotifyLogAction {
	public static PayNotifyLog createPayNotifyLog(String orderRecordId, String payNotifyLogBody) {
		if (StringUtil.stringIsNull(payNotifyLogBody) || StringUtil.stringIsNull(orderRecordId)) {
			return null;
		}
		Date date = new Date();
		PayNotifyLog payNotifyLog = new PayNotifyLog();
		payNotifyLog.setPayNotiftyLogId(IdUtil.getUuid());
		payNotifyLog.setOrderRecordId(orderRecordId);
		payNotifyLog.setPayNotifyCreateTime(date);
		payNotifyLog.setPayNotifyLogBody(payNotifyLogBody);
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			PayNotifyLogMapper payNotifyLogMapper = sqlSession.getMapper(PayNotifyLogMapper.class);
			int result = payNotifyLogMapper.insert(payNotifyLog);
			if (result != 1) {
				MybatisManager.log.warn("创建PayNotifyLog失败");
				return null;
			}
			sqlSession.commit();
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("创建PayNotifyLog失败", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return payNotifyLog;
	}
}
