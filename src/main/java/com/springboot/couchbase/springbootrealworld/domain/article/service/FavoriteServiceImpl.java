package com.springboot.couchbase.springbootrealworld.domain.article.service;

import com.springboot.couchbase.springbootrealworld.domain.article.dto.ArticleDto;
import com.springboot.couchbase.springbootrealworld.domain.article.dto.FavoriteDto;
import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument;
import com.springboot.couchbase.springbootrealworld.domain.article.entity.FavoriteDocument;
import com.springboot.couchbase.springbootrealworld.domain.article.repository.ArticleRepository;
import com.springboot.couchbase.springbootrealworld.domain.article.repository.FavoriteRepository;
import com.springboot.couchbase.springbootrealworld.domain.profile.dto.ProfileDto;
import com.springboot.couchbase.springbootrealworld.domain.profile.service.ProfileService;
import com.springboot.couchbase.springbootrealworld.domain.user.repository.UserRepository;
import com.springboot.couchbase.springbootrealworld.exception.AppException;
import com.springboot.couchbase.springbootrealworld.exception.Error;
import com.springboot.couchbase.springbootrealworld.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private final ArticleService articleService;

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private ProfileService profileService;

    @Autowired
    private final UserRepository userRepository;


    @Override
    public List<FavoriteDto> getFavoritesBySlug(String slug, AuthUserDetails authUserDetails) {
        String articleId = articleRepository.findBySlug(slug).getId();
        List<FavoriteDocument> favoriteEntities = favoriteRepository.findByArticleId(articleId);
        return favoriteEntities.stream().map(favoriteEntity -> convertToDTO(authUserDetails, favoriteEntity)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ArticleDto delete(String slug, AuthUserDetails authUserDetails) {
        ArticleDocument article = articleRepository.findBySlug(slug);
        if (article == null) throw new AppException(Error.ARTICLE_NOT_FOUND);
        String articleId = article.getId();
        List<FavoriteDocument> favoriteEntities = favoriteRepository.findByArticleId(articleId);
        favoriteRepository.deleteAll(favoriteEntities);
        return articleService.getArticle(slug, authUserDetails);
    }

    private FavoriteDto convertToDTO(AuthUserDetails authUserDetails, FavoriteDocument favoriteDocument) {
        ProfileDto author = profileService.getProfileByUserId(favoriteDocument.getAuthor().getId(), authUserDetails);
        return FavoriteDto.builder()
                .id(favoriteDocument.getId())
                .author(author)
                .build();
    }


}
