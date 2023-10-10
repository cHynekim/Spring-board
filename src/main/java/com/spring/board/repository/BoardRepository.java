package com.spring.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>{
	//pk가 int이기에 integer
	//검색어가 들어간 모든 글을 띄움 = Containing!
	Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
}
