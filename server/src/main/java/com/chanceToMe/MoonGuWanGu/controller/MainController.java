package com.chanceToMe.MoonGuWanGu.controller;

import com.chanceToMe.MoonGuWanGu.common.enums.ErrorCode;
import com.chanceToMe.MoonGuWanGu.common.exception.CustomException;
import com.chanceToMe.MoonGuWanGu.dto.CreateMemberDto;
import com.chanceToMe.MoonGuWanGu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody CreateMemberDto dto) {

        if (!isValidEmail(dto.getEmail())) {
            throw new CustomException(ErrorCode.INVALID);
        }

        memberService.createMember(dto.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
