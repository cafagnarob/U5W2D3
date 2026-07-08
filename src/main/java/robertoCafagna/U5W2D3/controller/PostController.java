package robertoCafagna.U5W2D3.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import robertoCafagna.U5W2D3.entities.Post;
import robertoCafagna.U5W2D3.payloads.PostResponsePayload;
import robertoCafagna.U5W2D3.payloads.PostsPayload;
import robertoCafagna.U5W2D3.services.PostService;

@RestController
@RequestMapping("/{posts}")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 1. GET http://localhost:3001/posts?page=2&size=10&orderBy=nome --> 200 OK    ARRAY DI POST
    @GetMapping
    public Page<Post> getPost(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(defaultValue = "titolo") String orderBy) {
        return this.postService.getAll(page, size, orderBy);
    }

    // 2. POST http://localhost:3001/posts (+req.body) --> 201 CREATED    POST APPENA CREATO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public PostResponsePayload savePost(@RequestBody PostsPayload body) {
        Post saved = this.postService.save(body);
        return new PostResponsePayload(saved.getId());
    }

    // 3. GET http://localhost:3001/posts/{postId} --> 200 OK  POST TROVATO
    @GetMapping("/{postId}")
    public Post getById(@PathVariable Long postId) {
        return this.postService.findById(postId);
    }

    // 4. PUT http://localhost:3001/posts/{postId} (+payload) --> 200 OK  POST AGGIORNATO
    @PutMapping("/{postId}")
    public Post findByIdAndUpdate(@PathVariable long postId, @RequestBody PostsPayload body) {
        return this.postService.findByIdAndUpdate(postId, body);
    }

    //5.DELETE http://localhost:3001/posts/{postId} --> 204 NO CONTENT
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long postId) {
        this.postService.findByIdAndDelete(postId);
    }


}
