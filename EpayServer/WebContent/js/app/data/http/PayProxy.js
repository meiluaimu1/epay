function PayProxy() {
	this.NAME = "PayProxy";
	this.getPayHtml = function(orderRecordId) {
		var data = {
			"hOpCode" : 220,
			"orderRecordId" : orderRecordId
		};

		var sendParam = new SendParam();
		sendParam.successHandle = this.getPayHtmlSuccess;
		sendParam.failHandle = this.getPayHtmlFail;
		sendParam.object = this;
		sendParam.data = data;
		sendParam.url = $T.url.url;
		sendParam.token = $T.cookieParam.getCookieParam($T.cookieName.TOKEN);
		$T.httpUtil.send(sendParam);
	}
	this.getPayHtmlSuccess = function(result, sendParam) {
		$T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.ADD_PAY_HTML, result));
	}
	this.getPayHtmlFail = function(result, sendParam) {

	}
}
$T.payProxy = new PayProxy();