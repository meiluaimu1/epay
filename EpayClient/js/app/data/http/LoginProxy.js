function LoginProxy() {
    this.NAME = "LoginProxy";
    this.login = function (token) {
        var data = {
            "hOpCode": 230,
            "token": token
        };

        var sendParam = new SendParam();
        sendParam.successHandle = this.loginSuccess;
        sendParam.failHandle = this.loginFail;
        sendParam.object = this;
        sendParam.data = data;
        sendParam.url = $T.url.url;
        $T.httpUtil.send(sendParam);
    }
    this.loginSuccess = function (result, sendParam) {
        $T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.LOGIN_SUCCESS));
    }
    this.loginFail = function (result, sendParam) {
        $T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.LOGIN_FAIL));
    }
}
$T.loginProxy = new LoginProxy();