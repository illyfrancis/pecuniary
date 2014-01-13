define ['backbone'], (Backbone) -> 
  Backbone.View.extend({
    el: 'body'
    load: () -> this.render()
    render: () -> 
      this.$el.append('<div>yo!</div>')
      this
  })

/*
define ['backbone'], (Backbone) -> 
  Backbone.View.extend 
    el: 'body'
    load: -> this.render()
    render: -> 
      this.$el.append '<div>yo!</div>'
      this

define ['backbone'], (Backbone) -> Backbone.View.extend
  el: 'body'
  load: -> this.render()
  render: -> 
    this.$el.append '<div>yo!</div>'
    this

define ['backbone'], (Backbone) -> Backbone.View.extend
  el: 'body'
  load: -> @render()
  render: -> 
    @.$el.append '<div>yo!</div>'; @
*/