package com.goldenraspberryawards;

import com.goldenraspberryawards.model.Movie;
import com.goldenraspberryawards.repository.MovieRepository;
import com.goldenraspberryawards.service.AwardsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc   // Isso garante que MockMvc seja configurado automaticamente
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)  // Garantir um contexto limpo antes de cada teste
public class AwardsServiceIntegrationTest {

    @Autowired
    private AwardsService awardsService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    public void shouldReturnCorrectMinAndMaxIntervals() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/awards/intervals")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min[0].interval").value(1))  // Min esperado
                .andExpect(jsonPath("$.max[0].interval").value(13));  // Max esperado
    }
}
