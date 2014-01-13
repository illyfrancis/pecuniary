define ['backbone'], (Backbone) -> 
  Backbone.View.extend({
    el: 'body'
    load: () -> this.render()
    render: () -> 
      this.$el.append('<div>yo!</div>')
      this
  })