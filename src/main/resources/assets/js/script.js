/*
 * Copyright 2014 Kenshoo.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Created by Pmuruge on 8/28/2015.
 */
$( document).ready(function() {
    $("#eval-btn").click(function() {
        execute();
    });
    $('#templateAndModelForm textarea, #templateAndModelForm select').keydown(function (e) {
        if ((e.keyCode == 10 || e.keyCode == 13) && e.ctrlKey) {
            execute();
        }
    });
    $.blockUI.defaults.fadeIn = 1000;
    $.blockUI.defaults.fadeOut = 0;
});

var execute = function() {
    $.blockUI({ message: null });
    if (checkFormSendable()) {
        $("#error").hide();
        var payload = {
            "template": $("#template").val(),
            "dataModel": $("#dataModel").val(),
            "outputFormat": $("#outputFormat").val(),
            "locale": $("#locale").val(),
            "timeZone": $("#timeZone").val()
        }
        $.ajax({
            method: "POST",
            url: "/api/execute",
            data: JSON.stringify(payload),
            headers: {
                "Content-Type":"application/json"
            }
        })
        .done(function(data) {
            if (data.problems && data.problems.length != 0) {
                $("#result").addClass("error");
                // Currently we just show the first error:
                $("#result").val(data.problems[0].message);
            } else {
                $("#result").removeClass("error");
                $("#result").val(data.result);
            }
        })
        .fail(function(data) {
            $("#result").val(data.responseJSON.errorCode + ": " + data.responseJSON.errorDescription);
            $("#result").addClass("error");
        }).always(function(data) {
            $(".resultContainer").show();
            autosize.update($("#result"));
        });
    } else {
        $.unblockUI();
    }
};

var checkFormSendable = function() {
    if($.trim($("#template").val()) === "" ) {
        $("#result").addClass("error");
        $("#result").val("Template was empty; nothing to do.");
        $(".resultContainer").show();
        return false;
    }
    return true;
};

$(document).ajaxStart(function() {
    $("#eval-btn").attr("disabled","true");
});

$(document).ajaxStop(function() {
    $.unblockUI();
    $("#eval-btn").removeAttr("disabled");
});
