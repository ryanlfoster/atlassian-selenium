/*->
#name>Tables
#javascript>No
#css>Yes
#description> Standards Patterns and Styling for HTML Tables
<-*/
(function() {
    AJS.tables = AJS.tables || {};
    AJS.tables.rowStriping = function () {
        var tables = AJS.$("table.aui");
        for (var i=0, ii = tables.length; i < ii; i++) {
            AJS.$("tbody tr:odd", tables[i]).addClass("zebra");
        };
    };
    AJS.$(AJS.tables.rowStriping);
})();
