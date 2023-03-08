package com.epam.esm.controller;

import com.epam.esm.assembler.TagModelAssembler;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.util.JsonMapperUtil;
import com.epam.esm.util.ModelFactory;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    @Mock
    private TagService tagService;

    @Mock
    private TagModelAssembler tagModelAssembler;

    @InjectMocks
    private TagController tagController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(tagController).build();
    }

    @Test
    @DisplayName("GET /api/tags/{id} - Success")
    void tagById_shouldReturnTag() throws Exception {
        Tag tag = ModelFactory.createTag();
        TagDto tagDto = ModelFactory.toTagDto(tag);
        long id = tagDto.getId();
        given(tagService.getById(id)).willReturn(tagDto);
        given(tagModelAssembler.toModel(tagDto)).willReturn(tagDto);

        ResultActions resultActions = mockMvc.perform(get("/api/tags/{id}", id)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        resultActions.andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(tagDto.getName()));
    }


    @Test
    @DisplayName("GET /api/tags - Success")
    void allTags_shouldReturnTwoTags() throws Exception {
        int page = 1;
        int size = 5;
        Tag tag1 = ModelFactory.createTag();
        TagDto tagDto1 = ModelFactory.toTagDto(tag1);
        Tag tag2 = ModelFactory.createTag();
        TagDto tagDto2 = ModelFactory.toTagDto(tag2);
        List<TagDto> tags = List.of(tagDto1, tagDto2);
        given(tagModelAssembler.toCollectionModel(any(), anyInt(), anyInt()))
                .willReturn(PagedModel.of(tags, new PagedModel.PageMetadata(0,0,0)));
        given(tagService.getAll(page, size)).willReturn(new PageImpl<>(tags));


        ResultActions resultActions = mockMvc.perform(get("/api/tags"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.content[0].id", is((int) tag1.getId())))
                .andExpect(jsonPath("$.content[0].name", is(tag1.getName())))
                .andExpect(jsonPath("$.content[1].id", is((int) tag2.getId())))
                .andExpect(jsonPath("$.content[1].name", is(tag2.getName())));
    }

    @Test
    @DisplayName("GET /api/tags/popular - Success")
    void popularTag_shouldReturnTag() throws Exception {
        Tag tag = ModelFactory.createTag();
        TagDto tagDto = ModelFactory.toTagDto(tag);
        when(tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders()).thenReturn(tagDto);
        when(tagModelAssembler.toModel(any())).thenReturn(tagDto);

        ResultActions resultActions = mockMvc.perform(get("/api/tags/popular")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.id").value(tagDto.getId()))
                .andExpect(jsonPath("$.name").value(tagDto.getName()));
    }

    @Test
    @DisplayName("POST /api/tag - Success")
    void saveTag_shouldReturnTag() throws Exception {
        Tag tag = ModelFactory.createTag();
        TagDto tagDto = ModelFactory.toTagDto(tag);
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
    void deleteTag_shouldDelete() throws Exception {
        long id = 1;

        mockMvc.perform(delete("/api/tags/{id}", id))
                .andExpect(status().isNoContent());
    }
}