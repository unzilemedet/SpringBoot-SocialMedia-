package com.bilgeadam.service;

import com.bilgeadam.repository.ICommentRepository;
import com.bilgeadam.repository.entity.Comment;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class CommentService extends ServiceManager<Comment, String> {

    private final ICommentRepository commentRepository;

    public CommentService(ICommentRepository commentRepository) {
        super(commentRepository);
        this.commentRepository = commentRepository;
    }
}
