/**
 * Firebug notifier - to post a warning note about potential performance issues in our apps if Firebug is enabled
 *
 * @method warnAboutFirebug
 * @namespace AJS
 * @for AJS
 * @param message {string} [optional] Contents of warning, defaults to English message if not specified
 *
 */
AJS.warnAboutFirebug = function (message) {
    if (!AJS.Cookie.read("COOKIE_FB_WARNING") && window.console && window.console.firebug) {
        if (!message){
            message = "Firebug is known to cause performance problems with Atlassian products. Try disabling it, if you notice any issues.";
        }
        var $warning = AJS.$("<div id='firebug-warning'><p>" + message + "</p><a class='close'>Close</a></div>");
        AJS.$(".close", $warning).click(function () {
            $warning.slideUp("fast");
            AJS.Cookie.save("COOKIE_FB_WARNING", "true");
        });
        $warning.prependTo(AJS.$("body"));
    }
};
