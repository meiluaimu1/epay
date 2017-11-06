package org.epay.action;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.epay.config.AppConfig;
import org.epay.dao.base.AppMapper;
import org.epay.model.base.App;
import org.epay.model.base.AppCriteria;
import org.epay.protobuf.http.AppProto.AppData;
import org.epay.tool.StringUtil;
import org.epay.tool.TimeUtils;
import org.epay.util.IdUtil;
import org.grain.mariadb.MybatisManager;

public class AppAction {
	public static App createApp(String appName, String appReturnUrl, String appNotifyUrl) {
		if (StringUtil.stringIsNull(appName)) {
			return null;
		}
		Date date = new Date();
		App app = new App();
		app.setAppId(IdUtil.getUuid());
		app.setAppName(appName);
		app.setAppKey(IdUtil.getUuid());
		app.setAppCreateTime(date);
		app.setAppUpdateTime(date);
		app.setAppState((byte) AppConfig.STATE_USABLE);
		if (!StringUtil.stringIsNull(appReturnUrl)) {
			app.setAppReturnUrl(appReturnUrl);
		}
		if (!StringUtil.stringIsNull(appNotifyUrl)) {
			app.setAppNotifyUrl(appNotifyUrl);
		}
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			AppMapper appMapper = sqlSession.getMapper(AppMapper.class);
			int result = appMapper.insert(app);
			if (result != 1) {
				MybatisManager.log.warn("创建app失败");
				return null;
			}
			sqlSession.commit();
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("创建app失败", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return app;
	}

	public static App updateApp(String appId, String appName, boolean updateAppKey, int appState, String appReturnUrl, String appNotifyUrl) {
		if (StringUtil.stringIsNull(appId)) {
			return null;
		}
		App app = getAppById(appId);
		if (app == null) {
			return null;
		}
		Date date = new Date();
		App appNew = new App();
		appNew.setAppId(appId);
		appNew.setAppUpdateTime(date);
		if (!StringUtil.stringIsNull(appName)) {
			appNew.setAppName(appName);
		}
		if (updateAppKey) {
			appNew.setAppKey(IdUtil.getUuid());
		}
		if (appState == AppConfig.STATE_USABLE || appState == AppConfig.STATE_DISABLED || appState == AppConfig.STATE_DELETE) {
			appNew.setAppState((byte) appState);
		}
		if (!StringUtil.stringIsNull(appReturnUrl)) {
			appNew.setAppReturnUrl(appReturnUrl);
		}
		if (!StringUtil.stringIsNull(appNotifyUrl)) {
			appNew.setAppNotifyUrl(appNotifyUrl);
		}
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			AppMapper appMapper = sqlSession.getMapper(AppMapper.class);
			int result = appMapper.updateByPrimaryKeySelective(appNew);
			if (result != 1) {
				MybatisManager.log.warn("修改app失败");
				return null;
			}
			sqlSession.commit();
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("修改app异常", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return getAppById(appNew.getAppId());
	}

	public static App getAppById(String appId) {
		if (StringUtil.stringIsNull(appId)) {
			return null;
		}
		SqlSession sqlSession = null;
		App app;
		try {
			sqlSession = MybatisManager.getSqlSession();
			AppMapper appMapper = sqlSession.getMapper(AppMapper.class);
			app = appMapper.selectByPrimaryKey(appId);
			if (app == null) {
				MybatisManager.log.warn("通过appId:" + appId + "获取app为空");
			}
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("获取app异常", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		return app;
	}

	public static AppData.Builder getAppBuilder(App app) {
		AppData.Builder dataBuilder = AppData.newBuilder();
		dataBuilder.setAppId(app.getAppId());
		dataBuilder.setAppName(app.getAppName());
		dataBuilder.setAppKey(app.getAppKey());
		dataBuilder.setAppCreateTime(TimeUtils.dateToString(app.getAppCreateTime()));
		dataBuilder.setAppUpdateTime(TimeUtils.dateToString(app.getAppUpdateTime()));
		dataBuilder.setAppState(app.getAppState());
		if (app.getAppReturnUrl() != null) {
			dataBuilder.setAppReturnUrl(app.getAppReturnUrl());
		}
		if (app.getAppNotifyUrl() != null) {
			dataBuilder.setAppNotifyUrl(app.getAppNotifyUrl());
		}
		return dataBuilder;
	}

	public static List<App> getAppList(String appName, String appCreateTimeGreaterThan, String appCreateTimeLessThan, int appState, int limitStart, int pageSize) {
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			AppMapper appMapper = sqlSession.getMapper(AppMapper.class);
			AppCriteria appCriteria = new AppCriteria();
			AppCriteria.Criteria criteriaApp = appCriteria.createCriteria();
			if (!StringUtil.stringIsNull(appName)) {
				criteriaApp.andAppNameLikeInsensitive("%" + appName + "%");
			}
			if (!StringUtil.stringIsNull(appCreateTimeGreaterThan)) {
				Date appCreateTimeGreaterThanDate = TimeUtils.stringToDate(appCreateTimeGreaterThan);
				if (appCreateTimeGreaterThanDate != null) {
					criteriaApp.andAppCreateTimeGreaterThanOrEqualTo(appCreateTimeGreaterThanDate);
				}
			}
			if (!StringUtil.stringIsNull(appCreateTimeLessThan)) {
				Date appCreateTimeLessThanDate = TimeUtils.stringToDate(appCreateTimeLessThan);
				if (appCreateTimeLessThanDate != null) {
					criteriaApp.andAppCreateTimeLessThanOrEqualTo(appCreateTimeLessThanDate);
				}
			}
			if (appState == AppConfig.STATE_USABLE || appState == AppConfig.STATE_DISABLED || appState == AppConfig.STATE_DELETE) {
				criteriaApp.andAppStateEqualTo((byte) appState);
			}
			appCriteria.setOrderByClause("app_name asc");
			if (pageSize > 0) {
				appCriteria.setLimitStart(limitStart);
				appCriteria.setPageSize(pageSize);
			}
			List<App> appList = appMapper.selectByExample(appCriteria);
			return appList;
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("查询applist失败", e);
			return null;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}

	public static long getAppListCount(String appName, String appCreateTimeGreaterThan, String appCreateTimeLessThan, int appState) {
		SqlSession sqlSession = null;
		try {
			sqlSession = MybatisManager.getSqlSession();
			AppMapper appMapper = sqlSession.getMapper(AppMapper.class);
			AppCriteria appCriteria = new AppCriteria();
			AppCriteria.Criteria criteriaApp = appCriteria.createCriteria();
			if (!StringUtil.stringIsNull(appName)) {
				criteriaApp.andAppNameLikeInsensitive("%" + appName + "%");
			}
			if (!StringUtil.stringIsNull(appCreateTimeGreaterThan)) {
				Date appCreateTimeGreaterThanDate = TimeUtils.stringToDate(appCreateTimeGreaterThan);
				if (appCreateTimeGreaterThanDate != null) {
					criteriaApp.andAppCreateTimeGreaterThanOrEqualTo(appCreateTimeGreaterThanDate);
				}
			}
			if (!StringUtil.stringIsNull(appCreateTimeLessThan)) {
				Date appCreateTimeLessThanDate = TimeUtils.stringToDate(appCreateTimeLessThan);
				if (appCreateTimeLessThanDate != null) {
					criteriaApp.andAppCreateTimeLessThanOrEqualTo(appCreateTimeLessThanDate);
				}
			}
			if (appState == AppConfig.STATE_USABLE || appState == AppConfig.STATE_DISABLED || appState == AppConfig.STATE_DELETE) {
				criteriaApp.andAppStateEqualTo((byte) appState);
			}
			appCriteria.setOrderByClause("app_name asc");

			long count = appMapper.countByExample(appCriteria);
			return count;
		} catch (Exception e) {
			if (sqlSession != null) {
				sqlSession.rollback();
			}
			MybatisManager.log.error("查询applistcount失败", e);
			return -1;
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
	}
}
