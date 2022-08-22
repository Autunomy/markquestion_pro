package com.hty.markquestion.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {

    private JwtUtil() {
    }

    public static final long EXPIRE = 1000 * 60 * 60 * 24;//24小时
    public static final String APP_SECRET = "kKgbzOBWsE9fF1Pr";//私钥

    /**
     * 生成一个jwt字符串
     *
     * @param username 自定义字段username
     * @return
     */
    public static String genToken(String username) {
        String jwtToken = Jwts.builder()
                //jwt的头信息
                .setHeaderParam("typ", "JWT")//令牌的类型
                .setHeaderParam("alg", "HS256")//签名使用的算法

                //jwt的有效载荷(内容部分)
                .setSubject("hty")//主题
                .setIssuedAt(new Date())//发布时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))//到期时间
                .claim("username", username)//自定义字段

                //签名哈希
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)//(私钥)防伪标志
                .compact();
        return jwtToken;
    }


    /**
     * 根据token字符串获取用户名
     *
     * @param request
     * @return
     */
    public static String getUsernameByToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = bearerToken.substring(bearerToken.indexOf(" ") + 1);
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("username");
    }

}
