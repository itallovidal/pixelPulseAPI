package api.pixelPulse.pixelPulse.controllers;

import api.pixelPulse.pixelPulse.DTOs.DTOLogin;
import api.pixelPulse.pixelPulse.DTOs.DTOUser;
import api.pixelPulse.pixelPulse.entities.Database;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// endpoint
@RequestMapping("/users")
@RestController()
public class UsersController {


    // HTTP METHOD
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody DTOLogin loginData){
        Database db = new Database();

        try {
            DTOUser user = db.getUser(loginData.email(), loginData.password());
            return ResponseEntity.status(200).body(user);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(404).body("Não conseguimos encontrar o usuário requisitado.");
        }
    }

    // HTTP METHOD
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody DTOUser user){
        Database db = new Database();

        try {
            db.register(user);
            return ResponseEntity.status(201).body("Usuário criado com sucesso.");

        }catch (Exception e){
            return ResponseEntity.status(500).body("Ocorreu um erro. Tente novamente mais tarde.");
        }
    }
}
