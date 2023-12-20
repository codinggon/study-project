package com.gon.fitness.web.settings.form;

import com.gon.fitness.domain.account.Account;
import lombok.Data;

@Data
public class Profile {

    private String bio;

    private String url;

    private String occupation;

    private String location;
    private String profileImage;

    public Profile(Account account) {
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
        this.profileImage = account.getProfileImage();
    }
}
