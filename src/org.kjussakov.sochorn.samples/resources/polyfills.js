// Polyfills for some basics - only the bare minimum.
// Look at https://github.com/shendepu/nashorn-polyfill for more complete list of Nashorn polyfills

var global = this;
var self = this;
var window = this;
var process = {env: {}};
var console = {};
console.debug = print;
console.warn = print;
console.log = print;
console.error = print;
console.trace = print;

// The WebSocket Nashorn polyfill
var WebSocket = Java.type('org.kjussakov.sochorn.WebSocket');

// This is only needed for the Echo test to signal that the test is complete
var Echo = Java.type('org.kjussakov.sochorn.samples.Echo');