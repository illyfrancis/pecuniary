/*global define*/
define(['backbone'], function (Backbone) {

    var Transaction = Backbone.Model.extend({

        defaults: {
            name: '',
            number: ''
        }
    });

    return Transaction;

});