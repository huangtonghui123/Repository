var billObj;

//订单管理页面上点击删除按钮弹出删除框(billlist.jsp)
function deleteBill(obj) {
    $.ajax({
        type: "GET",
        url: path + "/bill/delBill.json",
        data: {billid: obj.attr("billid")},
        dataType: "json",
        success: function (data) {
            if (data.delResult == "true") {//删除成功：移除删除行
                cancleBtn();
                obj.parents("tr").remove();
            } else if (data.delResult == "false") {//删除失败
                //alert("对不起，删除订单【"+obj.attr("billcc")+"】失败");
                changeDLGContent("对不起，删除订单【" + obj.attr("billcc") + "】失败");
            } else if (data.delResult == "notexist") {
                //alert("对不起，订单【"+obj.attr("billcc")+"】不存在");
                changeDLGContent("对不起，订单【" + obj.attr("billcc") + "】不存在");
            }
        },
        error: function (data) {
            alert("对不起，删除失败");
        }
    });
}

function openYesOrNoDLG() {
    $('.zhezhao').css('display', 'block');
    $('#removeBi').fadeIn();
}

function cancleBtn() {
    $('.zhezhao').css('display', 'none');
    $('#removeBi').fadeOut();
}

function changeDLGContent(contentStr) {
    var p = $(".removeMain").find("p");
    p.html(contentStr);
}

$(function () {
    /*$(".viewBill").on("click",function(){
        //将被绑定的元素（a）转换成jquery对象，可以使用jquery方法
        var obj = $(this);
        window.location.href=path+"/bill/billView/"+ obj.attr("billid");
    });*/

    $(".viewBill").on("click", function () {
        //将被绑定的元素（a）转换成jquery对象，可以使用jquery方法
        var obj = $(this);
        $.getJSON(path + "/bill/billView.json", {id: obj.attr("billid")},
            function (data) {
                if (data != "null") {
                    $(".providerView #billCode").html(data.billCode);
                    $(".providerView #productName").html(data.productName);
                    $(".providerView #productUnit").html(data.productUnit);
                    $(".providerView #productCount").html(data.productCount);
                    $(".providerView #totalPrice").html(data.totalPrice);
                    $(".providerView #providerName").html(data.providerName);
                    $(".providerView #isPayment").html(data.isPayment == "1" ? "未付款" : "已付款");
                } else {
                    alert("您访问的页面不存在");
                }
            });
    });

    $(".modifyBill").on("click", function () {
        var obj = $(this);
        window.location.href = path + "/bill/toBillModify.html?billid=" + obj.attr("billid");
    });
    $('#no').click(function () {
        cancleBtn();
    });

    $('#yes').click(function () {
        deleteBill(billObj);
    });

    $(".deleteBill").on("click", function () {
        billObj = $(this);
        changeDLGContent("你确定要删除订单【" + billObj.attr("billcc") + "】吗？");
        openYesOrNoDLG();
    });

    /*$(".deleteBill").on("click",function(){
        var obj = $(this);
        if(confirm("你确定要删除订单【"+obj.attr("billcc")+"】吗？")){
            $.ajax({
                type:"GET",
                url:path+"/bill.do",
                data:{method:"delbill",billid:obj.attr("billid")},
                dataType:"json",
                success:function(data){
                    if(data.delResult == "true"){//删除成功：移除删除行
                        alert("删除成功");
                        obj.parents("tr").remove();
                    }else if(data.delResult == "false"){//删除失败
                        alert("对不起，删除订单【"+obj.attr("billcc")+"】失败");
                    }else if(data.delResult == "notexist"){
                        alert("对不起，订单【"+obj.attr("billcc")+"】不存在");
                    }
                },
                error:function(data){
                    alert("对不起，删除失败");
                }
            });
        }
    });*/
});