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
import robertoCafagna.U5W2D3.exceptions.NotFoundException;
import robertoCafagna.U5W2D3.exceptions.ValidationException;
import robertoCafagna.U5W2D3.payloads.PostsDTO;

import robertoCafagna.U5W2D3.repositories.PostsRepository;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PostService {
    private final PostsRepository postsRepository;
    private final UsersService usersService;
    private final Cloudinary fileUploader;

    public PostService(PostsRepository postsRepository, UsersService usersService, Cloudinary fileUploader) {
        this.postsRepository = postsRepository;
        this.usersService = usersService;
        this.fileUploader = fileUploader;
    }


    public Post save(PostsDTO body) {
        User userFromDB = this.usersService.findById(body.userId());

        //1. creiamo il nuovo oggetto Post con i valori del body
        Post newPost = new Post(body.categoria(), body.titolo(), body.contenuto(), body.minutiDiLettura(), userFromDB);
        //2.salvo
        Post savedPost = this.postsRepository.save(newPost);
        //3. log
        log.info("il POST " + savedPost.getId() + " salvato");
        return savedPost;
    }


    public Page<Post> getAll(int page, int size, String orderBy) {
        if (size > 20) size = 20;
        if (size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return this.postsRepository.findAll(pageable);
    }

    public Post findById(Long postId) {
        return this.postsRepository.findById(postId).orElseThrow(() -> new NotFoundException(postId));
    }

    public Post findByIdAndUpdate(Long postId, PostsDTO body) {
        Post found = this.findById(postId);
        //controlli sull'email

        found.setContenuto(body.contenuto());
        found.setTitolo(body.titolo());
        found.setCategoria(body.categoria());
        found.setMinutiDiLettura(body.minutiDiLettura());

        Post UpdatePost = this.postsRepository.save(found);

        return UpdatePost;
    }


    public void findByIdAndDelete(Long userId) {
        Post found = this.findById(userId);
        this.postsRepository.delete(found);
    }

    public void updatePic(Long postId, MultipartFile file) {
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
        // 2. Find by id del post
        Optional<Post> foud = this.postsRepository.findById(postId);
        Post post = postsRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(postId));
        // 3. Upload del file su Cloudinary
        try {
            Map res = fileUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = res.get("secure_url").toString();
            System.out.println(url);
            // 4. Se l'upload va a buon fine, Cloudinary ci restituirà l'url dell'immagine.
            // Save del post
            post.setCover(url);
            postsRepository.save(post);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}
