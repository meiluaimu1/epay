function AppMediator() {

	this.init = function(view) {
		$("#createApp").on("click", this.onCreateApp);
		$("#updateApp").on("click", this.onUpdateApp);
		$("#getApp").on("click", this.onGetApp);
		$("#getAppList").on("click", this.onGetAppList);
	}
	// 注销方法
	this.dispose = function() {

	}
	// 关心消息数组
	this.listNotificationInterests = [];
	// 关心的消息处理
	this.handleNotification = function(data) {

	}
	this.onCreateApp = function() {
		var appName = $("#appName").val();
		var appReturnUrl = $("#appReturnUrl").val();
		var appNotifyUrl = $("#appNotifyUrl").val();
		$T.appProxy.createApp(appName, appReturnUrl, appNotifyUrl);
	}
	this.onUpdateApp = function() {
		var appId = $("#appId_update").val();
		var appName = $("#appName_update").val();
		var updateAppKey = $("#updateAppKey_update").val();
		var appState = $("#appState_update").val();
		var appReturnUrl = $("#appReturnUrl_update").val();
		var appNotifyUrl = $("#appNotifyUrl_update").val();
		$T.appProxy.updateApp(appId, appName, updateAppKey, appState, appReturnUrl, appNotifyUrl);
	}
	this.onGetApp = function() {
		var appId = $("#appId_get").val();
		$T.appProxy.getApp(appId);
	}
	this.onGetAppList = function() {
		var appName = $("#appName_getlist").val();
		var appCreateTimeGreaterThan = $("#appCreateTimeGreaterThan").val();
		var appCreateTimeLessThan = $("#appCreateTimeLessThan").val();
		var appState = $("#appState_getlist").val();
		var currentPage = $("#currentPage").val();
		var pageSize = $("#pageSize").val();
		$T.appProxy.getAppList(appName, appCreateTimeGreaterThan, appCreateTimeLessThan, appState, currentPage, pageSize);
	}

}
$T.appMediator = new AppMediator();