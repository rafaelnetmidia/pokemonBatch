package com.pokemon.writer;

import com.pokemon.reader.Pokemon;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

@StepScope
@Component
public class PokemonFileWriter implements ItemWriter<Map<String, Object>> {

    private static final String FILE_PATH = "C:\\teste\\pokemon_data.txt";

    public void write(Chunk<? extends Map<String, Object>> items) throws Exception {

        List<Pokemon> listaDeItens = (List<Pokemon>) items.getItems().get(0);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            int totalPokemons = 0;

            for (Pokemon item : listaDeItens) {

                writer.write("name: " + item.getName() + ", url: " + item.getUrl() + "\n");
                totalPokemons++;
            }

            writer.write("Total: " + totalPokemons + "\n");
        }
    }
}
