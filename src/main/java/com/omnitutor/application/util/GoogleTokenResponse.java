package com.omnitutor.application.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
class GoogleTokenResponse {

	 @JsonProperty("access_token")
	 private String accessToken;

	 @JsonProperty("token_type")
	 private String tokenType;

	 @JsonProperty("expires_in")
	 private int expiresIn;
	
}
