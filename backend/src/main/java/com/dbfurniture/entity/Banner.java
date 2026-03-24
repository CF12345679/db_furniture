package com.dbfurniture.entity;

import javax.persistence.*;

@Entity
@Table(name = "banners")
public class Banner extends BaseEntity {

    @Column(length = 200)
    private String title;

    @Column(name = "link_type", length = 50)
    private String linkType;

    @Column(name = "link_value", length = 255)
    private String linkValue;

    @Column(name = "image_id", nullable = false, length = 255)
    private String imageId;

    private Integer sort;

    @Column(name = "is_active")
    private Boolean isActive;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLinkType() { return linkType; }
    public void setLinkType(String linkType) { this.linkType = linkType; }

    public String getLinkValue() { return linkValue; }
    public void setLinkValue(String linkValue) { this.linkValue = linkValue; }

    public String getImageId() { return imageId; }
    public void setImageId(String imageId) { this.imageId = imageId; }

    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
