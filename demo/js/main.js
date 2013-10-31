var app = app || {}, console = console || {};
console.log = console.log ||  function () {};
console.log("started...");

define(['backbone', 'models/Account'], function (Backbone, Account) {

    var Accounts = Backbone.Collection.extend({
        model: Account,
        url: '/api/accounts',
        selectSome: function () {
            console.log('do stuff');
            this.nums = this.pluck('number');
            console.log('number size: ' + this.nums.length);
            this.huge = this.nums.concat(this.nums);
            console.log('number size: ' + this.huge.length);

            this.findSome();
        },
        findSome: function () {
            this.index = _.indexOf(this.huge, '6023717');
            this.lastindex = _.lastIndexOf(this.huge, '6023717');
        }
    });

    var accounts = new Accounts();
    app.acct = accounts || {};

    accounts.fetch({
        success: function () {
            console.log('fetched');
            accounts.selectSome();
        }
    });
});