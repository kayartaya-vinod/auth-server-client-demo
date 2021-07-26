package com.kvinod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@WebFilter(urlPatterns = "/")
@Order(1)
public class ClaimsFilter implements Filter {
    @Value("${authUrl}")
    String authUrl;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");

        if (authorization != null) {
            String[] parts = authorization.split(" ");
            if (parts.length == 2) {
                String jwt = parts[1];
                String url = String.format("%s/?jwt=%s", authUrl, jwt);
                RestTemplate template = new RestTemplate();
                String claims = template.getForObject(url, String.class);
                ObjectMapper om = new ObjectMapper();
                TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
                };
                Map<String, Object> map = om.readValue(claims, typeRef);
                req.setAttribute("claims", map);
            }
        }
        chain.doFilter(req, response);
    }

}
