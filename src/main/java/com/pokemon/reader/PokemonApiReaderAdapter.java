package com.pokemon.reader;

import org.springframework.batch.item.ItemReader;

import java.util.List;

public class PokemonApiReaderAdapter implements ItemReader<List<Pokemon>> {

    private final PokemonApiReader delegate;

    public PokemonApiReaderAdapter(PokemonApiReader delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Pokemon> read() throws Exception {
        return delegate.read();
    }
}
