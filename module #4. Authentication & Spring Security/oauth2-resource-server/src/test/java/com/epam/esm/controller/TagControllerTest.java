package com.epam.esm.controller;

import com.epam.esm.assembler.TagAssembler;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.util.JsonMapperUtil;
import com.epam.esm.util.ModelFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TagController.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
@EnableMethodSecurity
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @MockBean
    private TagAssembler tagAssembler;

    @Test
    @DisplayName("GET /api/tags/{id} - Success")
    void getById_shouldReturnTag() throws Exception {
        // given
        Tag tag = ModelFactory.createTag();
        TagDto tagDto = ModelFactory.toTagDto(tag);
        long id = tagDto.getId();
        given(tagService.getById(id)).willReturn(tagDto);
        given(tagAssembler.toModel(tagDto)).willReturn(tagDto);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/tags/{id}", id)
                .with(jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_tag.read")))
                .contentType("application/json"))
            .andExpect(status().isOk());

        resultActions
            .andExpect(jsonPath("$.name").value(tagDto.getName()));

        // then
        then(tagService).should().getById(anyLong());
        then(tagAssembler).should().toModel(any(TagDto.class));
    }


    @Test
    @DisplayName("GET /api/tags - Success")
    void getAll_shouldReturnTwoTags() throws Exception {
        // given
        int page = 1;
        int size = 5;
        Tag tag1 = ModelFactory.createTag();
        TagDto tagDto1 = ModelFactory.toTagDto(tag1);
        Tag tag2 = ModelFactory.createTag();
        TagDto tagDto2 = ModelFactory.toTagDto(tag2);
        List<TagDto> tags = List.of(tagDto1, tagDto2);
        given(tagAssembler.toCollectionModel(any(), any(Link.class)))
            .willReturn(PagedModel.of(tags, new PagedModel.PageMetadata(0, 0, 0)));
        given(tagService.getAll(page, size)).willReturn(new PageImpl<>(tags));

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/tags")
            .with(jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_tag.read"))))
            .andExpect(status().isOk());

        resultActions
            .andExpect(jsonPath("$._embedded.tagDtoList[0].name", is(tag1.getName())))
            .andExpect(jsonPath("$._embedded.tagDtoList[1].name", is(tag2.getName())));

        // then
        then(tagService).should().getAll(anyInt(), anyInt());
        then(tagAssembler).should().toCollectionModel(any(), any(Link.class));
    }

    @Test
    @DisplayName("GET /api/tags/popular - Success")
    void getPopular_shouldReturnTag() throws Exception {
        // given
        Tag tag = ModelFactory.createTag();
        TagDto tagDto = ModelFactory.toTagDto(tag);
        when(tagService.getMostWidelyUsedTagWithHighestCostOfAllOrders()).thenReturn(tagDto);
        when(tagAssembler.toModel(any())).thenReturn(tagDto);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/tags/popular")
                .with(jwt().authorities(createAuthorityList("ROLE_USER", "SCOPE_tag.read")))
                .contentType("application/json"))
            .andExpect(status().isOk());

        resultActions
            .andExpect(jsonPath("$.name").value(tagDto.getName()));

        // then
        then(tagService).should().getMostWidelyUsedTagWithHighestCostOfAllOrders();
        then(tagAssembler).should().toModel(any(TagDto.class));
    }

    @Test
    @DisplayName("POST /api/tags - Success")
    void save_shouldReturnTag() throws Exception {
        // given
        Tag tag = ModelFactory.createTag();
        TagDto tagDto = ModelFactory.toTagDto(tag);
        given(tagService.save(any())).willReturn(tagDto);
        given(tagAssembler.toModel(any())).willReturn(tagDto);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/tags")
            .with(jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_tag.write")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonMapperUtil.asJson(tagDto)));
        resultActions
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(tagDto.getName()));

        // then
        then(tagService).should().save(any(TagDto.class));
        then(tagAssembler).should().toModel(any(TagDto.class));
    }

    @Test
    @DisplayName("DELETE /api/tags/{id} - Success")
    void deleteById_shouldDelete() throws Exception {
        // given
        long id = 1;

        // when
        mockMvc.perform(delete("/api/tags/{id}", id)
            .with(jwt().authorities(createAuthorityList("ROLE_ADMIN", "SCOPE_tag.write"))))
            .andExpect(status().isNoContent());

        // then
        then(tagService).should().delete(anyLong());
    }
}