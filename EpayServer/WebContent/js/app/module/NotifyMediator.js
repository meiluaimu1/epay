function NotifyMediator() {

	this.init = function(view) {
		$("#verifyNotify").on("click", this.onVerifyNotify);
	}
	// 注销方法
	this.dispose = function() {

	}
	// 关心消息数组
	this.listNotificationInterests = [];
	// 关心的消息处理
	this.handleNotification = function(data) {

	}
	this.onVerifyNotify = function() {
		var notifyId = $("#notifyId").val();
		var appId = $("#appId").val();
		$T.notifyProxy.verifyNotify(notifyId, appId);
	}

}
$T.notifyMediator = new NotifyMediator();