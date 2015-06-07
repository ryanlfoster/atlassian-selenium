// fix nested draggables in IE - http://dev.jqueryui.com/ticket/4333
jQuery.ui.draggable.prototype._mouseCapture = (function (orig) {
    return function (event) {
        var result = orig.call(this, event);
        if (result && jQuery.browser.msie) {
            event.stopPropagation();
        }
        return result;
    };
})(jQuery.ui.draggable.prototype._mouseCapture);
