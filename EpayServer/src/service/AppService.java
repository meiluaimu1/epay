package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.grain.httpserver.HttpPacket;
import org.grain.httpserver.IHttpListener;

import action.AppAction;
import dao.model.base.App;
import http.HOpCodePayCenter;
import protobuf.http.AppProto.CreateAppC;
import protobuf.http.AppProto.CreateAppS;
import protobuf.http.AppProto.GetAppC;
import protobuf.http.AppProto.GetAppListC;
import protobuf.http.AppProto.GetAppListS;
import protobuf.http.AppProto.GetAppS;
import protobuf.http.AppProto.UpdateAppC;
import protobuf.http.AppProto.UpdateAppS;
import tool.PageFormat;
import tool.PageObj;

public class AppService implements IHttpListener {

	@Override
	public Map<String, String> getHttps() {
		HashMap<String, String> map = new HashMap<>();
		map.put(HOpCodePayCenter.CREATE_APP, "createAppHandle");
		map.put(HOpCodePayCenter.UPDATE_APP, "updateAppHandle");
		map.put(HOpCodePayCenter.GET_APP, "getAppHandle");
		map.put(HOpCodePayCenter.GET_APP_LIST, "getAppListHandle");
		return map;
	}

	public HttpPacket createAppHandle(HttpPacket httpPacket) {
		CreateAppC message = (CreateAppC) httpPacket.getData();
		App app = AppAction.createApp(message.getAppName(), message.getAppReturnUrl(), message.getAppNotifyUrl());
		if (app == null) {
			return null;
		}
		CreateAppS.Builder builder = CreateAppS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		builder.setApp(AppAction.getAppBuilder(app));
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket updateAppHandle(HttpPacket httpPacket) {
		UpdateAppC message = (UpdateAppC) httpPacket.getData();
		App app = AppAction.updateApp(message.getAppId(), message.getAppName(), message.getUpdateAppKey(), message.getAppState(), message.getAppReturnUrl(), message.getAppNotifyUrl());
		if (app == null) {
			return null;
		}
		UpdateAppS.Builder builder = UpdateAppS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		builder.setApp(AppAction.getAppBuilder(app));
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket getAppHandle(HttpPacket httpPacket) {
		GetAppC message = (GetAppC) httpPacket.getData();
		App app = AppAction.getAppById(message.getAppId());
		if (app == null) {
			return null;
		}
		GetAppS.Builder builder = GetAppS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		builder.setApp(AppAction.getAppBuilder(app));
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}

	public HttpPacket getAppListHandle(HttpPacket httpPacket) {
		GetAppListC message = (GetAppListC) httpPacket.getData();
		long count = AppAction.getAppListCount(message.getAppName(), message.getAppCreateTimeGreaterThan(), message.getAppCreateTimeLessThan(), message.getAppState());
		if (count == -1) {
			return null;
		}
		PageObj pageObj = PageFormat.getStartAndEnd(message.getCurrentPage(), message.getPageSize(), (int) count);
		List<App> appList = AppAction.getAppList(message.getAppName(), message.getAppCreateTimeGreaterThan(), message.getAppCreateTimeLessThan(), message.getAppState(), pageObj.start, pageObj.pageSize);
		if (appList == null) {
			return null;
		}
		GetAppListS.Builder builder = GetAppListS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		for (int i = 0; i < appList.size(); i++) {
			App app = appList.get(i);
			builder.addAppList(AppAction.getAppBuilder(app));
		}
		builder.setCurrentPage(pageObj.currentPage);
		builder.setAllNum(pageObj.allNum);
		builder.setPageSize(pageObj.pageSize);
		builder.setTotalPage(pageObj.totalPage);
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}
}
