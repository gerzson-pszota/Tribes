package org.gfa.avustribesbackend.configs;

import org.gfa.avustribesbackend.components.RequestResponseLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RequestResponseInterceptorConfig implements WebMvcConfigurer {

  private final RequestResponseLoggingInterceptor requestResponseLoggingInterceptor;

  @Autowired
  public RequestResponseInterceptorConfig(RequestResponseLoggingInterceptor requestResponseLoggingInterceptor) {
    this.requestResponseLoggingInterceptor = requestResponseLoggingInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RequestResponseLoggingInterceptor());
  }
}
