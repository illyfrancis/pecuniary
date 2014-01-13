/*global define*/
define(['backbone'], function (Backbone) {
  return Backbone.Model.extend({
    defaults: {
      number: '',
      date: '',
      payee: '',
      amount: '',
      type: '', // charge or credit
      category: '',
      properties: '', // what is this?
      memo: ''
    },
    idAttribute: '_id'
  });
});