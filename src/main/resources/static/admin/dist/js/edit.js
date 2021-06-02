var blogEditor;
// Tags Input
$('#blogTags').tagsInput({
    width: '100%',
    height: '38px',
    defaultText: 'Blog Tag'
});

// Initialize Select2 Elements
$('.select2').select2()



$(function () {

    blogEditor = editormd("blog-editormd", {
        width: "100%",
        height: 640,
        syncScrolling: "single",
        path: "/admin/plugins/editormd/lib/",
        toolbarModes: 'full',

        /**load image configuration*/
        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"], //image type allowed
        imageUploadURL: "/admin/blogs/md/uploadfile"
        // onload: function (obj) { //callback after uploading
        // }
    });


    // var path = "../languages/en.js";
    // editormd.loadScript(path + "en", function(){
    //     blogEditor.lang = editormd.defaults.lang;
    //     blogEditor.recreate();
    //
    // });


    // editor content upload
    document.getElementById("blog-editormd").addEventListener("paste", function (e) {
        var clipboardData = e.clipboardData;
        if (clipboardData) {
            var items = clipboardData.items;
            if (items && items.length > 0) {
                for (var item of items) {
                    if (item.type.startsWith("image/")) {
                        var file = item.getAsFile();
                        if (!file) {
                            alert("Invalid file");
                            return;
                        }
                        var formData = new FormData();
                        formData.append('file', file);
                        var xhr = new XMLHttpRequest();
                        xhr.open("POST", "/admin/upload/file");
                        xhr.onreadystatechange = function () {
                            if (xhr.readyState == 4 && xhr.status == 200) {
                                var json=JSON.parse(xhr.responseText);
                                if (json.resultCode == 200) {
                                    blogEditor.insertValue("![](" + json.data + ")");
                                } else {
                                    alert("failed");
                                }
                            }
                        }
                        xhr.send(formData);
                    }
                }
            }
        }
    });

    new AjaxUpload('#uploadCoverImage', {
        action: '/admin/upload/file',
        name: 'file',
        autoSubmit: true,
        responseType: "json",
        onSubmit: function (file, extension) {
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
                alert('only support jpg、png、gif file！');
                return false;
            }
        },
        onComplete: function (file, r) {
            if (r != null && r.resultCode == 200) {
                $("#blogCoverImage").attr("src", r.data);
                $("#blogCoverImage").attr("style", "width: 128px;height: 128px;display:block;");
                return false;
            } else {
                alert("error");
            }
        }
    });
});

$('#confirmButton').click(function () {
    var blogTitle = $('#blogName').val();
    var blogSubUrl = $('#blogSubUrl').val();
    var blogCategoryId = $('#blogCategoryId').val();
    var blogTags = $('#blogTags').val();
    var blogContent = blogEditor.getMarkdown();
    if (isNull(blogTitle)) {
        swal("Input Title !", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogTitle, 150)) {
        swal("Title is too long", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogSubUrl, 150)) {
        swal("url is too long", {
            icon: "error",
        });
        return;
    }
    if (isNull(blogCategoryId)) {
        swal("choose category", {
            icon: "error",
        });
        return;
    }
    if (isNull(blogTags)) {
        swal("input tag", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogTags, 150)) {
        swal("tag is too long", {
            icon: "error",
        });
        return;
    }
    if (isNull(blogContent)) {
        swal("input content", {
            icon: "error",
        });
        return;
    }
    if (!validLength(blogTags, 100000)) {
        swal("content is too long", {
            icon: "error",
        });
        return;
    }
    $('#articleModal').modal('show');
});

$('#saveButton').click(function () {
    var blogId = $('#blogId').val();
    var blogTitle = $('#blogName').val();
    var blogSubUrl = $('#blogSubUrl').val();
    var blogCategoryId = $('#blogCategoryId').val();
    var blogTags = $('#blogTags').val();
    var blogContent = blogEditor.getMarkdown();
    var blogCoverImage = $('#blogCoverImage')[0].src;
    var blogStatus = $("input[name='blogStatus']:checked").val();
    var enableComment = $("input[name='enableComment']:checked").val();
    if (isNull(blogCoverImage) || blogCoverImage.indexOf('img-upload') != -1) {
        swal("cover is required", {
            icon: "error",
        });
        return;
    }
    var url = '/admin/blogs/save';
    var swlMessage = 'saved successfully';
    var data = {
        "blogTitle": blogTitle, "blogSubUrl": blogSubUrl, "blogCategoryId": blogCategoryId,
        "blogTags": blogTags, "blogContent": blogContent, "blogCoverImage": blogCoverImage, "blogStatus": blogStatus,
        "enableComment": enableComment, "blogId":blogId
    };
    // if (blogId > 0) {
    //     url = '/admin/blogs/update';
    //     swlMessage = 'update successfully';
    //     data = {
    //         "blogId": blogId,
    //         "blogTitle": blogTitle,
    //         "blogSubUrl": blogSubUrl,
    //         "blogCategoryId": blogCategoryId,
    //         "blogTags": blogTags,
    //         "blogContent": blogContent,
    //         "blogCoverImage": blogCoverImage,
    //         "blogStatus": blogStatus,
    //         "enableComment": enableComment
    //     };
    // }

    $.ajax({
        type: 'POST',
        url: url,
        data: data,
        success: function (result) {
            if (result.resultCode == 200) {
                $('#articleModal').modal('hide');
                swal({
                    title: swlMessage,
                    type: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: 'Return',
                    confirmButtonClass: 'btn btn-success',
                    buttonsStyling: false
                }).then(function () {
                    window.location.href = "/admin/blogs";
                })
            }
            else {
                $('#articleModal').modal('hide');
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

$('#cancelButton').click(function () {
    window.location.href = "/admin/blogs";
});

/**
 * Random Cover Function
 */
$('#randomCoverImage').click(function () {
    var rand = parseInt(Math.random() * 40 + 1);
    $("#blogCoverImage").attr("src", '/admin/dist/img/rand/' + rand + ".jpg");
    $("#blogCoverImage").attr("style", "width:160px ;height: 120px;display:block;");
});
