package com.spring.board.controller;

import com.spring.board.entity.*;
import com.spring.board.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {
	@GetMapping("/board/write")	//localhost:8080/board/write
	//Get/PostMapping(URL-보낼 주소)
	public String boardWriteForm() {
		return "boardwrite2";
	}
//	@PostMapping("/board/writepro")
//	public String boardWritePro(String title, String content) {
//		System.out.println("title : " + title);
//		System.out.println("content : " + content);
//		
//		return "";
//	}
	@Autowired
	private BoardService boardService;
	
	@PostMapping("/board/writepro")
	public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {
//		System.out.println(board.getTitle());
		boardService.write(board, file);
		//추가코드
		model.addAttribute("message", "글 작성 완료");
		model.addAttribute("searchUrl", "/board/list");
		return "message";
//		return "";
	}
	
//	@GetMapping("/board/list")
//	public String boardList(Model model) {
//		model.addAttribute("list", boardService.boardList());
//		return "boardList";
//	}
	//페이징 처리
	@GetMapping("/board/list")
	public String boardList(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, String searchKeyword) {
		
//		Page<Board> list =  boardService.boardList(pageable); => paging
		//검색
		Page<Board> list = null;
		if(searchKeyword == null) {
			list = boardService.boardList(pageable);
		}
		else {
			list = boardService.boardSearchList(searchKeyword, pageable);	//searchKeyword를 key로 pageable(검색어)를 GET방식으로 받음
		}
		
		int nowPage = list.getPageable().getPageNumber() + 1;
		int startPage = Math.max(nowPage-4, 1);	//현재 위치한 페이지 기준 앞 페이지가 4개, 제일 앞 페이지가 1이다
		int endPage = Math.min(nowPage+5, list.getTotalPages());	//현재 위치한 페이지 기준 뒤 페이지가 5개, 제일 맨 뒤페이지는 총 페이지 갯수의 수
		
		model.addAttribute("list", list);
		model.addAttribute("nowPage", nowPage);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		return "boardList";
	}
	
	@GetMapping("/board/view")
	public String boardview(Model model, Integer id) {
		model.addAttribute("board", boardService.boardView(id));
		return "boardView";
	}
	@PostMapping("/board/update-heart/{id}")
	public String heartUpdate(@PathVariable("id") Integer id, Board board, Model model) {
		Board boardTemp = boardService.boardView(id);
		boardTemp.setHeart(!boardTemp.getHeart());
		boardService.heartSave(boardTemp);
		return "redirect:/board/view?id={id}";
	}
	@GetMapping("/board/delete")
	public String boardDelete(Integer id) {
		boardService.boardDelete(id);
		return "redirect:/board/list";
	}
	@GetMapping("/board/modify/{id}")
	public String boardModify(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("board", boardService.boardView(id));
		return "boardmodify";
	}
	@PostMapping("/board/update/{id}")
	public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file)throws Exception {
		//기존 객체 불러옴
		Board boardTemp = boardService.boardView(id);
		
		//불러온 객체 위에 덮어쓰기
		boardTemp.setTitle(board.getTitle());
		boardTemp.setContent(board.getContent());
		
		boardService.write(boardTemp, file);
		
		//추가
		model.addAttribute("message", "비 오지마 수정 완료");
		model.addAttribute("searchUrl", "/board/list");
		
//		return "redirect:/board/list";
		return "message";
	}
}