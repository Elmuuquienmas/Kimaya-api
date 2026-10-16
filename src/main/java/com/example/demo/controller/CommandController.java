package com.example.demo.controller;

import com.example.demo.dto.CommandRequest;
import com.example.demo.model.CommandLog;
import com.example.demo.model.GameSession;
import com.example.demo.repository.CommandLogRepository;
import com.example.demo.repository.GameSessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Endpoint que consume la PWA (Gamepad.jsx / ControllerPage.jsx) para registrar
 * cada comando enviado desde el mando.
 */
@RestController
@RequestMapping("/api/v1/commands")
@CrossOrigin(origins = "*")
public class CommandController {

    private final CommandLogRepository commandLogRepository;
    private final GameSessionRepository gameSessionRepository;

    public CommandController(CommandLogRepository commandLogRepository,
                              GameSessionRepository gameSessionRepository) {
        this.commandLogRepository = commandLogRepository;
        this.gameSessionRepository = gameSessionRepository;
    }

    @PostMapping
    public ResponseEntity<CommandLog> registrarComando(@RequestBody CommandRequest request) {
        Optional<GameSession> sesionExistente = gameSessionRepository.findById(request.sessionId());

        if (sesionExistente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        CommandLog commandLog = new CommandLog();
        commandLog.setGameSession(sesionExistente.get());
        commandLog.setActionType(request.actionType());
        commandLog.setPayload(request.payload());
        commandLog.setExecutedAt(LocalDateTime.now());

        CommandLog guardado = commandLogRepository.save(commandLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping
    public ResponseEntity<List<CommandLog>> listarComandos() {
        return ResponseEntity.ok(commandLogRepository.findAll());
    }
}
