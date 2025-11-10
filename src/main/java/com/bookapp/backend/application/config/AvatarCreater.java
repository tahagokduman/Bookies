package com.bookapp.backend.application.config;

import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.ports.out.IAvatarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AvatarCreater implements ApplicationRunner {

    private final IAvatarRepository avatarRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(avatarRepository.findAll().isEmpty()){
            System.out.println("Avatars are uploading.");
            List<Avatar> list = new ArrayList<>();
            Avatar avatar1 = new Avatar("avatar.png");
            Avatar avatar2 = new Avatar("bookbibliofil.png");
            Avatar avatar3 = new Avatar("bookcat.png");
            Avatar avatar4 = new Avatar("bookfriends.png");
            Avatar avatar5 = new Avatar("bookworms.png");
            list.add(avatar1);
            list.add(avatar2);
            list.add(avatar3);
            list.add(avatar4);
            list.add(avatar5);
            for(Avatar avatar : list){
                avatarRepository.save(avatar);
            }
            System.out.println("Avatars uploaded safely");
        }

    }
}
