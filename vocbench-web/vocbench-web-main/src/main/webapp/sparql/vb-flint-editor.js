/*
 * This SPARQL Editor is built on the Flint SPARQL Editor
 * (http://openuplabs.tso.co.uk/demos/sparqleditor) based on version 1.0.3
 * (https://github.com/TSO-Openup/FlintSparqlEditor/releases/tag/v1.0.3).
 */
function FlintEditor(container, status, submit, jsonObj) {

	"use strict";
	
	var editor;
	var cm;
	
	var json = JSON.parse(jsonObj);
	var editorMode = json.editorMode;
	var nsPrefixMappings = json.nsPrefixMappings;
	var namedGraphs = json.namedGraphs;
	
	
	function FlintButton(submit) {
		this.disable = function() {
			submit.disabled = true; 
		};

		this.enable = function() {
			submit.disabled = false;
		};
	}
	
	function FlintStatus(status) {
		var line = 0;
		var position = 0;
		var queryValid = "valid";

		this.setLine = function(cursorLine) {
			line = cursorLine;
		};

		this.setQueryValid = function(valid) {
			if (valid) {
				queryValid = "valid";
			} else {
				queryValid = "invalid";
			}
		};

		this.setPosition = function(cursorPosition) {
			position = cursorPosition;
		};

		this.display = function(container) {
			//$('#' + container).append("<div id='flint-status'>...</div>");
		};

		this.updateStatus = function() {
			status.value = "Line: " + (line + 1) + "; Position: " + (position + 1)
							+ "; Query is " + queryValid;
		};
	}

	// This next section is the code for the actual editing window
	function FlintCodeEditor(flint, editorMode, cm) {
		
		var initialQuery = "SELECT * WHERE {\n   ?s ?p ?o\n}\nLIMIT 10";
		var clearError = function() {
		};
		var markerHandle = null;
		var codeEditor = this;
		var queryType = "";

		this.getTitle = function() {
			return "";
		};

		// ----------------------------------------------------------------
		// Autocompletion code, based on the example for javascript

		function stopEvent() {
			if (this.preventDefault) {
				this.preventDefault();
				this.stopPropagation();
			} else {
				this.returnValue = false;
				this.cancelBubble = true;
			}
		}

		function addStop(event) {
			if (!event.stop) {
				event.stop = stopEvent;
			}
			return event;
		}

		function connect(node, type, handler) {

			function wrapHandler(event) {
				handler(addStop(event || window.event));
			}

			if (typeof node.addEventListener == "function") {
				node.addEventListener(type, wrapHandler, false);
			} else {
				node.attachEvent("on" + type, wrapHandler);
			}
		}

		function forEach(arr, f) {
			var i;
			var e;
			for (i = 0, e = arr.length; i < e; ++i) {
				f(arr[i]);
			}
		}

		function memberChk(el, arr) {
			var i;
			var e;
			for (i = 0, e = arr.length; i < e; ++i) {
				if (arr[i] == el) {
					return (true);
				}
			}
			return false;
		}

		// Extract context info needed for autocompletion / keyword buttons
		// based on cursor position
		function getPossiblesAtCursor() {
			// We want a single cursor position.
			if (cm.somethingSelected()) {
				return;
			}
			// Find the token at the cursor
			var cur = cm.getCursor(false);
			var cur1 = {
				line : cur.line,
				ch : cur.ch
			};

			// Before cursor
			var charBefore = cm.getRange({
				line : cur.line,
				ch : cur.ch - 1
			}, {
				line : cur.line,
				ch : cur.ch
			});

			// Cursor position on the far left (ch=0) is problematic
			// - if we ask CodeMirror for token at this position, we don't
			// get back the token at the beginning of the line
			// - hence use adjusted position cur1 to recover this token.
			if (cur1.ch == 0 && cm.lineInfo(cur1.line).text.length > 0) {
				cur1.ch = 1;
			}
			var token = cm.getTokenAt(cur1);
			var charAfter;
			var possibles = null;
			var start = token.string.toLowerCase();
			var insertPos = null;
			var insertEnd = false;
			var insertStart = false;

			// if the token is whitespace, use empty string for matching
			// and set insertPos, so that selection will be inserted into
			// into space, rather than replacing it.
			if (token.className == "sp-ws") {
				start = "";
				// charAfter is char after cursor
				charAfter = cm.getRange({
					line : cur.line,
					ch : cur.ch
				}, {
					line : cur.line,
					ch : cur.ch + 1
				});
				insertPos = cur;
			} else {
				// charAfter is char after end of token
				charAfter = cm.getRange({
					line : cur.line,
					ch : token.end
				}, {
					line : cur.line,
					ch : token.end + 1
				});
				if (token.className != "sp-invalid"
						&& token.className != "sp-prefixed"
						&& (token.string != "<" || !memberChk("IRI_REF",
								token.state.possibleCurrent))
				// OK when "<" is start of URI
				) {
					if (token.end == cur.ch && token.end != 0) {
						insertEnd = true;
						start = "";
						insertPos = cur;
					} else if (token.start == cur.ch) {
						insertStart = true;
						start = "";
						insertPos = cur;
					}
				}
			}

			if (token.className === "sp-comment") {
				possibles = [];
			} else {
				if (cur1.ch > 0 && !insertEnd) {
					possibles = token.state.possibleCurrent;
				} else {
					possibles = token.state.possibleNext;
				}
			}

			return {
				"token" : token, // codemirror token object
				"possibles" : possibles, // array of possibles terminals from
				// grammar
				"insertPos" : insertPos, // Position in line to insert text,
				// or null if replacing existing
				// text
				"insertStart" : insertStart, // true if position of insert
				// adjacent to start of a non-ws
				// token
				"insertEnd" : insertEnd, // true if ... ... end of a ...
				"charAfter" : charAfter, // char found straight after cursor
				"cur" : cur, // codemirror {line,ch} object giving pos of
				// cursor
				"start" : start
			// Start of token for autocompletion
			};
		}

		var keywords = /^(GROUP_CONCAT|DATATYPE|BASE|PREFIX|SELECT|CONSTRUCT|DESCRIBE|ASK|FROM|NAMED|ORDER|BY|LIMIT|ASC|DESC|OFFSET|DISTINCT|REDUCED|WHERE|GRAPH|OPTIONAL|UNION|FILTER|GROUP|HAVING|AS|VALUES|LOAD|CLEAR|DROP|CREATE|MOVE|COPY|SILENT|INSERT|DELETE|DATA|WITH|TO|USING|NAMED|MINUS|BIND|LANGMATCHES|LANG|BOUND|SAMETERM|ISIRI|ISURI|ISBLANK|ISLITERAL|REGEX|TRUE|FALSE|UNDEF|ADD|DEFAULT|ALL|SERVICE|INTO|IN|NOT|IRI|URI|BNODE|RAND|ABS|CEIL|FLOOR|ROUND|CONCAT|STRLEN|UCASE|LCASE|ENCODE_FOR_URI|CONTAINS|STRSTARTS|STRENDS|STRBEFORE|STRAFTER|YEAR|MONTH|DAY|HOURS|MINUTES|SECONDS|TIMEZONE|TZ|NOW|UUID|STRUUID|MD5|SHA1|SHA256|SHA384|SHA512|COALESCE|IF|STRLANG|STRDT|ISNUMERIC|SUBSTR|REPLACE|EXISTS|COUNT|SUM|MIN|MAX|AVG|SAMPLE|SEPARATOR|STR)$/i;
		var punct = /^(\*|\.|\{|\}|,|\(|\)|;|\[|\]|\|\||&&|=|!=|!|<=|>=|<|>|\+|-|\/|\^\^|\?|\||\^)$/;
		function getCompletions(token, start, possibles) {

			var found = [];

			var state = token.state;
			var stack = state.stack;
			var top = stack.length - 1;
			var topSymbol = stack[top];

			// Skip optional clutter
			while (/^(\*|\?).*/.test(topSymbol) && top > 0) {
				topSymbol = stack[--top];
			}

			var lastProperty = token.state.lastProperty;
			// Is a class expected in this position?
			// If the preceding property was rdf:type and an object is expected,
			// then a class is expected.
			var isClassPos = false;
			if (lastProperty
					.match(/^a|rdf:type|<http:\/\/www.w3.org\/1999\/02\/22-rdf-syntax-ns#type>$/)
					&& ((start == "" && (topSymbol == "object"
							|| topSymbol == "objectList" || topSymbol == "objectListPath")) || (start != "" && topSymbol == "}"))) {
				isClassPos = true;
			}

			// test the case of the 1st non-space char
			var startIsLowerCase = /^ *[a-z]/.test(token.string);

			// Where case is flexible
			function maybeAdd(str) {
				var obj = str;

				if (typeof str == "string") {
					obj = {};
					obj.text = str;
					obj.label = str;
				}

				if (obj.text.toUpperCase().indexOf(start.toUpperCase()) == 0) {
					if (startIsLowerCase) {
						obj.text = obj.text.toLowerCase();
					} else {
						obj.text = obj.text.toUpperCase();
					}

					found.push(obj);
				}
				/*
				if (str.toUpperCase().indexOf(start.toUpperCase()) === 0) {
					if (startIsLowerCase) {
						found.push(str.toLowerCase());
					} else {
						found.push(str.toUpperCase());
					}
				}*/
			}

			// Where case is not flexible
			function maybeAddCS(str) {
				var obj = str;

				if (typeof str == "string") {
					obj = {};
					obj.text = str;
					obj.label = str;
				}

				if (obj.text.toUpperCase().indexOf(start.toUpperCase()) == 0) {
					found.push(obj);
				}
				
				/**
				if (str.toUpperCase().indexOf(start.toUpperCase()) === 0) {
					found.push(str);
				}**/
			}

			// Add items from the fetched sets of properties / classes
			// set is "properties" or "classes"
			// varName is "p" or "o"
			function addFromCollectedURIs(set, varName) {
				if (/:/.test(start)) {
					// Prefix has been entered - give items matching prefix
					/**var activeDataItem = editor.getSidebar().getActiveDataItem();
					if (activeDataItem) {
						for ( var k = 0; k < activeDataItem.prefixes.length; k++) {
							var ns = activeDataItem.prefixes[k].uri;
							for ( var j = 0; j < activeDataItem[set].results.bindings.length; j++) {
								var fragments = activeDataItem[set].results.bindings[j][varName].value
										.match(/(^\S*[#\/])([^#\/]*$)/);
								if (fragments.length == 3 && fragments[1] == ns)
									maybeAddCS(activeDataItem.prefixes[k].prefix + ":"
											+ fragments[2]);
							}
						}
					}**/
				} else if (/^</.test(start)) {
					// Looks like a URI - add matching URIs
					/**var activeDataItem = editor.getSidebar().getActiveDataItem();
					if (activeDataItem) {
						for ( var j = 0; j < activeDataItem[set].results.bindings.length; j++)
							maybeAddCS("<"
									+ activeDataItem[set].results.bindings[j][varName].value
									+ ">");
					}**/
				}
			}

			function gatherCompletions() {
				var i;
				var j;
				var activeDataItem;
				if (isClassPos)
					addFromCollectedURIs("classes", "o");
				for (i = 0; i < possibles.length; ++i) {
					if (possibles[i] == "VAR1" && state.allowVars) {
						maybeAddCS("?");
					} else if (keywords.exec(possibles[i])) {
						// keywords - the strings stand for themselves
						maybeAdd(possibles[i]);
					} else if (punct.exec(possibles[i])) {
						// punctuation - the strings stand for themselves
						maybeAddCS(possibles[i]);
					} else if (possibles[i] == "STRING_LITERAL1") {
						maybeAddCS('"');
						maybeAddCS("'");
					} else if (possibles[i] == "IRI_REF") {
						var stack = token.state.stack;

						// The stack is inspected in order to verify if we are in a "FROM NAMED" context
						if ((stack.length >= 1
								&& stack[stack.length - 1] == "sourceSelector")
								|| (stack.length >= 2
									&& stack[stack.length - 2] == "groupGraphPattern")) {
							for ( var j = 0; j < namedGraphs.length; j++) {
								maybeAddCS("<" + namedGraphs[j] + ">");
							}
						} else if (!/^</.test(start)) {
							maybeAddCS("<");
						}
					} else if (possibles[i] == "BLANK_NODE_LABEL" && state.allowBnodes) {
						maybeAddCS("_:");
					} else if (possibles[i] == "a") {
						// Property expected here - fetch possibilities
						maybeAddCS("a");
						addFromCollectedURIs("properties", "p");
					} else if (possibles[i] == "PNAME_LN" && !/:$/.test(start)) {
						// prefixed names - offer prefixes
						/**activeDataItem = editor.getSidebar().getActiveDataItem();
						if (activeDataItem !== undefined
								&& activeDataItem.prefixes != undefined
								&& activeDataItem.prefixes.length) {
							for (j = 0; j < activeDataItem.prefixes.length; j++) {
								maybeAddCS(activeDataItem.prefixes[j].prefix + ":");
							}
						}**/
					}
					  else if (possibles[i] == "PNAME_NS") {
						var stack = token.state.stack;

						// The parser stack is inspected in order to verify if we are in a prefix declaration.
						if (stack.length >= 3
								&& stack[stack.length - 2] == "IRI_REF"
								&& stack[stack.length - 3] == "*prefixDecl") {
							var prefixes = [];
							var acc = "";
							
							for ( var ns in nsPrefixMappings) {
								if (prefixes.length != 0) {
									acc += "PREFIX ";
								}
								acc += ns + ": <" + nsPrefixMappings[ns] + ">\n";
								prefixes.push({
									text : ns + ": <" + nsPrefixMappings[ns] + ">",
									label : ns + ":"
								});
							}

							maybeAddCS({
								text : acc,
								label : "All prefixes"
							});

							for ( var j = 0; j < prefixes.length; j++) {
								maybeAddCS(prefixes[j]);
							}
						}
					}
				}
			}

			gatherCompletions();
			return found;
		}

		function insertOrReplace(str, tkposs) {
			if ((tkposs.insertStart || tkposs.charAfter !== " ")
					&& /^[A-Za-z\*]*$/.exec(str)) {
				str = str + " ";
			}
			if (tkposs.insertEnd) {
				str = " " + str;
			}
			if (tkposs.insertPos) {
				// Insert between spaces
				cm.replaceRange(str, tkposs.insertPos);
			} else {
				// Replace existing token
				cm.replaceRange(str, {
					line : tkposs.cur.line,
					ch : tkposs.token.start
				}, {
					line : tkposs.cur.line,
					ch : tkposs.token.end
				});
			}
		}

		function startComplete() {

			// We want a single cursor position.
			if (cm.somethingSelected()) {
				return;
			}

			var tkposs = getPossiblesAtCursor();
			var stack = tkposs.token.state.stack;

			var completions = getCompletions(tkposs.token, tkposs.start,
					tkposs.possibles);

			if (!completions.length) {
				return;
			}

			// When there is only one completion, use it directly.
			if (completions.length === 1) {
				insertOrReplace(completions[0].text, tkposs);
				return true;
			}

			// Build the select widget
			var complete = document.createElement("div");
			complete.className = "completions";
			var sel = complete.appendChild(document.createElement("select"));
			sel.multiple = true;
			var i;
			for (i = 0; i < completions.length; ++i) {
				var opt = sel.appendChild(document.createElement("option"));
				opt.setAttribute('value', completions[i].text);
				opt.appendChild(document.createTextNode(completions[i].label));
			}
			sel.firstChild.selected = true;
			sel.size = Math.min(10, completions.length);
			var pos = cm.cursorCoords();

			complete.style.position = "absolute";
			complete.style.left = pos.x + "px";
			complete.style.top = pos.yBot + "px";

			document.body.appendChild(complete);

			// Hack to hide the scrollbar.
			if (completions.length <= 10) {
				complete.style.width = (sel.clientWidth - 1) + "px";
			}

			var done = false;
			function close() {
				if (done) {
					return;
				}
				done = true;
				complete.parentNode.removeChild(complete);
			}
			function pick() {
				insertOrReplace(sel.options[sel.selectedIndex].value, tkposs);
				close();
				setTimeout(function() {
					cm.focus();
				}, 50);
			}
			connect(sel, "blur", close);
			connect(sel, "keydown", function(event) {
				var code = event.keyCode;
				// Enter and space
				if (code === 13 || code === 32) {
					event.stop();
					pick();
				}
				// Escape
				else if (code === 27) {
					event.stop();
					close();
					cm.focus();
				} else if (code !== 38 && code !== 40) {
					close();
					cm.focus();
					setTimeout(startComplete, 50);
				}
			});
			connect(sel, "dblclick", pick);

			sel.focus();
			// Opera sometimes ignores focusing a freshly created node
			if (window.opera) {
				setTimeout(function() {
					if (!done) {
						sel.focus();
					}
				}, 100);
			}
			return true;
		}

		function autocompleteKeyEventHandler(i, e) {
			// Hook into ctrl-space
			if (e.keyCode == 32 && (e.ctrlKey || e.metaKey) && !e.altKey) {
				e.stop();
				return startComplete();
			}
		}

		function cmUpdate() {

			if (clearError !== null) {
				clearError();
				clearError = null;
			}

			if (markerHandle !== null) {
				cm.clearMarker(markerHandle);
			}
			var state;
			var l;
			for (l = 0; l < cm.lineCount(); ++l) {
				state = cm.getTokenAt({
					line : l,
					ch : cm.getLine(l).length
				}).state;
				if (state.OK === false) {
					markerHandle = cm
							.setMarker(l,
									"<span style=\"color: #f00 ; font-size: large;\">&rarr;</span> %N%");
					clearError = cm.markText({
						line : l,
						ch : state.errorStartPos
					}, {
						line : l,
						ch : state.errorEndPos
					}, "sp-error");
					break;
				}
			}

			if (state.complete) {
				flint.getSubmitButton().enable();
				flint.getStatusArea().setQueryValid(true);
			} else {
				flint.getSubmitButton().disable();
				flint.getStatusArea().setQueryValid(false);
				
			}

			flint.getStatusArea().updateStatus();
			if (state.queryType) {
				queryType = state.queryType.toUpperCase();
			} else {
				queryType = "";
			}
		}

		// Enable/disable the keyword buttons depending on the possibilities at
		// cursor position
		function updateKeywordTable() {
			var tkposs = getPossiblesAtCursor();

			function getButtonFn(str) {
				return function(e) {
					insertOrReplace(str, tkposs);
					cm.focus();
					e.stopPropagation();
				};
			}

			if (tkposs != undefined) {
				// Update keywords in the sidebar
				///flint.getSidebar().updateKeywords(tkposs.possibles, getButtonFn);
			}
		}

		function cmCursor() {

			updateKeywordTable();

			flint.getStatusArea().setLine(cm.getCursor().line);
			flint.getStatusArea().setPosition(cm.getCursor().ch);
			flint.getStatusArea().updateStatus();
		}

		this.setCodeMode = function(editorMode) {
			cm.setOption("mode", editorMode);
			updateKeywordTable();
		};

		this.getCodeMirror = function() {
			return cm;
		};

		this.getQueryType = function() {
			return queryType;
		};
		
		this.display = function() {
			var flintEditItem = new FlintEditItem(initialQuery);
			cm.setOption("onHighlightComplete", cmUpdate);
			cm.setOption("onCursorActivity", cmCursor);
			cm.setOption("onKeyEvent", autocompleteKeyEventHandler);
			cm.setValue(flintEditItem.getText());
			cm.setCursor(flintEditItem.getLine(), flintEditItem.getCh());
			cm.focus();
		};

		// Stores all the information for an editor tab item
		function FlintEditItem(text) {

			var content = text;
			var cursorLine = 0;
			var cursorCh = 0;

			this.setText = function(text) {
				content = text;
			};

			this.getText = function() {
				return content;
			};

			this.getLine = function() {
				return cursorLine;
			};

			this.getCh = function() {
				return cursorCh;
			};

			this.setCursor = function(line, ch) {
				cursorLine = line;
				cursorCh = ch;
			};
		}

	}

	function Flint(container, status, submit, editorMode, cm) {

		var flint = this, editorId = "flint-editor", flintCodeEditor;

		// Keywords can be grouped using a string for the second array item
		this.sparql1Keywords = [ [ "BASE", "" ], [ "PREFIX", "" ],
				[ "SELECT", "" ], [ "ASK", "" ], [ "CONSTRUCT", "" ],
				[ "DESCRIBE", "" ], [ "DISTINCT", "MODIFIER" ], [ "REDUCED", "" ],
				[ "FROM", "" ], [ "NAMED", "" ], [ "WHERE", "" ], [ "GRAPH", "" ],
				[ "UNION", "" ], [ "FILTER", "" ], [ "OPTIONAL", "" ],
				[ "ORDER", "MODIFIER" ], [ "LIMIT", "MODIFIER" ],
				[ "OFFSET", "MODIFIER" ], [ "BY", "MODIFIER" ], [ "ASC", "" ],
				[ "DESC", "" ], [ "STR", "STRING" ], [ "LANG", "" ],
				[ "LANGMATCHES", "STRING" ], [ "DATATYPE", "" ], [ "BOUND", "" ],
				[ "SAMETERM", "" ], [ "ISIRI", "TERM" ], [ "ISURI", "TERM" ],
				[ "ISBLANK", "TERM" ], [ "ISLITERAL", "TERM" ], [ "REGEX", "STRING" ] ];

		this.sparql11Query = [ [ "BASE", "" ], [ "PREFIX", "" ], [ "SELECT", "" ],
				[ "ASK", "" ], [ "CONSTRUCT", "" ], [ "DESCRIBE", "" ],
				[ "DISTINCT", "MODIFIER" ], [ "REDUCED", "" ], [ "FROM", "" ],
				[ "NAMED", "" ], [ "WHERE", "" ], [ "GRAPH", "" ], [ "UNION", "" ],
				[ "FILTER", "" ], [ "OPTIONAL", "" ], [ "ORDER", "MODIFIER" ],
				[ "LIMIT", "MODIFIER" ], [ "OFFSET", "MODIFIER" ],
				[ "BY", "MODIFIER" ], [ "ASC", "" ], [ "DESC", "" ],
				[ "STR", "STRING" ], [ "LANG", "" ], [ "LANGMATCHES", "STRING" ],
				[ "DATATYPE", "" ], [ "BOUND", "" ], [ "SAMETERM", "" ],
				[ "ISIRI", "TERM" ], [ "ISURI", "TERM" ], [ "ISBLANK", "TERM" ],
				[ "ISLITERAL", "TERM" ], [ "REGEX", "STRING" ], [ "HAVING", "" ],
				[ "GROUP", "" ], [ "VALUES", "" ], [ "IF", "" ], [ "COALESCE", "" ],
				[ "EXISTS", "" ], [ "NOT", "" ], [ "ISNUMERIC", "TERM" ],
				[ "IRI", "TERM" ], [ "BNODE", "TERM" ], [ "STRDT", "TERM" ],
				[ "STRLANG", "TERM" ], [ "UUID", "TERM" ], [ "STRUUID", "TERM" ],
				[ "STRLEN", "STRING" ], [ "SUBSTR", "STRING" ], [ "LCASE", "STRING" ],
				[ "UCASE", "STRING" ], [ "STRSTARTS", "STRING" ],
				[ "STRENDS", "STRING" ], [ "CONTAINS", "STRING" ],
				[ "STRBEFORE", "STRING" ], [ "STRAFTER", "STRING" ],
				[ "ENCODE_FOR_URI", "STRING" ], [ "CONCAT", "STRING" ],
				[ "REPLACE", "STRING" ], [ "NOW", "DATE" ], [ "YEAR", "DATE" ],
				[ "MONTH", "DATE" ], [ "DAY", "DATE" ], [ "HOURS", "DATE" ],
				[ "MINUTES", "DATE" ], [ "SECONDS", "DATE" ], [ "TIMEZONE", "DATE" ],
				[ "TZ", "DATE" ], [ "MD5", "HASH" ], [ "SHA1", "HASH" ],
				[ "SHA256", "HASH" ], [ "SHA384", "HASH" ], [ "SHA512", "HASH" ],
				[ "ABS", "NUMERIC" ], [ "ROUND", "NUMERIC" ], [ "CEIL", "NUMERIC" ],
				[ "FLOOR", "NUMERIC" ], [ "RAND", "NUMERIC" ],
				[ "REDUCED", "MODIFIER" ], [ "COUNT", "AGGREGATE" ],
				[ "SUM", "AGGREGATE" ], [ "MIN", "AGGREGATE" ], [ "MAX", "AGGREGATE" ],
				[ "AVG", "AGGREGATE" ], [ "SAMPLE", "AGGREGATE" ],
				[ "GROUP_CONCAT", "AGGREGATE" ] ];

		this.sparql11Update = [ [ "BASE", "" ], [ "PREFIX", "" ], [ "SELECT", "" ],
				[ "DISTINCT", "MODIFIER" ], [ "REDUCED", "" ], [ "NAMED", "" ],
				[ "WHERE", "" ], [ "GRAPH", "" ], [ "TO", "UPDATE" ], [ "USING", "" ],
				[ "DEFAULT", "" ], [ "ALL", "" ], [ "UNION", "" ], [ "FILTER", "" ],
				[ "OPTIONAL", "" ], [ "ORDER", "MODIFIER" ], [ "LIMIT", "MODIFIER" ],
				[ "OFFSET", "MODIFIER" ], [ "BY", "MODIFIER" ], [ "ASC", "" ],
				[ "DESC", "" ], [ "STR", "STRING" ], [ "LANG", "" ],
				[ "LANGMATCHES", "STRING" ], [ "DATATYPE", "" ], [ "BOUND", "" ],
				[ "SAMETERM", "" ], [ "ISIRI", "TERM" ], [ "ISURI", "TERM" ],
				[ "ISBLANK", "TERM" ], [ "ISLITERAL", "TERM" ], [ "REGEX", "STRING" ],
				[ "HAVING", "" ], [ "GROUP", "" ], [ "VALUES", "" ], [ "IF", "" ],
				[ "COALESCE", "" ], [ "EXISTS", "" ], [ "NOT", "" ],
				[ "ISNUMERIC", "TERM" ], [ "IRI", "TERM" ], [ "BNODE", "TERM" ],
				[ "STRDT", "TERM" ], [ "STRLANG", "TERM" ], [ "UUID", "TERM" ],
				[ "STRUUID", "TERM" ], [ "STRLEN", "STRING" ], [ "SUBSTR", "STRING" ],
				[ "LCASE", "STRING" ], [ "UCASE", "STRING" ],
				[ "STRSTARTS", "STRING" ], [ "STRENDS", "STRING" ],
				[ "CONTAINS", "STRING" ], [ "STRBEFORE", "STRING" ],
				[ "STRAFTER", "STRING" ], [ "ENCODE_FOR_URI", "STRING" ],
				[ "CONCAT", "STRING" ], [ "REPLACE", "STRING" ], [ "NOW", "DATE" ],
				[ "YEAR", "DATE" ], [ "MONTH", "DATE" ], [ "DAY", "DATE" ],
				[ "HOURS", "DATE" ], [ "MINUTES", "DATE" ], [ "SECONDS", "DATE" ],
				[ "TIMEZONE", "DATE" ], [ "TZ", "DATE" ], [ "MD5", "HASH" ],
				[ "SHA1", "HASH" ], [ "SHA256", "HASH" ], [ "SHA384", "HASH" ],
				[ "SHA512", "HASH" ], [ "ABS", "NUMERIC" ], [ "ROUND", "NUMERIC" ],
				[ "CEIL", "NUMERIC" ], [ "FLOOR", "NUMERIC" ], [ "RAND", "NUMERIC" ],
				[ "REDUCED", "MODIFIER" ], [ "COUNT", "AGGREGATE" ],
				[ "SUM", "AGGREGATE" ], [ "MIN", "AGGREGATE" ], [ "MAX", "AGGREGATE" ],
				[ "AVG", "AGGREGATE" ], [ "SAMPLE", "AGGREGATE" ],
				[ "GROUP_CONCAT", "AGGREGATE" ], [ "DATA", "UPDATE" ],
				[ "INSERT", "UPDATE" ], [ "DELETE", "UPDATE" ], [ "CREATE", "UPDATE" ],
				[ "DROP", "UPDATE" ], [ "COPY", "UPDATE" ], [ "MOVE", "UPDATE" ],
				[ "ADD", "UPDATE" ], [ "LOAD", "UPDATE" ], [ "INTO", "UPDATE" ],
				[ "WITH", "UPDATE" ], [ "SILENT", "UPDATE" ] ];

		try {
			// Add status area
			var statusArea = new FlintStatus(status);
			this.getStatusArea = function() {
				return statusArea;
			};
			
			// Add submit button
			var submitButton = new FlintButton(submit);
			this.getSubmitButton = function() {
				return submitButton;
			};
			
			// Add actual code editing area
			flintCodeEditor = new FlintCodeEditor(flint, editorMode, cm);
			flintCodeEditor.display(editorId);
			
			this.getCodeEditor = function() {
				return flintCodeEditor.getCodeMirror();
			};
			
			this.setCodeMode = function(mode) {
				return flintCodeEditor.setCodeMode(mode);
			};
			
			statusArea.display(editorId);
			statusArea.updateStatus();

		} catch (e) {
			window.alert(e);
		}
	}
	
	cm = CodeMirror.fromTextArea(container, {
		mode : editorMode,
		lineNumbers : true,
		indentUnit : 3,
		tabMode : "indent",
		matchBrackets : true 
	});

	editor = new Flint(container, status, submit, editorMode, cm);

	this.getEditor = function() {
		return editor;
	};
	
	this.getCMEditor = function() {
		return cm;
	};
	
	this.getValue = function() {
		return cm.getValue();
	};
	
	this.setValue = function(code) {
		cm.setValue(code);
	};
	
	this.focus = function(code) {
		cm.focus();
	};
	
	this.getEditorMode = function() {
		return editorMode;
	};
	
	this.setEditorMode = function(mode) {
		editorMode = mode;
		editor.setCodeMode(mode);
	};

}
