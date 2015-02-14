<#escape x as x?html>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <link rel="stylesheet" href="css/main.css">
    <link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
    
    <script src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
    <script src="js/jquery.autosize.min.js"></script>
    <script>
        $(function() {
            // Auto-focus on first form input:
            $('#templateAndModelForm *:input[type!=hidden]:first').focus();
            
            // Submit form when Ctrl+Enter is hit in a textarea:            
            $('#templateAndModelForm textarea').keydown(function (e) {
                if ((e.keyCode == 10 || e.keyCode == 13) && e.ctrlKey) {
                    $('#templateAndModelForm').submit();
                }
            });
        
            // Dynamically adapt text areas heights to their content:
            $('#templateAndModelForm textarea').autosize();
        
            // Show/hide data model examples:
            $("#showHideDataModelExamples").click(function(e) {
            	 $("#dataModelExamples").toggle();
           		 $("#hideDataModelExamplesLabel").toggle();
                 $("#showDataModelExamplesLabel").toggle();
            	 
                 e.preventDefault();
            	 return false;
            })
        });
    </script>
    
    <title>Online FreeMarker Template Tester</title>
</head>
<body>
<div id="layout">
    <div id="main">
        <div class="header">
            <h1>Online FreeMarker Template Tester</h1>
        </div>

        <div class="content">
            <form id="templateAndModelForm" method="post" class="pure-form pure-form-stacked">
                <label for="template">Template <span class="faint">(FreeMarker ${freeMarkerVersion})</span></label>
                <textarea name="template" class="pure-input-1 source-code"
                        placeholder="Enter template, like: Hello ${r'${user}'}!"
                >${template}</textarea>
    
                <label for="template">
                    Data model
                    (<a id="showHideDataModelExamples" href="#"><!--
                    --><span id="showDataModelExamplesLabel">show</span><!--
                    --><span id="hideDataModelExamplesLabel" class="hiddenByDefault">hide</span>
                    examples</a>)
                </label>
                <pre id="dataModelExamples" class="hiddenByDefault">someString = Some value
otherString = "JSON\nsyntax"
someNumber = 3.14
someBoolean = true
someDate = 2014-02-28
someTime = 20:50:30.5+02:00
someDatetime = 2014-02-28T18:50Z
someList = ["JSON", "syntax", 1, 2, 3 ]
someMap = { "JSON syntax": true, "nestedList": [1, 2, 3] }
someXML = &lt;example x="1"&gt;text&lt;/example&gt;</pre>
                <textarea name="dataModel" class="pure-input-1 source-code"
                        placeholder='Enter one or more assignments (e.g. user = John Doe), starting each in its own line.'
                >${dataModel}</textarea>
    
                <div class="formBottomButtonsContainer">
	                <input id="eval-btn" type="submit" value="Evaluate" class="pure-button pure-button-primary"/>
	                &nbsp; <span class="faint">Ctrl+Enter in input fields will submit this form too</span>
                </div>
                
                <#if hasResult>
                    <div class="resultContainer">
                        <label for="result">Result</label>
                        <textarea id="result" class="pure-input-1 source-code <#if errorResult> error</#if>" readonly>${result}</textarea>
                    </div>
                </#if>
            </form>
        </div><!-- content -->
        
        <div class="footer">
            FreeMarker documentation:
            <a href="http://freemarker.org/docs/" target="_blank">Contents</a>
            |
            <a href="http://freemarker.org/docs/dgui_template_overallstructure.html" target="_blank">Overall&nbsp;syntax</a>
            |
            <a href="http://freemarker.org/docs/dgui_template_exp.html#exp_cheatsheet" target="_blank">Expression&nbsp;syntax</a>
            |
            <a href="http://freemarker.org/docs/ref_directive_alphaidx.html" target="_blank">List&nbsp;of&nbsp;&lt;#<i>directives</i>&gt;</a>
            |
            <a href="http://freemarker.org/docs/ref_builtins_alphaidx.html" target="_blank">List&nbsp;of&nbsp;<tt>?<i>built_in</i></tt> functions</a>
        </div><!-- footer -->
    </div><!-- main -->
    
    <!-- Fork me on GitHub: -->
    <a href="https://github.com/kenshoo/freemarker-online" target="_blank"><img style="position: absolute; top: 0; right: 0; border: 0;" src="https://camo.githubusercontent.com/a6677b08c955af8400f44c6298f40e7d19cc5b2d/68747470733a2f2f73332e616d617a6f6e6177732e636f6d2f6769746875622f726962626f6e732f666f726b6d655f72696768745f677261795f3664366436642e706e67" alt="Fork me on GitHub" data-canonical-src="https://s3.amazonaws.com/github/ribbons/forkme_right_gray_6d6d6d.png"></a>
</div><!-- layout -->
</body>
</html>
</#escape>