function LeftMediator() {

	this.init = function(view) {
		$("#left_button").on("click", this.onClick);
		$("#left_button1").on("click", this.onClick1);
		$("#left_button2").on("click", this.onClick2);
		$("#left_button3").on("click", this.onClick3);
		$("#left_button4").on("click", this.onClick4);
		$("#left_button5").on("click", this.onClick5);
	}
	// 注销方法
	this.dispose = function() {
		$("#left_button").remove("click", this.onClick);
	}
	// 关心消息数组
	this.listNotificationInterests = [];
	// 关心的消息处理
	this.handleNotification = function(data) {

	}
	this.onClick = function(event) {
		$T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.CHANGE_BODY, "app"));
	}
	this.onClick1 = function(event) {
		$T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.CHANGE_BODY, "order_record"));
	}
	this.onClick2 = function(event) {
		$T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.CHANGE_BODY, "update_order_record"));
	}
	this.onClick3 = function(event) {
		$T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.CHANGE_BODY, "order_record_list"));
	}
	this.onClick4 = function(event) {
		$T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.CHANGE_BODY, "pay"));
	}
	this.onClick5 = function(event) {
		$T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.CHANGE_BODY, "notify"));
	}

}
$T.leftMediator = new LeftMediator();