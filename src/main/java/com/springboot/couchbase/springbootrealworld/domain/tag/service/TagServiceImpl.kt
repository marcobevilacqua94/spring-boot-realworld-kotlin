package com.springboot.couchbase.springbootrealworld.domain.tag.service

import com.springboot.couchbase.springbootrealworld.domain.article.repository.ArticleRepository
import com.springboot.couchbase.springbootrealworld.domain.tag.entity.ArticleTagRelationDocument
import com.springboot.couchbase.springbootrealworld.domain.tag.repository.TagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagServiceImpl @Autowired constructor(
        private val tagRepository: TagRepository,
        private val articleRepository: ArticleRepository
) : TagService {

    override fun getAllTagList(): List<String> {
        val results = tagRepository.findAll()
        return results.map { it.tag }
    }
}
