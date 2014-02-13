var assert = require("assert"),
    fs = require("fs");

fs.readFile("test/files/testa.txt", function(err, data) {
    assert.equal(err.message, "NoSuchFileException: C:\\Documents and Settings\\parroit\\jode\\test\\files\\testa.txt");

});


fs.readFile("test/files/test.txt", function(err, data) {
    if (err) {
        throw err;
    }
    assert.equal(err, null);
    assert.equal(data, "this is a test");
});


fs.readFile("test/files/test.txt", {
    bufferSize: 5
}, function(err, data) {
    if (err) {
        throw err;
    }
    assert.equal(err, null);
    assert.equal(data, "this is a test");
});



fs.readFile("test/files/test.txt", {
    bufferSize: 3
}, function(err, data) {
    if (err) {
        throw err;
    }
    assert.equal(err, null);
    assert.equal(data, "this is a test");
});
