/**
 * Created by IntelliJ IDEA.
 * User: rsmart
 * Date: 23/12/10
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
Selenium.prototype.generateKeyEvent = function(locator, eventType, keyCode, characterCode, canBubble, controlKeyDown, altKeyDown, shiftKeyDown, metaKeyDown) {

    var element = this.browserbot.findElement(locator);
    canBubble = (typeof(canBubble) == undefined) ? true : canBubble;
    if (element.fireEvent && element.ownerDocument && element.ownerDocument.createEventObject) { // IE
        var keyEvent = createEventObject(element, controlKeyDown, altKeyDown, shiftKeyDown, metaKeyDown);
        keyEvent.keyCode = keyCode;
        keyEvent.characterCode = characterCode;

        element.fireEvent('on' + eventType, keyEvent);
    }
    else {
        var evt;
        if (window.KeyEvent) {
            evt = document.createEvent('KeyEvents');
            evt.initKeyEvent(eventType, true, true, window, controlKeyDown, altKeyDown, shiftKeyDown, metaKeyDown, keyCode, characterCode);
        } else {
            evt = document.createEvent('UIEvents');

            evt.shiftKey = shiftKeyDown;
            evt.metaKey = metaKeyDown;
            evt.altKey = altKeyDown;
            evt.ctrlKey = controlKeyDown;

            evt.initUIEvent(eventType, true, true, window, 1);
            evt.keyCode = keycode;
            evt.which = keycode;
            evt.characterCode = characterCode;
        }

        element.dispatchEvent(evt);
    }
};
