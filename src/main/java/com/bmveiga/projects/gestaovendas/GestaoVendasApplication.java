package com.bmveiga.projects.gestaovendas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.bmveiga.projects.gestaovendas.entidades"})
@EnableJpaRepositories(basePackages = {"com.bmveiga.projects.gestaovendas.repositorio"})
@ComponentScan(basePackages = {"com.bmveiga.projects.gestaovendas.servico", "com.bmveiga.projects.gestaovendas.controlador", "com.bmveiga.projects.gestaovendas.excecao"})
@SpringBootApplication
public class GestaoVendasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoVendasApplication.class, args);
	}

}
