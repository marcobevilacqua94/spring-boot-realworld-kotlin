package com.springboot.couchbase.springbootrealworld.domain.article.controller;

import com.springboot.couchbase.springbootrealworld.domain.article.dto.ArticleDto;
import com.springboot.couchbase.springbootrealworld.domain.article.dto.CommentDto;
import com.springboot.couchbase.springbootrealworld.domain.article.dto.FavoriteDto;
import com.springboot.couchbase.springbootrealworld.domain.article.model.FeedParams;
import com.springboot.couchbase.springbootrealworld.domain.article.service.ArticleService;
import com.springboot.couchbase.springbootrealworld.domain.article.service.CommentService;
import com.springboot.couchbase.springbootrealworld.domain.article.service.FavoriteService;
import com.springboot.couchbase.springbootrealworld.exception.AppException;
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private CouchbaseTemplate couchbaseTemplate;


    //Adding an Article
    @PostMapping
    public ArticleDto.SingleArticle<ArticleDto> createArticle(@RequestBody ArticleDto.SingleArticle<ArticleDto> article, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        System.out.println(article);
        try {
            return new ArticleDto.SingleArticle<>(articleService.createArticle(article.getArticle(), authUserDetails));
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Get an Article using slug
    @GetMapping("/{slug}")
    public ArticleDto.SingleArticle<ArticleDto> getArticle(@PathVariable String slug, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            return new ArticleDto.SingleArticle<>(articleService.getArticle(slug, authUserDetails));
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Update an Article using slug
    @PutMapping("/{slug}")
    public ArticleDto.SingleArticle<ArticleDto> createArticle(@PathVariable String slug, @RequestBody ArticleDto.SingleArticle<ArticleDto.Update> article, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            return new ArticleDto.SingleArticle<>(articleService.updateArticle(slug, article.getArticle(), authUserDetails));
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Delete an Article using slug
    @DeleteMapping("/{slug}")
    public void deleteArticle(@PathVariable String slug, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            articleService.deleteArticle(slug, authUserDetails);
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Post a comment with a slug as parameter
    @PostMapping("/{slug}/comments")
    public CommentDto.SingleComment addCommentsToAnArticle(@PathVariable String slug, @RequestBody CommentDto.SingleComment comment, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            System.out.println(comment);
            return CommentDto.SingleComment.builder()
                    .comment(commentService.addCommentsToAnArticle(slug, comment.getComment(), authUserDetails))
                    .build();
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Get all comments to an article with a slug as parameter
    @GetMapping("/{slug}/comments")
    public CommentDto.MultipleComments getCommentsFromAnArticle(@PathVariable String slug, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            return CommentDto.MultipleComments.builder()
                    .comments(commentService.getCommentsBySlug(slug, authUserDetails))
                    .build();
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //delete a comment to an article with a comment id as parameter
    @DeleteMapping("/comments/{commentId}")
    public void deleteComment(@PathVariable("commentId") String commentId, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            commentService.delete(commentId, authUserDetails);
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Post a favorite article with a slug as a parameter
    @PostMapping("/{slug}/favorite")
    public ArticleDto.SingleArticle<ArticleDto> favoriteArticle(@PathVariable String slug, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            return new ArticleDto.SingleArticle<>(articleService.favoriteArticle(slug, authUserDetails));
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Get a favorite article with a slug as a parameter
    @GetMapping("/{slug}/favorites")
    public FavoriteDto.MultipleFavorites getFavoritesFromAnArticle(@PathVariable String slug, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            return FavoriteDto.MultipleFavorites.builder()
                    .favorites(favoriteService.getFavoritesBySlug(slug, authUserDetails))
                    .build();
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Delete a favorite article with a slug as a parameter
    @DeleteMapping("/{slug}/favorite")
    public ArticleDto.SingleArticle deleteFavorite(@PathVariable("slug") String slug, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            return new ArticleDto.SingleArticle(favoriteService.delete(slug, authUserDetails));
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Get all article with pagination and user authentication
    @GetMapping("/all")
    public ArticleDto.MultipleArticle feedArticles(@ModelAttribute @Valid FeedParams feedParams, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            List<ArticleDto> articles = articleService.feedArticles(authUserDetails, feedParams);
            return ArticleDto.MultipleArticle.builder().articles(articles).articlesCount(articles.size()).build();
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Get all article with user authentication
    @GetMapping("/feed")
    public ArticleDto.MultipleArticle getArticles(@AuthenticationPrincipal AuthUserDetails authUserDetails) {
        try {
            List<ArticleDto> articles = articleService.getAllArticles(authUserDetails);
            return ArticleDto.MultipleArticle.builder()
                    .articles(articles)
                    .articlesCount(articles.size())
                    .build();
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

    //Get all article without user authentication
    @GetMapping
    public ArticleDto.MultipleArticle getArticlesYouFollow() {
        try {
            List<ArticleDto> articles = articleService.getAllArticlesYouFollow();
            return ArticleDto.MultipleArticle.builder()
                    .articles(articles)
                    .articlesCount(articles.size())
                    .build();
        } catch (AppException aex) {
            throw new ResponseStatusException(
                    aex.getError().getStatus().value(), aex.getError().getMessage(), aex);
        }
    }

}
