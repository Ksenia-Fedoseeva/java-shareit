package ru.practicum.shareit.json;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestResponseDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime created = LocalDateTime.of(2025, 2, 28, 10, 0);
        ItemRequestResponseDto dto = new ItemRequestResponseDto(1L, "Need a laptop", created, List.of());

        JsonContent<ItemRequestResponseDto> result = json.write(dto);

        assertThat(result).hasJsonPathStringValue("$.created");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2025-02-28 10:00:00");
    }
}