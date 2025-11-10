package com.bookapp.backend.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AvatarEntity extends BaseEntity<Long>{

    private String avatar;

}
