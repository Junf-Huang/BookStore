$(document).ready(function () {

    //load img
    var src = $('#photoName').text();
    if(src!='') {
        src = "/resource/books/" + src;
        $('#cover').attr("src", src);
    }

    //hide nav
    var page = $('#page').text();
    if(page == 0){
        console.log("page=",page);
        $('#page').next().hide();
    }

    var stockQuantity = Number($('#stockQuantity').text());
    if (stockQuantity ===0){
        $('#num').val(0);
        alert("书已卖完");
    }

    //show modal
    $('.evaluator').on('click',function (event) {
        //防止打开link
        event.preventDefault();

        $('.evaluatorForm #exampleModal').modal();
    });

    $('.purchase').on('click', function (event) {

        var stockQuantity = Number($('#stockQuantity').text());
        if (stockQuantity === 0) {
            alert("书已卖完");
            event.preventDefault();
        }
        else {
            var href = $(this).attr('href');
            var test = $(this).text();
            var num = Number($('#num').val());
            if (test === '购买')
                href = href + '&' + 'number=' + num + '&' + 'balance=' + 'true';
            if (test === '加入购物车')
                href = href + '&' + 'number=' + num;
            alert("href" + href);
            $(this).attr('href', href)
        }
    });

    //commodity amount add reduce
    $('#add').on('click', function () {
        var num = Number($('#num').val());
        var stockQuantity = Number($('#stockQuantity').text());
        if(num < stockQuantity)
            $('#num').val(num + 1);
        else
            $('#num').val(stockQuantity);
    });
    $('#reduce').on('click', function () {
        var num = Number($('#num').val());
        var stockQuantity = Number($('#stockQuantity').text());
        if(stockQuantity <= 0)
            $('#num').val(0);
        else if(num <= 1)
                $('#num').val(1);
            else
                $('#num').val(num-1);
    });

});
