// template engine wrapper

define(['underscore'], function (_) {
    // var Baz = function () {
    // };

    // _.extend(Baz.prototype, {
    // 	compile: function (tpl) {
    // 		var engine = _.template(tpl)
    // 		return {
    // 			render: function (contenxt) {
    // 				return engine(context)
    // 			}
    // 		}
    // 	},
    // });

    return {
    	compile: function (tpl) {
		var engine = _.template(tpl)
		return {
			render: function (context) {
				return engine(context)
			}
		}
    }}
});