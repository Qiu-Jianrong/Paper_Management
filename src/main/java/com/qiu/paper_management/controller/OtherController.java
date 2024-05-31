package com.qiu.paper_management.controller;

import com.qiu.paper_management.pojo.Comment;
import com.qiu.paper_management.pojo.Result;
import com.qiu.paper_management.service.ArticleService;
import com.qiu.paper_management.service.CategoryService;
import com.qiu.paper_management.service.OtherService;
import com.qiu.paper_management.service.UserService;
import com.qiu.paper_management.utils.OssUtil;
import com.qiu.paper_management.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@Validated
public class OtherController {
    @Autowired
    OtherService otherService;
    @Autowired
    ArticleService articleService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;
    @PostMapping("/upload")
    public Result<String> Upload(MultipartFile file, Integer id) throws Exception {
        String originalFilename = file.getOriginalFilename();
        // 将文件存储到oss云服务器上，UUID保证名字唯一，不因文件名重复而发生覆盖
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf('.'));
//        file.transferTo(new File("C:\\Users\\Qiu\\Desktop\\files\\" + filename));
        String originalObj = otherService.findObjById(id);
        String url = OssUtil.uploadFile(originalObj, filename, file.getInputStream());
        otherService.uploadFile(id, url);
        return Result.success(url);
    }

    // 对comment的参数进行校验
    // 1. category_id和article_id有且仅有一个为-1，且另一个一定是存在的
    private void Existence(Integer articleId, Integer categoryId){
        // 有且仅有一个为-1
        if (articleId * categoryId > 0)
            throw new RuntimeException("参数不合法！您不能同时评论文章和文献库！");
        if (articleId >= 0){
            if (articleService.getDetail(articleId) == null)
                throw new RuntimeException("您评论的文章不存在或已被删除！");
        }
        if (categoryId >= 0){
            if (categoryService.findCategoryById(categoryId) == null)
                throw new RuntimeException("您评论的文献库不存在或已被删除！");
        }
    }
    @PostMapping("/comment")
    public Result postComment(@RequestBody @Validated Comment comment){
        // 1. 校验合法性
        Existence(comment.getArticleId(), comment.getCategoryId());

        // 2. 发布评论
        comment.setCriticId(ThreadLocalUtil.getId());
        otherService.postComment(comment);
        return Result.success();
    }

    @GetMapping("/comment")
    public Result<List<Comment>> getComment(@RequestParam Integer categoryId, @RequestParam Integer articleId){
        // 1. 校验合法性
        Existence(articleId, categoryId);

        // 2. 返回对应评论区
        List<Comment> comments = otherService.getComment(articleId, categoryId);

        // 3. 附上评论者头像和email
        for (Comment comment : comments){
            comment.setCriticFace(userService.getImgById(comment.getCriticId()));
            comment.setCriticEmail(userService.getEmailById(comment.getCriticId()));
        }
        return Result.success(comments);
    }
}
