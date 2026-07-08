package robertoCafagna.U5W2D3.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W2D3.entities.Post;
import robertoCafagna.U5W2D3.entities.User;
import robertoCafagna.U5W2D3.exceptions.BadRequestException;
import robertoCafagna.U5W2D3.exceptions.NotFoundException;
import robertoCafagna.U5W2D3.payloads.UsersPayload;
import robertoCafagna.U5W2D3.repositories.PostsRepository;
import robertoCafagna.U5W2D3.repositories.UsersRepository;

import java.util.List;

@Service
@Slf4j
public class UsersService {
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    public UsersService(UsersRepository usersRepository, PostsRepository postsRepository) {
        this.usersRepository = usersRepository;
        this.postsRepository = postsRepository;
    }

    public User save(UsersPayload body) {
        // 1. Verifichiamo che l'email non sia già in uso e altri controlli
        if (this.usersRepository.existsByEmail(body.getEmail())) {
            throw new BadRequestException("L'indirizzo email " + body.getEmail() + " è già utilizzato!");
        }
        //2. creiamo il nuovo oggetto User con i valori del body
        User newUser = new User(body.getNome(), body.getCognome(), body.getEmail(), body.getDataDiNascita());
        //4.salvo
        User savedUser = this.usersRepository.save(newUser);
        //5. log
        log.info("Utente " + savedUser.getId() + " salvato");
        return savedUser;
    }

    public Page<User> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.usersRepository.findAll(pageable);
    }

    public User findById(Long userId) {
        return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByIdAndUpdate(Long userId, UsersPayload body) {
        User found = this.findById(userId);

        //controlli sull'email
        if (!found.getEmail().equals(body.getEmail()))
            if (this.usersRepository.existsByEmail(body.getEmail()))
                throw new BadRequestException("L'indirizzo email " + body.getEmail() + " è già utilizzato!");

        found.setNome(body.getNome());
        found.setCognome(body.getCognome());
        found.setDataDiNascita(body.getDataDiNascita());
        found.setEmail(body.getEmail());

        User UpdateUser = this.usersRepository.save(found);

        return UpdateUser;
    }

    public void findByIdAndDelete(Long userId) {
        User found = this.findById(userId);
        this.usersRepository.delete(found);
    }

    public List<Post> getPostsByUser(Long userId) {
        return postsRepository.findByUser_Id(userId);
    }


}
