package com.bookapp.backend.adapter.in.web.dto.response.user;

import com.bookapp.backend.domain.model.base.NonNegativeInteger;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO extends RepresentationModel<UserResponseDTO> {
    private Long id;
    private String username;
    private String email;
    private LocalDate birthdayDate;
    private NonNegativeInteger followersCount;
    private NonNegativeInteger followingCount;
}
