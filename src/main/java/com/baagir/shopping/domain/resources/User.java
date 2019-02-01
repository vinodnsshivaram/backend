package com.baagir.shopping.domain.resources;

import com.baagir.shopping.domain.AbstractLinkableEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonPropertyOrder({ "_links", "_embedded", "userName", "emailAddress", "firstName",
        "lastName", "id", "createdAt", "updatedAt"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "user")
public class User extends AbstractLinkableEntity {

    @ApiModelProperty(value = "id", notes = "System generated uuid to identify the user.", dataType = "UUID")
    @JsonProperty("id")
    @Id
    private String id;

    @ApiModelProperty(value="lastName", dataType = "string",notes = "lastName of the user.", required = true)
    @Size(min = 1)
    @NotBlank(message = "lastName can't be empty!")
    private String lastName;

    @ApiModelProperty(value="firstName", dataType = "string",notes = "firstName of the user.", required = true)
    @Size(min = 1)
    @NotBlank(message = "firstName can't be empty!")
    private String firstName;

    @ApiModelProperty(value="userName", dataType = "string",notes = "userName of the user.", required = true)
    @Size(min = 1)
    @NotBlank(message = "userName can't be empty!")
    private String userName;

    @ApiModelProperty(value="password", dataType = "string",notes = "password of the user.", required = true)
    @Size(min = 1)
    @NotBlank(message = "password can't be empty!")
    private String password;

    @ApiModelProperty(value="emailAddress", dataType = "string",notes = "emailAddress of the user.", required = true)
    @Size(min = 1)
    @NotBlank(message = "emailAddress can't be empty!")
    @Email
    private String emailAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
