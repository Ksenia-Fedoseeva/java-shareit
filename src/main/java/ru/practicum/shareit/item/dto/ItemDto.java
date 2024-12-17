package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.annotations.ValidationGroup;
//import ru.practicum.shareit.annotations.ValidationGroup;


/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(groups = ValidationGroup.OnCreate.class)
    private String name;

    @NotBlank(groups = ValidationGroup.OnCreate.class)
    private String description;

    @NotNull(groups = ValidationGroup.OnCreate.class)
    private Boolean available;
}
