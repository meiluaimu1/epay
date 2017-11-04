function RecordMediator() {
    this.token;
    this.userId;
    this.init = function (view) {
        this.token = $T.getUrlParam.getUrlParam($T.httpConfig.TOKEN);
        this.userId = $T.getUrlParam.getUrlParam('userId');
        if (this.token == null || this.userId == null) {
            window.location.href = 'login.html';
        }
        $T.cookieParam.setCookieParam($T.cookieName.TOKEN, this.token);
        $("#orderRecordUserId").val(this.userId);
        $("#orderRecordOrderId").val(Math.uuid());
        $("#orderRecordReturnUrl").val($T.url.clientUrl + 'success.html');
        $("#orderRecordNotifyUrl").val($T.url.clientUrl + 'success.html');
        $("#orderGoodsGoodsId").val(Math.uuid());
        $T.appProxy.getAppList(null, null, null, 1)
    }
    // 注销方法
    this.dispose = function () {

    }
    // 关心消息数组
    this.listNotificationInterests = [$T.notificationExt.GET_APP_LIST_SUCCESS,$T.notificationExt.CREATE_RECORD_SUCCESS];
    // 关心的消息处理
    this.handleNotification = function (data) {
        switch (data[0].name) {
            case $T.notificationExt.GET_APP_LIST_SUCCESS:
                $("#appId").val(data[0].body['appList'][0].appId);
                $("#createRecord").on("click", this.onCreateRecord);
                break;
            case $T.notificationExt.CREATE_RECORD_SUCCESS:
                window.location.href = "loginPayCenter.html?token=" + this.token+"&orderRecordId="+data[0].body['orderRecordData']['orderRecordId'];
                break;
        }
    }
    this.advanceTime = function (passedTime) {

    }
    this.onCreateRecord = function () {
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
                "orderGoodsGoodsId": orderGoodsGoodsId + "_" + i,
                "orderGoodsName": orderGoodsName,
                "orderGoodsPrice": orderGoodsPrice,
                "orderGoodsNumber": orderGoodsNumber,
                "orderGoodsDetail": orderGoodsDetail,
                "orderGoodsUrl": orderGoodsUrl
            };
            orderGoodsArray.push(data);
        }

        $T.orderRecordProxy.createOrderRecord(appId, orderRecordOrderId, orderRecordTotalPrice, orderRecordUserId, orderRecordOrderDetail, orderRecordUserName, orderRecordReturnUrl, orderRecordNotifyUrl, orderGoodsArray);
    }
}
$T.recordMediator = new RecordMediator();