define(['underscore', 'typeahead', 'Baz'], function(_, tt, Baz) {
  $('.places').typeahead({
    name: 'places',
    local: ["Alabama","Alaska","Akka","Amanda","Aioki","Ado","West Virginia","Wisconsin","Wyoming"]
    // limit: 10
  });

  $('.multiplePlaces').typeahead([{
      name: 'places',
      local: ["Alabama","Alaska","Akka","Aioki","Ado","West Virginia","Wisconsin","Wyoming"],
      header: "<h3>Places to avoid</h3>",
      footer: "<p>place foot</p>"
    }, {
      name: 'people',
      local: ["Amanda","Bridget","Belinda","Cathy","Christine","Catherine"],
      header: "<h3>People you may know</h3>",
      footer: "<p>people foot</p>"
  }]);

  var transform = function(securities) {
    console.log("tx");
    return _.map(securities, function(item) {
      console.log("secId: " + item.secId);
      return {
        value: item.secId,
        tokens: [item.secId]
      }
    });
  }

  console.log("transform: " + JSON.stringify(transform([{secId: "111", type: "cupic"}])));

  $('.securities').typeahead({
    name: 'securities',
    remote: {
      url: "api/securities/search/%QUERY",
      filter: transform
    }
  });
})