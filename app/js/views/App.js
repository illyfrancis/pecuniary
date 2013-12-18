/*global define*/
define(['backbone'], function(Backbone) {
  return Backbone.View.extend({
    el: 'body',
    load: function() {
      this.render();
    },
    render: function() {
      this.$el.append("<div>In main</div>");
      return this;
    }
  });
});