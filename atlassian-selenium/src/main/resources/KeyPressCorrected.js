Selenium.prototype.getBB = function() {
    return this.browserbot;
};

selenium.getBB().triggerKeyEvent = function(element, eventType, keySequence, canBubble, controlKeyDown, altKeyDown, shiftKeyDown, metaKeyDown, toKeyCode, toCharacterCode) {
    var potentialCode = getKeyCodeFromKeySequence(keySequence);
    var keycode = 0;
    var characterCode = 0;
    if (toKeyCode == undefined || toKeyCode == true) {
        keycode = potentialCode;
    }
    if (toCharacterCode == undefined || toCharacterCode == true) {
        characterCode = potentialCode;
    }
    canBubble = (typeof(canBubble) == undefined) ? true : canBubble;
    if (element.fireEvent && element.ownerDocument && element.ownerDocument.createEventObject) { // IE
        var keyEvent = createEventObject(element, controlKeyDown, altKeyDown, shiftKeyDown, metaKeyDown);
        keyEvent.keyCode = potentialCode;
        element.fireEvent('on' + eventType, keyEvent);
    }
    else {
        var evt;
        if (window.KeyEvent) {
            evt = document.createEvent('KeyEvents');
            evt.initKeyEvent(eventType, true, true, window, controlKeyDown, altKeyDown, shiftKeyDown, metaKeyDown, keycode, characterCode);
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

Selenium.prototype.doKeyPress = function(locator, keySequence) {
    /**
   * Simulates a user pressing and releasing a key.
   *
   * @param locator an <a href="#locators">element locator</a>
   * @param keySequence Either be a string("\" followed by the numeric keycode
   *  of the key to be pressed, normally the ASCII value of that key), or a single
   *  character. For example: "w", "\119".
   */
    var element = this.browserbot.findElement(locator);
    triggerKeyEvent(element, 'keypress', keySequence, true,
        this.browserbot.controlKeyDown,
        this.browserbot.altKeyDown,
            this.browserbot.shiftKeyDown,
            this.browserbot.metaKeyDown,
            this.browserbot.toKeyCode,
            this.browserbot.toCharacterCode);
};

Selenium.prototype.doKeyDown = function(locator, keySequence) {
    /**
   * Simulates a user pressing a key (without releasing it yet).
   *
   * @param locator an <a href="#locators">element locator</a>
   * @param keySequence Either be a string("\" followed by the numeric keycode
   *  of the key to be pressed, normally the ASCII value of that key), or a single
   *  character. For example: "w", "\119".
   */
    var element = this.browserbot.findElement(locator);
    triggerKeyEvent(element, 'keydown', keySequence, true,
        this.browserbot.controlKeyDown,
            this.browserbot.altKeyDown,
            this.browserbot.shiftKeyDown,
            this.browserbot.metaKeyDown,
            this.browserbot.toKeyCode,
            this.browserbot.toCharacterCode);
};

Selenium.prototype.doKeyUp = function(locator, keySequence) {
    /**
   * Simulates a user releasing a key.
   *
   * @param locator an <a href="#locators">element locator</a>
   * @param keySequence Either be a string("\" followed by the numeric keycode
   *  of the key to be pressed, normally the ASCII value of that key), or a single
   *  character. For example: "w", "\119".
   */
    var element = this.browserbot.findElement(locator);
    triggerKeyEvent(element, 'keyup', keySequence, true,
        this.browserbot.controlKeyDown,
            this.browserbot.altKeyDown,
        this.browserbot.shiftKeyDown,
            this.browserbot.metaKeyDown,
            this.browserbot.toKeyCode,
            this.browserbot.toCharacterCode);
};


triggerKeyEvent = selenium.getBB().triggerKeyEvent;

commandFactory.registerAll(selenium);


selenium.getBB().toKeyCode = true;
selenium.getBB().toCharacterCode = true;


