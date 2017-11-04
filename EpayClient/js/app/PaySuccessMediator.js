function PaySuccessMediator() {
    this.loginSuccess = false;
    this.nowTime = 0;
    this.locationUrl;
    this.init = function (view) {
        var height = $(window).height();
        $(".bg-e8e").innerHeight(height)
        if ($T.cookieParam.getCookieParam($T.cookieName.TOKEN) == null) {
            alert("未登录");
            return;
        }
        var orderRecordId = $T.getUrlParam.getUrlParam("orderRecordId");
        if (orderRecordId == null) {
            alert("订单记录为空");
            return;
        }
        $T.payProxy.getReturnUrl(orderRecordId);
    }
    // 注销方法
    this.dispose = function () {

    }
    // 关心消息数组
    this.listNotificationInterests = [$T.notificationExt.GET_RETURN_URL_SUCCESS, $T.notificationExt.GET_RETURN_URL_FAIL, $T.notificationExt.PAYCENTER_ERROR];
    // 关心的消息处理
    this.handleNotification = function (data) {
        switch (data[0].name) {
            case $T.notificationExt.GET_RETURN_URL_SUCCESS:
                this.loginSuccess = true;
                this.locationUrl = data[0].body.returnUrl;
                break;
            case $T.notificationExt.GET_RETURN_URL_FAIL:

                break;
            case $T.notificationExt.PAYCENTER_ERROR:
                alert($T.payCenterErrorMsg.errorMap[data[0].body]);
                break;
        }
    }
    this.advanceTime = function (passedTime) {
        if (this.loginSuccess) {
            this.nowTime += passedTime;
            $("#return_time").html(5 - parseInt(this.nowTime));
            if (this.nowTime > 5) {
                window.location.href = this.locationUrl;
                this.loginSuccess = false;
            }
        }
    }
}
$T.paySuccessMediator = new PaySuccessMediator();