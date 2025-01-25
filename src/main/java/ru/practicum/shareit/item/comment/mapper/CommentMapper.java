package ru.practicum.shareit.item.comment.mapper;

import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.comment.model.Comment;

public class CommentMapper {
    public static CommentResponseDto toResponseDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }
}
