package robertoCafagna.U5W2D3.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import robertoCafagna.U5W2D3.entities.Post;
import robertoCafagna.U5W2D3.entities.User;
import robertoCafagna.U5W2D3.exceptions.BadRequestException;
import robertoCafagna.U5W2D3.exceptions.NotFoundException;
import robertoCafagna.U5W2D3.exceptions.ValidationException;
import robertoCafagna.U5W2D3.payloads.UsersDTO;
import robertoCafagna.U5W2D3.repositories.PostsRepository;
import robertoCafagna.U5W2D3.repositories.UsersRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Slf4j
public class UsersService {
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;
    private final Cloudinary fileUploader;


    public UsersService(UsersRepository usersRepository, PostsRepository postsRepository, Cloudinary fileUploader) {
        this.usersRepository = usersRepository;
        this.postsRepository = postsRepository;
        this.fileUploader = fileUploader;
    }

    public User save(UsersDTO body) {
        // 1. Verifichiamo che l'email non sia già in uso e altri controlli
        if (this.usersRepository.existsByEmail(body.email())) {
            throw new BadRequestException("L'indirizzo email " + body.email() + " è già utilizzato!");
        }
        //2. creiamo il nuovo oggetto User con i valori del body
        User newUser = new User(body.nome(), body.cognome(), body.email(), body.dataDiNascita());
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

    public User findByIdAndUpdate(Long userId, UsersDTO body) {
        User found = this.findById(userId);

        //controlli sull'email
        if (!found.getEmail().equals(body.email()))
            if (this.usersRepository.existsByEmail(body.email()))
                throw new BadRequestException("L'indirizzo email " + body.email() + " è già utilizzato!");

        found.setNome(body.nome());
        found.setCognome(body.cognome());
        found.setDataDiNascita(body.dataDiNascita());
        found.setEmail(body.email());

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

    public void updatePic(Long userId, MultipartFile file) {
        // 1. Controlli vari tipo file non più grande di tot, tipo di file permesso solo GIF
        if (file.isEmpty()) {
            throw new ValidationException("inserire un file");
        }
        if (file.getSize() >= 10 * 1024 * 1024) {
            throw new ValidationException("file di dimensione troppo grande, inserire un file di massimo 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/gif"))) {

            throw new ValidationException("Formato file non supportato");
        }
        // 2. Find by id dell'utente
        Optional<User> foud = this.usersRepository.findById(userId);
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId));
        // 3. Upload del file su Cloudinary
        try {
            Map res = fileUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = res.get("secure_url").toString();
            System.out.println(url);
            // 4. Se l'upload va a buon fine, Cloudinary ci restituirà l'url dell'immagine.
            // save dell'utente
            user.setAvatar(url);
            usersRepository.save(user);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }


}
