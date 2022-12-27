function go(page) {
	var limit = $('#viewcount').val();
	var data = "limit=" + limit + "&page=" + page;
	ajax(data);
}

function setPaging(href, digit){
	active="";
	gray="";
	if(href=="") { //href가 빈문자열인 경우
		if(isNaN(digit)){//이전&nbsp; 또는 다음&nbsp;
			gray=" gray";
		}else{
		    active=" active";
		}
	}
	var output = "<li class='page-item" + active + "'>";
	anchor = "<a class='page-link" + gray + "'"  + href + ">"+ digit + "</a></li>";
	output += anchor;
	return output;
}

function ajax(data) {
	// 1줄보기 선택시 리턴된 데이터
	/*
	 {"listcount":10,"startpage":1,"limit":3,"boardlist":[{"uploadfile":null,"board_RE_LEV":0,"board_DATE":"2021-09-08","board_PASS":"1","board_NAME":"admin",
	 "board_NUM":7,"board_SUBJECT":"test","board_CONTENT": ......}
	 */
	console.log(data)
	output = "";
	const token=localStorage.getItem('user-token');
	$.ajax({
		type : "get",
		data : data,		
		url:"/board/boards",
		dataType : "json",
		cache : false,
		beforeSend : function(xhr)
        {   //데이터를 전송하기 전에 헤더에 csrf값을 설정합니다.
        	xhr.setRequestHeader("authorization", token);			
        },
		success : function(data) {
			$("#viewcount").val(data.limit);
			$("table").find("font").text("글 개수 : " + data.listcount);

			if (data.listcount > 0) { // 총갯수가 0개이상인 경우
				$('h3').hide();
				$("tbody").remove();
				var num = data.listcount - (data.page - 1) * data.limit;
				console.log(num)
				output = "<tbody>";
				$(data.boardlist).each(
						function(index, item) {
							output += '<tr><td>' + (num--) + '</td>'
							blank_count = item.board_RE_LEV * 2 + 1;
							blank = '&nbsp;';
							for (var i = 0; i < blank_count; i++) {
								blank += '&nbsp;&nbsp;';
							}
							img="";
							if (item.board_RE_LEV > 0) {
								img="<img src='../resources/image/AnswerLine.gif'>";
							}
							
							var subject=item.board_SUBJECT.replace(/</g,'&lt;')
						    subject=subject.replace(/>/g,'&gt;')
							
							output +=  "<td><div>" + blank + img
							output += ' <a href="/jsp/board/' + item.board_NUM  + '/detail">'
							output += subject +  '<span class="gray small">[' + item.cnt + ']</span></a></div></td>'
							output += '<td><div>' + item.board_NAME+'</div></td>'
							output += '<td><div>' + item.board_DATE+'</div></td>'
							output += '<td><div>' + item.board_READCOUNT
									+ '</div></td></tr>'
						})
				output += "</tbody>"
				$('table').append(output)//table 완성
				
				$(".pagination").empty(); //페이징 처리
				output = "";
				
				digit = '이전&nbsp;'
				href="";	
				if (data.page > 1) {
					href = 'href=javascript:go(' + (data.page - 1) + ')';
				}
				output += setPaging(href, digit);
				
				for (var i = data.startpage; i <= data.endpage; i++) {
					digit = i;
					href="";
					if (i != data.page) {
						href = 'href=javascript:go(' + i + ')';
					} 
					output += setPaging(href, digit);
				}

				
				digit = '다음&nbsp;';
				href="";
				if (data.page < data.maxpage) {
					href = 'href=javascript:go(' + (data.page + 1) + ')';
				} 
				output += setPaging(href, digit);

				$('.pagination').append(output)
			}else{
			   $('h3').show();
			   $('#list').hide();
			}
		},
		error : function() {
			console.log('에러')
		}
	})// ajax end
} // fucntion ajax end

$(function() {

   
    go(1);
	$("#viewcount").change(function() {
		go(1);//보여줄 페이지를 1페이지로 설정합니다.
	});// change end

   	  $("button").click(function(){
   		  location.href="write";
     })
})

