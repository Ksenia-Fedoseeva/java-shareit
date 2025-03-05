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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    void testCreateBooking() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                1L
        );

        BookingResponseDto responseDto = new BookingResponseDto(
                1L, requestDto.getStart(), requestDto.getEnd(), Status.WAITING, null, null
        );

        when(bookingService.createBooking(anyLong(), any(BookingRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER, "1")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }


    @Test
    void testApproveBooking() throws Exception {
        BookingResponseDto responseDto = new BookingResponseDto(
                1L, LocalDateTime.now(), LocalDateTime.now().plusDays(2), Status.APPROVED, null, null
        );

        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(responseDto);

        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header(USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testGetBookingById() throws Exception {
        BookingResponseDto responseDto = new BookingResponseDto(
                1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), Status.WAITING, null, null
        );

        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(responseDto);

        mockMvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void testGetUserBookings() throws Exception {
        List<BookingResponseDto> responseList = List.of(
                new BookingResponseDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), Status.WAITING,
                        null, null),
                new BookingResponseDto(2L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(4),
                        Status.APPROVED, null, null)
        );

        when(bookingService.getBookings(anyLong(), anyString(), eq(false))).thenReturn(responseList);

        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].status").value("WAITING"))
                .andExpect(jsonPath("$[1].status").value("APPROVED"));
    }

    @Test
    void testGetOwnerBookings() throws Exception {
        List<BookingResponseDto> responseList = List.of(
                new BookingResponseDto(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), Status.APPROVED,
                        null, null)
        );

        when(bookingService.getBookings(anyLong(), anyString(), eq(true))).thenReturn(responseList);

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, "1")
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }
}