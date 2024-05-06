package com.chanceToMe.MoonGuWanGu.controller;

import com.chanceToMe.MoonGuWanGu.dto.CreateMetaDataDto;
import com.chanceToMe.MoonGuWanGu.dto.DeleteMetaDataDto;
import com.chanceToMe.MoonGuWanGu.model.MetaData;
import com.chanceToMe.MoonGuWanGu.service.MetaDataService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metadata")
public class MetaDataController {

  @Autowired
  MetaDataService metaDataService;

  @PostMapping
  public ResponseEntity add(@RequestBody @Valid CreateMetaDataDto dto) {
    metaDataService.addMetaData(dto.getImageUrl(), dto.getGrade(), dto.getCategory());

    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @GetMapping
  public ResponseEntity find(@RequestParam(value = "category") String category) {
    List<MetaData> metaDataList = metaDataService.retrieveMetaDataByCategory(category);

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("list", metaDataList);

    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
  }

  @DeleteMapping
  public ResponseEntity delete(@RequestBody @Valid DeleteMetaDataDto dto) {
    UUID deletedId = metaDataService.deleteMetaData(dto.getId());

    return ResponseEntity.status(HttpStatus.OK).body(deletedId);
  }
}
