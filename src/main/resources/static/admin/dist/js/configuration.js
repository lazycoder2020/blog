$(function () {
    //web update
    $('#updateWebsiteButton').click(function () {
        $("#updateWebsiteButton").attr("disabled", true);
        //ajax call
        var params = $("#websiteForm").serialize();
        $.ajax({
            type: "POST",
            url: "/admin/configurations/website",
            data: params,
            success: function (result) {
                if (result.resultCode == 200 && result.data) {
                    swal("succeed", {
                        icon: "success",
                    });
                }
                else {
                    swal(result.message, {
                        icon: "error",
                    });
                };
            },
            error: function () {
                swal("failed", {
                    icon: "error",
                });
            }
        });
    });
    //personal info
    $('#updateUserInfoButton').click(function () {
        $("#updateUserInfoButton").attr("disabled", true);
        var params = $("#userInfoForm").serialize();
        $.ajax({
            type: "POST",
            url: "/admin/configurations/userInfo",
            data: params,
            success: function (result) {
                if (result.resultCode == 200&& result.data) {
                    swal("succeed", {
                        icon: "success",
                    });
                }
                else {
                    swal(result.message, {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("failed", {
                    icon: "error",
                });
            }
        });
    });
    //footer info
    $('#updateFooterButton').click(function () {
        $("#updateFooterButton").attr("disabled", true);
        var params = $("#footerForm").serialize();
        $.ajax({
            type: "POST",
            url: "/admin/configurations/footer",
            data: params,
            success: function (result) {
                if (result.resultCode == 200&& result.data) {
                    swal("succeed", {
                        icon: "success",
                    });
                }
                else {
                    swal(result.message, {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("failed", {
                    icon: "error",
                });
            }
        });
    });

})
