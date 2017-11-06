package org.epay.action;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.epay.config.CommonConfigPayCenter;
import org.epay.config.NotifyConfig;
import org.epay.dao.base.NotifyMapper;
import org.epay.model.base.Notify;
import org.epay.model.base.NotifyCriteria;
import org.epay.tool.StringUtil;
import org.epay.util.IdUtil;
import org.grain.mariadb.MybatisManager;

public class NotifyAction {
	public static Notify createNotify(String appId, String orderRecordId, int notifyType) {
		if (StringUtil.stringIsNull(orderRecordId) || StringUtil.stringIsNull(appId)) {
			return null;
		}
		if (notifyType != NotifyConfig.TYPE_RETURN && notifyType != NotifyConfig.TYPE_NOTIFY) {
			return null;
		}
		Date date = new Date();
		Notify notify = new Notify();
		notify.setNotifyId(IdUtil.getUuid());
		notify.setOrderRecordId(orderRecordId);
		notify.setAppId(appId);
		notify.setNotifyCreateTime(date);
		Date expireTime = new Date(date.getTime() + CommonConfigPayCenter.NOTIFY_EXPIRE_TIME);
		notify.setNotifyExpireTime(expireTime);
		notify.setNotifyType((byte) notifyType);
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			NotifyMapper notifyMapper = sqlSession.getMapper(NotifyMapper.class);
			int result = notifyMapper.insert(notify);
			if (result != 1) {
				MybatisManager.log.warn("创建notify失败");
				return null;
			}
			sqlSession.commit();
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("创建notify失败", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return notify;
	}

	public static Notify getNotify(String notifyId) {
		if (StringUtil.stringIsNull(notifyId)) {
			return null;
		}
		SqlSession sqlSession = null;
		Notify notify;
		try {
			sqlSession = MybatisManager.getSqlSession();
			NotifyMapper notifyMapper = sqlSession.getMapper(NotifyMapper.class);
			notify = notifyMapper.selectByPrimaryKey(notifyId);
			if (notify == null) {
				MybatisManager.log.warn("通过notifyId:" + notifyId + "获取notify为空");
			}
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("获取notify异常", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return notify;

	}

	public static List<Notify> getNotifyList(String appId, String orderRecordId, int notifyType, int limitStart, int pageSize) {
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			NotifyMapper notifyMapper = sqlSession.getMapper(NotifyMapper.class);
			NotifyCriteria notifyCriteria = new NotifyCriteria();
			NotifyCriteria.Criteria criteria = notifyCriteria.createCriteria();
			if (!StringUtil.stringIsNull(appId)) {
				criteria.andAppIdEqualTo(appId);
			}
			if (!StringUtil.stringIsNull(orderRecordId)) {
				criteria.andOrderRecordIdEqualTo(orderRecordId);
			}

			if (notifyType == NotifyConfig.TYPE_NOTIFY || notifyType == NotifyConfig.TYPE_RETURN) {
				criteria.andNotifyTypeEqualTo((byte) notifyType);
			}
			notifyCriteria.setOrderByClause("notify_create_time desc");
			notifyCriteria.setLimitStart(limitStart);
			notifyCriteria.setPageSize(pageSize);
			List<Notify> notifyList = notifyMapper.selectByExample(notifyCriteria);
			return notifyList;
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("获取notifylist失败", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	public static Notify updateNotify(String notifyId, String notifyResult) {
		if (StringUtil.stringIsNull(notifyId)) {
			return null;
		}
		Notify notify = getNotify(notifyId);
		if (notify == null) {
			return null;
		}
		Notify notifyNew = new Notify();
		notifyNew.setNotifyId(notifyId);
		notifyNew.setNotifyResult(notifyResult);

		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			NotifyMapper notifyMapper = sqlSession.getMapper(NotifyMapper.class);
			int result = notifyMapper.updateByPrimaryKeySelective(notifyNew);
			if (result != 1) {
				MybatisManager.log.warn("修改notify失败");
				return null;
			}
			sqlSession.commit();
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("修改notify异常", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return getNotify(notifyNew.getNotifyId());
	}
}
