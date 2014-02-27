console.log("in start-init.js");

define(['apps/Router'], function(Router) {
  var router = new Router();
  // Backbone.history.start({pushState: true, root: "/Repository/git/pecuniary/pecuniary-services/src/main/webapp/app/"});
  Backbone.history.start({pushState: true, root: "/money/app/"});
  // Backbone.history.start();

  // router.navigate("payee");
  //router.navigate("payee", {trigger: true});
});