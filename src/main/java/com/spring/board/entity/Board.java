package com.spring.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	private String content;
	private String filename;
	private String filepath;
	private Boolean heart;
}
/*
 * @Entity : DB 테이블에 대응하는 하나의 클래스
 * @Id : 객체의 식별자로 사용할 필드 (primary key와 같음 - 식별키)
 * @GeneratedValue 전략 :  id값 안만들어도 DB가 직접 생성
 * => .IDENTITY , mySQL
 * 
 * @Data : @Getter/@Setter, @ToString 등을 합침
 */