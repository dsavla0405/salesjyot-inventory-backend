package com.example.inventorygenius.entity;

import jakarta.persistence.*;

@Entity()
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone", nullable = false)
    private String phone;

    public Client() {
        
    }

    public Client(Long clientId, String email, String password, String companyName, String firstName, String lastName,
            String phone) {
        this.clientId = clientId;
        this.email = email;
        this.password = password;
        this.companyName = companyName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Long getUserId() {
        return clientId;
    }

    public void setUserId(Long userId) {
        this.clientId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	@Override
	public String toString() {
		return "Client [clientId=" + clientId + ", email=" + email + ", password=" + password + ", companyName="
				+ companyName + ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone + "]";
	}
    
    
    
}

