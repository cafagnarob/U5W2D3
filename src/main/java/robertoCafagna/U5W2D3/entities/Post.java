package robertoCafagna.U5W2D3.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    @Setter
    private String titolo;

    @Column(nullable = false)
    @Setter
    private String cover;

    @Column(nullable = false)
    @Setter
    private String contenuto;

    @Column(nullable = false)
    @Setter
    private Integer minutiDiLettura;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Post(String categoria, String titolo, String contenuto, Integer minutiDiLettura, User user) {

        this.categoria = categoria;
        this.titolo = titolo;
        this.cover = "https://picsum.photos/200/300";
        this.contenuto = contenuto;
        this.minutiDiLettura = minutiDiLettura;
        this.user = user;
    }


}
