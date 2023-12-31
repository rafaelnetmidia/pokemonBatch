package com.pokemon.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@StepScope
@Component
public class PokemonApiReader implements ItemReader<List<Pokemon>> {

    private static final String API_URL = "https://pokeapi.co/api/v2/pokemon?limit=1000000&offset=0";

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public List<Pokemon> read() throws Exception {
        String jsonResponse = restTemplate.getForObject(API_URL, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> pokemonData = objectMapper.readValue(jsonResponse, Map.class);

        if (pokemonData.containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) pokemonData.get("results");

            return results.stream()
                    .map(result -> {
                        Pokemon pokemon = new Pokemon();
                        pokemon.setName((String) result.get("name"));
                        pokemon.setUrl((String) result.get("url"));
                        return pokemon;
                    })
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
