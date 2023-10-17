package vnu.uet.moonbe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vnu.uet.moonbe.dto.APIResponse;
import vnu.uet.moonbe.services.StorageService;

@RestController
@RequestMapping("/files")
public class FileController {


    private final StorageService storageService;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/new")
    public ResponseEntity<APIResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileId = storageService.store(file);

        return ResponseEntity.ok(APIResponse.of(fileId));
    }

    @GetMapping(value = "/{filename:.+}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();


        return ResponseEntity.ok().body(file);
    }


}
