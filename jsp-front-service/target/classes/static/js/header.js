$(function(){

$.ajax({
       url : '/user/members/getId',
       async:false,
       success : function(id){ 
           if(id != ''){
               let output ='<nav class="navbar navbar-expand-sm right-block navbar-dark">'
		          + '<ul class="navbar-nav">'
		          + ' <li class="nav-item">'
		          + '  <a class="nav-link"  id="logout">'
		          + '    <span id="loginid">' + id + '</span>님(로그아웃)'
		          + '  </a>'
		          + ' </li>'
		          + ' <li class="nav-item">'
		          + '  <a class="nav-link" href="/jsp/update">정보수정</a>'
		          + ' </li>';
               
                if(id =='admin') {
                     output+=  '<li class="nav-item dropdown">'
                            +  ' <a 	class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown"> 관리자 </a>'
                            +  '   <div class="dropdown-menu">'
	                        +  '     <a class="dropdown-item" href="/jsp/user/list">회원정보</a>'
	                        +  '     <a class="dropdown-item" href="/jsp/board/list">게시판</a>'
	                        +  '   </div>'
	                        +  '</li>'
              }
          
	          output += '</nav>';
			  $('body').prepend(output);
			  
			  $('#board_name').val(id); //http://localhost:8000/jsp/board/write 에서 필요
        }
         else {
                location.href='/jsp/login';   
        } 
       
       }
    
    })


   //로그아웃 처리    
    $('body').on('click','#logout', function(){
    
        $.ajax({
           url : '/user/members/logout',
           type : 'get',
           success: function(message){
                console.log(message);
        		localStorage.removeItem('user-token');
                location.href='/jsp/login';
           }
        
        });//ajax end
        
    
    
    })

})//ready