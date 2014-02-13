var System = Java.type("java.lang.System"),
	File = Java.type("java.io.File"),
	PlatformCommon = Java.type("jode.PlatformCommon"),
	Runtime = Java.type("java.lang.Runtime"),
	EventEmitter = require("events").EventEmitter,
	util = require("util"),
	EventLoop = Java.type("jode.EventLoop");


function Process(){
	EventEmitter.call(this);
}

util.inherits(Process,EventEmitter);


var process = new Process();

module.exports = process;


(function execPath() {
    var javaHome = System.getProperty("java.home");
    var folder = new File(javaHome,"bin");
    process.execPath = new File(folder,"java.exe").getAbsolutePath();

})();

process.argv = Java.from(PlatformCommon.INSTANCE.args);

process.argv.splice(0, 0, "java.exe");

process.execArgv = [];

process.exit = function(code){
	System.exit(code);
};

process.abort = function(){
	System.exit(-1);
};

process.chdir = function(directory) {

};

process.cwd = function() {
	return System.getProperty("user.dir");
};

process.env = System.getenv();

var version = process.version = "0.0.1";

process.versions = {
	java: System.getProperty("java.version"),
    	jna: "4.0.0",
	jode: version
};

process.config = {};




process.arch = System.getProperty("os.arch")==="x86" ? "ia32" : "x64";
process.platform = System.getProperty("os.name").indexOf("Windows") > -1 ? "win32" : "linux";


var Platform = Java.type("jode.Platform_" + process.platform);

process.pid = Platform.INSTANCE.pid();


process.memoryUsage = function(){
	return {
		heapTotal: Runtime.getRuntime().totalMemory(),
		heapUsed: Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
	};
};

process.nextTick = function(callback) {
	EventLoop.INSTANCE.run(callback);
};