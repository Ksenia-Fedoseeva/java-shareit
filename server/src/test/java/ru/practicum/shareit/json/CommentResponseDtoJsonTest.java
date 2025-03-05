package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentResponseDtoJsonTest {
    @Autowired
    private JacksonTester<CommentResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime created = LocalDateTime.of(2025, 2, 28, 15, 45);
        CommentResponseDto dto = new CommentResponseDto(1L, "Great item!", "Alice", created);

        JsonContent<CommentResponseDto> result = json.write(dto);

        assertThat(result).hasJsonPathStringValue("$.created");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2025-02-28 15:45:00");
    }
}