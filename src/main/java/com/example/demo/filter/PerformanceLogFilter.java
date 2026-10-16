package com.example.demo.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filtro CRITICO para la rubrica del laboratorio.
 * Intercepta TODAS las peticiones HTTP y evidencia en consola:
 *  - metodo HTTP y URI
 *  - codigo de respuesta
 *  - tiempo exacto de procesamiento (ms)
 *  - memoria JVM consumida durante la peticion (bytes/KB), calculada
 *    como la diferencia de memoria usada antes y despues de chain.doFilter().
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PerformanceLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Runtime runtime = Runtime.getRuntime();

        String metodo = request.getMethod();
        String uri = request.getRequestURI();

        // Memoria usada justo antes de procesar la peticion
        long memoriaAntes = runtime.totalMemory() - runtime.freeMemory();
        long inicio = System.currentTimeMillis();

        try {
            chain.doFilter(servletRequest, servletResponse);
        } finally {
            long fin = System.currentTimeMillis();
            // Memoria usada justo despues de procesar la peticion
            long memoriaDespues = runtime.totalMemory() - runtime.freeMemory();

            long tiempoMs = fin - inicio;
            long memoriaDiffBytes = memoriaDespues - memoriaAntes;
            double memoriaDiffKb = memoriaDiffBytes / 1024.0;
            int codigoRespuesta = response.getStatus();

            System.out.println("================ [PERFORMANCE LOG] ================");
            System.out.println(" Metodo HTTP      : " + metodo);
            System.out.println(" URI solicitada    : " + uri);
            System.out.println(" Codigo respuesta  : " + codigoRespuesta);
            System.out.println(" Tiempo procesado  : " + tiempoMs + " ms");
            System.out.println(" Memoria JVM usada : " + memoriaDiffBytes + " bytes (" + String.format("%.2f", memoriaDiffKb) + " KB)");
            System.out.println("=====================================================");
        }
    }
}
