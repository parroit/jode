(function(exports, global) {
    var print = global.print;

    var formatRegExp = /%[sdj]/g;

    function format(f) {
        //var util = require("util");

        if (typeof f !== "string") {
            var objects = [];
            for (var idx = 0; idx < arguments.length; idx++) {
                //objects.push(util.inspect(arguments[i]));
                objects.push(String(arguments[idx]));
            }
            return objects.join(" ");
        }


        var i = 1;
        var args = arguments;
        var str = String(f).replace(formatRegExp, function(x) {
            switch (x) {
                case "%s":
                    return String(args[i++]);
                case "%d":
                    return Number(args[i++]);
                case "%j":
                    //return JSON.stringify(args[i++]);
                    return String(args[i++]);
                default:
                    return x;
            }
        });

        for (var len = args.length, x = args[i]; i < len; x = args[++i]) {
            if (x === null || typeof x !== "object") {
                str += " " + x;
            } else {
                //str += " " + util.inspect(x);
                str += " " + String(x);
            }
        }
        return str;
    }

    function log() {
        print(format.apply(this, arguments) + "\n");

        
    }

    exports.console = {
        log: log
    };

})(this, this);
