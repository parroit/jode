var format = require("util").format;

function log() {
    print(format.apply(this, arguments) + "\n");


}

module.exports = {
    log: log
};
