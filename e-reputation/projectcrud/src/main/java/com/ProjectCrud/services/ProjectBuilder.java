package com.ProjectCrud.services;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

public class ProjectBuilder {

	@Bean
	public WebClient.Builder getWebCleintBuilder()
	{
		return WebClient.builder();
	}
}
