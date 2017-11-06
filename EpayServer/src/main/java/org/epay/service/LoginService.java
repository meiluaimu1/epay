package org.epay.service;

import java.util.HashMap;
import java.util.Map;

import org.epay.action.PCErrorPack;
import org.epay.action.IdentityAction;
import org.epay.data.UserData;
import org.epay.http.HOpCodePayCenter;
import org.epay.protobuf.http.LoginProto.LoginC;
import org.epay.protobuf.http.LoginProto.LoginS;
import org.epay.protobuf.http.PCErrorProto.PCError;
import org.epay.protobuf.http.PCErrorProto.PCErrorCode;
import org.grain.httpserver.HttpException;
import org.grain.httpserver.HttpPacket;
import org.grain.httpserver.IHttpListener;

public class LoginService implements IHttpListener {

	@Override
	public Map<String, String> getHttps() {
		HashMap<String, String> map = new HashMap<>();
		map.put(HOpCodePayCenter.LOGIN, "loginHandle");
		return map;
	}

	public HttpPacket loginHandle(HttpPacket httpPacket) throws HttpException {
		LoginC message = (LoginC) httpPacket.getData();
		UserData userData = IdentityAction.getUser(message.getToken());
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
