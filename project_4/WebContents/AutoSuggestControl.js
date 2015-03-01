/* Adapted from the given example:
 * Creating an Autosuggest Textbox with Javascript
 * by Nicholas C. Zakas
 */

function AutoSuggestControl(oTextbox, oProvider) {
  this.textbox = oTextbox;
  this.provider = oProvider;
  this.init();
}

AutoSuggestControl.prototype.selectRange = function (iStart, iLength) {
  if (this.textbox.createTextRange) {
    // IE
    var oRange = this.textbox.createTextRange(); 
    oRange.moveStart("character", iStart); 
    oRange.moveEnd("character", iLength - this.textbox.value.length); 
    oRange.select();
  } else if (this.textbox.setSelectionRange) {
    // Firefox
    this.textbox.setSelectionRange(iStart, iLength);
  }

  this.textbox.focus(); 
};

AutoSuggestControl.prototype.typeAhead = function (sSuggestion) {
  if (this.textbox.createTextRange || this.textbox.setSelectionRange) {
    var iLen = this.textbox.value.length; 
    this.textbox.value = sSuggestion; 
    this.selectRange(iLen, sSuggestion.length);
  }
};

AutoSuggestControl.prototype.autosuggest = function (aSuggestions) {
  // Make sure aSuggestions isn't empty
  if (aSuggestions.length > 0) {
    this.typeAhead(aSuggestions[0]);
  }
};

AutoSuggestControl.prototype.handleKeyUp = function (oEvent) {
  var iKeyCode = oEvent.keyCode;

  if (iKeyCode < 32 || (iKeyCode >= 33 && iKeyCode <= 46) || (iKeyCode >= 112 && iKeyCode <= 123)) {
    //ignore
  } else {
    this.provider.requestSuggestions(this);
  }
};

AutoSuggestControl.prototype.init = function () {
  var oThis = this;
  this.textbox.onkeyup = function (oEvent) {
    if (!oEvent) {
      oEvent = window.event;
    }
    oThis.handleKeyUp(oEvent);
  };
};