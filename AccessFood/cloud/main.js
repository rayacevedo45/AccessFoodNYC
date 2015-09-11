
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.define("averageRatings", function(request, response) {
	var query = new Parse.Query("Review");
	query.equalTo("vendor", request.params.vendor);
	query.find({
		success: function(results) {
			var sum = 0;
			for (var i = 0; i < results.length; ++i) {
				sum += results[i].get("rating");
			}
			response.success(sum / results.length);
		},
		error: function() {
			response.error("vendor lookup failed");
		}
	});
});
