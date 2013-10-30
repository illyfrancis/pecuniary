var app = app || {}, console = console || {};
console.log = console.log ||  function () {};
console.log("started...");

define(['backbone', 'models/Account'], function (Backbone, Account) {

    var Accounts = Backbone.Collection.extend({
        model: Account,
        url: '/api/accounts',
        selectSome: function () {
            
        }
    });

    var accounts = new Accounts();
    app.accounts = accounts || {};

    // accounts.fetch();
});