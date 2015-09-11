/**
 * Created by Pmuruge on 8/28/2015.
 */
$( document).ready(function(){
    $("#eval-btn").click(function(){
        execute();
    });
    $('#templateAndModelForm textarea').keydown(function (e) {
        if ((e.keyCode == 10 || e.keyCode == 13) && e.ctrlKey) {
            execute();
        }
    });
});

    var execute = function() {
        if(validForm()) {
            $("#error").hide();
            var payload = {
                "template": $("#template").val(),
                "dataModel": $("#dataModel").val()
            }
            $.ajax({
                method: "POST",
                url: "/api/execute",
                data: JSON.stringify(payload),
                headers: {
                    "Content-Type":"application/json"
                }
            })
                .done(function( data ) {
                    if(data.problems) {
                        var error = data.problems.dataModel ? data.problems.dataModel : data.problems.template;
                        $("#result").addClass("error");
                        $("#result").html(error);
                    }
                    else {
                        $("#result").removeClass("error");
                        $("#result").html(data.result);
                    }
                })
                .fail(function(data){
                    $("#result").html(data.responseJSON.errorCode + ": " + data.responseJSON.errorDescription);
                    $("#result").addClass("error");
                }).always(function(data){
                    $(".resultContainer").show();
                    autosize.update($("#result"));
                });
        }
    };
    var validForm = function() {
        var error = true;
        if($("#template").val().trim() === "" ) {
            $("#error").show();
            error = false;
        }
        return error;
    };

    $( document ).ajaxStart(function() {
        $.blockUI({ message: null });
        $("#eval-btn").attr("disabled","true");
    });

    $( document ).ajaxStop(function() {
        $.unblockUI();
        $("#eval-btn").removeAttr("disabled");
    });

