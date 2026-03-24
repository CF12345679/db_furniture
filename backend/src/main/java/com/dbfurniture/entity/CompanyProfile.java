package com.dbfurniture.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "company_profile")
public class CompanyProfile extends BaseEntity {

    @Column(name = "company_name", length = 200)
    private String companyName;

    @Column(length = 50)
    private String phone;

    @Column(length = 500)
    private String address;

    @Column(name = "we_chat", length = 100)
    private String weChat;

    @Column(name = "we_chat_qr_image_id", length = 255)
    private String weChatQrImageId;

    @Column(name = "nav_lat")
    private Double navLat;

    @Column(name = "nav_lng")
    private Double navLng;

    @Column(name = "seo_title", length = 200)
    private String seoTitle;

    @Column(name = "seo_description", columnDefinition = "TEXT")
    private String seoDescription;

    // Getters and Setters
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getWeChat() { return weChat; }
    public void setWeChat(String weChat) { this.weChat = weChat; }

    public String getWeChatQrImageId() { return weChatQrImageId; }
    public void setWeChatQrImageId(String weChatQrImageId) { this.weChatQrImageId = weChatQrImageId; }

    public Double getNavLat() { return navLat; }
    public void setNavLat(Double navLat) { this.navLat = navLat; }

    public Double getNavLng() { return navLng; }
    public void setNavLng(Double navLng) { this.navLng = navLng; }

    public String getSeoTitle() { return seoTitle; }
    public void setSeoTitle(String seoTitle) { this.seoTitle = seoTitle; }

    public String getSeoDescription() { return seoDescription; }
    public void setSeoDescription(String seoDescription) { this.seoDescription = seoDescription; }
}
