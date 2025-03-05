package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.comment.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateItem() throws Exception {
        ItemDto requestDto = new ItemDto(null, "Table", "Wooden table", true, null);
        ItemDto responseDto = new ItemDto(1L, "Table", "Wooden table", true, null);

        when(itemService.createItem(any(ItemDto.class), anyLong())).thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, "1")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Table"));
    }

    @Test
    void testUpdateItem() throws Exception {
        ItemDto requestDto = new ItemDto(null, "Updated Table", "Updated description", true,
                null);
        ItemDto responseDto = new ItemDto(1L, "Updated Table", "Updated description", true,
                null);

        when(itemService.updateItem(anyLong(), any(ItemDto.class), anyLong())).thenReturn(responseDto);

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, "1")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Table"));
    }

    @Test
    void testGetItemById() throws Exception {
        ItemResponseDto responseDto = new ItemResponseDto(1L, "Table", "Wooden table",
                true, null, null, List.of());

        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(responseDto);

        mockMvc.perform(get("/items/1")
                        .header(USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Table"));
    }

    @Test
    void testGetAllItemsByUser() throws Exception {
        List<ItemDto> responseList = List.of(
                new ItemDto(1L, "Table", "Wooden table", true, null),
                new ItemDto(2L, "Chair", "Comfortable chair", true, null)
        );

        when(itemService.getAllItemsByUser(anyLong())).thenReturn(responseList);

        mockMvc.perform(get("/items")
                        .header(USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Table"))
                .andExpect(jsonPath("$[1].name").value("Chair"));
    }

    @Test
    void testSearchItems() throws Exception {
        List<ItemDto> responseList = List.of(
                new ItemDto(1L, "Table", "Wooden table", true, null)
        );

        when(itemService.searchItems(anyString())).thenReturn(responseList);

        mockMvc.perform(get("/items/search")
                        .param("text", "table"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Table"));
    }

    @Test
    void testCreateComment() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto("Great item!");
        CommentResponseDto responseDto = new CommentResponseDto(1L, "Great item!", "User",
                LocalDateTime.now());

        when(itemService.addComment(anyLong(), anyLong(), any(CommentRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, "1")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Great item!"));
    }
}
