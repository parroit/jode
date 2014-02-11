exports.init = function(global) {

    global._module._builtins = require("./builtins");

    global.console = require("./console");

    var timers = require("./timers");

    global.setInterval = timers.setInterval;
    global.setTimeout = timers.setTimeout;
    global.clearInterval = timers.clearInterval;
    global.clearTimeout = timers.clearTimeout;

};
