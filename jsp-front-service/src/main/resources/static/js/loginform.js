$(document).ready(function() {
    $("#check").click(function() {
       if ($.trim($("#id").val()) == "") {
        alert("ID를 입력하세요");
        $("#id").focus();
        return false;
       }
       if ($.trim($("#pass").val()) == "") {
        alert("비밀번호를 입력하세요");
        $("#pass").focus();
        return false;
       }
       
      });//submit() end

   });//ready() end 

 


 

