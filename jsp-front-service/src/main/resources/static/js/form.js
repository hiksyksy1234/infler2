$(document).ready(function(){
			$("form").submit(function(){				
				if($("#board_subject").val()==""){
					alert("제목을 입력 하세요");
					$("#board_subject").focus();
					return false;
				}
				
				if($("#board_content").val()==""){
					alert("내용을 입력 하세요");
					$("#board_content").focus();
					return false;
				}
				if($("#board_pass").val()==""){
					alert("비밀번호를 입력 하세요");
					$("#board_pass").focus();
					return false;
				}
			});			
		});	