package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.annotations.ValidationGroup;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(groups = ValidationGroup.OnCreate.class, message = "Название вещи должно быть указано")
    private String name;

    @NotBlank(groups = ValidationGroup.OnCreate.class, message = "Описание вещи должно быть указано")
    private String description;

    @NotNull(groups = ValidationGroup.OnCreate.class, message = "Cтатус для аренды вещи должен быть указан")
    private Boolean available;

    @Positive(message = "ID запроса должен быть положительным числом")
    private Long requestId;
}
