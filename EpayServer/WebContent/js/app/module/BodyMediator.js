function BodyMediator() {

	this.init = function(view) {

		$T.moduleManager.loadModule("html/app.html", document.getElementById("body"), "body", $T.appMediator);
	}
	// 注销方法
	this.dispose = function() {

	}
	// 关心消息数组
	this.listNotificationInterests = [ $T.notificationExt.CHANGE_BODY ];
	// 关心的消息处理
	this.handleNotification = function(data) {
		switch (data[0].name) {
		case $T.notificationExt.CHANGE_BODY:
			if (data[0].body == "app") {
				$T.moduleManager.loadModule("html/app.html", document.getElementById("body"), "body", $T.appMediator);
			} else if (data[0].body == "order_record") {
				$T.moduleManager.loadModule("html/order_record.html", document.getElementById("body"), "body", $T.orderRecordMediator);
			}else if (data[0].body == "update_order_record") {
				$T.moduleManager.loadModule("html/update_order_record.html", document.getElementById("body"), "body", $T.updateOrderRecordMediator);
			}else if (data[0].body == "order_record_list") {
				$T.moduleManager.loadModule("html/order_record_list.html", document.getElementById("body"), "body", $T.orderRecordListMediator);
			}else if (data[0].body == "pay") {
				$T.moduleManager.loadModule("html/pay.html", document.getElementById("body"), "body", $T.payMediator);
			}else if (data[0].body == "notify") {
				$T.moduleManager.loadModule("html/notify.html", document.getElementById("body"), "body", $T.notifyMediator);
			}
			break;
		}
	}
}
$T.bodyMediator = new BodyMediator();