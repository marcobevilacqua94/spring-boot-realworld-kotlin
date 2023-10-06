package com.springboot.couchbase.springbootrealworld.domain.tag.service;

import com.springboot.couchbase.springbootrealworld.domain.article.repository.ArticleRepository;
import com.springboot.couchbase.springbootrealworld.domain.tag.entity.ArticleTagRelationDocument;
import com.springboot.couchbase.springbootrealworld.domain.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ArticleRepository articleRepository;


    @Override
    public List<String> getAllTagList() {
        Iterable<ArticleTagRelationDocument> results = tagRepository.findAll();
        return StreamSupport.stream(results.spliterator(), false).map(e -> e.getTag()).collect(Collectors.toList());
//        return results.parallelStream().map(s -> s.getTag()).toList();
//        return tagRepository.findAllTagList();
    }


}
