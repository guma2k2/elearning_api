package com.backend.elearning.domain.topic;

import com.backend.elearning.domain.category.CategoryListGetVM;
import com.backend.elearning.domain.category.CategoryPostVM;
import com.backend.elearning.domain.category.CategoryVM;
import com.backend.elearning.domain.common.PageableData;
import com.backend.elearning.exception.ErrorVm;
import com.backend.elearning.utils.Constants;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TopicController {
    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }


    @GetMapping("/topic/{id}")
    public ResponseEntity<TopicVM> getTopicById (
            @PathVariable("id") Integer topicId
    ) {
        TopicVM topicVM = topicService.getTopicById(topicId);
        return ResponseEntity.ok().body(topicVM);
    }
    @GetMapping("/topics/category/{id}")
    public ResponseEntity<List<TopicVM>> getTopicsByCategoryId (
            @PathVariable("id") Integer categoryId
    ) {
        List<TopicVM> topics = topicService.getTopicsByCategoryId(categoryId);
        return ResponseEntity.ok().body(topics);
    }

    @GetMapping("/admin/topic/paging")
    public ResponseEntity<PageableData<TopicVM>> getPageableTopic (
            @RequestParam(value = "pageNum", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_NUMBER) int pageNum,
            @RequestParam(value = "pageSize", defaultValue = Constants.PageableConstant.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword

    ) {
        PageableData<TopicVM> pageableTopics = topicService.getPageableTopics(pageNum, pageSize, keyword);
        return ResponseEntity.ok().body(pageableTopics);
    }

    @PostMapping("/admin/topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content =
            @Content(schema = @Schema(implementation = TopicVM.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated name", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
    })
    public ResponseEntity<TopicVM> create (
            @RequestBody TopicPostVM topicPostVM
    ) {
        TopicVM topicVM = topicService.create(topicPostVM);
        return ResponseEntity.status(HttpStatus.CREATED).body(topicVM);
    }

    @PutMapping("/admin/topic/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content =
            @Content(schema = @Schema(implementation = ErrorVm.class))),
            @ApiResponse(responseCode = "409", description = "Duplicated name", content =
            @Content(schema = @Schema(implementation = ErrorVm.class)))
    })
    public ResponseEntity<TopicVM> update (
            @RequestBody TopicPostVM topicPostVM,
            @PathVariable("id") Integer id
    ) {
        topicService.update(topicPostVM, id);
        return ResponseEntity.noContent()
                .build();
    }



    @DeleteMapping("/admin/topic/{id}")
    public ResponseEntity<Void> delete (
            @PathVariable("id") Integer id
    ) {
        topicService.delete(id);
        return null;
    }
}
