function reg_check(){
    var password = $("#password").val();
    var password_repeat = $("#password_repeat").val();
    if (password_repeat !== password){
        $("#rep_pass-help").text("两次输入的密码不一致");
    }
    else{
        $(".form-checkd").submit();
    }
}
//初始化fileinput控件（第一次初始化）
function initFileInput(ctrlName, uploadUrl) {
    var control = $('#' + ctrlName);
    control.fileinput({
        language: 'zh', //设置语言
        uploadUrl: uploadUrl, //上传的地址
        allowedFileExtensions : all,//接收的文件后缀
        showUpload: false, //是否显示上传按钮
        showCaption: false,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
    });
}
