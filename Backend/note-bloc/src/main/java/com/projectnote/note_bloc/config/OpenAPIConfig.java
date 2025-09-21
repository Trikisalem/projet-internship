package com.projectnote.note_bloc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    
    @Value("${app.openapi.dev-url:http://localhost:8080}")
    private String devUrl;
    
    @Value("${app.openapi.prod-url:https://api.notebloc.com}")
    private String prodUrl;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("📝 Note-Bloc API")
                .version("v1.0.0")
                .description("""
                    **API REST complète pour la gestion de notes avancée**
                    
                    ### Fonctionnalités principales :
                    - 🔐 Authentification JWT avec refresh tokens
                    - 📝 CRUD complet des notes avec Markdown
                    - 🏷️ Système de tags pour organiser les notes
                    - 👥 Partage de notes entre utilisateurs
                    - 🔗 Génération de liens publics
                    - 🔍 Recherche et filtres avancés
                    - 📄 Pagination intelligente
                    
                    ### Technologies utilisées :
                    - Spring Boot 3.x
                    - Spring Security avec JWT
                    - Spring Data JPA
                    - PostgreSQL
                    - OpenAPI 3.0
                    """)
                .contact(new Contact()
                    .name("📧 Équipe Note-Bloc")
                    .email("contact@notebloc.com")
                    .url("https://notebloc.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url(devUrl)
                    .description("🔧 Serveur de développement"),
                new Server()
                    .url(prodUrl)
                    .description("🚀 Serveur de production")
            ))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("""
                            **Token JWT pour l'authentification**
                            
                            Pour obtenir un token :
                            1. Utilisez POST /api/v1/auth/login
                            2. Copiez le token de la réponse
                            3. Cliquez sur "Authorize" et collez : Bearer YOUR_TOKEN
                            """)));
    }
}
