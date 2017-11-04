function AppProxy() {
    this.NAME = "AppProxy";
    this.createApp = function (appName, appReturnUrl, appNotifyUrl) {
        var data = {
            "hOpCode": 200,
            "appName": appName,
            "appReturnUrl": appReturnUrl,
            "appNotifyUrl": appNotifyUrl
        };

        var sendParam = new SendParam();
        sendParam.successHandle = this.createAppSuccess;
        sendParam.failHandle = this.createAppFail;
        sendParam.object = this;
        sendParam.data = data;
        sendParam.url = $T.url.url;
        sendParam.token = $T.cookieParam.getCookieParam($T.cookieName.TOKEN);
        $T.httpUtil.send(sendParam);
    }
    this.createAppSuccess = function (result, sendParam) {

    }
    this.createAppFail = function (result, sendParam) {

    }
    this.updateApp = function (appId, appName, updateAppKey, appState, appReturnUrl, appNotifyUrl) {
        var data = {
            "hOpCode": 201,
            "appId": appId,
            "appName": appName,
            "updateAppKey": updateAppKey == 1 ? true : false,
            "appState": appState,
            "appReturnUrl": appReturnUrl,
            "appNotifyUrl": appNotifyUrl
        };

        var sendParam = new SendParam();
        sendParam.successHandle = this.updateAppSuccess;
        sendParam.failHandle = this.updateAppFail;
        sendParam.object = this;
        sendParam.data = data;
        sendParam.url = $T.url.url;
        sendParam.token = $T.cookieParam.getCookieParam($T.cookieName.TOKEN);
        $T.httpUtil.send(sendParam);
    }
    this.updateAppSuccess = function (result, sendParam) {

    }
    this.updateAppFail = function (result, sendParam) {

    }
    this.getApp = function (appId) {
        var data = {
            "hOpCode": 202,
            "appId": appId
        };

        var sendParam = new SendParam();
        sendParam.successHandle = this.getAppSuccess;
        sendParam.failHandle = this.getAppFail;
        sendParam.object = this;
        sendParam.data = data;
        sendParam.url = $T.url.url;
        sendParam.token = $T.cookieParam.getCookieParam($T.cookieName.TOKEN);
        $T.httpUtil.send(sendParam);
    }
    this.getAppSuccess = function (result, sendParam) {

    }
    this.getAppFail = function (result, sendParam) {

    }
    this.getAppList = function (appName, appCreateTimeGreaterThan, appCreateTimeLessThan, appState, currentPage, pageSize) {
        var data = {
            "hOpCode": 203,
            "appName": appName,
            "appCreateTimeGreaterThan": appCreateTimeGreaterThan,
            "appCreateTimeLessThan": appCreateTimeLessThan,
            "appState": appState,
            "currentPage": currentPage,
            "pageSize": pageSize
        };

        var sendParam = new SendParam();
        sendParam.successHandle = this.getAppListSuccess;
        sendParam.failHandle = this.getAppListFail;
        sendParam.object = this;
        sendParam.data = data;
        sendParam.url = $T.url.url;
        sendParam.token = $T.cookieParam.getCookieParam($T.cookieName.TOKEN);
        $T.httpUtil.send(sendParam);
    }
    this.getAppListSuccess = function (result, sendParam) {
        $T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.GET_APP_LIST_SUCCESS, result));
    }
    this.getAppListFail = function (result, sendParam) {

    }
}
$T.appProxy = new AppProxy();