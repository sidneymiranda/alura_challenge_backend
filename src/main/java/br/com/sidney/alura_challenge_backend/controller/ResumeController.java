package br.com.sidney.alura_challenge_backend.controller;

import br.com.sidney.alura_challenge_backend.service.ResumeService;
import br.com.sidney.alura_challenge_backend.dto.ResumeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resume")
public class ResumeController {

    private final ResumeService service;

    @Autowired
    public ResumeController(ResumeService service) {
        this.service = service;
    }

    @GetMapping(value = "/{year}/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResumeResponse> resumeMonth(@PathVariable Integer year, @PathVariable Integer month) {
        return ResponseEntity.ok().body(this.service.getResume(year, month));
    }
}
