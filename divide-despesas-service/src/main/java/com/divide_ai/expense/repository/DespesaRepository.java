package com.divide_ai.expense.repository;

import com.divide_ai.expense.entity.Despesa;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class DespesaRepository {
    
    private final DynamoDbTable<Despesa> table;
    
    public DespesaRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("despesas", TableSchema.fromBean(Despesa.class));
    }
    
    public Despesa save(Despesa despesa) {
        if (despesa.getId() == null) {
            despesa.setId(UUID.randomUUID().toString());
        }
        table.putItem(despesa);
        return despesa;
    }
    
    public Optional<Despesa> findById(String id) {
        return Optional.ofNullable(table.getItem(r -> r.key(k -> k.partitionValue(id))));
    }
    
    public List<Despesa> findAll() {
        try {
            return table.scan().items().stream().toList();
        } catch (Exception e) {
            System.err.println("Erro ao buscar despesas: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao acessar DynamoDB", e);
        }
    }
    
    public void deleteById(String id) {
        table.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }
}