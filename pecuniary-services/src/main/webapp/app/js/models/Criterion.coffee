define ['backbone'], (Backbone) -> Backbone.Model.extend

  defaults:
    name: ''
    title: ''
    isApplied: false

  removeFilter: -> 
    @setFilter(false)
    return

  toggleFilter: ->
    @setFilter(!this.get('isApplied'))
    return

  setFilter: (status) ->
    @set('isApplied', status is true, validate: true)
    return

  hydrate: (filter) ->
    @setFilter(filter.isApplied)
    return

  preserve: ->
    name: @get('name')
    isApplied: @get('isApplied')

  reset: -> 
    @removeFilter()
    return

  queryCriteria: -> {}
