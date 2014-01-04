define ['underscore', 'backbone'], (_, Backbone) -> 

  class Formatter
    initialize: (options) ->
      @dateFormat = 'YYYY/MM/DD'
      @dateTimeFormat = 'YYYY/MM/DD hh:mm'

    _formatDate: (number, format) ->
      isValid = not _.isUndefined(number) and not _.isNaN(number) and _.isNumber(number)
      if isValid 
        moment(number).format(format) 
      else ''

    formatDate: (number) ->
      @_formatDate(number, @dateFormat)

    formatDateTime: (number) ->
      @_formatDate(number, @dateTimeFormat)

    formatDecimal: (number, precision) ->
      parts = parseFloat(number).toFixed(precision).toString().split('.')
      parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",")
      parts.join('.')
