(function(window) {
    ATLWD.scriptloader = {};
    ATLWD.scriptloader.loadedScripts = [];

    ATLWD.scriptloader.scriptLoaded = function(scriptName) {
        window.console.log("scriptLoaded called!");
        ATLWD.scriptloader.loadedScripts.push(scriptName);
    };

    ATLWD.scriptloader.isLoaded = function(scriptName) {
        window.console.log("scriptLoaded called!");
        return ATLWD.arrayContains(ATLWD.scriptloader.loadedScripts, scriptName);
    };

})(window);

