package com.ap.WebNotes.controller;


import com.ap.WebNotes.dto.NotaDto;
import com.ap.WebNotes.model.IDs;
import com.ap.WebNotes.model.Nota;
import com.ap.WebNotes.service.implementations.NoteServiceImpl;
import com.ap.WebNotes.utils.enums.CodAzioneEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.UtilsClass;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/web-notes", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebNotesController extends UtilsClass {

    @Autowired
    private NoteServiceImpl noteService;

    @ApiOperation("Api che restituisce una lista di note")
    @GetMapping("/notes")
    public ResponseEntity<List<Nota>> getNote(
            @RequestParam(value = "mock", required = false, defaultValue = "false") Boolean mock
    ) {

        if (Boolean.TRUE.equals(mock)) {
            logger.info("Fine chiamata servizio home mock -> {}", mock);
            //SETTING MOCK TO DO
            return ResponseEntity.ok(Collections.singletonList(new Nota()));
        }
        logger.info("Inizio chiamata al servizio home");
        List<Nota> listaNote = noteService.getAll();
        return ResponseEntity.ok(listaNote);
    }

    @ApiOperation("Api che permette di inserire una nota")
    @PostMapping("/notes")
    public ResponseEntity<String> postNota(
            @RequestParam("codAzione") CodAzioneEnum codAzione,
            @RequestParam(value = "mock", required = false, defaultValue = "false") Boolean mock,
            @RequestBody NotaDto dto
    ) {
        if (Boolean.TRUE.equals(mock)) {
            logger.info("Fine chiamata servizio postNota, mock -> {}", mock);
        }

        Nota nota = new Nota()
                .setId(dto.getId())
                .setTitolo(dto.getTitolo())
                .setContenuto(dto.getContenuto());
        String message = null;
        logger.info("Inizio chiamata servizio postNota, codAzione -> {}", codAzione);
        if (!nota.getContenuto().isEmpty() &&
                !nota.getTitolo().isEmpty()) {
            noteService.saveNota(nota);
            List<Nota> listaNote = noteService.getAll();
            if (!listaNote.isEmpty()) {
                message = "OK";
                return ResponseEntity.ok(message);
            } else {
                message = "KO";
                return ResponseEntity.ok(message);
            }
        } else {
            message = "KO";
            return ResponseEntity.ok(message);
        }
    }

    @ApiOperation("Api che permette di ricercare una nota per ID")
    @GetMapping("/notes/{id}")
    public ResponseEntity<Nota> getNota(
            @PathVariable("id") Integer id,
            @RequestParam(value = "mock", required = false, defaultValue = "false") Boolean mock
    ) {
        if (Boolean.TRUE.equals(mock))
            logger.info("Fine chiamata servizio getNota mock -> {}", mock);
        logger.info("Inizio chiamata servizio getNota");
        if (id != null) {
            Optional<Nota> notaResult = noteService.findById(id);
            if (notaResult.isPresent()) {
                return ResponseEntity.ok(notaResult.get());
            } else {
                ResponseEntity.ok(new Nota());
            }
        }
        return ResponseEntity.ok(new Nota());
    }


    @ApiOperation("Api che permette di modificare una determinata nota")
    @PutMapping("/notes/{id}")
    public ResponseEntity<String> putNote(
            @RequestBody NotaDto dto,
            @PathVariable("id") Integer id,
            @RequestParam(value = "mock", required = false, defaultValue = "false") Boolean mock
    ) {
        if (Boolean.TRUE.equals(mock))
            logger.info("Fine chiamata servizio putNote mock -> {}", mock);

        Nota nota = new Nota()
                .setId(dto.getId())
                .setTitolo(dto.getTitolo())
                .setContenuto(dto.getContenuto());
        logger.info("Inizio chiamata servizio putNote");
        String message = null;
        if (id != null &&
                noteService.findById(id).isPresent() &&
                nota != null) {
            noteService.saveNota(nota);
            message = "OK";
            return ResponseEntity.ok(message);
        } else {
            message = "KO";
            return ResponseEntity.ok(message);
        }

    }

    @ApiOperation("Api che permette di eliminate una determinata nota")
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<String> deleteNota(
            @PathVariable("id") Integer id,
            @RequestParam(value = "mock", required = false, defaultValue = "false") Boolean mock
    ) {
        if (Boolean.TRUE.equals(mock))
            logger.info("Fine chiamata servizio deleteNota mock -> {}", mock);

        logger.info("Inizio chiamata servizio deleteNota");
        String message = null;
        if (id != null) {
            Optional<Nota> foundId = noteService.findById(id);
            if (foundId.isPresent()) {
                noteService.delete(id);
                message = "OK";
            } else {
                message = "KO";
            }
            return ResponseEntity.ok(message);
        }
        message = "ID is null";
        return ResponseEntity.ok(message);
    }

    @ApiOperation("Api che permette di eliminare n note")
    @DeleteMapping("/notes")
    public ResponseEntity<String> deleteNotes(
            @RequestBody IDs dto,
            @RequestParam(value = "mock", required = false, defaultValue = "false") Boolean mock
    ) {
        if (Boolean.TRUE.equals(mock))
            logger.info("Fine chiamata servizio deleteNotes mock -> {}", mock);

        logger.info("Inizio chiamata al servizio deleteNotes");
        if (dto != null) {
            for (Integer id : dto.getListIds()) {
                noteService.delete(id);
            }
        } else {
            return ResponseEntity.ok("KO");
        }
        return ResponseEntity.ok("OK");
    }
}