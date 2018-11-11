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
class GoogleCredentials {

	 @JsonProperty("private_key")
	 private String privateKey;

	 @JsonProperty("client_email")
	 private String clientEmail;

	 @JsonProperty("token_uri")
	 private String tokenUri;
	
}
