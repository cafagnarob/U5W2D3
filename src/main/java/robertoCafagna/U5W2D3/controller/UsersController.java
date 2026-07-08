package robertoCafagna.U5W2D3.controller;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import robertoCafagna.U5W2D3.entities.User;
import robertoCafagna.U5W2D3.payloads.UserResponsePayload;
import robertoCafagna.U5W2D3.payloads.UsersPayload;
import robertoCafagna.U5W2D3.services.UsersService;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    // 1. GET http://localhost:3001/users?page=2&size=10&orderBy=nome --> 200 OK    ARRAY DI UTENTI
    @GetMapping
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               @RequestParam(defaultValue = "cognome") String orderBy) {
        return this.usersService.getAll(page, size, orderBy);
    }

    // 2. POST http://locahost:3001/users (+req.body) --> 201 CREATED    ID UTENTE APPENA CREATO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public UserResponsePayload saveUser(@RequestBody UsersPayload body) {
        User saved = this.usersService.save(body);
        return new UserResponsePayload(saved.getId());
    }

    // 3. GET http://locahost:3001/users/{userId} --> 200 OK  UTENTE TROVATO
    @GetMapping("/{userId}")
    public User getById(@PathVariable Long userId) {
        return this.usersService.findById(userId);
    }

    // 4. PUT http://localhost:3001/users/{userId} (+payload) --> 200 OK  UTENTE AGGIORNATO
    @PutMapping("/{userId}")
    public User findByIdAndUpdate(@PathVariable long userId, @RequestBody UsersPayload body) {
        return this.usersService.findByIdAndUpdate(userId, body);
    }

    //5.DELETE http://localhost:PORT/users/{userId} --> 204 NO CONTENT
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long userId) {
        this.usersService.findByIdAndDelete(userId);
    }


    @GetMapping("/test")
    public String test() {
        return "CIAO";
    }
}
