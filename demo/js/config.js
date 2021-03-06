// Require.js allows us to configure shortcut alias
require.config({
    // baseUrl: '.',
    paths: {
        'jquery': '../lib/jquery-1.9.1',
        'jquery.ui': '../lib/jquery-ui-1.9.0.custom',
        'bootstrap': '../lib/bootstrap-3.0.0/js/bootstrap',
        'underscore': '../lib/underscore-1.5.2',
        'backbone': '../lib/backbone-1.1.0',
        'moment': '../lib/moment-2.3.1',
        'text': '../lib/require/text'
    },
    shim: {
        'underscore': {
            exports: '_'
        },
        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },
        'jquery.ui': {
            deps: ['jquery']
        },
        'bootstrap': {
            deps: ['jquery', 'jquery.ui']
        }
    }
    // ,
    // urlArgs: 'bust=' + (new Date()).getTime()
});

require(['main'], function () {});