package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;


@Validated
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
	private final BookingClient bookingClient;
	public static final String USER_ID_HEADER = "X-Sharer-User-Id";

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestBody @Valid BookingRequestDto bookingRequestDto,
												@RequestHeader(USER_ID_HEADER) Long userId) {
		return bookingClient.createBooking(userId, bookingRequestDto);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(@PathVariable Long bookingId,
												 @RequestHeader(USER_ID_HEADER) Long userId,
												 @RequestParam Boolean approved) {
		return bookingClient.approveBooking(bookingId, userId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@PathVariable Long bookingId,
												 @RequestHeader(USER_ID_HEADER) Long userId) {
		return bookingClient.getBookingById(bookingId, userId);
	}

	@GetMapping
	public ResponseEntity<Object> getUserBookings(
			@RequestHeader(USER_ID_HEADER) Long userId,
			@RequestParam(defaultValue = "ALL") String state) {
		return bookingClient.getUserBookings(userId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerBookings(
			@RequestHeader(USER_ID_HEADER) Long userId,
			@RequestParam(defaultValue = "ALL") String state) {
		return bookingClient.getOwnerBookings(userId, state);
	}
}
