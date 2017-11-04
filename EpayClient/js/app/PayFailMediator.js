function PayFailMediator() {
    this.init = function (view) {
        var height = $(window).height();
        $(".bg-e8e").innerHeight(height)
    }
    // 注销方法
    this.dispose = function () {

    }
    // 关心消息数组
    this.listNotificationInterests = [];
    // 关心的消息处理
    this.handleNotification = function (data) {

    }
    this.advanceTime = function (passedTime) {

    }
}
$T.payFailMediator = new PayFailMediator();