Selenium.prototype.getTagName = function(locator) {
    var element = this.browserbot.findElement(locator);
    if (element) {
        return element.tagName;
    }
    else return null;
};


