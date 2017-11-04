function OrderRecordProxy() {
    this.NAME = "OrderRecordProxy";
    this.createOrderRecord = function (appId, orderRecordOrderId, orderRecordTotalPrice, orderRecordUserId, orderRecordOrderDetail, orderRecordUserName, orderRecordReturnUrl, orderRecordNotifyUrl, orderGoodsArray) {
        var data = {
            "hOpCode": 210,
            "appId": appId,
            "orderRecordOrderId": orderRecordOrderId,
            "orderRecordTotalPrice": orderRecordTotalPrice,
            "orderRecordUserId": orderRecordUserId,
            "orderRecordOrderDetail": orderRecordOrderDetail,
            "orderRecordUserName": orderRecordUserName,
            "orderRecordReturnUrl": orderRecordReturnUrl,
            "orderRecordNotifyUrl": orderRecordNotifyUrl,
            "orderGoodsArray": orderGoodsArray
        };

        var sendParam = new SendParam();
        sendParam.successHandle = this.createOrderRecordSuccess;
        sendParam.failHandle = this.createOrderRecordFail;
        sendParam.object = this;
        sendParam.data = data;
        sendParam.url = $T.url.url;
        sendParam.token = $T.cookieParam.getCookieParam($T.cookieName.TOKEN);
        $T.httpUtil.send(sendParam);
    }
    this.createOrderRecordSuccess = function (result, sendParam) {
        $T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.CREATE_RECORD_SUCCESS, result));
    }
    this.createOrderRecordFail = function (result, sendParam) {

    }
    this.updateOrderRecord = function (orderRecordId, orderRecordPayStatus, orderRecordStatus, orderRecordPayChannel, orderRecordNotifyResult, orderRecordNotifyTime) {
        var data = {
            "hOpCode": 211,
            "orderRecordId": orderRecordId,
            "orderRecordPayStatus": orderRecordPayStatus,
            "orderRecordStatus": orderRecordStatus,
            "orderRecordPayChannel": orderRecordPayChannel,
            "orderRecordNotifyResult": orderRecordNotifyResult,
            "orderRecordNotifyTime": orderRecordNotifyTime
        };

        var sendParam = new SendParam();
        sendParam.successHandle = this.updateOrderRecordSuccess;
        sendParam.failHandle = this.updateOrderRecordFail;
        sendParam.object = this;
        sendParam.data = data;
        sendParam.url = $T.url.url;
        sendParam.token = $T.cookieParam.getCookieParam($T.cookieName.TOKEN);
        $T.httpUtil.send(sendParam);
    }
    this.updateOrderRecordSuccess = function (result, sendParam) {

    }
    this.updateOrderRecordFail = function (result, sendParam) {

    }
    this.getOrderRecord = function (orderRecordId) {
        var data = {
            "hOpCode": 212,
            "orderRecordId": orderRecordId
        };

        var sendParam = new SendParam();
        sendParam.successHandle = this.getOrderRecordSuccess;
        sendParam.failHandle = this.getOrderRecordFail;
        sendParam.object = this;
        sendParam.data = data;
        sendParam.url = $T.url.url;
        sendParam.token = $T.cookieParam.getCookieParam($T.cookieName.TOKEN);
        $T.httpUtil.send(sendParam);
    }
    this.getOrderRecordSuccess = function (result, sendParam) {
        $T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.GET_ORDER_RECORD_SUCCESS, result));
    }
    this.getOrderRecordFail = function (result, sendParam) {

    }
    this.getOrderRecordList = function (appId, orderRecordOrderId, orderRecordCreateTimeGreaterThan, orderRecordCreateTimeLessThan, orderRecordPayStatus, orderRecordStatus, orderRecordUserId, orderRecordPayChannel, orderRecordOrderDetail, orderRecordNotifyResult, currentPage, pageSize) {
        var data = {
            "hOpCode": 213,
            "appId": appId,
            "orderRecordOrderId": orderRecordOrderId,
            "orderRecordCreateTimeGreaterThan": orderRecordCreateTimeGreaterThan,
            "orderRecordCreateTimeLessThan": orderRecordCreateTimeLessThan,
            "orderRecordPayStatus": orderRecordPayStatus,
            "orderRecordStatus": orderRecordStatus,
            "orderRecordUserId": orderRecordUserId,
            "orderRecordPayChannel": orderRecordPayChannel,
            "orderRecordOrderDetail": orderRecordOrderDetail,
            "orderRecordNotifyResult": orderRecordNotifyResult,
            "currentPage": currentPage,
            "pageSize": pageSize
        };

        var sendParam = new SendParam();
        sendParam.successHandle = this.getOrderRecordListSuccess;
        sendParam.failHandle = this.getOrderRecordListFail;
        sendParam.object = this;
        sendParam.data = data;
        sendParam.url = $T.url.url;
        sendParam.token = $T.cookieParam.getCookieParam($T.cookieName.TOKEN);
        $T.httpUtil.send(sendParam);
    }
    this.getOrderRecordListSuccess = function (result, sendParam) {

    }
    this.getOrderRecordListFail = function (result, sendParam) {

    }
}
$T.orderRecordProxy = new OrderRecordProxy();