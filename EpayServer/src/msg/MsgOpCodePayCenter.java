package msg;

import org.grain.threadmsg.ThreadMsgManager;

public class MsgOpCodePayCenter {
	public static String ADD_NOTIFY = "ADD_NOTIFY";

	public static void init() {
		ThreadMsgManager.addMapping(ADD_NOTIFY, null);
	}
}
