package com.example.firstproject.controller;
import com.example.firstproject.Service.CommentService;
import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j // 로깅을 위한 어노테이션
public class ArticleController {

    @Autowired // 스프링 부트가 미리 생성해놓은 객체를 가져다가 자동완성
    private ArticleRepository articleRepository;

    @Autowired
    private CommentService commentService;

    @GetMapping ("/articles/new") // 글쓰는 페이지 반환해주는 메소드
    public String newArticleform() {
        return "articles/new";
    }

    @PostMapping("/articles/create") // 글쓰는 페이지에서 실제로 글을 입력했을때 서버랑 디비에 글을 올려줘서 저장하는 역할하는 메소드
    public String createAction(ArticleForm form) {
        log.info(form.toString());
//        System.out.println(form.toString()); -> 로깅기능으로 대체

        // dto를 Entity로 변환
        Article article = form.toEntity();
        log.info(form.toString());
//       System.out.println(article.toString());

        // repository에게 entity를 db안에 저장하게 함.
        Article saved = articleRepository.save(article);
        log.info(saved.toString());
//        System.out.println(saved.toString());

        return "redirect:/articles/" + saved.getId();

    }

    @GetMapping("/articles/{id}") // 입력한 특정글 조회, 글에 댓글 여러개 달려있는거 같이 가져오는 역할을 함
    public String show(@PathVariable Long id, Model model) {
        log.info("id = " +id);

        // 1: id로 데이터를 가져옴!
        Article articleEntity = articleRepository.findById(id).orElse(null);
        List<CommentDto> commentDtos = commentService.comments(id);

        // 2: 가져온 데이터를 모델에 등록!
        model.addAttribute("article", articleEntity);
        model.addAttribute("commentDtos", commentDtos);

        // 3: 보여줄 페이지를 설정!
        return "articles/show";

    }

    @GetMapping("/articles") // 글 전체 리스트를 보여줌. 이때 댓글은 안보여줘도 됨.
    public String index(Model model) {

        // 1: Article 묶음을 id를 이용하여 가져온다.
        List<Article> articleEntityList = (List<Article>) articleRepository.findAll();

        // 2: 가져온 Article 묶음을 뷰로 전달.
        model.addAttribute("articleList", articleEntityList);

        // 3: 뷰 페이지를 설정.
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit") // 글 편집하는 페이지를 가져옴
    public String edit(@PathVariable Long id, Model model) {

        //수정할 데이터를 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 모델에 데이터를 등록!
        model.addAttribute("article", articleEntity);

        // 뷰페이지 설정
        return "articles/edit";
    }

    @PostMapping("/articles/update") // 편집을 하고 완료버튼을 눌렀을때 다시 서버와 디비로 저장하는 역할을 함.
    public String update(ArticleForm form) {
        log.info(form.toString());

        // 1: DTO를 엔티티로 변환
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());

        // 2: 엔티티를 DB로 저장
        // 2-1: DB에서 기존 데이터를 가져옴
        Article target = articleRepository.findById(articleEntity.getId())
                .orElse(null);
        // 2-2: 기존 데이터가 있다면, 값을 갱신
        if (target != null) {
            articleRepository.save(articleEntity);
        }
        // 3: 수정 결과 페이지로 리다이렉트
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping ("/articles/{id}/delete") // 삭제버튼을 누르면 별도 페이지 생성이 필요 없으므로 위에처럼 페이지 호출 기능은 없음.
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        log.info("삭제 요청이 들어왔습니다");

        // 삭제 대상을 가져온다.
        Article target = articleRepository.findById(id).orElse(null);
        log.info(target.toString());

        // 대상을 삭제한다
        if (target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제가 완료 되었습니다");
        }
        // 결과 페이지로 리다이렉트 한다.

        return "redirect:/articles";

    }
}