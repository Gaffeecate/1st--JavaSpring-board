package com.example.firstproject.dto;

import com.example.firstproject.entity.Article;
import lombok.AllArgsConstructor;
import lombok.ToString;


@AllArgsConstructor // 생성자 생략가능
@ToString // toString() 생성 생략가능
public class ArticleForm {

    private Long id; // id field 추가

    private String title;
    private String content;

    public Article toEntity() { // 여기있는 코드의 역할?
        return new Article(id, title, content);


    }
}
