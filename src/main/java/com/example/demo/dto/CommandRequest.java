package com.example.demo.dto;

/**
 * DTO de entrada para POST /api/v1/commands.
 * sessionId referencia a un GameSession ya existente.
 */
public record CommandRequest(Long sessionId, String actionType, String payload) {
}
