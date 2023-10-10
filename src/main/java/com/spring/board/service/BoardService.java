package com.spring.board.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.board.entity.Board;
import com.spring.board.repository.BoardRepository;

@Service
public class BoardService {
	@Autowired
	private BoardRepository boardRepository;
	
//	public void write(Board board) {
//		boardRepository.save(board);
//	}
	public void write(Board board, MultipartFile file)throws Exception{
		if(file.isEmpty()) {
			board.setFilename(board.getFilename());
			board.setFilepath(board.getFilepath());
		}
		else {
			//저장할 파일 위치 설정
			String projectPath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\files";
			//파일의 고유 번호 설정으로 중복 방지를 위해 적용
			UUID uuid = UUID.randomUUID();
			//uuid + 원래 이름
			String fileName = uuid + "_" + file.getOriginalFilename();
			//해당 경로에 파일을 저장함
			File saveFile = new File(projectPath, fileName);
			//지정한 경로에 파일이 저장된다
			file.transferTo(saveFile);
			//파일이름과 경로를 db에 저장해준다
			board.setFilename(fileName);
			board.setFilepath("/files/" + fileName);
		}
		
		boardRepository.save(board);
	}
	
//	public List<Board> boardList(Pageable pageable){
//		return boardRepository.findAll();
//	}
	public Page<Board> boardList(Pageable pageable){
		return boardRepository.findAll(pageable);
	}
	public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){
		return boardRepository.findByTitleContaining(searchKeyword, pageable);
	}
	public Board boardView(Integer id) {
		return boardRepository.findById(id).get();
	}
	public void boardDelete(Integer id) {
		boardRepository.deleteById(id);
	}
	public void heartSave(Board board) {
		boardRepository.save(board);
	}
}
