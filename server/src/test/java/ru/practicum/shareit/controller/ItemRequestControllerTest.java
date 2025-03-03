package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    void testCreateItemRequest() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto("I need a laptop");
        ItemRequestResponseDto responseDto = new ItemRequestResponseDto(1L, "I need a laptop",
                LocalDateTime.now(), List.of());

        when(itemRequestService.createItemRequest(any(ItemRequestDto.class), anyLong())).thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, "1")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("I need a laptop"));
    }


    @Test
    void testGetUserItemRequests() throws Exception {
        List<ItemRequestResponseDto> responseList = List.of(
                new ItemRequestResponseDto(1L, "I need a laptop", LocalDateTime.now(), List.of()),
                new ItemRequestResponseDto(2L, "Looking for a bike", LocalDateTime.now().minusDays(1),
                        List.of())
        );

        when(itemRequestService.getUserRequests(anyLong())).thenReturn(responseList);

        mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].description").value("I need a laptop"))
                .andExpect(jsonPath("$[1].description").value("Looking for a bike"));
    }

    @Test
    void testGetAllItemRequests() throws Exception {
        List<ItemRequestResponseDto> responseList = List.of(
                new ItemRequestResponseDto(1L, "Looking for a TV", LocalDateTime.now(), List.of())
        );

        when(itemRequestService.getAllRequests(anyLong())).thenReturn(responseList);

        mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].description").value("Looking for a TV"));
    }

    @Test
    void testGetItemRequestById() throws Exception {
        ItemRequestResponseDto responseDto = new ItemRequestResponseDto(1L, "Need a couch",
                LocalDateTime.now(), List.of());

        when(itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(responseDto);

        mockMvc.perform(get("/requests/1")
                        .header(USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Need a couch"));
    }
}
