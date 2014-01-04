define([
    'underscore',
    'backbone',
    'models/Account'
], function (_, Backbone, Account) {

    var Accounts = Backbone.Collection.extend({
        model: Account,
        url: '/api/accountsfilter/search',
        parse: function (response, options) {
        	return response.accounts;
        }
    });

    return Accounts;
});