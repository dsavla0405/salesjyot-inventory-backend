package com.example.inventorygenius.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "portal")
public class Portal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portal_id")
    private Long portalId;

    @Column(name = "portal_name")
    private String portal_name;

    @Column (name = "user-email")
    private String userEmail;

    public Portal() {

    }
    
    public Portal(Long portalId, String portal_name, String userEmail){
        this.portalId = portalId;
        this.portal_name = portal_name;
        this.userEmail = userEmail;
    }

    public Long getPortalId() {
        return portalId;
    }

    public void setPortalId(Long portalId) {
        this.portalId = portalId;
    }

    public String getPortalName() {
        return portal_name;
    }

    public void setPortalName(String portal_name) {
        this.portal_name = portal_name;
    }

    public String getPortal_name() {
        return portal_name;
    }

    public void setPortal_name(String portal_name) {
        this.portal_name = portal_name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
