// package com.telia.Lease_management.utils;

// import java.io.IOException;
// import java.nio.charset.Charset;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpInputMessage;
// import org.springframework.http.HttpOutputMessage;
// import org.springframework.http.MediaType;
// import org.springframework.http.converter.AbstractHttpMessageConverter;
// import org.springframework.http.converter.HttpMessageConverter;
// import org.springframework.http.converter.HttpMessageNotReadableException;
// import org.springframework.http.converter.HttpMessageNotWritableException;
// import org.springframework.util.StreamUtils;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.telia.Lease_management.dto.ifuDto.IfuResponse;

// @Configuration
// public class RequestHtmlToJson {
    

//     private final ObjectMapper objectMapper;

//     public RequestHtmlToJson(ObjectMapper objectMapper) {
//         this.objectMapper = objectMapper;
//     }

    
//     @Bean
//     public HttpMessageConverter<?> customJsonConverter() {
//         return new AbstractHttpMessageConverter<Object>(MediaType.TEXT_HTML) {
//             @Override
//             protected boolean supports(Class<?> clazz) {
//                 return IfuResponse.class.isAssignableFrom(clazz);
//             }

//             @Override
//             protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
//                 String body = StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
//                 // Si la réponse est HTML, essayez de la convertir en JSON
//                 return objectMapper.readValue(body, IfuResponse.class);
//             }

//             @Override
//             protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
//                 // Vous pouvez ignorer cette partie pour maintenant
//             }
//         };
// }


// }
