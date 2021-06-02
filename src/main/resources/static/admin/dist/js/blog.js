$(function () {
    $("#jqGrid").jqGrid({
        url: '/admin/blogs/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'blogId', index: 'blogId', width: 50, key: true, hidden: true},
            {label: 'TITLE', name: 'blogTitle', index: 'blogTitle', width: 140},
            {label: 'COVER_IMAGE', name: 'blogCoverImage', index: 'blogCoverImage', width: 120, formatter: coverImageFormatter},
            {label: 'VIEWS', name: 'blogViews', index: 'blogViews', width: 60},
            {label: 'STATE', name: 'blogStatus', index: 'blogStatus', width: 60, formatter: statusFormatter},
            {label: 'CATEGORY', name: 'blogCategoryName', index: 'blogCategoryName', width: 60},
            {label: 'CREATE_TIME', name: 'createTime', index: 'createTime', width: 90}
        ],
        height: 700,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: 'loading...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            //hide grid bottom slide
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

    function coverImageFormatter(cellvalue) {
        return "<img src='" + cellvalue + "' height=\"120\" width=\"160\" alt='coverImage'/>";
    }

    function statusFormatter(cellvalue) {
        if (cellvalue == 0) {
            return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 50%;\">Draft</button>";
        }
        else if (cellvalue == 1) {
            return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 50%;\">Deploy</button>";
        }
    }

});

/**
 * Search Function
 */
function search() {
    //Title Keyword
    var keyword = $('#keyword').val();
    if (!validLength(keyword, 20)) {
        swal("The String is too long!", {
            icon: "error",
        });
        return false;
    }
    //encapsulate the data
    var searchData = {keyword: keyword};
    //pass parameter
    $("#jqGrid").jqGrid("setGridParam", {postData: searchData});
    //start from first page
    $("#jqGrid").jqGrid("setGridParam", {page: 1});
    //submit post request and reload table
    $("#jqGrid").jqGrid("setGridParam", {url: '/admin/blogs/list'}).trigger("reloadGrid");
}

/**
 * jqGrid reload
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function addBlog() {
    window.location.href = "/admin/blogs/edit";
}

function editBlog() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    window.location.href = "/admin/blogs/edit/" + id;
}

function deleteBlog() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    var data = {"ids": ids};
    swal({
        title: "DELETE",
        text: "CONFIRM TO DELETE?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "POST",
                    url: "/admin/blogs/delete",
                    traditional: true,
                    // contentType: "application/json",
                    // data: JSON.stringify(ids),
                    data: data,
                    success: function (r) {
                        if (r.resultCode == 200) {
                            swal("SUCCESSFULLY DELETED", {
                                icon: "success",
                            });
                            $("#jqGrid").trigger("reloadGrid");
                        } else {
                            swal(r.message, {
                                icon: "error",
                            });
                        }
                    }
                });
            }
        }
    );
}
