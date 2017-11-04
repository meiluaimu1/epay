function UpdateOrderRecordMediator() {

	this.init = function(view) {
		$("#updateOrderRecord").on("click", this.onUpdateOrderRecord);
		$("#getOrderRecord").on("click", this.onGetOrderRecord);

	}
	// 注销方法
	this.dispose = function() {

	}
	// 关心消息数组
	this.listNotificationInterests = [];
	// 关心的消息处理
	this.handleNotification = function(data) {

	}
	this.onUpdateOrderRecord = function() {

		var orderRecordId = $("#orderRecordId").val();
		var orderRecordPayStatus = $("#orderRecordPayStatus").val();
		var orderRecordStatus = $("#orderRecordStatus").val();
		var orderRecordPayChannel = $("#orderRecordPayChannel").val();
		var orderRecordNotifyResult = $("#orderRecordNotifyResult").val();
		var orderRecordNotifyTime = $("#orderRecordNotifyTime").val();

		$T.orderRecordProxy.updateOrderRecord(orderRecordId, orderRecordPayStatus, orderRecordStatus, orderRecordPayChannel, orderRecordNotifyResult, orderRecordNotifyTime);
	}
	this.onGetOrderRecord = function() {
		var orderRecordId = $("#orderRecordId_get").val();
		$T.orderRecordProxy.getOrderRecord(orderRecordId);
	}

}
$T.updateOrderRecordMediator = new UpdateOrderRecordMediator();