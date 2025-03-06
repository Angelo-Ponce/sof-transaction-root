package com.sof.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private Long personId;
    private String identification;
    private String name;
    private String gender;
    private Integer age;
    private String address;
    private String phone;
    private String clientId;
    private String password;
    private Boolean status;
}
