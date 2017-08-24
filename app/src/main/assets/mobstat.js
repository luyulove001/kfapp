/**
 * @file support js stat
 */

var BaiduMobStat = (function () {
    var invokeNatvieMethod = function (action, obj) {
        console.info('invokeNatvieMethod');

        // call native method
        var cmd = {
            action: action,
            obj: obj
        };

        var iFrame = document.createElement('iframe');
        iFrame.setAttribute('src', 'bmtj:' + JSON.stringify(cmd));
        iFrame.setAttribute('style', 'display:none;');
        iFrame.setAttribute('height', '0px');
        iFrame.setAttribute('width', '0px');
        iFrame.setAttribute('frameborder', '0');

        document.body.appendChild(iFrame);

        // 发起请求后这个iFrame就没用了，所以把它从dom上移除掉
        iFrame.parentNode.removeChild(iFrame);
        iFrame = null;
    };

    return {
        onPageStart: function (page) {
            var obj = {
                'page': page
            };

            invokeNatvieMethod('onPageStart', obj);
        },
        onPageEnd: function (page) {
            var obj = {
                'page': page
            };

            invokeNatvieMethod('onPageEnd', obj);
        },
        onEvent: function (id, label, acc) {
            var obj = {
                'event_id': id,
                'label': label,
                'acc': typeof acc === 'number' ? acc : 1
            };

            invokeNatvieMethod('onEvent', obj);
        },
        onEventStart: function (id, label) {
            var obj = {
                'event_id': id,
                'label': label
            };

            invokeNatvieMethod('onEventStart', obj);
        },
        onEventEnd: function (id, label) {
            var obj = {
                'event_id': id,
                'label': label
            };

            invokeNatvieMethod('onEventEnd', obj);
        },
        onEventDuration: function (id, label, duration) {
            var obj = {
                'event_id': id,
                'label': label,
                'duration': typeof duration === 'number' ? duration : -1
            };

            invokeNatvieMethod('onEventDuration', obj);
        },
        onEventWithAttributes: function (id, label, acc, attributes) {
            var obj = {
                'event_id': id,
                'label': label,
                'acc': typeof acc === 'number' ? acc : 1,
                'attributes': typeof attributes === 'object' ? attributes : {}
            };

            invokeNatvieMethod('onEvent', obj);
        },
        onEventEndWithAttributes: function (id, label, attributes) {
            var obj = {
                'event_id': id,
                'label': label,
                'attributes': typeof attributes === 'object' ? attributes : {}
            };

            invokeNatvieMethod('onEventEnd', obj);
        },
        onEventDurationWithAttributes: function (id, label, duration, attributes) {
            var obj = {
                'event_id': id,
                'label': label,
                'duration': typeof duration === 'number' ? duration : 0,
                'attributes': typeof attributes === 'object' ? attributes : {}
            };

            invokeNatvieMethod('onEventDuration', obj);
        }
    };
}());