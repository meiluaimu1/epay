function OrderRecordListMediator() {

	this.init = function(view) {
		$("#getOrderRecordList").on("click", this.onGetOrderRecordList);

	}
	// 注销方法
	this.dispose = function() {

	}
	// 关心消息数组
	this.listNotificationInterests = [];
	// 关心的消息处理
	this.handleNotification = function(data) {

	}
	this.onGetOrderRecordList = function() {

		var appId = $("#appId").val();
		var orderRecordOrderId = $("#orderRecordOrderId").val();
		var orderRecordCreateTimeGreaterThan = $("orderRecordCreateTimeGreaterThan").val();
		var orderRecordCreateTimeLessThan = $("#orderRecordCreateTimeLessThan").val();
		var orderRecordPayStatus = $("#orderRecordPayStatus").val();
		var orderRecordStatus = $("#orderRecordStatus").val();
		var orderRecordUserId = $("#orderRecordUserId").val();
		var orderRecordPayChannel = $("#orderRecordPayChannel").val();
		var orderRecordOrderDetail = $("#orderRecordOrderDetail").val();
		var orderRecordNotifyResult = $("#orderRecordNotifyResult").val();
		var currentPage = $("#currentPage").val();
		var pageSize = $("#pageSize").val();
		$T.orderRecordProxy.getOrderRecordList(appId, orderRecordOrderId, orderRecordCreateTimeGreaterThan, orderRecordCreateTimeLessThan, orderRecordPayStatus, orderRecordStatus, orderRecordUserId, orderRecordPayChannel, orderRecordOrderDetail, orderRecordNotifyResult, currentPage, pageSize);
	}

}
$T.orderRecordListMediator = new OrderRecordListMediator();