package hu.webuni.hr.mzsombor.security;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.webuni.hr.mzsombor.config.HrConfigProperties;

@Service
public class JwtService {
	
	@Autowired
	HrConfigProperties config;
	
	private static final String AUTH = "auth";
	private static final String EMPLOYEEID = "employeeid";
	private int timeout = config.getJwt().getTimeout();
	private Algorithm alg = Algorithm.HMAC256(config.getJwt().getSecret());
	private String issuer = config.getJwt().getIssuer();

	public String createJwtToken(EmployeeUserDetails principal) {
		return JWT.create()
			.withSubject(principal.getUsername())
			.withArrayClaim(AUTH, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
			.withClaim(EMPLOYEEID, principal.getEmployeeId())
			.withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(timeout)))
			.withIssuer(issuer)
			.sign(alg);
		
	}

	public EmployeeUserDetails parseJwt(String jwtToken) {
		DecodedJWT decodedJwt = JWT.require(alg)
			.withIssuer(issuer)
			.build()
			.verify(jwtToken);
		return new EmployeeUserDetails(decodedJwt.getSubject(), "dummy", 
				decodedJwt.getClaim(AUTH).asList(String.class)
				.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
				decodedJwt.getClaim(EMPLOYEEID).asLong()
				);
		
	}

}
