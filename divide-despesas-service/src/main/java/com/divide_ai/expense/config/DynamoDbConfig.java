package com.divide_ai.expense.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.divide_ai.expense.entity.Despesa;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {
    
    @Bean
    public DynamoDbClient dynamoDbClient() {
        try {
            String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
            String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
            
            return DynamoDbClient.builder()
                    .region(Region.US_EAST_1)
                    .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                    .build();
        } catch (Exception e) {
            System.err.println("Erro ao configurar DynamoDB: " + e.getMessage());
            throw e;
        }
    }
    
    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        
        // Criar tabela se não existir
        createTableIfNotExists(enhancedClient, dynamoDbClient);
        
        return enhancedClient;
    }
    
    private void createTableIfNotExists(DynamoDbEnhancedClient enhancedClient, DynamoDbClient dynamoDbClient) {
        try {
            DynamoDbTable<Despesa> table = enhancedClient.table("despesas", TableSchema.fromBean(Despesa.class));
            table.createTable();
            System.out.println("Tabela 'despesas' criada com sucesso!");
        } catch (Exception e) {
            System.out.println("Tabela 'despesas' já existe ou erro: " + e.getMessage());
        }
    }
}