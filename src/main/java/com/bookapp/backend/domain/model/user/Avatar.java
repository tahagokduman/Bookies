package com.bookapp.backend.domain.model.user;

import com.bookapp.backend.domain.model.base.BaseModel;
import lombok.*;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class Avatar{
    private Long id;
    private String avatar;

    public Avatar(String avatar) {
        this.avatar = avatar;
    }
    public Avatar(Long id){
        this.id = id;
    }
}
