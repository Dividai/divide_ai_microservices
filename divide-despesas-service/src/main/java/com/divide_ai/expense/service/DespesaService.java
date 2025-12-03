package com.divide_ai.expense.service;

import com.divide_ai.expense.entity.Despesa;
import com.divide_ai.expense.repository.DespesaRepositoryMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DespesaService {
    
    @Autowired
    private DespesaRepositoryMemory despesaRepository;
    
    public List<Despesa> findAll() {
        return despesaRepository.findAll();
    }
    
    public Optional<Despesa> findById(String id) {
        return despesaRepository.findById(id);
    }
    
    public Despesa save(Despesa despesa) {
        // Validate and sanitize input
        if (despesa.getDescricao() != null && containsHtmlTags(despesa.getDescricao())) {
            throw new IllegalArgumentException("Descrição contém conteúdo não permitido");
        }
        
        // Set defaults if null
        if (despesa.getTipoDivisao() == null || despesa.getTipoDivisao().isEmpty()) {
            despesa.setTipoDivisao("IGUAL");
        }
        if (despesa.getParticipantesIds() == null || despesa.getParticipantesIds().isEmpty()) {
            despesa.setParticipantesIds(List.of(despesa.getPagadorId()));
        }
        if (despesa.getDataCreacao() == null) {
            despesa.setDataCreacao(java.time.Instant.now());
        }
        return despesaRepository.save(despesa);
    }
    
    private boolean containsHtmlTags(String input) {
        return input.contains("<") && input.contains(">");
    }
    
    public void deleteById(String id) {
        despesaRepository.deleteById(id);
    }
    
    public List<Despesa> findByGrupoId(Long grupoId) {
        if (grupoId == null) {
            throw new IllegalArgumentException("ID do grupo não pode ser nulo");
        }
        return despesaRepository.findAll().stream()
                .filter(d -> d.getGrupoId() != null && d.getGrupoId().equals(grupoId))
                .toList();
    }
    
    public List<Despesa> findByUsuarioId(Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        return despesaRepository.findAll().stream()
                .filter(d -> (d.getPagadorId() != null && d.getPagadorId().equals(usuarioId)) || 
                           (d.getParticipantesIds() != null && d.getParticipantesIds().contains(usuarioId)))
                .toList();
    }
    
    public Map<Long, Double> calcularDivisao(Despesa despesa) {
        Map<Long, Double> divisao = new HashMap<>();
        
        if (despesa.getParticipantesIds() == null || despesa.getParticipantesIds().isEmpty()) {
            return divisao;
        }
        
        switch (despesa.getTipoDivisao().toUpperCase()) {
            case "IGUAL":
                double valorPorPessoa = despesa.getValor() / despesa.getParticipantesIds().size();
                for (Long participanteId : despesa.getParticipantesIds()) {
                    divisao.put(participanteId, valorPorPessoa);
                }
                break;
                
            case "PERSONALIZADA":
                // Para demonstração, vamos usar uma divisão 70/30 se houver 2 participantes
                if (despesa.getParticipantesIds().size() == 2) {
                    divisao.put(despesa.getParticipantesIds().get(0), despesa.getValor() * 0.7);
                    divisao.put(despesa.getParticipantesIds().get(1), despesa.getValor() * 0.3);
                } else {
                    // Fallback para divisão igual
                    double valor = despesa.getValor() / despesa.getParticipantesIds().size();
                    for (Long participanteId : despesa.getParticipantesIds()) {
                        divisao.put(participanteId, valor);
                    }
                }
                break;
                
            default:
                // Divisão igual como padrão
                double valorDefault = despesa.getValor() / despesa.getParticipantesIds().size();
                for (Long participanteId : despesa.getParticipantesIds()) {
                    divisao.put(participanteId, valorDefault);
                }
        }
        
        return divisao;
    }
    
    public Map<Long, Map<String, Double>> calcularSaldosGrupo(Long grupoId) {
        if (grupoId == null) {
            throw new IllegalArgumentException("ID do grupo não pode ser nulo");
        }
        List<Despesa> despesas = findByGrupoId(grupoId);
        Map<Long, Map<String, Double>> saldos = new HashMap<>();
        
        if (despesas.isEmpty()) {
            return saldos;
        }
        
        // Obter todos os usuários únicos do grupo
        Set<Long> usuarios = new HashSet<>();
        for (Despesa despesa : despesas) {
            usuarios.add(despesa.getPagadorId());
            if (despesa.getParticipantesIds() != null) {
                usuarios.addAll(despesa.getParticipantesIds());
            }
        }
        
        // Inicializar saldos
        for (Long usuarioId : usuarios) {
            Map<String, Double> saldoUsuario = new HashMap<>();
            saldoUsuario.put("totalPago", 0.0);
            saldoUsuario.put("totalDevido", 0.0);
            saldoUsuario.put("saldoFinal", 0.0);
            saldos.put(usuarioId, saldoUsuario);
        }
        
        // Calcular totais pagos
        for (Despesa despesa : despesas) {
            Map<String, Double> saldoPagador = saldos.get(despesa.getPagadorId());
            saldoPagador.put("totalPago", saldoPagador.get("totalPago") + despesa.getValor());
        }
        
        // Calcular totais devidos
        for (Despesa despesa : despesas) {
            Map<Long, Double> divisao = calcularDivisao(despesa);
            for (Map.Entry<Long, Double> entry : divisao.entrySet()) {
                Map<String, Double> saldoParticipante = saldos.get(entry.getKey());
                if (saldoParticipante != null) {
                    saldoParticipante.put("totalDevido", 
                        saldoParticipante.get("totalDevido") + entry.getValue());
                }
            }
        }
        
        // Calcular saldos finais
        for (Map<String, Double> saldo : saldos.values()) {
            double saldoFinal = saldo.get("totalPago") - saldo.get("totalDevido");
            saldo.put("saldoFinal", saldoFinal);
        }
        
        return saldos;
    }
    
    public Map<String, Object> processarTodasDespesasGrupo(Long grupoId) {
        List<Despesa> despesas = findByGrupoId(grupoId);
        Map<String, Object> resultado = new HashMap<>();
        
        Map<String, Map<Long, Double>> divisoes = new HashMap<>();
        for (Despesa despesa : despesas) {
            divisoes.put(despesa.getId(), calcularDivisao(despesa));
        }
        
        Map<Long, Map<String, Double>> saldos = calcularSaldosGrupo(grupoId);
        
        resultado.put("despesas", despesas);
        resultado.put("divisoes", divisoes);
        resultado.put("saldos", saldos);
        resultado.put("totalDespesas", despesas.size());
        resultado.put("valorTotal", despesas.stream().mapToDouble(Despesa::getValor).sum());
        
        return resultado;
    }
    
    public Map<String, Object> obterEstatisticasGrupo(Long grupoId) {
        List<Despesa> despesas = findByGrupoId(grupoId);
        Map<String, Object> estatisticas = new HashMap<>();
        
        if (despesas.isEmpty()) {
            estatisticas.put("totalDespesas", 0);
            estatisticas.put("valorTotal", 0.0);
            estatisticas.put("valorMedio", 0.0);
            estatisticas.put("maiorDespesa", 0.0);
            estatisticas.put("menorDespesa", 0.0);
            return estatisticas;
        }
        
        double valorTotal = despesas.stream().mapToDouble(Despesa::getValor).sum();
        double valorMedio = valorTotal / despesas.size();
        double maiorDespesa = despesas.stream().mapToDouble(Despesa::getValor).max().orElse(0.0);
        double menorDespesa = despesas.stream().mapToDouble(Despesa::getValor).min().orElse(0.0);
        
        // Estatísticas por tipo de divisão
        Map<String, Long> porTipoDivisao = despesas.stream()
            .collect(Collectors.groupingBy(Despesa::getTipoDivisao, Collectors.counting()));
        
        // Usuário que mais gastou
        Map<Long, Double> gastosPorUsuario = new HashMap<>();
        for (Despesa despesa : despesas) {
            gastosPorUsuario.merge(despesa.getPagadorId(), despesa.getValor(), Double::sum);
        }
        
        Optional<Map.Entry<Long, Double>> maiorGastador = gastosPorUsuario.entrySet().stream()
            .max(Map.Entry.comparingByValue());
        
        estatisticas.put("totalDespesas", despesas.size());
        estatisticas.put("valorTotal", valorTotal);
        estatisticas.put("valorMedio", valorMedio);
        estatisticas.put("maiorDespesa", maiorDespesa);
        estatisticas.put("menorDespesa", menorDespesa);
        estatisticas.put("porTipoDivisao", porTipoDivisao);
        estatisticas.put("gastosPorUsuario", gastosPorUsuario);
        
        if (maiorGastador.isPresent()) {
            estatisticas.put("maiorGastadorId", maiorGastador.get().getKey());
            estatisticas.put("maiorGastadorValor", maiorGastador.get().getValue());
        }
        
        return estatisticas;
    }
}