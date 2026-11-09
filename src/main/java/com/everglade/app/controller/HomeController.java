package com.everglade.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Mengarahkan halaman pembuka ke mockup login sebelum masuk aplikasi.
 *
 * <p>Mockup autentikasi (ADR-0005): tanpa backend auth, halaman ini hanya memutar
 * alur {@code / -> /login.html -> /index.html}. Tanpa controller ini, Spring Boot
 * menyajikan {@code static/index.html} sebagai welcome page sehingga login dilewati.</p>
 */
@Controller
public class HomeController {

    /**
     * Mengarahkan pembuka aplikasi ke halaman login.
     *
     * @return redirect ke {@code /login.html}
     */
    @GetMapping("/")
    public String halaman() {
        return "redirect:/login.html";
    }
}
