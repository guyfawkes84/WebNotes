package com.ap.webnotes.factory;

import com.ap.webnotes.dto.NotaDto;
import com.ap.webnotes.model.Nota;
import org.springframework.stereotype.Service;

@Service
public class PostNoteFactory {

    public Nota postNota(NotaDto dto) {
        return new Nota()
                .setId(dto.getId())
                .setTitolo(dto.getTitolo())
                .setContenuto(dto.getContenuto());

    }
}
