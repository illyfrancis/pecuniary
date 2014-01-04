/*global define*/
define(['backbone'], function (Backbone) {
  return Backbone.Model.extend({
    defaults: {
      name: '',
      phone: '',
      accountNumber: ''
    },
    move: function () {
      // move transaction to a different payee
    }
  });
});