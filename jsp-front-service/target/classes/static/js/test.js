$(document).ready(function(){

	$('form').submit(function(){

		if($('#id').val()==""){

			alert("아이디를 입력하세요");

			$('#id').focus();

			return false;

		}

		if($('#pass').val()==""){

			alert("암호를 입력하세요");

			$('#pass').focus();

			return false;

		}

	});

});
