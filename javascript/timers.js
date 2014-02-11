(function (exports, global) {

	var Timers = Java.type("jode.Timers");

	function setTimeout(cb,milliseconds){
		Timers.INSTANCE.setTimeout(cb, milliseconds);
	} 

	function setInterval(cb,milliseconds){
		Timers.INSTANCE.setInterval(cb, milliseconds);
	} 


	function clearTimeout(id){
		Timers.INSTANCE.clearTimeout(id);
	} 

	function clearInterval(id){
		Timers.INSTANCE.clearInterval(id);
	} 

	exports.setInterval = setInterval;
	exports.setTimeout = setTimeout;
	exports.clearInterval = clearInterval;
	exports.clearTimeout = clearTimeout;

})(this, this);
