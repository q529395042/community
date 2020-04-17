function post() {
    var content=$("#content").val();
    var parentId=$("#parentId").val();

    if (content==null){
        alert("回复内容不能为空");
    }
    $.ajax({
        type: 'POST',
        url: '/comment',
        dataType:'json',
        contentType: 'application/json',
        data:JSON.stringify({
            parentId: parentId,
            content:content,
            type:1,
            targetId:0
        }),
        success(response){
            console.log(response);
            if (response.code==200){
                location.reload();
            } else if (response.code==2002){
                var isAccepted=confirm(response.message);
                if (isAccepted){
                window.open("/login");
                window.localStorage.setItem("iteam","true");
                }else{
                    alert(response.code);
                }
            } else{
                alert(response.message)
            }
        },
        error(e){
            alert(e.message);
        }
    });
}


function comment(e) {
    var id=e.getAttribute("data-id");
    var dataNumber=e.getAttribute("data-number");
    var twoCommentId='#two-comment'+id;
    if (dataNumber%2==0){
        $.ajax({
            url: '/comment/'+id,
            type: 'GET',
            dataType:'json',
            contentType: 'application/json',
            success:function(data) {
                console.log(1)
                data = data.data;
                if (data!=null){
                    data.forEach(function (iteam) {
                        console.log(iteam);
                        let date=new Date();
                        date.setDate(iteam.gmtModified);
                        let commentDate=formatDate(date);
                        $(twoCommentId).append(` <div class="media-left img-warper">
                                        <a href="#">
                                            <img class="media-object img-rounded img-warper" src="${iteam.user.avatarUrl}"
                                                 alt="...">
                                        </a>
                                        </div>
                                    <div class="media-body ">
                                        <h4 class="media-heading"><span class="color-bar"
                                                                     >${iteam.user.name}</span></h4>
                                        <div >${iteam.content}</div>
                                        <div class="menu">
                                            <span class="data-location"></span>
                                        </div>
                                    </div>
                                    <hr>
                                    
                                
                `)
                    });
                    $(twoCommentId).append(`<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
                                
                                <input type="text" class="form-control" name="comment2"  id="comment2${id}"  placeholder="评论一下">
                                <button type="button" class="btn btn-success data-location"  onclick="comment2ById(this,${id})">评论</button>
                                </div>`);
                    e.setAttribute("data-number",Number(dataNumber)+1);
                    dataNumber=Number(dataNumber)+1;
                    var reply=$("#comment-reply"+id);
                    var collage=$("#comment-"+id);
                    collage.toggleClass("in");
                    reply.toggleClass("active");
                }else{
                    $(twoCommentId).append(`<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12">
                              
                                <input type="text" class="form-control" name="comment2"  id="comment2${id}"  placeholder="评论一下">
                                <button type="button" class="btn btn-success data-location"  onclick="comment2ById(this,${id})">评论</button>
                                </div>`);
                    e.setAttribute("data-number",Number(dataNumber)+1);
                    dataNumber=Number(dataNumber)+1;
                    var reply=$("#comment-reply"+id);
                    var collage=$("#comment-"+id);
                    collage.toggleClass("in");
                    reply.toggleClass("active");
                }

            },
            error(e){
                alert(e.message);
            }
        });
    } else{
        $(twoCommentId).empty();
        var test=e.setAttribute("data-number",Number(dataNumber)+1);
        dataNumber=Number(dataNumber)+1;
        var reply=$("#comment-reply"+id);
        var collage=$("#comment-"+id);
        var isis=collage.toggleClass("in");
        reply.toggleClass("active");
    }

}

function formatDate(longDate){
    var date = new Date(longDate);
    var yyyy = date.getFullYear();
    var mm = date.getMonth() + 1;
    if (mm < 10) {
        mm = "0" + mm;
    }
    var dd = date.getDate();
    if (dd < 10) {
        dd = "0" + dd;
    }
    return yyyy + "-" + mm + "-" + dd;
}


//二级回复的回复
function  comment2ById(e,id) {
    var content=$("#comment2"+id).val();
    console.log(content);
    var parentId=$("#parentId").val();
    if (content==null){
        alert("回复内容不能为空");
    }
    $.ajax({
        type: 'POST',
        url: '/comment',
        dataType:'json',
        contentType: 'application/json',
        data:JSON.stringify({
            parentId: parentId,
            content:content,
            type:1,
            targetId:id
        }),
        success(response){
            if (response.code===200){
            location.reload();
            } else {
                alert(response.message);
            }
        },
        error(e){
            alert(e.message);
        }
    });
}
    
