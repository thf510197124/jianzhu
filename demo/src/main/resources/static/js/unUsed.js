function getCode(node){
        const type1 = $(node).parents("tr").find(".mtype").val();
        const name1 = $(node).parents("tr").find(".mname").val();
        const text = $(node).parents("tr").find(".texture").val();
        const unit = $(node).parents("tr").find(".munit").val();
        const spec1 = $(node).parents("tr").find(".mspec").val();
        const code = $(node).parents("tr").find(".mcode");
        if (notNull(type1) || notNull(unit) || notNull(name1) || notNull(text) || notNull(spec1)){
            const list = {type: type1, name: name1, texture: text, unit:unit, spec:spec1};
            $.ajax({
                type:"post",
                url:[[@{/material/tip/code}]],
                contentType:'application/json;charset=utf-8',
                data: JSON.stringify(list),
                success:function (data) {
                    if (data!=null && data[0] !== "<"){
                        $(code).val(data)
                    }
                },
                error:function(){
                    $(tip).hide();
                }
            })
        }
}