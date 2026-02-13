package com.example.demo.infra.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.application.port.JwTokenManagerPort;
import com.example.demo.infra.jwt.generator.JwTokenGenerator;
import com.example.demo.infra.jwt.provider.JwTokenProvider;
import com.example.demo.infra.jwt.validator.JwTokenValidator;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
class JwTokenManagerAdapter implements JwTokenManagerPort {

	private JwTokenGenerator jwTokenGenerator;
	private JwTokenProvider jwTokenProvider;
	private JwTokenValidator jwTokenValidator;

	@Override
	public String generateToken(String username, String email, List<String> role) {
		return jwTokenGenerator.generateToken(username, email, role);
	}

	@Override
	public boolean validateToken(String token) {
		return jwTokenValidator.validateToken(token);
	}

	@Override
	public String getEmail(String token) {
		return jwTokenProvider.getEmail(token);
	}

	@Override
	public List<String> getRoleList(String token) {
		return jwTokenProvider.getRoleList(token);
	}

	@Override
	public String getUsername(String token) {
		return jwTokenProvider.getUsername(token);
	}

}
