package com.goldenraspberryawards;

import com.goldenraspberryawards.model.Movie;
import com.goldenraspberryawards.repository.MovieRepository;
import com.goldenraspberryawards.service.AwardsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)  // Garantir um contexto limpo antes de cada teste
public class AwardsServiceIntegrationTest {

    @Autowired
    private AwardsService awardsService;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void testLoadMoviesFromCsv() {
        // Testa se os filmes sao carregados corretamente do CSV
        List<Movie> movies = movieRepository.findAll();
        assertFalse(movies.isEmpty(), "O banco de dados nao deve estar vazio apos carregar o CSV");
    }

    @Test
    public void testCalculateAwardIntervals() {
        // Testa se o servico de calculo dos intervalos retorna o valor correto
        awardsService.loadMoviesFromCsv();  // Carregar os filmes
        var result = awardsService.calculateAwardIntervals();
        assertNotNull(result, "O resultado nao deve ser nulo");
        assertFalse(result.getMin().isEmpty(), "Deve retornar pelo menos um produtor com o menor intervalo");
        assertFalse(result.getMax().isEmpty(), "Deve retornar pelo menos um produtor com o maior intervalo");
    }
}
