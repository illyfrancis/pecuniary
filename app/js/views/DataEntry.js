define([
  'underscore',
  'backbone',
  'text!templates/DataEntry.html',
  'datepicker'
], function (_, Backbone, tpl, dp) {
// define([
//   'text!templates/DataEntry.html',
//   'datepicker'
// ], function (tpl, dp) {

  return Backbone.View.extend({

    // tagName: 'tr',

    template: _.template(tpl),

    events: {
      'click .destroy': 'destroy',
      'typeahead:selected': 'tell',
      'clearDate': 'messageFromDatePicker',
      'changeDate': 'messageFromDatePicker',
      'changeYear': 'messageFromDatePicker',
      'changeMonth': 'messageFromDatePicker',
      // ,
      // 'onRender .checkIn': 'messageFromDatePicker'
    },

    initialize: function () {
      // this.listenTo(this.model, 'change', this.render);
    },

    render: function () {
      // this.$el.empty(); -> $.html() internally calls .empty() before appending
      this.$el.html(this.template());
      this.$('.donation').typeahead({
        name: 'places',
        remote: {
          url: 'api/securities/search/%QUERY',
          filter: function (securities) {
            return _.map(securities, function (item) {
              return {
                value: item.secId,
                tokens: [item.secId],
                item: item
              }
            });
          }
        },
        limit: 10
      });
      this.$('.checkIn, .checkOut').datepicker({
        autoclose: true
      });
      // this.delegateEvents();
      return this;
    },

    toggleSelection: function () {
      this.model.toggle();
    },

    tell: function () {
      console.log('some event change: [' + arguments[0].type + "] : [" + JSON.stringify(arguments[1]) + "]");
    },

    destroy: function () {
      this.$('.donation').typeahead('destroy');
    },

    messageFromDatePicker: function (e) {
      console.log('datepicker event [' + e.type + "] : " + e.date);
    }
  });

});

// coffeeScript =>

/*
define ['underscore', 'backbone', 'text!templates/DataEntry.html'] (_, Backbone, tpl) ->
  Backbone.View.extend

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
      console.log('some messsage')
      return

    destroy: ->
      @$('.donation').typeahead('destroy')
      return

*/