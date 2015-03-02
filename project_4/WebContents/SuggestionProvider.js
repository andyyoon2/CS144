/* Adapted from the given example:
 * Creating an Autosuggest Textbox with Javascript
 * by Nicholas C. Zakas
 */

function SuggestionProvider() {}

SuggestionProvider.prototype.requestSuggestions = function (oAutoSuggestControl, bTypeAhead) {
  var aSuggestions = [];
  var sTextboxValue = oAutoSuggestControl.textbox.value;
  var url = "/eBay/suggest?q=" + sTextboxValue;

  // Ajax request
  $.get(url, function (data) {
    var $xml = $(data);
    // Populate aray of results
    var results = [];
    $.each($xml.find("suggestion"), function (key, value) {
      results.push($( value ).attr("data"));
    });
    
    if (sTextboxValue.length > 0){
      for (var i=0; i < results.length; i++) {
        if (results[i].indexOf(sTextboxValue) == 0) {
          aSuggestions.push(results[i]);
        }
      }
      oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
    }
  })
    .fail(function (data) {
      console.log(data);
    });
};