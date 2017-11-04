function LoginPayCenterMediator() {
    this.orderRecordId;
    this.token;
    this.loginSuccess = false;
    this.nowTime = 0;
    this.init = function (view) {
        $(window).resize(function () {
            size();
        });
        function size() {
            var $width = $(window).width();
            var $height = $(window).height();
            $(".bg_wrapper").css({
                "width": $width,
                "height": $height,
                "display": "table-cell",
                "vertical-align": "middle"
            });
        }

        size();
        this.token = $T.getUrlParam.getUrlParam($T.httpConfig.TOKEN);
        if (this.token == null) {
            // token是空应该提示无法登陆
            $("#tips_box_wait").addClass("hide");
            $("#tips_box_fail").removeClass("hide");
            return;
        }
        this.orderRecordId = $T.getUrlParam.getUrlParam("orderRecordId");
        if (this.orderRecordId == null) {
            $("#tips_box_wait").addClass("hide");
            $("#tips_box_fail").removeClass("hide");
            return;
        }
        // 设置cookie
        $T.loginProxy.login(this.token);
    }
    // 注销方法
    this.dispose = function () {

    }
    // 关心消息数组
    this.listNotificationInterests = [$T.notificationExt.LOGIN_SUCCESS, $T.notificationExt.LOGIN_FAIL];
    // 关心的消息处理
    this.handleNotification = function (data) {
        switch (data[0].name) {
            case $T.notificationExt.LOGIN_SUCCESS:
                $T.cookieParam.setCookieParam($T.cookieName.TOKEN, this.token);
                this.loginSuccess = true;
                $("#tips_box_wait").addClass("hide");
                $("#tips_box_success").removeClass("hide");
                break;
            case $T.notificationExt.LOGIN_FAIL:
                $("#tips_box_wait").addClass("hide");
                $("#tips_box_fail").removeClass("hide");
                break;
        }
    }
    this.advanceTime = function (passedTime) {
        if (this.loginSuccess) {
            this.nowTime += passedTime;
            if (this.nowTime > 1) {
                window.location.href = "index.html?orderRecordId=" + this.orderRecordId;
                this.loginSuccess = false;
            }
        }
    }
}
$T.loginPayCenterMediator = new LoginPayCenterMediator();