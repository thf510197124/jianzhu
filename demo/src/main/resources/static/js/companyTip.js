$(document).ready(function () {
    const companyNode = $(".company");
    companyNode.blur(function (){
        const tip = $(this).next("div.tip");
        alert("tip消失")
        tip.fadeOut();
    })
    companyNode.bind("input propertychange",function (){
        const company = $(this).val();
        if (company.length > 0){
            $.ajax({
                type:"post",
                url:baseUrl + "company/nameList.json",
                contentType:'application/json;charset=utf-8',
                data:JSON.stringify(name),
                success:function (data) {
                    $(comTb).html("");
                    if (data!=null){
                        for(var i=0;i<$(data).length;i++){
                            /*$("#tbcontent").addClass("show");*/
                            $(comTb).append("<div class='item' onclick='mousedown(this)'>" + data[i] + "</div>")
                        }
                        $(comTb).slideDown();
                    }
                }
            });
        }
    })
})