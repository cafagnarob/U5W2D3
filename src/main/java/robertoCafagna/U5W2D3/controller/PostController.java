package robertoCafagna.U5W2D3.controller;

import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import robertoCafagna.U5W2D3.entities.Post;
import robertoCafagna.U5W2D3.exceptions.ValidationException;
import robertoCafagna.U5W2D3.payloads.PostResponsePayload;
import robertoCafagna.U5W2D3.payloads.PostsDTO;
import robertoCafagna.U5W2D3.services.PostService;

import java.util.stream.Collectors;

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
    public PostResponsePayload savePost(@Valid @RequestBody PostsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String error = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new ValidationException(error);
        }
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
    public Post findByIdAndUpdate(@PathVariable long postId, @Valid @RequestBody PostsDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String error = validationResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new ValidationException(error);
        }
        return this.postService.findByIdAndUpdate(postId, body);
    }

    //5.DELETE http://localhost:3001/posts/{postId} --> 204 NO CONTENT
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable long postId) {
        this.postService.findByIdAndDelete(postId);
    }

    //6. PATCH PICTURE http://localhost:3001/posts/{postId}/cover (+payload) --> 204
    @PatchMapping("/{postId}/cover")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void UpdateCover(@PathVariable Long postId, @RequestParam("cover_picture") MultipartFile file) {
        this.postService.updatePic(postId, file);
    }


}
