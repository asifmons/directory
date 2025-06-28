package com.stjude.directory.controller;

import com.stjude.directory.dto.ApiResponse;
import com.stjude.directory.dto.HighlightPreviewResponse;
import com.stjude.directory.dto.PagedResponse;
import com.stjude.directory.service.HighlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequestMapping("/api/highlights")
@RestController
public class HighlightController {

    private final HighlightService highlightService;

    public HighlightController(HighlightService highlightService) {
        this.highlightService = highlightService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createHighlight(
            @RequestParam("name") String name,
            @RequestParam("images") List<MultipartFile> images) {

        try {
            String highlightId = highlightService.createHighlight(name, images);
            return ResponseEntity.ok(ApiResponse.success("Highlight created successfully", highlightId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create highlight"));
        }
    }

    @GetMapping("/preview")
    public ResponseEntity<ApiResponse<PagedResponse<HighlightPreviewResponse>>> getHighlightPreviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            PagedResponse<HighlightPreviewResponse> previews =
                    highlightService.getHighlightPreviews(page, size);
            return ResponseEntity.ok(ApiResponse.success(previews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch highlights"));
        }
    }

    @GetMapping("/{highlightId}/images")
    public ResponseEntity<ApiResponse<PagedResponse<String>>> getHighlightImages(
            @PathVariable String highlightId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            PagedResponse<String> images =
                    highlightService.getHighlightImages(highlightId, page, size);
            return ResponseEntity.ok(ApiResponse.success(images));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch highlight images"));
        }
    }

    @PostMapping("/{highlightId}/images")
    public ResponseEntity<ApiResponse<String>> addImagesToHighlight(
            @PathVariable String highlightId,
            @RequestParam("images") List<MultipartFile> images) {

        try {
            highlightService.addImagesToHighlight(highlightId, images);
            return ResponseEntity.ok(ApiResponse.success("Images added to highlight successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to add images to highlight"));
        }
    }

    @DeleteMapping("/{highlightId}")
    public ResponseEntity<ApiResponse<String>> deleteHighlight(@PathVariable String highlightId) {
        try {
            highlightService.deleteHighlight(highlightId);
            return ResponseEntity.ok(ApiResponse.success("Highlight deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete highlight"));
        }
    }
}
