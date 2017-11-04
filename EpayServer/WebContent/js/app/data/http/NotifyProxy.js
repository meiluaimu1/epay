function NotifyProxy() {
	this.NAME = "NotifyProxy";
	this.verifyNotify = function(notifyId, appId) {
		var data = {
			"hOpCode" : 222,
			"notifyId" : notifyId,
			"appId" : appId
		};

		var sendParam = new SendParam();
		sendParam.successHandle = this.verifyNotifySuccess;
		sendParam.failHandle = this.verifyNotifyFail;
		sendParam.object = this;
		sendParam.data = data;
		sendParam.url = $T.url.url;
		$T.httpUtil.send(sendParam);
	}
	this.verifyNotifySuccess = function(result, sendParam) {

	}
	this.verifyNotifyFail = function(result, sendParam) {

	}
}
$T.notifyProxy = new NotifyProxy();