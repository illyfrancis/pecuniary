/*global define*/
define(['backbone'], function (Bbackbone) {

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
