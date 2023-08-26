//fun1函数进行不同的验证，如果验证不通过，则调用fun2
//fun2为禁用按钮,设置input的颜色等
function validate(node1,fun1,node2,fun2){
    if (!fun1(node1)){
        fun2(node2);
    }
}
function notNull(node){
    if (node.tagName === 'input'){
        const text = node.val();
        if (text === "" || text === null){
            return false;
        }
    }
    if (node.tagName === 'select'){
        const text = node.val();
        if (text === "" || text === null){
            return false;
        }
    }
}