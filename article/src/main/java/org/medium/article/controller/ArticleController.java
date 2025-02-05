package org.medium.article.controller;

import lombok.RequiredArgsConstructor;
import org.medium.article.domain.Article;
import org.medium.article.domain.ArticlesWithUser;
import org.medium.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{id}")
    public Mono<Article> getArticleByID(@PathVariable String id) {
        return articleService.getArticleById(id);
    }

    @GetMapping(value = "/plain/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getArticleAsPlainText(@PathVariable("id") String id, Model model) throws ExecutionException, InterruptedException {
        Mono<Article> article = articleService.getArticleById(id);
        Article art = article.toFuture().get();
        return art.toString();
    }

    @GetMapping("/q")
    public Flux<Article> getArticleByID(@RequestParam(defaultValue = "viewsCount") String orderBy,
                                        @RequestParam(defaultValue = "10") long limit,
                                        @RequestParam(defaultValue = "0") long offset) {
        return articleService.getPageArticle(orderBy, limit, offset);
    }

    @GetMapping("/q/withUser")
    public Mono<ArticlesWithUser> getAllArticlesWithUsers(@RequestParam(defaultValue = "viewsCount") String orderBy,
                                                          @RequestParam(defaultValue = "10") long limit,
                                                          @RequestParam(defaultValue = "0") long offset) {
        return articleService.getArticlesWithUser(orderBy, limit, offset);
    }

    @PostMapping(value = "/")
    public Mono<Article> createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    @DeleteMapping(value = "/{id}")
    public Mono<Void> deleteArticle(@PathVariable String id) {
        return articleService.deleteArticle(id);
    }

}
