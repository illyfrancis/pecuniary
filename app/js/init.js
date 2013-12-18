/*global define*/
var app = app || {};
app.data = app.data || {};

var console = console || {};
console.log = console.log ||  function () {};

define([
    'jquery',
    'underscore',
    'backbone',
    'bootstrap',
    'apps/Extension2',
//    'apps/EventBus'
//    'apps/Repository',
//    'apps/Mediator'
//    'views/BogoApp'
    'views/App'
//], function ($, _, Backbone, Bootstrap, Extension, EventBus, Repository, Mediator, BogoApp) {
], function ($, _, Backbone, Bootstrap, Extension, App) {

    // disable cache (esp for IE)
    $.ajaxSetup({ cache: false });

    // enable tooltips
    $('body').tooltip({
        selector: '[rel=tooltip]'
    });

    // loading image
    // http://stackoverflow.com/questions/68485/how-to-show-loading-spinner-in-jquery
    $('#loadingDiv').hide();
    $(document).ajaxStart(function () {
        console.log('ajax start');
        $('#loadingDiv').show();
    }).ajaxStop(function () {
        console.log('ajax stop');
        $('#loadingDiv').hide();
    });

    //-------------------------------------------------------------------------
    // Mediator
    //-------------------------------------------------------------------------
    // new Mediator(EventBus);

    //-------------------------------------------------------------------------
    // main app (app.views.bogo)
    //-------------------------------------------------------------------------
    var bogo = new App();
    bogo.load();

    // export
    // app.bogo = bogo;
    // app.repo = Repository;
    // app.ev = EventBus;
});
