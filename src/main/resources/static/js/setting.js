$(function () {
    $("#uploadForm").submit(upload);
    $("form").submit(check_data);
    $("input").focus(clear_error);
});

function check_data() {
    var pwd1 = $("#new-password").val();
    var pwd2 = $("#confirm-password").val();
    if (pwd1 != pwd2) {
        $("#confirm-password").addClass("is-invalid");
        return false;
    }
    return true;
}

function upload() {
    $.ajax({
        url: "http://upload-z2.qiniup.com",
        method: "post",
        processData: false,//不把表单的内容转为字符串
        contentType: false,//不让jquery设置上传类型，浏览器会自动进行设置，避免边界问题
        data: new FormData($("#uploadForm")[0]),
        success: function (data) {
            if (data && data.code == 0) {
                //更新头像访问路径
                $.post(
                    CONTEXT_PATH + "/user/header/url",
                    {"fileName": $("input[name='key']").val()},
                    function (data) {
                        data = $.parseJSON(data);//前面不用解析因为七牛云返回的是JSON格式数据，而我们服务器返回的是字符串
                        if (data.code == 0) {
                            window.location.reload();//刷新当前页面
                        } else {
                            alert(data.msg);
                        }
                    }
                );
            } else {
                alert("上传失败!")
            }
        }
    });
    //不往下提交，否则会变成同步处理（此时没有指定action，就会出问题）
    return false;
}