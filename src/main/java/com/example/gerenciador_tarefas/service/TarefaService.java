package com.example.gerenciador_tarefas.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gerenciador_tarefas.model.Tarefa;
import com.example.gerenciador_tarefas.model.Usuario;
import com.example.gerenciador_tarefas.repository.TarefaRepository;
import com.example.gerenciador_tarefas.repository.UsuarioRepository;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Cadastrar Tarefa (com Criador e já atribuindo usuários)
    public Tarefa cadastrar(Tarefa tarefa, Long criadorId) {
        Optional<Usuario> criadorOpt = usuarioRepository.findById(criadorId);
        
        if (criadorOpt.isPresent()) {
            tarefa.setCriador(criadorOpt.get()); // Define o dono
            tarefa.setStatus("PENDENTE"); 
            
            // Verifica se já vieram usuários para compartilhar na hora da criação
            if (tarefa.getUsuarios() != null && !tarefa.getUsuarios().isEmpty()) {
                Set<Usuario> usuariosAtribuidos = new HashSet<>();
                
                for (Usuario usuarioEnviado : tarefa.getUsuarios()) {
                    usuarioRepository.findById(usuarioEnviado.getId())
                                    .ifPresent(usuariosAtribuidos::add);
                }
                tarefa.setUsuarios(usuariosAtribuidos);
            }
            
            return tarefaRepository.save(tarefa);
        }
        return null;
    }

    // 2. Buscar Tarefa por ID
    public Optional<Tarefa> buscarPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    // 3. Listar Todas as Tarefas (O método que estava faltando!)
    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    // 4. Atualizar Status (O outro método que estava faltando!)
    public Tarefa atualizarStatus(Long id, String status) {
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(id);
        
        if (tarefaOpt.isPresent()) {
            Tarefa tarefa = tarefaOpt.get();
            tarefa.setStatus(status);
            return tarefaRepository.save(tarefa);
        }
        return null;
    }

    public Tarefa compartilharTarefa(Long tarefaId, Long usuarioId) {
        Optional<Tarefa> tarefaOpt = tarefaRepository.findById(tarefaId);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);

        // Se tanto a tarefa quanto o usuário existirem no banco
        if (tarefaOpt.isPresent() && usuarioOpt.isPresent()) {
            Tarefa tarefa = tarefaOpt.get();
            Usuario usuario = usuarioOpt.get();
            
            // Adiciona o usuário na lista da tarefa e salva
            tarefa.getUsuarios().add(usuario);
            return tarefaRepository.save(tarefa);
        }
        return null; // Retorna null se não achar a tarefa ou o usuário
    }
}