function IndexMediator() {
    this.isCanPay = false;
    this.orderRecord;
    this.init = function (view) {
        if ($T.cookieParam.getCookieParam($T.cookieName.TOKEN) == null) {
            window.location.href='login.html'
            return;
        }
        var orderRecordId = $T.getUrlParam.getUrlParam("orderRecordId");
        if (orderRecordId == null) {
            alert("订单记录为空");
            window.location.href='login.html'
            return;
        }
        $T.orderRecordProxy.getOrderRecord(orderRecordId);


        function reH() {
            var h = $(window).height()
            $("body").css("min-height", h)
        }

        reH();
        $(window).resize(function () {
            reH();
        })

        $(".orderbtn").click(function () {
            $(this).toggleClass("up");
            $(this).parent().next().toggle();

        })

        //支付成功提示弹窗
        // layer.msg('支付成功', {
        //     area: ["430px", "90px"],
        //     icon: 1,
        //     time: 2000,//2秒关闭（如果不配置，默认是3秒）
        // }, function () {
        //     //do something
        // });
        // $("body").on("click", function () {
        //     layer.closeAll();
        // })
        $("#index_pay").on("click", this.onPayClick);
    }
    // 注销方法
    this.dispose = function () {

    }
    // 关心消息数组
    this.listNotificationInterests = [$T.notificationExt.PAYCENTER_ERROR, $T.notificationExt.GET_ORDER_RECORD_SUCCESS, $T.notificationExt.ADD_PAY_HTML];
    // 关心的消息处理
    this.handleNotification = function (data) {
        switch (data[0].name) {
            case $T.notificationExt.PAYCENTER_ERROR:
                alert($T.payCenterErrorMsg.errorMap[data[0].body]);
                break;
            case $T.notificationExt.GET_ORDER_RECORD_SUCCESS:
                this.initData(data[0].body.orderRecordData);
                this.isCanPay = true;
                break;
            case $T.notificationExt.ADD_PAY_HTML:
                $("#index_payhtml").append(data[0].body.payHtml);
                $("#alipaysubmit").submit();
                break;
        }
    }
    this.initData = function (data) {
        this.orderRecord = data;
        $("#index_total_price").html(data.orderRecordTotalPrice);
        $("#index_order_record_order_id").html(data.orderRecordOrderId);
        $("#index_order_goods_list").empty();
        for (var i = 0; i < data.orderGoodsArray.length; i++) {
            var orderGoods = data.orderGoodsArray[i];
            var view = this.createGoodsView(orderGoods);
            $("#index_order_goods_list").append(view);
        }

    }
    this.createGoodsView = function (orderGoods) {
        var view = document.createElement("li");
        var body =
            '<div class="orderLT mb20">' +
            '<p class="fl">' + orderGoods.orderGoodsName + '</p>' +
            '<div class="fr">' +
            '<p class="fl">数量：<span>' + orderGoods.orderGoodsNumber + '</span></p>' +
            '<p class="fl">单价：<span>' + orderGoods.orderGoodsPrice + '</span></p>' +
            '<div class="clear"></div>' +
            '</div>' +
            '<div class="clear"></div>' +
            '</div>' +
            '<p class="proDetail">产品详情：<span>' + orderGoods.orderGoodsDetail + '</span></p>';
        view.innerHTML = body;
        return $(view);
    }
    this.onPayClick = function () {
        if (!$T.indexMediator.isCanPay) {
            alert("不能支付");
        }
        $T.payProxy.getPayHtml($T.indexMediator.orderRecord.orderRecordId)
    }
}
$T.indexMediator = new IndexMediator();