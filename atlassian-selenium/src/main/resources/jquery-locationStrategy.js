/**
 * NOTE: This code is wrapped in a function by the selenium client. It looks like
 * function(locator, inDocument, inWindow). See Selenium.prototype.doAddLocationStrategy
 * in selenium-api.js.
 *
 * A locator that uses jQuery to locate elements. It makes it possible to identify elements using jQuery selectors.
 * For example, jquery=#dialog .error:contains(HSP-1).
 *
 * @param locator the locator. Selenium will have stripped off the 'jquery=' part.
 * @param inDocument the document that is actually being tested. Selenium may be running
 *   in a different document.
 * @param inWindow the window of the document being tested. Selenium may be running in a
 *   different window.
 */

var attrLocator = null;
//This is not necessary since Selenium will strip off the attribute for us (see BrowserBot.prototype.findAttribute in
//selenium-browserbot.js). Leaving this around in-case people are using the jquery=selector@attribute outside of
//the getAttribute selenium call.
var inx = locator.lastIndexOf('@'), isAttr = false;
if(inx != -1) {
    attrLocator = locator.substring(inx + 1);
    locator = locator.substring(0, inx);
    isAttr = true;
}

var found;
if (inWindow.jQuery) {
    //If jQuery exists in the target window then use it to find the element.
    found = inWindow.jQuery(locator);
} else {
    //This is probably broken because of https://extranet.atlassian.com/x/rKgubw. Unfortunately there are times
    //when the target window does not have jQuery and we must do this instead.
    LOG.warn("Using the hacky and possibly broken selector because the target window does not have jQuery.");
    found = jQuery(inDocument).find(locator);
}

if (found.length >= 1) {
    if (isAttr) {
        return found.attr(attrLocator);
    } else {
        return found[0];
    }
} else {
    return null;
}