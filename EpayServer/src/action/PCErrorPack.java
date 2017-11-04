package action;

import http.HOpCodePayCenter;
import protobuf.http.PCErrorProto.PCError;
import protobuf.http.PCErrorProto.PCErrorCode;

public class PCErrorPack {
	public static PCError create(PCErrorCode pcErrorCode, String errorHOpCode) {
		PCError.Builder errorBuilder = PCError.newBuilder();
		errorBuilder.setHOpCode(HOpCodePayCenter.PC_ERROR);
		errorBuilder.setErrorCode(pcErrorCode);
		errorBuilder.setErrorHOpCode(errorHOpCode);
		return errorBuilder.build();
	}
}
