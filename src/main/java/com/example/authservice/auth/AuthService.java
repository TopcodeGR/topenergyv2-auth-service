package com.example.authservice.auth;


import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.*;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.authservice.user.UserAccessToken;
import com.example.authservice.user.UserRefreshToken;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.example.authservice.user.UserTokens;
import reactor.util.annotation.Nullable;
import reactor.core.publisher.Mono;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import com.example.authservice.user.User;

@Component
public class AuthService {

    @Value("${auth.jwt-secret}")
    private String jwtSecret;

    @Autowired
    ReactiveMongoTemplate template;

    public Boolean validateUserPassword(String userPassword, String givenPassword){
        return new BCryptPasswordEncoder().matches(givenPassword,userPassword);
    }


    public Boolean validateJWTToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build();

        try{
            DecodedJWT decodedJWT = verifier.verify(token);
        }
        catch(JWTVerificationException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Nullable
    public String getUserIdFromToken(String token){

        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build();

        try{
            DecodedJWT decodedJWT = verifier.verify(token);
            String userId = decodedJWT.getClaim("userId").toString();
            if(userId == null || userId.isEmpty()){
                return null;
            }
            return userId;
        }
        catch(JWTVerificationException e){
            return null;
        }
    }



    public UserTokens issueUserTokens(String userId, String clientId){
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        HashMap<String,String> payload = new HashMap<String, String>();

        Calendar accessTokenCal = Calendar.getInstance();
        accessTokenCal.setTime(new Date());
        accessTokenCal.add(Calendar.HOUR_OF_DAY, 1);

        Calendar refreshTokenCal = Calendar.getInstance();
        refreshTokenCal.setTime(new Date());
        refreshTokenCal.add(Calendar.WEEK_OF_MONTH, 1);

        payload.put("userId",userId);
        payload.put("clientId",clientId);


        String accessTokenString = JWT.create()
                .withIssuer("auth0")
                .withExpiresAt(accessTokenCal.getTime())
                .withPayload(payload)
                .sign(algorithm);

        String refreshTokenString = JWT.create()
                .withIssuer("auth0")
                .withExpiresAt(refreshTokenCal.getTime())
                .withPayload(payload)
                .sign(algorithm);

        UserAccessToken accessToken = new UserAccessToken(accessTokenString,accessTokenCal.getTime());
        UserRefreshToken refreshToken = new UserRefreshToken(refreshTokenString,refreshTokenCal.getTime());
        return new UserTokens(accessToken,refreshToken,clientId);

    }

    public Mono<User> updateUserTokens( User u, String clientId){
        UserTokens userTokens = this.issueUserTokens(u.get_id(),clientId);
        u.updateOrInsertAccessToken(userTokens.getAccessToken(),clientId);
        u.updateOrInsertRefreshToken(userTokens.getRefreshToken(),clientId);

        Update update = new Update();
        update.set("accessTokens",u.getAccessTokens());
        update.set("refreshTokens",u.getRefreshTokens());

        Query query = new Query();
        query.addCriteria(Criteria.where("_id")
                .is(new ObjectId(u.get_id())));
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);
        return template.findAndModify(query,update,findAndModifyOptions,User.class);
    }






}
