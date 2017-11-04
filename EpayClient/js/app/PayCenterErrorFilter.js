function PayCenterErrorFilter() {
    this.filter = function (result, sendParam) {
        if (result.hOpCode == 199) {
            $T.viewManager.notifyObservers($T.viewManager.getNotification($T.notificationExt.PAYCENTER_ERROR, result.errorCode));
            return false;
        } else {
            return true;
        }
    }
}