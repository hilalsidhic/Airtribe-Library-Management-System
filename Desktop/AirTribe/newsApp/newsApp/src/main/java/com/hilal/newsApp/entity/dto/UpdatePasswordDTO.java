package com.hilal.newsApp.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePasswordDTO {
    private String username;
    private String oldPassword;
    private String newPassword;
}
