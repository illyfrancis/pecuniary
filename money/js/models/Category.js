/*global define*/
define(['backbone'], function (Backbone) {

    var Category = Backbone.Model.extend({

        defaults: {
            name: '',
            type: 'income'  // income|exprense|tax
        }
    });

    return Category;

});