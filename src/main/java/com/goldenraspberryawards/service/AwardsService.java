package com.goldenraspberryawards.service;

import com.goldenraspberryawards.dto.AwardInterval;
import com.goldenraspberryawards.dto.AwardIntervalResponse;
import com.goldenraspberryawards.model.Movie;
import com.goldenraspberryawards.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class AwardsService {

    @Autowired
    private MovieRepository repository;

    @PostConstruct
    public void loadMoviesFromCsv() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/movielist_(13).csv"))) {
            String line;
            reader.readLine(); // Ignorar a primeira linha (cabecalho)
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(";");

                // Garantir que temos pelo menos as 4 primeiras colunas (ano, titulo, estudios, produtores)
                if (columns.length < 4) {
                    System.out.println("Linha invalida ou incompleta: " + line);
                    continue;  // Ignorar linhas incompletas
                }

                int year;
                try {
                    year = Integer.parseInt(columns[0]);  // Garantir que o campo 'year' seja valido
                } catch (NumberFormatException e) {
                    System.out.println("Ano invalido na linha: " + line);
                    continue;  // Ignorar linhas com ano invalido
                }

                String title = columns[1].trim();
                String producers = columns[3].trim();

                // Verificar se a coluna 'winner' existe e tratar como false se estiver ausente ou vazia
                boolean winner = columns.length >= 5 && "yes".equalsIgnoreCase(columns[4].trim());

                // Processar multiplos produtores (separados por ", ")
                for (String producer : producers.split(", ")) {
                    producer = producer.trim();  // Remover espacos extras

                    if (!producer.isEmpty()) {  // Validar se o produtor não esta vazio
                        Movie movie = new Movie();
                        movie.setReleaseYear(year);
                        movie.setTitle(title);
                        movie.setProducer(producer);
                        movie.setWinner(winner);
                        repository.save(movie);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public AwardIntervalResponse calculateAwardIntervals() {
        List<Movie> winners = repository.findByWinnerTrue();
        Map<String, List<Integer>> producerWins = new HashMap<>();

        // Agrupar anos de vitória por produtor
        for (Movie movie : winners) {
            producerWins.computeIfAbsent(movie.getProducer(), k -> new ArrayList<>()).add(movie.getReleaseYear());
        }

        List<AwardInterval> minIntervals = new ArrayList<>();
        List<AwardInterval> maxIntervals = new ArrayList<>();

        // Processar intervalos para cada produtor
        for (Map.Entry<String, List<Integer>> entry : producerWins.entrySet()) {
            if (entry.getValue().size() > 1) {  // Apenas processar produtores com mais de uma vitória
                processProducerIntervals(entry.getKey(), entry.getValue(), minIntervals, maxIntervals);
            }
        }

        // Retornar os produtores com menores e maiores intervalos sem duplicação
        return new AwardIntervalResponse(minIntervals, maxIntervals);
    }
    private void processProducerIntervals(String producer, List<Integer> winYears,
                                          List<AwardInterval> minIntervals,
                                          List<AwardInterval> maxIntervals) {
        Collections.sort(winYears);  // Ordenar os anos de vitória

        int minInterval = Integer.MAX_VALUE;
        int maxInterval = Integer.MIN_VALUE;
        AwardInterval minIntervalRecord = null;
        AwardInterval maxIntervalRecord = null;

        // Percorrer os anos de vitória e calcular os intervalos
        for (int i = 1; i < winYears.size(); i++) {
            int interval = winYears.get(i) - winYears.get(i - 1);
            int previousWin = winYears.get(i - 1);
            int followingWin = winYears.get(i);

            // Verificar o menor intervalo
            if (interval < minInterval) {
                minInterval = interval;
                minIntervalRecord = new AwardInterval(producer, interval, previousWin, followingWin);
            }

            // Verificar o maior intervalo
            if (interval > maxInterval) {
                maxInterval = interval;
                maxIntervalRecord = new AwardInterval(producer, interval, previousWin, followingWin);
            }
        }

        // Atualizar lista de mínimos intervalos
        if (minIntervalRecord != null) {
            if (minIntervals.isEmpty() || minIntervalRecord.getInterval() < minIntervals.get(0).getInterval()) {
                minIntervals.clear();
                minIntervals.add(minIntervalRecord);
            } else if (minIntervalRecord.getInterval() == minIntervals.get(0).getInterval()) {
                minIntervals.add(minIntervalRecord);
            }
        }

        // Atualizar lista de máximos intervalos
        if (maxIntervalRecord != null) {
            if (maxIntervals.isEmpty() || maxIntervalRecord.getInterval() > maxIntervals.get(0).getInterval()) {
                maxIntervals.clear();
                maxIntervals.add(maxIntervalRecord);
            } else if (maxIntervalRecord.getInterval() == maxIntervals.get(0).getInterval()) {
                maxIntervals.add(maxIntervalRecord);
            }
        }
    }
}
