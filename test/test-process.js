var assert = require("assert");
console.log(process.env.TERM)
if (process.env.TERM !== "xterm") {
    assert.equal(process.execPath, "c:\\Programmi\\Java\\jdk1.8.0\\jre\\bin\\java.exe");
    assert.equal(process.argv.join("-"), ["java.exe", "test\\test-process.js"].join("-"));
    assert.equal(process.cwdir(), "C:\\Documents and Settings\\parroit\\jode");
    assert.equal(process.arch, "ia32");
    assert.equal(process.platform, "win32");
    assert.equal(process.env.OS, "Windows_NT");

} else {
    console.log(process.env.HOST)
    assert.equal(process.env.TERM, "xterm");
    assert.equal(process.execPath, "/home/parroit/programmi/jdk1.8.0/jre/bin/java.exe");
    assert.equal(process.argv.join("-"), ["java.exe", "test/test-process.js"].join("-"));
    assert.equal(process.cwdir(), "/media/parroit/Elements/src/jode");
    assert.equal(process.arch, "x64");
    assert.equal(process.platform, "linux");
    

}


assert.equal(process.version, "0.0.1");
assert.deepEqual(process.versions, {
    java: "1.8.0",
    jode: "0.0.1",
    jna: "4.0.0"
});

assert.equal(typeof process.pid === "number", true);



var mem = process.memoryUsage();

assert.equal(mem.heapTotal > 10000, true);
assert.equal(mem.heapUsed > 10000, true);

var runned = false;
process.nextTick(function() {
    runned = true;
});
setTimeout(function() {
    assert.equal(runned, true);
    console.log("nextTick working.");
}, 100);
