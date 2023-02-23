package com.epam.esm.controller;

import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.controller.util.JsonMapperUtil;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDto;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @MockBean
    private TagModelAssembler tagModelAssembler;

    @Test
    @DisplayName("GET /api/tags/{id} - Success")
    void testTagById() throws Exception {
        long id = 1;
        Tag tag = new Tag(id, "Test Tag");
        TagDto tagDto = new TagDto(id, tag.getName());
        given(tagService.getById(id)).willReturn(tagDto);
        given(tagModelAssembler.toModel(tagDto)).willReturn(tagDto);

        ResultActions resultActions = mockMvc.perform(get("/api/tags/{id}", id)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Test Tag"));
    }


    @Test
    @DisplayName("GET /api/tags - Success")
    void testAllTags() throws Exception {
        int page = 1;
        int size = 5;
        List<TagDto> tags = Arrays.asList(
                TagDto.builder().id(1L).name("tag1").build(),
                TagDto.builder().id(2L).name("tag2").build()
        );
        given(tagModelAssembler.toCollectionModel(any(), anyInt(), anyInt()))
                .willReturn(PagedModel.of(tags, new PagedModel.PageMetadata(0,0,0)));
        given(tagService.getAll(page, size)).willReturn(new PageImpl<>(tags));


        ResultActions resultActions = mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$._embedded.tagDtoList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.tagDtoList[0].name", is("tag1")))
                .andExpect(jsonPath("$._embedded.tagDtoList[1].id", is(2)))
                .andExpect(jsonPath("$._embedded.tagDtoList[1].name", is("tag2")));
    }

    @Test
    @DisplayName("GET /api/tags/popular - Success")
    void popularTag() throws Exception {
        TagDto tagDto1 = new TagDto();
        tagDto1.setId(1L);
        tagDto1.setName("tag1");
        when(tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders()).thenReturn(tagDto1);
        when(tagModelAssembler.toModel(any())).thenReturn(tagDto1);

        ResultActions resultActions = mockMvc.perform(get("/api/tags/popular")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.id").value(tagDto1.getId()))
                .andExpect(jsonPath("$.name").value(tagDto1.getName()));
    }

    @Test
    @DisplayName("POST /api/tag - Success")
    void saveTag() throws Exception {
        TagDto tagDto = new TagDto(1L, "Test Tag");
        given(tagService.save(any())).willReturn(tagDto);
        given(tagModelAssembler.toModel(any())).willReturn(tagDto);

        ResultActions resultActions = mockMvc.perform(post("/api/tags/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonMapperUtil.asJson(tagDto)));
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.name").value(tagDto.getName()));
    }

    @Test
    @DisplayName("DELETE /api/tags/{id} - Success")
    void deleteTag() throws Exception {
        long id = 1;

        mockMvc.perform(delete("/api/tags/{id}", id))
                .andExpect(status().isNoContent());
    }
}