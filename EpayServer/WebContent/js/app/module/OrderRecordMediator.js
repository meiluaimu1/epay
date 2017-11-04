function OrderRecordMediator() {

	this.init = function(view) {
		$("#createOrderRecord").on("click", this.onCreateOrderRecord);

	}
	// 注销方法
	this.dispose = function() {

	}
	// 关心消息数组
	this.listNotificationInterests = [];
	// 关心的消息处理
	this.handleNotification = function(data) {

	}
	this.onCreateOrderRecord = function() {

		var appId = $("#appId").val();
		var orderRecordOrderId = $("#orderRecordOrderId").val();
		var orderRecordTotalPrice = $("#orderRecordTotalPrice").val();
		var orderRecordUserId = $("#orderRecordUserId").val();
		var orderRecordOrderDetail = $("#orderRecordOrderDetail").val();
		var orderRecordUserName = $("#orderRecordUserName").val();
		var orderRecordReturnUrl = $("#orderRecordReturnUrl").val();
		var orderRecordNotifyUrl = $("#orderRecordNotifyUrl").val();

		var orderGoodsGoodsId = $("#orderGoodsGoodsId").val();
		var orderGoodsName = $("#orderGoodsName").val();
		var orderGoodsPrice = $("#orderGoodsPrice").val();
		var orderGoodsNumber = $("#orderGoodsNumber").val();
		var orderGoodsDetail = $("#orderGoodsDetail").val();
		var orderGoodsUrl = $("#orderGoodsUrl").val();

		var orderGoodsArray = new Array();
		var orderGoodsNum = $("#orderGoodsNum").val();
		for (var i = 0; i < orderGoodsNum; i++) {
			var data = {
				"orderGoodsGoodsId" : orderGoodsGoodsId + "_" + i,
				"orderGoodsName" : orderGoodsName,
				"orderGoodsPrice" : orderGoodsPrice,
				"orderGoodsNumber" : orderGoodsNumber,
				"orderGoodsDetail" : orderGoodsDetail,
				"orderGoodsUrl" : orderGoodsUrl
			};
			orderGoodsArray.push(data);
		}

		$T.orderRecordProxy.createOrderRecord(appId, orderRecordOrderId, orderRecordTotalPrice, orderRecordUserId, orderRecordOrderDetail, orderRecordUserName, orderRecordReturnUrl, orderRecordNotifyUrl, orderGoodsArray);
	}

}
$T.orderRecordMediator = new OrderRecordMediator();