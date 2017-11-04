package service;

import java.util.HashMap;
import java.util.Map;

import org.grain.httpserver.HttpException;
import org.grain.httpserver.HttpPacket;
import org.grain.httpserver.IHttpListener;

import action.PCErrorPack;
import action.UserAction;
import data.UserData;
import http.HOpCodePayCenter;
import protobuf.http.LoginProto.LoginC;
import protobuf.http.LoginProto.LoginS;
import protobuf.http.PCErrorProto.PCError;
import protobuf.http.PCErrorProto.PCErrorCode;

public class LoginService implements IHttpListener {

	@Override
	public Map<String, String> getHttps() {
		HashMap<String, String> map = new HashMap<>();
		map.put(HOpCodePayCenter.LOGIN, "loginHandle");
		return map;
	}

	public HttpPacket loginHandle(HttpPacket httpPacket) throws HttpException {
		LoginC message = (LoginC) httpPacket.getData();
		UserData userData = UserAction.getUser(message.getToken());
		if (userData == null) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_4, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		LoginS.Builder builder = LoginS.newBuilder();
		builder.setHOpCode(httpPacket.hSession.headParam.hOpCode);
		HttpPacket packet = new HttpPacket(httpPacket.hSession.headParam.hOpCode, builder.build());
		return packet;
	}
}
