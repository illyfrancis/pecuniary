/*global define*/
define(['backbone'], function (Backbone) {
  return Backbone.Model.extend({
    defaults: {
      name: '',
      type: 'bank', // credit card, cash, other, asset, liability
      financialInstitution: '', // american express ...
      accountNumber: '',
      comment: ''
    }
  });
});