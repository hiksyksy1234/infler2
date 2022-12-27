function go(page) {
	var limit = $('#limit').val();
	var data = "limit=" + limit 
	         + "&page=" + page
	         + "&search_field=" + $('#viewcount').val()
	         + "&search_word=" + $('input[name=search_word]').val();
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
	console.log(data)
	output = "";
	const token=localStorage.getItem('user-token');
	$.ajax({
		type : "get",
		data : data,		
		url:"/user/members",
		dataType : "json",
		cache : false,
		beforeSend : function(xhr)
        {   //데이터를 전송하기 전에 헤더에 csrf값을 설정합니다.
        	xhr.setRequestHeader("authorization", token);			
        },
		success : function(data) {
		    $('tbody').empty();
			$("#viewcount").val(data.search_field);
			$('#limit').val(data.limit);
			$('input[name=search_word]').val(data.search_word);
			$("table").find("font").text("글 개수 : " + data.listcount);

			if (data.listcount > 0) { // 총갯수가 0개이상인 경우
				var num = data.listcount - (data.page - 1) * data.limit;
				console.log(num)
				output = "";
				$(data.memberlist).each(
						function(index, item) {
							output += '<tr><td><a href="" >' + item.id + '</a></td>'
							       +  '<td>' + item.name + '</td>'
							       +  '<td><button class="btn btn-danger btn-sm">삭제</button></td></tr>'
						})
						console.log(output)
				$('table > tbody').append(output)//tbody 완성
				
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
			 
			    $('.row').empty();
			    output = '<font size=5>검색 결과가 없습니다.</font>'
			         
			    $('.container').html(output);      
			}
		},
		error : function() {
			console.log('에러')
		}
	})// ajax end
} // fucntion ajax end

$(function() {

   
    go(1);
	$("#limit").change(function() {
		go(1);//보여줄 페이지를 1페이지로 설정합니다.
	});// change end

     $("form").submit(function(event){
        event.preventDefault();
        go(1)
     })
     
	 
	 //검색어 공백 유효성 검사합니다.
	 $("button.btn-primary").click(function(){		
	    if($("input[name=seach_word]").val()==''){
		 alert("검색어를 입력하세요");
		 return false;
	    }
	    
	    word=$("input[name=search_word]").val();
	    if(selectedValue=='A'){	    	
	    	pattern=/^[0-9]{2}$/;
	        if (!pattern.test(word)){
	    	   alert("나이는 형식에 맞게 입력하세요(두자리 숫자)");
	    	   return false;
	       }
	    }
	    
	    
	    if(selectedValue=='G'){	    	
	       if (word!="남" && word!="여"){
	    	   alert("남 또는 여를 입력하세요");
	    	   return false;
	       }
	    }
	 });//button click end
	 
	 //검색어 입력창에 placeholder 나타나도록 합니다. 
	 $("#viewcount").change(function(){
	     selectedValue = $(this).val();
		 $("input").val('');	
		 let selectedOptionValue=$('#viewcount>option:selected').text()
		 $("input").attr("placeholder",selectedOptionValue + " 입력하세요");
		 
	 })
	 
	 
	  //회원 목록의 삭제를 클릭한 경우
	 $('body').on('click','.btn.btn-danger.btn-sm', function(){
	        var answer = confirm("정말 삭제하시겠습니까?");
		 console.log(answer);//취소를 클릭한 경우-false
		 if(!answer){//취소를 클릭한 경우
			 return false;
		 }
		 
		 const id = $(this).parent().parent().find('a').text();
		 $.ajax({
		    url : '/user/members/'+id,
		    method:'delete',
		    success : function(r){
		       if(r==1){
		          alert('삭제 성공입니다.');
		          go(1);
		          }
		    }
		 })
	 
	 })
	 
	 
	  //회원 목록의 회원을 클릭한 경우
	 $('body').on('click','td a', function(event){
	     event.preventDefault();
	     const id=$(this).text();
		 $.ajax({
		    url : '/user/members/'+id,
		    method:'get',
		    dataType:'json',
		    success : function(member){
		       $('.container').children().hide();
		       let output = `<table id=info class='table'><caption style='font-size:2rem'>회원정보</caption>`
		                 + `<tr><th>이름</th><td>${member.name}</td></tr>`
		                 + `<tr><th>나이</th><td>${member.age}</td></tr>`
		                 + `<tr><th>성별</th><td>${member.gender}</td></tr>`
		                 + `<tr><th>email</th><td>${member.email}</td></tr>`
		                 + `<tr><td colspan=2 class='golist'>리스트로 이동</td></tr>`
		                 + `</table>`
		       $('.container').append(output);          
		    }
		 })
	 })
	 
	 $('body').on('click','.golist', function(){
	    $('#info').remove();
	    $('.container').children().show();
	 })
	 
   	 
})

