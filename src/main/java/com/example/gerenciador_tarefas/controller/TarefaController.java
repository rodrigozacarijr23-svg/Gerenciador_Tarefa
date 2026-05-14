package com.example.gerenciador_tarefas.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gerenciador_tarefas.model.Tarefa;
import com.example.gerenciador_tarefas.service.TarefaService;

@RestController
@RequestMapping("/api/tarefas")
@CrossOrigin("*")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @PostMapping("/criador/{criadorId}")
    public ResponseEntity<Tarefa> cadastrar(@PathVariable Long criadorId, @RequestBody Tarefa tarefa) {
        Tarefa novaTarefa = tarefaService.cadastrar(tarefa, criadorId);
        
        if (novaTarefa != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
        }
        return ResponseEntity.badRequest().build(); 
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        return ResponseEntity.ok(tarefaService.listarTodas());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Tarefa> atualizarStatus(@PathVariable Long id, @RequestBody String novoStatus) {
        Tarefa tarefaAtualizada = tarefaService.atualizarStatus(id, novoStatus);
        if (tarefaAtualizada != null) {
            return ResponseEntity.ok(tarefaAtualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{tarefaId}/compartilhar/{usuarioId}")
    public ResponseEntity<Tarefa> compartilharTarefa(@PathVariable Long tarefaId, @PathVariable Long usuarioId) {
        Tarefa tarefaCompartilhada = tarefaService.compartilharTarefa(tarefaId, usuarioId);
        if (tarefaCompartilhada != null) {
            return ResponseEntity.ok(tarefaCompartilhada);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarTarefaPorId(@PathVariable Long id) {
        Optional<Tarefa> tarefa = tarefaService.buscarPorId(id);
        
        // Se a tarefa existir, retorna 200 (OK). Se não, retorna 404 (Not Found)
        return tarefa.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }
}