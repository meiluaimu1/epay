function PayMediator() {

	this.init = function(view) {
		$("#getPayHtml").on("click", this.onGetPayHtml);

	}
	// 注销方法
	this.dispose = function() {

	}
	// 关心消息数组
	this.listNotificationInterests = [ $T.notificationExt.ADD_PAY_HTML ];
	// 关心的消息处理
	this.handleNotification = function(data) {
		switch (data[0].name) {
		case $T.notificationExt.ADD_PAY_HTML:
			$("#paydiv")[0].innerHTML = data[0].body.payHtml;
			$("#alipaysubmit").submit();
			break;
		}
	}
	this.onGetPayHtml = function() {
		var orderRecordId = $("#orderRecordId").val();
		$T.payProxy.getPayHtml(orderRecordId);
	}

}
$T.payMediator = new PayMediator();