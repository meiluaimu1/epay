function PayCenterErrorMsg() {
    this.errorMap = [];
    this.init = function () {
        this.errorMap["ERROR_CODE_0"] = "未知错误";
        this.errorMap["ERROR_CODE_1"] = "该支付订单不存在";
        this.errorMap["ERROR_CODE_2"] = "你没有权限操作此订单";
        this.errorMap["ERROR_CODE_3"] = "该订单不合法，不存在商品";
        this.errorMap["ERROR_CODE_4"] = "你的身份不合法或者已过期";
    }
}
$T.payCenterErrorMsg = new PayCenterErrorMsg();
$T.payCenterErrorMsg.init();