package ru.otus.hw.controller.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.controller.rest.AuthorController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.service.AuthorService;

import java.util.List;

import static ru.otus.hw.controller.utils.JsonUtils.toJson;

@WebMvcTest(controllers = AuthorController.class)
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
class AuthorControllerTest {

    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Проверка получения списка авторов")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    void getAuthor_correctReturn_ListAuthors() throws Exception {
        List<AuthorDto> authors = List.of(
                new AuthorDto(1L, "Донателло"),
                new AuthorDto(2L, "Микиланджело"),
                new AuthorDto(3L, "Рафаэль")
        );
        Mockito.when(authorService.findAll()).thenReturn(authors);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/v1/author"));
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].fullName").value("Донателло"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].fullName").value("Микиланджело"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].fullName").value("Рафаэль"));
    }

    @Test
    @DisplayName("Проверка добавления автора")
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void addAuthor_correctSave_returnAuthor() throws Exception {
        AuthorDto requestDto = new AuthorDto(null, "Леонардо");
        AuthorDto responseDto = new AuthorDto(1L, "Леонардо");

        Mockito.when(authorService.save(Mockito.any(AuthorDto.class))).thenReturn(responseDto);


        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(requestDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Леонардо"));
    }


}