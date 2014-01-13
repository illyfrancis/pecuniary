/*global define*/
define(['backbone'], function (Backbone) {
  return Backbone.Model.extend({
    defaults: {
      name: '',
      type: 'E'
    }
  });
});