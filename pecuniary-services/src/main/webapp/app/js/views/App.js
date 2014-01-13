/*global define*/
define(['backbone', 'views/DataEntry'], function(Backbone, DataEntry) {
  return Backbone.View.extend({
    el: 'body',
    load: function() {
      this.render();
    },
    render: function() {
      this.$el.append("<div>In main</div>");
      var dataEntry = new DataEntry();
      this.$el.append(dataEntry.render().el)
      return this;
    }
  });
});