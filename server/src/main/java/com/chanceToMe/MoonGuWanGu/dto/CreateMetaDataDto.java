package com.chanceToMe.MoonGuWanGu.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMetaDataDto {

  @NotNull
  @NotBlank
  private String imageUrl;

  @Max(value = 5)
  @NotNull
  private Integer grade;

  @NotBlank
  @NotNull
  private String category;
}
