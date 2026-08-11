package com.rose.user.profile.entity;

import com.rose.user.entity.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user_profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "bio", length = 100)
    private String bio;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "avatar_url", length = 2083)
    private String avatarUrl;

    @Column(name = "banner_url", length = 2083)
    private String bannerUrl;

    @ElementCollection
    @CollectionTable(
            name = "user_profile_media_links",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @OrderColumn(name = "position")
    @Size(max = 16)
    private List<MediaLink> mediaLinks = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static Profile create(User user) {
        Profile userProfile = new Profile();
        userProfile.user = user;
        return userProfile;
    }
}