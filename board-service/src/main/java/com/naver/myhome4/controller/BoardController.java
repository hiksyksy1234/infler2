package com.naver.myhome4.controller;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.naver.myhome4.client.CommentServiceClient;
import com.naver.myhome4.domain.Board;
import com.naver.myhome4.domain.Comment;
import com.naver.myhome4.service.BoardService;

/**
 * CORS란? CORS(Cross-origin resource sharing)이란, 
 * 웹 페이지의 제한된 자원을 외부 도메인에서 접근을
 * 허용해주는 메커니즘입니다.
 */
//@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class BoardController {
	
	private static final Logger logger    = LoggerFactory.getLogger(BoardController.class);	
	
	private BoardService boardService;
	private CommentServiceClient commnetServiceClient;

	@Autowired
	public BoardController(BoardService boardService,CommentServiceClient commnetServiceClient) {
		this.boardService = boardService;
		this.commnetServiceClient = commnetServiceClient;
	}


	/* 아래 부분 추가해야 합니다.
	 * <dependency>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-configuration-processor</artifactId>
				<optional>true</optional>
		</dependency>
	 * 
	 */
	@Value("${my.savefolder}")
	private String saveFolder;
	
	@GetMapping(value = "/boards")
	public ResponseEntity<Map<String,Object>> boardListAjax(
		@RequestParam(value="page", defaultValue="1", required=false) int page,
		@RequestParam(value="limit", defaultValue="10", required=false) int limit,
		@RequestHeader(value="referer", defaultValue="") String beforeURL,
		@RequestHeader(value="autorization", defaultValue="") String token
         ) {
		
		int listcount = boardService.getListCount(); // 총 리스트 수를 받아옴

		// 총 페이지 수
		int maxpage = (listcount + limit - 1) / limit;

		//만약 maxpage가 2이고 page도 2인 경우 
		//2페이지의 목록의 수가 한 개인 상태에서 남은 항목 한개를 삭제한 경우
		//maxpage=1이 되고 page=2가 됩니다. 이런 경우  page는 maxpage로 수정합니다.
		if(page>maxpage) {
			page=maxpage;
		}
		
		
		// 현재 페이지에 보여줄 시작 페이지 수(1, 11, 21 등...)
		int startpage = ((page - 1) / 10) * 10 + 1;

		// 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30 등...)
		int endpage = startpage + 10 - 1;

		if (endpage > maxpage)
			endpage = maxpage;

		List<Board> boardlist = boardService.getBoardList(page, limit); // 리스트를 받아옴
        
		Map<String, Object> map = new HashMap<String,Object>();
        map.put("page", page);
        map.put("maxpage", maxpage);
        map.put("startpage", startpage);
        map.put("endpage", endpage);
        map.put("listcount", listcount);
        map.put("boardlist", boardlist);
        map.put("limit", limit);
        
        
        return  new ResponseEntity<>(map,HttpStatus.OK) ;
	}
	
	

	@RequestMapping(value="/boards/new",method=RequestMethod.POST)
	public ResponseEntity<String>  add(Board board) throws Exception{
	        
		MultipartFile uploadfile = board.getUploadfile();

		if (uploadfile!=null && !uploadfile.isEmpty()) {
			String fileName = uploadfile.getOriginalFilename();//원래 파일명
			board.setBOARD_ORIGINAL(fileName);// 원래 파일명 저장
			
			//c:/upload 생성합니다.
			//이전에는 직접 폴더를 생성했다면 지금은 File의 mkdir()로 폴더를 생성합니다.
			logger.info(saveFolder);
			File file = new File(saveFolder);
			if(!file.exists()) {
				if(file.mkdir()) {
					logger.info("폴더 생성");
				}else {
					logger.info("폴더 생성 실패");
				}
			}else {
				logger.info("폴더존재");
			}
			
			String fileDBName = fileDBName(fileName, saveFolder);
			logger.info("fileDBName = " + fileDBName);

			// transferTo(File path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
			uploadfile.transferTo(new File(saveFolder + fileDBName));

			// 바뀐 파일명으로 저장
			board.setBOARD_FILE(fileDBName);
		}

		boardService.insertBoard(board); // 저장메서드 호출

		return  new ResponseEntity<String>("success",HttpStatus.OK) ;
	}

	
	//게시판 사용될 정보 구하기
	@GetMapping(value= {"/boards/{num}"})
	public ResponseEntity<Map<String,Object>> Detail(@PathVariable int num) {	
		   Board board = boardService.getDetail(num);
		   Map<String, Object> map = new HashMap<String, Object>();
		   map.put("board", board);
		   return  new ResponseEntity<>(map,HttpStatus.OK) ;
	}
	
	@GetMapping("/boards/down")
	public ResponseEntity<byte[]> BoardFileDown(String filename, String original,
			HttpServletResponse response)  throws Exception {
				
				String sFilePath = saveFolder  + filename;
				logger.info(sFilePath);
				File file = new File(sFilePath);
				byte[] bytes = FileCopyUtils.copyToByteArray(file);
				
				String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");
				
				//Content-Disposition: attachment: 브라우저는 해당 Content를 처리하지 않고, 다운로드하게 됩니다.
				response.setHeader("Content-Disposition", "attachment;filename=" + sEncoding );
				
				response.setContentLength(bytes.length);		
				 return  new ResponseEntity<>(bytes,HttpStatus.OK) ;
			}
	
	/*
	     /boards/1, /boards/2... 처럼 주소 일부분의 값이 바뀌는 경우 {템플릿변수}로 표시하고
		  이 값을 사용하기 위해 @PathVariable를 이용합니다.
    */
		@DeleteMapping("/boards/{num}")
		public ResponseEntity<String> BoardDeleteAction(
		           String password, @PathVariable int num 
				)  {
			// 글 삭제 명령을 요청한 사용자가 글을 작성한 사용자인지 판단하기 위해
			// 입력한 비밀번호와 저장된 비밀번호를 비교하여 일치하면 삭제합니다.
			logger.info("비밀번호=" + password);
			logger.info("글번호=" + num);
			boolean usercheck = boardService.isBoardWriter(num, password);
			
			String message="";
			
			// 비밀번호 일치하지 않는 경우
			if (usercheck == false) {
				message="0";
			}else {

			// 비밀번호 일치하는 경우 삭제 처리 합니다.
				int result = boardService.boardDelete(num);
	
				// 삭제 처리 실패한 경우
				if (result == 0) {
					message= "-1";
				}else {
					message="1";
				}
						
				// 삭제 처리 성공한 경우 - 글 목록 보기 요청을 전송하는 부분입니다.
				logger.info("게시판 삭제 성공");		
				
			}
			return  new ResponseEntity<String>(message,HttpStatus.OK) ;
		}
		
	
	
	
	//게시판 수정처리하기
	@PatchMapping("/boards")
	public 	ResponseEntity<String> BoardModifyAction(
		    Board boarddata, 
			@RequestParam (value="check", defaultValue="", required=false) String check
			)  throws Exception {
		
		boolean usercheck = boardService.isBoardWriter(boarddata.getBOARD_NUM(), boarddata.getBOARD_PASS());
		String message="";
		// 비밀번호가 다른 경우
		if (usercheck == false) {
			 return  new ResponseEntity<>("Nopass",HttpStatus.OK) ; 
		}

		MultipartFile uploadfile = boarddata.getUploadfile();
		
		
		if (check != null && !check.equals("")) { // 기존파일 그대로 사용하는 경우입니다.
			logger.info("기존파일 그대로 사용합니다." + check);			
			boarddata.setBOARD_ORIGINAL(check);
		} else {		
			// 파일 변경한 경우
			if (uploadfile!=null && !uploadfile.isEmpty()) { 
				logger.info("파일 변경되었습니다.");	
				
				String fileName = uploadfile.getOriginalFilename(); // 원래 파일명
				boarddata.setBOARD_ORIGINAL(fileName);

				String fileDBName = fileDBName(fileName, saveFolder);

				// transferTo(File path) : 업로드한 파일을 매개변수의 경로에 저장합니다.
				uploadfile.transferTo(new File(saveFolder + fileDBName));

				// 바뀐 파일명으로 저장
				boarddata.setBOARD_FILE(fileDBName);
			} else {
				logger.info("선택 파일 없습니다.");
				boarddata.setBOARD_FILE("");//""로 초기화 합니다.
				boarddata.setBOARD_ORIGINAL("");//""로 초기화 합니다.
			}//else end
		}//else end

		// DAO에서 수정 메서드 호출하여 수정합니다.
		int result = boardService.boardModify(boarddata);
		// 수정에 실패한 경우
		if (result == 0) {
			message="fail";
		} else { // 수정 성공의 경우
			logger.info("게시판 수정 완료");
			message = "success";
		}
		 return  new ResponseEntity<>(message,HttpStatus.OK) ;
	}

	
	@PostMapping("/boards/reply/new")
	public ResponseEntity<String> BoardReplyAction(Board board) {
		int result = boardService.boardReply(board);
		if (result == 0) {
			return  new ResponseEntity<>("fail",HttpStatus.OK) ;
		}else {
			return  new ResponseEntity<>("success",HttpStatus.OK) ;
		}

	}
	

	private String fileDBName(String fileName, String saveFolder) {
		// 새로운 폴더 이름 : 오늘 년+월+일
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); // 오늘 년도 구합니다.
		int month = c.get(Calendar.MONTH) + 1; // 오늘 월 구합니다.
		int date = c.get(Calendar.DATE); // 오늘 일 구합니다.

		String homedir = saveFolder + File.separator + year + "-" + month + "-" + date;
		logger.info(homedir);
		
		//c:/upload/년도-달-일 폴더를 만듭니다.
		File path1 = new File(homedir);
		if (!(path1.exists())) {
			path1.mkdir();// 새로운 폴더를 생성
		}

		// 난수를 구합니다.
		Random r = new Random();
		int random = r.nextInt(100000000);

		/**** 확장자 구하기 시작 ****/
		int index = fileName.lastIndexOf(".");
		// 문자열에서 특정 문자열의 위치 값(index)를 반환한다.
		// indexOf가 처음 발견되는 문자열에 대한 index를 반환하는 반면,
		// lastIndexOf는 마지막으로 발견되는 문자열의 index를 반환합니다.
		// (파일명에 점에 여러개 있을 경우 맨 마지막에 발견되는 문자열의 위치를 리턴합니다.)
		logger.info("index = " + index);

		String fileExtension = fileName.substring(index + 1);
		logger.info("fileExtension = " + fileExtension);
		/**** 확장자 구하기 끝 ***/

		// 새로운 파일명
		String refileName = "bbs" + year + month + date + random + "." + fileExtension;
		logger.info("refileName = " + refileName);

		// 오라클 디비에 저장될 파일 명		
		String fileDBName =  File.separator + year + "-" + month + "-" + date + File.separator + refileName;
		logger.info("fileDBName = " + fileDBName);
		return fileDBName;
	}
	
	//board를 통해서 comment의 목록과 갯수를 가져오는 것을 FeignClient로 구현합니다.
	@GetMapping(value = "/comments")
	public ResponseEntity<Map<String, Object>> CommentList(
			@RequestParam int board_num, @RequestParam int page) {
		Map<String, Object>  map = commnetServiceClient.getCommentList_Count(board_num, page);
		return  new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	
	
//	
  //board-service에서 member-service의 회원 정보 수정에 대한 값을 불러오기 위한 곳입니다.
//	@RequestMapping(value = "/members/{id}", method = RequestMethod.GET)
//	public ResponseEntity<Member> member_info(@PathVariable String id) {
//		logger.info("id=" + id);
//		Member member= memberServiceClient.getMember(id);
//		return new ResponseEntity<>(member, HttpStatus.OK);
//	}
	
//	// 수정처리
//		@PutMapping(value = "/members")
//		public ResponseEntity<Integer> updateProcess(@RequestBody Member member) {
//			int result =  memberServiceClient.update(member);
//			return new ResponseEntity<>(result, HttpStatus.OK);
//		}

}
