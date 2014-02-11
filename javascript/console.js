(function (exports, global) {
	var print = global.print;

	function log() {
		var args = [].slice.call(arguments);
		print(args.join(" ")); 

	}

	exports.console = {
		log:log
	};

})(this, this);
