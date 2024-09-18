package infnet.pb.rss_bank.controller;

import infnet.pb.rss_bank.model.Usuario;
import infnet.pb.rss_bank.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public Usuario registrar(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // Aqui você pode implementar o login e retornar um JWT ou uma mensagem de sucesso
        return "Login realizado com sucesso";
    }
}
