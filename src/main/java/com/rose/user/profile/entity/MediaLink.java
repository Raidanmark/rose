package com.rose.user.profile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaLink {

    @Column(name = "name")
    @Size(max = 32)
    private String platformName;

    @Column(name = "url")
    @Size(max = 2083)
    private String url;
}
