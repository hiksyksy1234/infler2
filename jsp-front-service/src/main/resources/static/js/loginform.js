$(document).ready(function() {
    $("#check").click(function() {
       if ($.trim($("#id").val()) == "") {
        alert("ID�� �Է��ϼ���");
        $("#id").focus();
        return false;
       }
       if ($.trim($("#pass").val()) == "") {
        alert("��й�ȣ�� �Է��ϼ���");
        $("#pass").focus();
        return false;
       }
       
      });//submit() end

   });//ready() end 

 


 

