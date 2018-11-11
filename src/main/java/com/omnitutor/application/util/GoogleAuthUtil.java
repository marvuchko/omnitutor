package com.omnitutor.application.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleAuthUtil {
	
	public static GoogleTokenResponse authorize(String scope) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		ObjectMapper mapper = new ObjectMapper();
		GoogleCredentials credentials = createCredentials(mapper);
		RSAPrivateKey privateKey = generateRSAPrivateKey(credentials);
	    String compactedJWT = buildCompactedJWT(scope, credentials, privateKey);
	    Request request = buildRequest(compactedJWT, credentials);
	    return mapper
	    		.readValue(sendRequest(new OkHttpClient(), request), GoogleTokenResponse.class);
	}
	
	private static Request buildRequest(String compactedJWT, GoogleCredentials credentials) {
		RequestBody formBody = new FormBody.Builder()
	            .add(RequestUtil.GRANT_TYPE, RequestUtil.JWT_BEARER)
	            .add(RequestUtil.ASSERTION, compactedJWT).build();
		return new Request.Builder().url(credentials.getTokenUri()).post(formBody)
	            .build();
	}

	private static String buildCompactedJWT(String scope, GoogleCredentials credentials, RSAPrivateKey privateKey) {
		return Jwts.builder().setIssuer(credentials.getClientEmail())
	            .setAudience(credentials.getTokenUri())
	            .claim("scope", scope)
	            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
	            .setIssuedAt(Date.from(Instant.now())).signWith(SignatureAlgorithm.RS256, privateKey)
	            .compact();
	}

	private static RSAPrivateKey generateRSAPrivateKey(GoogleCredentials credentials) 
			throws NoSuchAlgorithmException, InvalidKeySpecException {
	    byte[] decoded = Base64.getDecoder().decode(parsePrivateKey(credentials.getPrivateKey()));
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
		return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(keySpec);
	}

	private static String parsePrivateKey(String privateKey) {
		return privateKey
				.replace("\n", "")
				.replace("-----BEGIN PRIVATE KEY-----", "")
	    		.replace("-----END PRIVATE KEY-----", "");
	}

	private static GoogleCredentials createCredentials(ObjectMapper mapper) throws IOException {
		File file = new ClassPathResource(ResourceUtil.CREDENTIALS).getFile();
		return mapper.readValue(Files.readAllBytes(Paths.get(file.getPath())), GoogleCredentials.class);
	}

	private static byte[] sendRequest(OkHttpClient client, Request request) throws IOException {
		try (Response response = client.newCall(request).execute();
		ResponseBody body = response.body()) {
		    return body != null ? body.bytes() : null;
		}
	}
}
