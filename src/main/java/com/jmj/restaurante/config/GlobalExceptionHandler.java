package com.jmj.restaurante.config;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {



    // Erro genérico (qualquer outro)
    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
        model.addAttribute("message",
            "Não é possível excluir este registro porque ele está em uso.");
        return "error"; // usa a mesma página
    }
}
