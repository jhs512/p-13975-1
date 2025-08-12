package com.back.domain.member.member.dto;

import com.back.domain.member.member.entity.Member;

public class MemberWithUsernameDto extends MemberDto {
    private final String username;

    public MemberWithUsernameDto(Member member) {
        super(member);
        this.username = member.getUsername();
    }

    public String getUsername() {
        return username;
    }
}
