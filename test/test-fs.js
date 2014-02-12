var assert = require("assert"),
	Files = Java.type("jode.Files");

Files.INSTANCE.readFile("test/files/test.txt",function(err,data){
	assert.equal(data,"this is a test");
});
