package org.epay.http.filter;

import org.epay.action.PCErrorPack;
import org.epay.action.IdentityAction;
import org.epay.data.UserData;
import org.epay.http.HOpCodePayCenter;
import org.epay.protobuf.http.PCErrorProto.PCError;
import org.epay.protobuf.http.PCErrorProto.PCErrorCode;
import org.grain.httpserver.HttpException;
import org.grain.httpserver.HttpPacket;
import org.grain.httpserver.IHttpFilter;

public class TokenHttpFilter implements IHttpFilter {

	@Override
	public boolean httpFilter(HttpPacket httpPacket) throws HttpException {
		if (httpPacket.hSession.headParam.token == null) {
			if (HOpCodePayCenter.LOGIN.equals(httpPacket.hSession.headParam.hOpCode) || HOpCodePayCenter.VERIFY_NOTIFY.equals(httpPacket.hSession.headParam.hOpCode) || HOpCodePayCenter.CREATE_ORDER_RECORD.equals(httpPacket.hSession.headParam.hOpCode)) {
				// 可以通过
				return true;
			} else {
				PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_4, httpPacket.hSession.headParam.hOpCode);
				throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
			}
		}
		UserData userData = IdentityAction.getUser(httpPacket.hSession.headParam.token);
		if (userData == null) {
			PCError errorPack = PCErrorPack.create(PCErrorCode.ERROR_CODE_4, httpPacket.hSession.headParam.hOpCode);
			throw new HttpException(HOpCodePayCenter.PC_ERROR, errorPack);
		}
		httpPacket.hSession.otherData = userData;
		return true;
	}

}
