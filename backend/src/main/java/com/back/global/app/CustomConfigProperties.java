package com.back.global.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "custom")
public class CustomConfigProperties {
    private List<NotProdMember> notProdMembers;

    public record NotProdMember(
            String username,
            String apiKey,
            String nickname,
            String profileImgUrl
    ) {
    }

    public List<NotProdMember> getNotProdMembers() {
        return notProdMembers;
    }

    public void setNotProdMembers(List<NotProdMember> notProdMembers) {
        this.notProdMembers = notProdMembers;
    }
}
