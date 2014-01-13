define ['underscore', 'backbone', 'text!templates/DataEntry.html'], 
  (_, Backbone, tpl) -> Backbone.View.extend

    tagName: 'div'

    template: _.template tpl

    events:
      'click .destroy': 'destroy'
      'typeahead:selected': 'tell'

    initialize: ->
      # this.listenTo()

    render: ->
      @$el.html @template()
      @$('.donation').typeahead
        name: 'places'
        remote:
          url: 'api/securities/search/%QUERY'
          filter: (securities) ->
            _.map securities, (item) ->
              value: item.secId
              tokens: [item.secId]
              item: item
        limit: 10
      @

    tell: ->
      console.log 'some messsage'
      return

    destroy: ->
      @$('.donation').typeahead 'destroy'
      return
    