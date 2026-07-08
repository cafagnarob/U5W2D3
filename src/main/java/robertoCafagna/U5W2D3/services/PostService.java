package robertoCafagna.U5W2D3.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import robertoCafagna.U5W2D3.entities.Post;
import robertoCafagna.U5W2D3.entities.User;
import robertoCafagna.U5W2D3.exceptions.NotFoundException;
import robertoCafagna.U5W2D3.payloads.PostsPayload;
import robertoCafagna.U5W2D3.repositories.PostsRepository;

@Slf4j
@Service
public class PostService {
    private final PostsRepository postsRepository;
    private final UsersService usersService;

    public PostService(PostsRepository postsRepository, UsersService usersService) {
        this.postsRepository = postsRepository;
        this.usersService = usersService;
    }


    public Post save(PostsPayload body) {
        User userFromDB = this.usersService.findById(body.getUserId());

        //1. creiamo il nuovo oggetto Post con i valori del body
        Post newPost = new Post(body.getCategoria(), body.getTitolo(), body.getContenuto(), body.getMinutiDiLettura(), userFromDB);
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

    public Post findByIdAndUpdate(Long postId, PostsPayload body) {
        Post found = this.findById(postId);
        //controlli sull'email

        found.setContenuto(body.getContenuto());
        found.setTitolo(body.getTitolo());
        found.setCategoria(body.getCategoria());
        found.setMinutiDiLettura(body.getMinutiDiLettura());

        Post UpdatePost = this.postsRepository.save(found);

        return UpdatePost;
    }


    public void findByIdAndDelete(Long userId) {
        Post found = this.findById(userId);
        this.postsRepository.delete(found);
    }


}
