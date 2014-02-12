var Files = Java.type("jode.Files");

function wrapException(ex) {
    return new Error(ex.getClass().getSimpleName() + ": " + ex.getMessage());
}

function readFile(path, options, cb) {
    if (typeof options === "function") {
        cb = options;
        options = {
            encoding: null,
            flag: "r",
            bufferSize: 10000
        };
    } else {
        options = options || {};
        options.encoding = options.encoding  || null;
        options.flag = options.flag || "r";
        options.bufferSize = options.bufferSize || 10000;
    }

    Files.INSTANCE.readFile(path, options.bufferSize, function(ex, data) {
        if (ex) {
            return cb(wrapException(ex), null);
        }
        cb(null, data);
    });


}

exports.readFile = readFile;
