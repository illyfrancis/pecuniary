/*global define*/
define(['backbone'], function (Backbone) {

  var Workspace = Backbone.Router.extend({

    routes: {
      "payee": "payee",
      "payees": "payees"
    },

    payee: function () {
      console.log("routed to payee");
    },

    payees: function () {
      console.log("routed to payees list");
    }
  });

  return Workspace;
    
});
